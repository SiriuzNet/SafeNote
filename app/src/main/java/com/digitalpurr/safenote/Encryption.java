package com.digitalpurr.safenote;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static void writeToFile(final String data, final Context context) throws Exception {
        try {
            final String filePath = context.getFilesDir().getPath() + File.separator + Consts.CONTAINER_FILE;
            FileOutputStream f = new FileOutputStream(new File(filePath));
            f.write(Encryption.encodeFile(data.getBytes()));
            f.flush();
            f.close();
        }
        catch (IOException e) {
            Log.e("SafeNote", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(final Context context) throws Exception {
        String ret = "";
        try {
            final String filePath = context.getFilesDir().getPath() + File.separator + Consts.CONTAINER_FILE;
            File file = new File(filePath);
            byte[] fileData = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
            ret = new String(Encryption.decodeFile(fileData), "UTF-8");
        }
        catch (FileNotFoundException e) {
            Log.e("SafeNote", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("SafeNote", "Can not read file: " + e.toString());
        }
        return ret;
    }
}
