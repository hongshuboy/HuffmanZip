package com.github.hongshuboy.huffman;


import com.github.hongshuboy.huffman.imp.Node;

import java.util.Map;

public interface HuffmanTreeMaker {
    Node createHuffmanTree(byte[] bytes);

    Map<Byte, String> getHuffmanCode(Node root);
}
