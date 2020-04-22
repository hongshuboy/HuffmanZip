package com.github.hongshuboy;

import java.util.Map;

public abstract class BaseDecoder<T> {
    protected abstract String bytesToString(byte[] bytes);

    protected abstract Object unzip(String code, Map<T, String> huffmanCode);
}
