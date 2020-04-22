package com.github.hongshuboy;

public interface StringCoder extends Coder{
    EncodeResult encode(String encode);

    String decode(EncodeResult encodeResult);
}
