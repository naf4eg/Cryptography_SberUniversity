package ru.bachev.hw2;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class Chiphers {
    public static String encript(Key key, String message) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        var cipher = getChipher();
        cipher.init(Cipher.ENCRYPT_MODE, key, getSpec());
        var cipherBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    public static String decript(Key key, String message) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        var cipher = getChipher();
        cipher.init(Cipher.DECRYPT_MODE, key, getSpec());
        var cipherBytes = cipher.doFinal(Base64.getDecoder().decode(message));
        return new String(cipherBytes);
    }

    private static AlgorithmParameterSpec getSpec() {
        return new IvParameterSpec(new byte[16]);
    }

    private static Cipher getChipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES/CBC/PKCS5Padding");
    }
}
