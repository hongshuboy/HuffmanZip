package com.github.hongshuboy.imp;

import java.util.Map;

public class FileEncodeResult extends EncodeResultImp {
    private static final long serialVersionUID = 2L;
    private String fileName;

    public FileEncodeResult(Map<Byte, String> huffmanCode, byte[] bytes, String fileName, int lastByteLength) {
        super(huffmanCode, bytes, (short) lastByteLength);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
