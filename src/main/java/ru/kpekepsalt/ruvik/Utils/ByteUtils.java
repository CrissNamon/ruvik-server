package ru.kpekepsalt.ruvik.Utils;

/**
 * Utils to work with bytes
 */
public class ByteUtils {

    /**
     * Converts byte array to hex string
     * @param bytes Bytes array
     * @return String in hex format
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for(byte b:bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
