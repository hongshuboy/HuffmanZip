package com.github.hongshuboy.imp;

import com.github.hongshuboy.BaseCoder;
import com.github.hongshuboy.FileCoder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.github.hongshuboy.utils.BytesUtils.bytesToString;
import static com.github.hongshuboy.utils.BytesUtils.unboxBytes;

public class FileCoderImp extends BaseCoder implements FileCoder {

    @Override
    public List<FileEncodeResult> encode(String target, String... paths) {
        if (paths.length == 0){
            throw new RuntimeException("paths can not be empty");
        }
        List<FileEncodeResult> results = encodeOnly(paths);
        ObjectOutputStream outputStream = null;
        target = parseTargetDir(target, paths);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(target);
            outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(results);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private String parseTargetDir(String target, String... paths) {
        if (target.lastIndexOf("\\") < 0) {
            String path = paths[0].substring(0, paths[0].lastIndexOf("\\"));
            target = path + "\\" + target;
        }
        return target;
    }

    /**
     * only return List<FileEncodeResult>,will not write to disk
     *
     * @param paths
     */
    public List<FileEncodeResult> encodeOnly(String... paths) {
        List<String> files = countFiles(paths);
        List<FileEncodeResult> encodeResults = new LinkedList<>();
        for (String file : files) {
            encodeResults.add(encode(Paths.get(file)));
        }
        return encodeResults;
    }

    private FileEncodeResult encode(Path path) {
        FileEncodeResult fileEncodeResult = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path.toFile());
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            HuffmanTreeMakerImp byteHuffmanTreeMakerImp = new HuffmanTreeMakerImp();
            Node root = byteHuffmanTreeMakerImp.createHuffmanTree(bytes);
            Map<Byte, String> huffmanCode = byteHuffmanTreeMakerImp.getHuffmanCode(root);
            BytesAndLastLength bytesAndLastLength = zip(bytes, huffmanCode);
            String filePath = path.toString();
            if (filePath.lastIndexOf("\\") > 0) {
                fileEncodeResult = new FileEncodeResult(huffmanCode, bytesAndLastLength.bytes, filePath.substring(filePath.lastIndexOf("\\") + 1), bytesAndLastLength.lastByteLength);
            } else {
                fileEncodeResult = new FileEncodeResult(huffmanCode, bytesAndLastLength.bytes, filePath, bytesAndLastLength.lastByteLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileEncodeResult;
    }


    private List<String> countFiles(String... paths) {
        List<String> list = new LinkedList<>();
        for (String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                countFiles(file, list);
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    private List<String> countFiles(File d, List<String> list) {
        File[] child = d.listFiles();
        for (File file : child) {
            if (file.isDirectory()) {
                countFiles(file, list);
            } else {
                if (!file.isHidden()) {
                    list.add(file.getAbsolutePath());
                }
            }
        }
        return list;
    }

    @Override
    public String[] decode(String filePath, String dst) {
        List<String> paths = new LinkedList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            List<FileEncodeResult> fileEncodeResults = (List<FileEncodeResult>) inputStream.readObject();
            dst = prepareDst(filePath, dst);
            for (FileEncodeResult fileEncodeResult : fileEncodeResults) {
                byte[] bytes = decodeFile(fileEncodeResult.getHuffmanCode(), fileEncodeResult.getZipBytes());
                paths.add(writeToFile(dst, fileEncodeResult.getFileName(), bytes));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return paths.toArray(new String[0]);
    }

    private String prepareDst(String filePath, String dst) {
        //make a absolute path
        int index = dst.lastIndexOf("\\");
        if (index < 0 || index == dst.indexOf("\\")) {
            dst = filePath.substring(0, filePath.lastIndexOf("\\") + 1) + dst;
        }
        //check if dst is a real directory
        File target = new File(dst);
        if (target.exists()) {
            if (!target.isDirectory()) {
                throw new RuntimeException("dst dir must be a folder");
            }
        } else {
            target.mkdirs();
        }
        return dst;
    }

    private String writeToFile(String dst, String fileName, byte[] bytes) {
        File file = new File(dst.concat("\\").concat(fileName));
        if (file.exists()) {
            throw new RuntimeException("FileAlreadyExists:\t" + file.getAbsolutePath());
        }
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    private byte[] decodeFile(Map<Byte, String> huffmanCode, byte[] zipBytes) {
        String code = bytesToString(zipBytes, (short) 0);
        return unboxBytes(unzip(code, huffmanCode));
    }
}
