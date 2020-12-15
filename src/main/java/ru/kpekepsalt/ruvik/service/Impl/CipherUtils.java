package ru.kpekepsalt.ruvik.service.Impl;

import java.math.BigInteger;
import java.util.Random;

public class CipherUtils {

    public static byte[] randomByteKey(int length) {
        return BigInteger.probablePrime(length*8-1, new Random())
                .toByteArray();
    }

    public static String randomStringKey(int length) {
        String key = ByteUtils.bytesToHex(randomByteKey(length*4));
        if(key.length() > length) {
            key = key.substring(0, length);
        }
        return key;
    }

}
