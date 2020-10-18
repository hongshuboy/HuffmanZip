package com.github.hongshuboy;


import com.github.hongshuboy.imp.Node;

import java.util.Map;

public interface HuffmanTreeMaker {
    Node createHuffmanTree(byte[] bytes);

    Map<Byte, String> getHuffmanCode(Node root);
}
