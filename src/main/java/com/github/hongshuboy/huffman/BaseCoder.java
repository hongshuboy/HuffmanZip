package com.github.hongshuboy.huffman;

import com.github.hongshuboy.huffman.utils.BytesUtils;

import java.util.*;

public abstract class BaseCoder {

    protected BytesAndLastLength zip(byte[] bytes, Map<Byte, String> code) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte c : bytes) {
            String s = code.get(c);
            stringBuilder.append(s);
        }
        return new BytesAndLastLength(BytesUtils.stringToBytes(bytes, stringBuilder.toString()), (short) (stringBuilder.length() % 8));
    }

    /**
     * 2.0版本更新记录[BUG修正]：
     * Special treatment有小概率无法正常生效，因为错误的Huffman Code同样可能在Map中被找到，从而导致下面的修复代码无法执行
     * 解决方案：引入lastByteLength {@link BytesUtils#bytesToString(byte[], short)}
     */
    protected Byte[] unzip(String code, Map<Byte, String> huffmanCode) {
        Map<String, Byte> map = new HashMap<>((int) (huffmanCode.size() / 0.75f) + 1);
        List<Byte> byteList = new LinkedList<>();
        huffmanCode.forEach((k, v) -> map.put(v, k));
        String s;
        for (int i = 0; i < code.length(); ) {
            for (int j = 1; j <= code.length() - i; j++) {
                s = code.substring(i, i + j);
                Byte t = map.get(s);
                if (t != null) {
                    byteList.add(t);
                    i = i + j;
                    break;
                } else if (j == code.length() - i) {
                    //Special treatment for the last byte
                    StringBuilder val = new StringBuilder(s);
                    Byte t1 = null;
                    for (int k = 0; k < 7 && t1 == null; k++) {
                        val.insert(0, '0');
                        t1 = map.get(val.toString());
                    }
                    byteList.add(t1);
                    byteList.removeAll(Collections.singletonList(null));
                    return byteList.toArray(new Byte[]{});
                }
            }
        }
        return byteList.toArray(new Byte[]{});
    }

    public static class BytesAndLastLength {
        public byte[] bytes;
        public short lastByteLength;

        public BytesAndLastLength(byte[] bytes, short lastByteLength) {
            this.bytes = bytes;
            this.lastByteLength = lastByteLength == 0 ? 8 : lastByteLength;
        }
    }
}
