package com.github.hongshuboy.huffman.imp;

import com.github.hongshuboy.huffman.EncodeResult;

import java.util.Map;

public class EncodeResultImp implements EncodeResult {
    private static final long serialVersionUID = 2L;
    private Map<Byte, String> huffmanCode;
    private byte[] bytes;
    private short lastByteLength = 0;

    private EncodeResultImp() {
    }

    public EncodeResultImp(Map<Byte, String> huffmanCode, byte[] bytes, short lastByteLength) {
        this.huffmanCode = huffmanCode;
        this.bytes = bytes;
        this.lastByteLength = lastByteLength;
    }

    @Override
    public Map<Byte, String> getHuffmanCode() {
        return huffmanCode;
    }

    @Override
    public byte[] getZipBytes() {
        return bytes;
    }

    @Override
    public short getLastByteLength() {
        return lastByteLength;
    }
}
