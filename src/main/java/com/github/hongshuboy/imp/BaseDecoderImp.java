package com.github.hongshuboy.imp;

import com.github.hongshuboy.BaseDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public abstract class BaseDecoderImp<T> extends BaseDecoder<T> {
    private Logger logger = LogManager.getLogger(BaseDecoderImp.class);

    @Override
    public String bytesToString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(byteToString(bytes[i], i == bytes.length - 1));
        }
        logger.debug("恢复后的编码:\n" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private String byteToString(byte b, boolean end) {
        int i = b;
        if (!end) {
            i = i | 256;
        }
        String s = Integer.toBinaryString(i);
        if (s.length() > 8) {
            return s.substring(s.length() - 8);
        }
        return s;
    }

    @Override
    protected abstract Object unzip(String code, Map<T, String> huffmanCode);
}
