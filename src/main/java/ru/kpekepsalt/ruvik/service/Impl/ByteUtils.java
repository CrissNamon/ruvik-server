package ru.kpekepsalt.ruvik.service.Impl;

public class ByteUtils {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for(byte b:bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
