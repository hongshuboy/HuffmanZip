package com.github.hongshuboy.imp;

import java.io.Serializable;

public class Node<T> implements Comparable<Node<T>>, Serializable {
    private static final long serialVersionUID = 1L;
    private T data;
    private int value; //权重
    private Node left;
    private Node right;

    public Node() {
    }

    public Node(T data, int value) {
        this.data = data;
        this.value = value;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
    public int compareTo(Node<T> o) {
        return this.value - o.value;
    }
}
