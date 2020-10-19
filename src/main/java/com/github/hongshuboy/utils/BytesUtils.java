package com.github.hongshuboy.utils;

public class BytesUtils {

    public static String bytesToString(byte[] bytes, short lastByteLength) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(byteToString(bytes[i], i == bytes.length - 1, lastByteLength));
        }
        return stringBuilder.toString();
    }

    private static String byteToString(byte b, boolean end, short lastByteLength) {
        int i = b;
        if (!end) {
            i = i | 256;
        }
        String s = Integer.toBinaryString(i);
        if (s.length() > 8) {
            return s.substring(s.length() - 8);
        }
        /*
         * 2.0版本新增，修正Last Byte造成的问题
         * Special treatment for the last byte
         */
        if (end && s.length() < lastByteLength) {
            StringBuilder stringBuilder = new StringBuilder(s);
            while (stringBuilder.length() < lastByteLength) {
                stringBuilder.insert(0, '0');
            }
            s = stringBuilder.toString();
        }
        return s;
    }

    public static byte[] stringToBytes(byte[] bytes, String string) {
        int len = (string.length() + 7) / 8;
        byte[] bts = new byte[len];
        for (int i = 0, index = 0; i < string.length(); i += 8, index++) {
            String s;
            if (i + 8 <= string.length()) {
                s = string.substring(i, i + 8);
            } else {
                s = string.substring(i);
            }
            bts[index] = Integer.valueOf(s, 2).byteValue();
        }
        return bts;
    }

    public static byte[] unboxBytes(Byte[] unzipBytes) {
        byte[] bytes = new byte[unzipBytes.length];
        for (int i = 0; i < unzipBytes.length; i++) {
            bytes[i] = unzipBytes[i];
        }
        return bytes;
    }
}
