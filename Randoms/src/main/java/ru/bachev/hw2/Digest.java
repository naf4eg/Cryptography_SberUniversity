package ru.bachev.hw2;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.digest.DigestUtils;

public class Digest {
    public static String getHash(String message) {
        var messageDigest = DigestUtils.getSha256Digest();
        var digest = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));
        return DigestUtils.sha256Hex(digest);
    }
}
