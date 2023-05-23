import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ru.bachev.hw4.PasswordBasedEncryption.decryptText;
import static ru.bachev.hw4.PasswordBasedEncryption.encryptText;
import static ru.bachev.hw4.PasswordBasedEncryption.getCipher;
import static ru.bachev.hw4.PasswordBasedEncryption.getSecretKey;


public class PasswordBasedEncryptionTest {

    @Test
    public void positiveTest() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        var password = "Password";
        var text = "Hello World";
        var key = getSecretKey(password);
        var cipher = getCipher();
        var encryptedText = encryptText(cipher, text, key);
        var decriptedText = decryptText(cipher, encryptedText, key);

        Assertions.assertEquals(decriptedText, text);
    }
}
