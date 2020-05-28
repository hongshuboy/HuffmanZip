package com.github.hongshuboy.imp;

import com.github.hongshuboy.EncodeResult;

import java.util.Map;

public class EncodeResultImp<T> implements EncodeResult{
    private static final long serialVersionUID = 1L;
    private Map<T, String> huffmanCode;
    private byte[] bytes;

    public EncodeResultImp() {
    }

    public EncodeResultImp(Map<T, String> huffmanCode, byte[] bytes) {
        this.huffmanCode = huffmanCode;
        this.bytes = bytes;
    }

    @Override
    public Map<T, String> getHuffmanCode() {
        return huffmanCode;
    }

    @Override
    public byte[] getZipBytes() {
        return bytes;
    }
}
