package ru.bachev.hw4;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PasswordBasedEncryption {
    public static SecretKey getSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[200];
        var secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        var pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, Short.MAX_VALUE, 256);
        var keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return new SecretKeySpec(keyFactory.generateSecret(pbeKeySpec).getEncoded(), "AES");
    }

    public static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public static byte[] encryptText(Cipher cipher, String text, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        return cipher.doFinal(text.getBytes());
    }

    public static String decryptText(Cipher cipher, byte[] textByte, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        return new String(cipher.doFinal(textByte));
    }
}
