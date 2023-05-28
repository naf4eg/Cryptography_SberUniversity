package ru.bachev.project;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;

public class DecriptApp {
    private static final String TRANSFORM = "RSA/ECB/PKCS1Padding";
    private static final String ALGORITHM_SIGN = "SHA256withRSA";


    public static void main(String[] args) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException {
        if (args.length < 5) {
            System.out.println("Должно быть 5 параметров: путь до keystore, пароль keystore, шифрованное словоб подпись");
        }
        var path = args[0];
        var keystorePassword = args[1];
        var cipherWord = args[2];
        var sign = args[3];
        var keyName = args[4];

        var fileInputStream = new FileInputStream(path);
        var keyStore = KeyStore.getInstance("JKS");
        keyStore.load(fileInputStream, keystorePassword.toCharArray());
        fileInputStream.close();

        var privateKey = (PrivateKey) keyStore.getKey(keyName, keystorePassword.toCharArray());
        var decryptedWord = new String(cipherWord(privateKey, Base64.getDecoder().decode(cipherWord)));

        var publicKey = keyStore.getCertificate(keyName).getPublicKey();
        boolean verified = verifySign(publicKey, decryptedWord, sign);

        System.out.println("расшифрованное слово: " + decryptedWord);
        System.out.println("Подпись верна: " + verified);
    }

    private static byte[] cipherWord(PrivateKey privateKey, byte[] word) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(DecriptApp.TRANSFORM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(word);
    }

    private static boolean verifySign(PublicKey publicKey, String decryptedWord, String sign) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        var signVerifier = Signature.getInstance(ALGORITHM_SIGN);
        signVerifier.initVerify(publicKey);
        signVerifier.update(decryptedWord.getBytes());
        return signVerifier.verify(Base64.getDecoder().decode(sign));
    }
}
