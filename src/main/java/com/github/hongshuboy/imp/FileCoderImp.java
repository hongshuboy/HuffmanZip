package com.github.hongshuboy.imp;

import com.github.hongshuboy.FileCoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileCoderImp extends BaseDecoderImp<Byte> implements FileCoder {
    private Logger logger = LogManager.getLogger(StringCoderImp.class);

    @Override
    public List<FileEncodeResult> encode(String target, String... paths) {
        List<FileEncodeResult> results = encodeOnly(paths);
        ObjectOutputStream outputStream = null;
        target = parseTargetDir(target, paths);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(target);
            outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(results);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        logger.info("压缩到：" + target);
        return target;
    }

    /**
     * only return List<FileEncodeResult>,will not write to disk
     *
     * @param paths
     * @return
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
            List<Node<Byte>> nodes = getNodes(bytes);
            HuffmanTreeMakerImp<Byte> byteHuffmanTreeMakerImp = new HuffmanTreeMakerImp<>();
            Node<Byte> root = byteHuffmanTreeMakerImp.createHuffmanTree(nodes);
            Map<Byte, String> huffmanCode = byteHuffmanTreeMakerImp.getHuffmanCode(root);
            byte[] zip = zip(bytes, huffmanCode);
            String filePath = path.toString();
            if (filePath.lastIndexOf("\\") > 0) {
                fileEncodeResult = new FileEncodeResult(huffmanCode, zip, filePath.substring(filePath.lastIndexOf("\\") + 1));
            } else {
                fileEncodeResult = new FileEncodeResult(huffmanCode, zip, filePath);
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

    private byte[] zip(byte[] bytes, Map<Byte, String> code) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte c : bytes) {
            String s = code.get(c);
            stringBuilder.append(s);
        }
        logger.debug("压缩后编码:\n" + stringBuilder.toString());
        int len = (stringBuilder.length() + 7) / 8;
        byte[] bts = new byte[len];
        for (int i = 0, index = 0; i < stringBuilder.length(); i += 8, index++) {
            String s;
            if (i + 8 <= stringBuilder.length()) {
                s = stringBuilder.substring(i, i + 8);
            } else {
                s = stringBuilder.substring(i);
            }
            bts[index] = Integer.valueOf(s, 2).byteValue();
        }
        return bts;
    }

    private List<Node<Byte>> getNodes(byte[] bytes) {
        List<Node<Byte>> list = new ArrayList<>(bytes.length);
        Map<Byte, Integer> map = new HashMap<>();
        for (byte c : bytes) {
            Integer count = map.get(c);
            if (count == null) {
                map.put(c, 1);
            } else {
                map.put(c, count + 1);
            }
        }
        map.forEach((k, v) -> list.add(new Node<Byte>(k, v)));
        return list;
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
                    logger.debug("file found:\t" + file.getName());
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        logger.debug("dst absolute path is : " + dst);
        //check if dst is a real directory
        File target = new File(dst);
        if (target.exists()) {
            if (!target.isDirectory()) {
                throw new RuntimeException("dst dir must be a folder");
            }
        } else {
            target.mkdir();
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        String code = bytesToString(zipBytes);
        List<Byte> byteList = unzip(code, huffmanCode);
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    @Override
    protected List<Byte> unzip(String code, Map<Byte, String> huffmanCode) {
        Map<String, Byte> map = new HashMap<>();
        List<Byte> byteList = new LinkedList<>();
        huffmanCode.forEach((k, v) -> map.put(v, k));
        String s;
        for (int i = 0; i < code.length(); ) {
            for (int j = 1; j <= code.length() - i; j++) {
                s = code.substring(i, i + j);
                Byte t = map.get(s);
                if (t != null) {
                    byteList.add(t);
                    i = i + j;
                    break;
                } else if (j == code.length() - i) {
                    //Special treatment for the last digit
                    StringBuilder val = new StringBuilder(s);
                    Byte t1 = null;
                    for (int k = 0; k < 7 && t1 == null; k++) {
                        val.insert(0, '0');
                        t1 = map.get(val.toString());
                    }
                    byteList.add(t1);
                    return byteList;
                }
            }
        }
        return byteList;
    }
}
