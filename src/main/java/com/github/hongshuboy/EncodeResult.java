package com.github.hongshuboy;

import java.io.Serializable;
import java.util.Map;

public interface EncodeResult extends Serializable {
    Map<Byte, String> getHuffmanCode();

    byte[] getZipBytes();

    short getLastByteLength();
}
