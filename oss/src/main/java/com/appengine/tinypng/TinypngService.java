package com.appengine.tinypng;

import com.tinify.Source;
import com.tinify.Tinify;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author fuyou
 * @Date 2018/12/25 14:12
 */
public class TinypngService {
    public static final String APIKEY = "pbbjDMZ8MtNxZv1l80SlhXPH9B0b83Zq";

    public static void main(String[] args) throws Exception {
        byte[] s = Files.readAllBytes(Paths.get("D:/banner1.png"));
        Files.write(Paths.get("D:/banner2.png"), compress(s));
    }

    public static byte[] compress(byte[] source) {
        Tinify.setKey(APIKEY);
        try {
            Source s = Tinify.fromBuffer(source);
            return s.toBuffer();
        } catch (Exception e) {
            return source;
        }
    }

}
