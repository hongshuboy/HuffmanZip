package com.github.hongshuboy;

public interface StringCoder {
    EncodeResult encode(String encode);

    String decode(EncodeResult encodeResult);
}
