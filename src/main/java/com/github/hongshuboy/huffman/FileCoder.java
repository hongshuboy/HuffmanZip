package com.github.hongshuboy.huffman;

import com.github.hongshuboy.huffman.imp.FileEncodeResult;

import java.util.List;

public interface FileCoder {
    /**
     * @param fileName 压缩后的文件名，也可以是全路径，默认压缩到当前路径
     * @param paths    可以是文件夹或者文件，待压缩的文件或文件夹
     */
    List<FileEncodeResult> encode(String fileName, String... paths);

    /**
     * @param filePath 待解压的文件，需要写全路径
     * @param dst      解压到目标文件夹，必须是文件夹
     * @return 解压后每个文件的完整路径
     */
    String[] decode(String filePath, String dst);
}
