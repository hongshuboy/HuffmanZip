package com.github.hongshuboy.imp;

import com.github.hongshuboy.BaseCoder;
import com.github.hongshuboy.EncodeResult;
import com.github.hongshuboy.StringCoder;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.github.hongshuboy.utils.BytesUtils.bytesToString;
import static com.github.hongshuboy.utils.BytesUtils.unboxBytes;

public class StringCoderImp extends BaseCoder implements StringCoder {

    @Override
    @SuppressWarnings("unchecked")
    public EncodeResult encode(String encodeString) {
        byte[] bytes = encodeString.getBytes(StandardCharsets.UTF_8);
        HuffmanTreeMakerImp characterHuffmanTreeMakerImp = new HuffmanTreeMakerImp();
        Node root = characterHuffmanTreeMakerImp.createHuffmanTree(bytes);
        Map<Byte, String> code = characterHuffmanTreeMakerImp.getHuffmanCode(root);
        BaseCoder.BytesAndLastLength bytesAndLastLength = zip(bytes, code);
        return new EncodeResultImp(code, bytesAndLastLength.bytes, bytesAndLastLength.lastByteLength);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String decode(EncodeResult encodeResult) {
        String s = bytesToString(encodeResult.getZipBytes(), encodeResult.getLastByteLength());
        Byte[] unzipBytes = unzip(s, encodeResult.getHuffmanCode());
        byte[] bytes = unboxBytes(unzipBytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
