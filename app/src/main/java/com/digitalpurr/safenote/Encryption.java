package com.digitalpurr.safenote;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
    private static IvParameterSpec IV_SPEC;
    private static SecretKeySpec SKEY_SPEC;

    private final static int KEY_LENGTH = 256;
    private final static int ITERATIONS = 1000;

    public static void generateKey(final String password) throws Exception {
        final byte[] salt = "catfurry".getBytes();
        final String iv = "@kittytruth9!6^^";
        IV_SPEC = new IvParameterSpec(iv.getBytes());
        final KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        final SecretKey key = new SecretKeySpec(keyBytes, "AES");
        SKEY_SPEC = new SecretKeySpec(key.getEncoded(), "AES");
    }

    public static byte[] encodeFile(final byte[] fileData) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, SKEY_SPEC, IV_SPEC);
        return cipher.doFinal(fileData);
    }

    public static byte[] decodeFile(final byte[] fileData) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, SKEY_SPEC, IV_SPEC);
        return cipher.doFinal(fileData);
    }
}
