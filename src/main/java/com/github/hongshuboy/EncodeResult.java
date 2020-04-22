package com.github.hongshuboy;

import java.io.Serializable;
import java.util.Map;

public interface EncodeResult<T> extends Serializable {
    Map<T, String> getHuffmanCode();

    byte[] getZipBytes();
}
