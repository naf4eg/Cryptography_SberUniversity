import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.bachev.hw2.Chiphers;
import ru.bachev.hw2.Digest;
import ru.bachev.hw2.SecretKeyGen;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncriptionDecriptionTest {

    @Test
    public void testEncriptionDecription() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String message = "hello world";

        var key = SecretKeyGen.of();
        var originalMessageHash = Digest.getHash(message);
        System.out.println("Шифруемое сообщение: " + message);
        System.out.println("Хэш собщения: " + originalMessageHash);

        var encriptMessage = Chiphers.encript(key, message);
        System.out.println("Зашифрованное сообщение: " + encriptMessage);

        var decriptMessage = Chiphers.decript(key, encriptMessage);
        System.out.println("Расшифрованное сообщение: " +decriptMessage);
        var decriptMessageHash = Digest.getHash(decriptMessage);
        System.out.println("Хэш расшифрованного собщения: " + originalMessageHash);

        assertEquals(originalMessageHash, decriptMessageHash);
        assertEquals(message, decriptMessage);
    }
}
