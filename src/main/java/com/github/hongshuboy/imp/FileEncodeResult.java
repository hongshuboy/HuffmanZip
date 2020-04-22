package com.github.hongshuboy.imp;

import java.io.Serializable;
import java.util.Map;

public class FileEncodeResult extends EncodeResultImp<Byte> implements Serializable {
    private String fileName;

    public FileEncodeResult() {
    }

    public FileEncodeResult(Map<Byte, String> huffmanCode, byte[] bytes, String fileName) {
        super(huffmanCode, bytes);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
