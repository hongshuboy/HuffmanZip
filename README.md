# HuffmanZip

使用赫夫曼编码进行字符串和文件、文件夹的压缩

*Files and String zipper based on huffman codes*

| License                                        | Code Beat                                                    | Language                                                     | Build                                                        | Size                                                         | Contributors                                                 |
| ---------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![Hex.pm](https://img.shields.io/hexpm/l/plug) | [![codebeat <br/>badge](https://codebeat.co/badges/32dc030c-9f15-4f40-ab0e-8c0250c2dbe3)](https://codebeat.co/projects/github-com-hongshuboy-huffmanzip-master) | ![language java](<https://img.shields.io/badge/java-v1.8-blue>) | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/hongshuboy/HuffmanZip) | ![GitHub repo size](https://img.shields.io/github/repo-size/hongshuboy/huffmanzip) | ![GitHub contributors](https://img.shields.io/github/contributors/hongshuboy/huffmanzip) |


- [x] 支持中文及其他语种 *Support Chinese and other languages*
- [x] 支持单个文件压缩 *Support single file compression*
- [x] 支持文件夹压缩（不压缩隐藏文件）*Support folder compression (will not compress hidden files)*
- [x] 支持文件与文件夹混合压缩 *Supports mixed compression of files and folders*
- [x] 支持不同路径的文件混合压缩 *Supports mixed compression of files with different paths*

## 上手指南 Getting Start

接下来通过简单的两个例子，分别实现字符串和文件（夹）的压缩和解压

*Next, through two simple examples, learn the compression and decompression of strings and files and folders*

**功能一，字符串压缩：使用下面两行代码即可实现对字符串的压缩和解压**

***Function one, string compression: Use the following two lines of code to compress and decompress the string***

​	1.字符串压缩（支持中文）：

​	*1.String compression（Support other languages）*

```java
//一行代码压缩字符串
EncodeResult encodeResult = Huffman.getStringCoder().encode("hello world");
```

返回值`encodeResult`包含两个部分

*The return value `encodeResult` contains two parts*

```java
encodeResult.getZipBytes()        //压缩好的byte数组
encodeResult.getHuffmanCode()     //哈夫曼编码，相当于解码byte数组的钥匙
```

​	2.字符串解压：

​	*2.String decompression*

​	解压需要压缩时的返回值`EncodeResult`作为参数

​	*The return value `EncodeResult` is needed when you want to unzip the bytes*

```java
//解压字符串
String s = Huffman.getStringCoder().decode(encodeResult);
```

这里的返回值`s`就是解压好的字符串了

*The return value `s` here is the decompressed string*

**功能二，文件压缩：使用下面两行代码即可实现对文件与文件夹的压缩和解压**

***Function 2: File compression: Use the following two lines of code to compress and decompress files or folders***

​	1.压缩文件，参数二为变长参数，可随意填写文件或文件夹

​	*1.Compressed file, parameter two is variable length parameter, you can fill in files or folders at will*

​	该方法有返回值`List<FileEncodeResult> encode`，但是一般可以忽略，除非你想修改压缩后的结构

​	*This method has a return value of `List <FileEncodeResult> encode`, but it can generally be ignored unless you want to modify the compressed structure*

```java
Huffman.getFileCoder().encode("zip.huff", "D:\\zip\\1.txt", "D:\\zip\\2.txt");
```

​	2.解压文件，返回值是解压后的文件路径，是一个字符串数组

​	*Unzip the file, the return value is the uncompressed file path, which is an array of strings*

```java
String[] paths = Huffman.getFileCoder().decode("D:/zip/zip.huff", "D:\\zip\\new\\");
for (String path : paths) {
    System.out.println(path);
}
```

**注意：**

***warning***

不能使用过于简单的文件或字符串进行压缩，比如一个txt文件内容全是1，否则将会抛出异常

*Do not use too simple files or strings for compression,for example, the content of a txt file is 1,otherwise, an exception will be thrown*

```java
throw new RuntimeException("Data is too simple to compress");
```

## 作者 Author

弘树丶

> wangpeng(hongshu)

Email:hongshuboy@gmail.com

## 版权说明 License 

本项目使用**Apache License 2.0**授权许可，详情请参阅 ***\LICENSE*** 和 ***\NOTICE***

*hongshuboy/HuffmanZip is licensed under the Apache License 2.0,please read LICENSE and NOTICE for more information*

Copyright ©2020 wangpeng(hongshu)

