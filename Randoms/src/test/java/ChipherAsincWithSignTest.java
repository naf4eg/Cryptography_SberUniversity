import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static ru.bachev.hw3.CipherAsincWithSign.*;

public class ChipherAsincWithSignTest {

    @Test
    public void positiveTest() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException {
        var java = "Java";
        var keyPair = getKeyPair();
        var cipher = getCipher();
        var encryptedText = encryptText(cipher, java, keyPair.getPublic());
        var decriptedText = decryptText(cipher, encryptedText, keyPair.getPrivate());

        var sign = getSign();
        var signed = createSign(sign, keyPair.getPrivate());
        var verified = verifySign(sign, signed, keyPair.getPublic());

        if (verified) {
            System.out.println(decriptedText);
            System.out.println("Sign is OK");
        }

        Assertions.assertEquals(decriptedText, java);
        Assertions.assertTrue(verified);
    }

    @Test
    public void negativeTest() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException {
        var java = "Java";
        var keyPair = getKeyPair();
        var keyPair2 = getKeyPair();
        var cipher = getCipher();
        var encryptedText = encryptText(cipher, java, keyPair.getPublic());
        var decriptedText = decryptText(cipher, encryptedText, keyPair.getPrivate());

        var sign = getSign();
        var signed = createSign(sign, keyPair.getPrivate());
        var verified = verifySign(sign, signed, keyPair2.getPublic());

        if (verified) {
            System.out.println(decriptedText);
            System.out.println("Sign is OK");
        }

        Assertions.assertEquals(decriptedText, java);
        Assertions.assertFalse(verified);
    }
}
