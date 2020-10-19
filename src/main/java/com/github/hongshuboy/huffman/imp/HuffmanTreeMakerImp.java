package com.github.hongshuboy.huffman.imp;

import com.github.hongshuboy.huffman.HuffmanTreeMaker;

import java.util.*;

public class HuffmanTreeMakerImp implements HuffmanTreeMaker {

    @Override
    public Node createHuffmanTree(byte[] bytes) {
        List<Node> nodes = getHuffmanNodes(bytes);
        while (nodes.size() > 1) {
            Collections.sort(nodes);
            Node left = nodes.get(0);
            Node right = nodes.get(1);
            Node parent = new Node(null, left.getValue() + right.getValue());
            parent.setLeft(left);
            parent.setRight(right);
            nodes.remove(left);
            nodes.remove(right);
            nodes.add(parent);
        }
        return nodes.get(0);
    }

    @Override
    public Map<Byte, String> getHuffmanCode(Node root) {
        Objects.requireNonNull(root, "huffman tree can not be null");
        if (root.getLeft() == null || root.getRight() == null) {
            throw new RuntimeException("Data is too simple to compress");
        }
        Map<Byte, String> map = new HashMap<>();
        getHuffmanCode(root.getLeft(), 0, new StringBuilder(), map);
        getHuffmanCode(root.getRight(), 1, new StringBuilder(), map);
        return map;
    }

    private Map<Byte, String> getHuffmanCode(Node node, int val1, StringBuilder val2, Map<Byte, String> map) {
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

    /**
     * 获得带权结点
     */
    public List<Node> getHuffmanNodes(byte[] bytes) {
        List<Node> list = new ArrayList<>(bytes.length);
        Map<Byte, Integer> map = new HashMap<>();
        for (byte c : bytes) {
            map.merge(c, 1, Integer::sum);
        }
        map.forEach((k, v) -> list.add(new Node(k, v)));
        return list;
    }
}
