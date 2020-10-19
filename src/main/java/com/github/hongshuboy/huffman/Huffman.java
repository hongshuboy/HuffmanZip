package com.github.hongshuboy.huffman;

import com.github.hongshuboy.huffman.imp.FileCoderImp;
import com.github.hongshuboy.huffman.imp.StringCoderImp;
/**
 *  _                                _
 * | |                              | |
 * | |__    ___   _ __    __ _  ___ | |__   _   _
 * | '_ \  / _ \ | '_ \  / _` |/ __|| '_ \ | | | |
 * | | | || (_) || | | || (_| |\__ \| | | || |_| |
 * |_| |_| \___/ |_| |_| \__, ||___/|_| |_|\__,_|
 *                        __/ |
 *                       |___/
 * @Email hongshuboy@gmail.com
 * @GitHub https://github.com/hongshuboy
 */

/**
 * @功能一，字符串压缩：使用下面两行代码即可实现对字符串的压缩和解压
 * <code>
 * //一行代码压缩字符串
 * EncodeResult encodeResult = Huffman.getStringCoder().encode("huffman");
 * //解压字符串
 * String s = Huffman.getStringCoder().decode(encodeResult);
 *
 * </code>
 *      返回值EncodeResult包含两部分，如下所示，A表示用于解码的哈弗曼编码，相当于解码的钥匙，
 * B为压缩好的byte数组
 * <code>
 * A: encodeResult.getHuffmanCode
 * B: encodeResult.getZipBytes
 * </code>
 *
 * @功能二，文件压缩：使用下面两行代码即可实现对文件与文件夹的压缩和解压
 * <code>
 * //压缩文件，参数二为变长参数，可随意填写文件或文件夹
 * Huffman.getFileCoder().encode("zip.huff", "D:\\zip\\1.txt", "D:\\zip\\2.txt");
 * //解压文件，返回值为解压后文件的路径
 * String[] paths = Huffman.getFileCoder().decode("D:\\zip\\zip.huff", "D:\\zip\\new\\");
 * </code>
 * <code>
 * encode方法可有返回值，比字符串压缩多了一个文件名
 * encode.getFileName
 * encode.getHuffmanCode
 * encode.getZipBytes
 *</code>
 */
public class Huffman {
    public static StringCoder getStringCoder() {
        return new StringCoderImp();
    }

    public static FileCoder getFileCoder() {
        return new FileCoderImp();
    }
}


