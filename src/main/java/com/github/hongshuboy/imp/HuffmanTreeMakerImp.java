package com.github.hongshuboy.imp;

import com.github.hongshuboy.HuffmanTreeMaker;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;

/**
 * @param <T> Character for String, Byte for File
 */
public class HuffmanTreeMakerImp<T> implements HuffmanTreeMaker<T> {
    private Logger logger = Logger.getLogger(HuffmanTreeMakerImp.class);

    @Override
    public Node<T> createHuffmanTree(List<Node<T>> nodes) {
        while (nodes.size() > 1) {
            Collections.sort(nodes);
            Node<T> left = nodes.get(0);
            Node<T> right = nodes.get(1);
            Node<T> parent = new Node<>(null, left.getValue() + right.getValue());
            parent.setLeft(left);
            parent.setRight(right);
            nodes.remove(left);
            nodes.remove(right);
            nodes.add(parent);
        }
        return nodes.get(0);
    }

    @Override
    public Map<T, String> getHuffmanCode(Node<T> root) {
        Objects.requireNonNull(root, "huffman tree can not be null");
        if (root.getLeft() == null || root.getRight() == null) {
            throw new RuntimeException("Data is too simple to compress");
        }
        Map<T, String> map = new HashMap<>();
        getHuffmanCode(root.getLeft(), 0, new StringBuilder(), map);
        getHuffmanCode(root.getRight(), 1, new StringBuilder(), map);
        logger.debug("\nhuffman code :");
        map.forEach((k, v) -> {
            logger.debug(MessageFormat.format("k:{0},v:{1}", k, v));
        });
        return map;
    }

    private Map<T, String> getHuffmanCode(Node<T> node, int val1, StringBuilder val2, Map<T, String> map) {
        StringBuilder stringBuilder = new StringBuilder(val2);
        stringBuilder.append(val1);
        if (node.getData() == null) {
            if (node.getLeft() != null) {
                getHuffmanCode(node.getLeft(), 0, stringBuilder, map);
            }
            if (node.getRight() != null) {
                getHuffmanCode(node.getRight(), 1, stringBuilder, map);
            }
        } else {
            map.put(node.getData(), stringBuilder.toString());
        }
        return map;
    }
}
