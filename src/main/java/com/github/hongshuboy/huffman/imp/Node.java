package com.github.hongshuboy.huffman.imp;

import java.io.Serializable;

public class Node implements Comparable<Node>, Serializable {
    private static final long serialVersionUID = 2L;
    private Byte data;
    private int value; //权重
    private Node left;
    private Node right;

    public Node() {
    }

    public Node(Byte data, int value) {
        this.data = data;
        this.value = value;
    }

    public Byte getData() {
        return data;
    }

    public void setData(Byte data) {
        this.data = data;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", value=" + value +
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return this.value - o.value;
    }
}
