package ru.bachev.project;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class EncryptApp {
    private static final String PATH = "/tmp/keystore/keystore.jks";
    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORM = "RSA/ECB/PKCS1Padding";
    private static final String ALGORITHM_SIGN = "SHA256withRSA";
    private static final String KEY_NAME= "key";

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, OperatorCreationException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        if (args.length < 3) {
            System.out.println("укажите 3 параметра: тип keystore, password, cipher word");
            return;
        }

        var basicOrSecure = args[0];
        var keystorePassword = args[1];
        var word = args[2];
        var keystoreType = getKeystoreType(basicOrSecure);
        var keyStore = KeyStore.getInstance(keystoreType);
        keyStore.load(null, null);
        var keys = keys();
        var publicKey = keys.getPublic();
        var privateKey = keys.getPrivate();
        var certificate = getCertificate(publicKey, privateKey);
        keyStore.setKeyEntry(KEY_NAME, privateKey, keystorePassword.toCharArray(), new Certificate[]{certificate});
        var cipherWord = cipherWord(publicKey, word);
        var signed = sign(privateKey, word);
        saveKeystore(keyStore, keystorePassword);

        System.out.println("Тип keyStore: " + keystoreType);
        System.out.println("Тип keyStore: " + KEY_NAME);
        System.out.println("Зашифрованное слово: " + Base64.getEncoder().encodeToString(cipherWord));
        System.out.println("Подпись: " + Base64.getEncoder().encodeToString(signed));
    }

    private static KeyPair keys() throws NoSuchAlgorithmException {
        var keyPair = KeyPairGenerator.getInstance(ALGORITHM);
        keyPair.initialize(2048);
        return keyPair.generateKeyPair();
    }

    private static String getKeystoreType(String basicOrSecure) {
        var keystoreTypeList = new ArrayList<String>(2);
        keystoreTypeList.add("JKS");
        keystoreTypeList.add("JCEKS");
        var keystoreTypeListSize = keystoreTypeList.size();

        if ("Basic".equals(basicOrSecure)) {
            var random = new Random();
            return keystoreTypeList.get(random.nextInt(keystoreTypeListSize));
        } else if ("Secure".equals(basicOrSecure)) {
            var secureRandom = new SecureRandom();
            return keystoreTypeList.get(secureRandom.nextInt(keystoreTypeListSize));
        } else {
            System.out.println("Необходимо выбрать Basic или Secure");
            return "";
        }
    }

    private static X509Certificate getCertificate(PublicKey publicKey, PrivateKey privateKey) throws OperatorCreationException, CertificateException {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 10000000L);
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        X500Name name = new X500Name("CN=Cert");
        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                name,
                serialNumber,
                startDate,
                endDate,
                name,
                SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
        return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
    }

    private static void saveKeystore(KeyStore keyStore, String keystorePassword) {
        try (FileOutputStream fos = new FileOutputStream(PATH)) {
            keyStore.store(fos, keystorePassword.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] sign(PrivateKey privateKey, String word) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(ALGORITHM_SIGN);
        sign.initSign(privateKey);
        sign.update(word.getBytes());
        return sign.sign();
    }

    private static byte[] cipherWord(PublicKey publicKey, String word) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(word.getBytes());
    }
}
