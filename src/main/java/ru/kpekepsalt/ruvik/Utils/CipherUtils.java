package ru.kpekepsalt.ruvik.Utils;

import java.math.BigInteger;
import java.util.Random;

/**
 * Cipher utils
 */
public class CipherUtils {

    /**
     * Generates byte key
     * @param length Key length
     * @return Generated key
     */
    public static byte[] randomByteKey(int length) {
        return BigInteger.probablePrime(length*8-1, new Random())
                .toByteArray();
    }

    /**
     * Generates string key
     * @param length Key length
     * @return Generated key
     */
    public static String randomStringKey(int length) {
        String key = ByteUtils.bytesToHex(randomByteKey(length*4));
        if(key.length() > length) {
            key = key.substring(0, length);
        }
        return key;
    }

}
