package ru.bachev.hw3;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class CipherAsincWithSign {
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
    public static boolean verifySign(Signature signature, byte[] signed, PublicKey publicKey) throws InvalidKeyException, SignatureException {
        signature.initVerify(publicKey);
        return signature.verify(signed);
    }
    public static byte[] createSign(Signature signature, PrivateKey privateKey) throws InvalidKeyException, SignatureException {
        signature.initSign(privateKey);
        return signature.sign();
    }

    public static Signature getSign() throws NoSuchAlgorithmException {
        return Signature.getInstance("SHA256withRSA");
    }

    public static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("RSA");
    }

    public static byte[] encryptText(Cipher cipher, String text, PublicKey publicKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(text.getBytes());
    }

    public static String decryptText(Cipher cipher, byte[] textByte, PrivateKey privateKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(textByte));
    }
}
