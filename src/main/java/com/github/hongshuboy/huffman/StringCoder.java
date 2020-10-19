package com.github.hongshuboy.huffman;

public interface StringCoder {
    EncodeResult encode(String encode);

    String decode(EncodeResult encodeResult);
}
