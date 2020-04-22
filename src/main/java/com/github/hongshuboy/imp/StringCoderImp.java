package com.github.hongshuboy.imp;

import com.github.hongshuboy.EncodeResult;
import com.github.hongshuboy.StringCoder;
import org.apache.log4j.Logger;

import java.util.*;

public class StringCoderImp extends BaseDecoderImp<Character> implements StringCoder {
    private Logger logger = Logger.getLogger(StringCoderImp.class);

    @Override
    @SuppressWarnings("unchecked")
    public EncodeResult encode(String encode) {
        char[] chars = encode.toCharArray();
        logger.debug(Arrays.toString(chars));
        List<Node<Character>> nodes = getNodes(chars);
        HuffmanTreeMakerImp<Character> characterHuffmanTreeMakerImp = new HuffmanTreeMakerImp<>();
        Node<Character> root = characterHuffmanTreeMakerImp.createHuffmanTree(nodes);
        Map<Character, String> code = characterHuffmanTreeMakerImp.getHuffmanCode(root);
        byte[] zip = zip(chars, code);
        return new EncodeResultImp(code, zip);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String decode(EncodeResult encodeResult) {
        String s = bytesToString(encodeResult.getZipBytes());
        return unzip(s, encodeResult.getHuffmanCode());
    }

    @Override
    protected String unzip(String code, Map<Character, String> huffmanCode) {
        Map<String, Character> map = new HashMap<>(huffmanCode.size());
        huffmanCode.forEach((k, v) -> map.put(v, k));
        StringBuilder stringBuilder = new StringBuilder();
        String s;
        for (int i = 0; i < code.length(); ) {
            for (int j = 1; j <= code.length() - i; j++) {
                s = code.substring(i, i + j);
                Character t = map.get(s);
                if (t != null) {
                    stringBuilder.append(t);
                    i = i + j;
                    break;
                } else if (j == code.length() - i) {
                    //Special treatment for the last digit
                    StringBuilder val = new StringBuilder(s);
                    Character t1 = null;
                    for (int k = 0; k < 7 && t1 == null; k++) {
                        val.insert(0, '0');
                        t1 = map.get(val.toString());
                    }
                    stringBuilder.append(t1);
                    return stringBuilder.toString();
                }
            }
        }
        return stringBuilder.toString();
    }

    private byte[] zip(char[] chars, Map<Character, String> code) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : chars) {
            String s = code.get(c);
            stringBuilder.append(s);
        }
        logger.debug("压缩后的编码:\n" + stringBuilder.toString());
        int len = (stringBuilder.length() + 7) / 8;
        byte[] bytes = new byte[len];
        for (int i = 0, index = 0; i < stringBuilder.length(); i += 8, index++) {
            String s;
            if (i + 8 <= stringBuilder.length()) {
                s = stringBuilder.substring(i, i + 8);
            } else {
                s = stringBuilder.substring(i);
            }
            bytes[index] = Integer.valueOf(s, 2).byteValue();
        }
        return bytes;
    }

    /*private Map<Character, String> getHuffmanCode(Node<Character> root) {
        Objects.requireNonNull(root, "huffman tree can not be null");
        Map<Character, String> map = new HashMap<>();
        getHuffmanCode(root.getLeft(), 0, new StringBuilder(), map);
        getHuffmanCode(root.getRight(), 1, new StringBuilder(), map);
        logger.debug("\nhuffman code :");
        map.forEach((k, v) -> {
            logger.debug(MessageFormat.format("k:{0},v:{1}", k, v));
        });
        return map;
    }*/

/*    private Map<Character, String> getHuffmanCode(Node<Character> node, int val1, StringBuilder val2, Map<Character, String> map) {
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
    }*/

    /*private Node<Character> createHuffmanTree(List<Node<Character>> nodes) {
        while (nodes.size() > 1) {
            Collections.sort(nodes);
            Node<Character> left = nodes.get(0);
            Node<Character> right = nodes.get(1);
            Node<Character> parent = new Node<>(null, left.getValue() + right.getValue());
            parent.setLeft(left);
            parent.setRight(right);
            nodes.remove(left);
            nodes.remove(right);
            nodes.add(parent);
        }
        return nodes.get(0);
    }*/

    private List<Node<Character>> getNodes(char[] chars) {
        List<Node<Character>> list = new ArrayList<>(chars.length);
        Map<Character, Integer> map = new HashMap<>(chars.length * 2);
        for (char c : chars) {
            Integer count = map.get(c);
            if (count == null) {
                map.put(c, 1);
            } else {
                map.put(c, count + 1);
            }
        }
        map.forEach((k, v) -> list.add(new Node<Character>(k, v)));
        return list;
    }
}
