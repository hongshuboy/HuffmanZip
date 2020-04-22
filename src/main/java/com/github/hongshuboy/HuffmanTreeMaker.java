package com.github.hongshuboy;


import com.github.hongshuboy.imp.Node;

import java.util.List;
import java.util.Map;

public interface HuffmanTreeMaker<T> {
    Node<T> createHuffmanTree(List<Node<T>> nodes);
    Map<T, String> getHuffmanCode(Node<T> root);
}
