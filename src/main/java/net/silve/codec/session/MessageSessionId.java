package net.silve.codec.session;

import io.netty.util.AsciiString;
import org.hashids.Hashids;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class MessageSessionId {

    private static final SecureRandom random = new SecureRandom();

    private MessageSessionId() {
    }

    public static AsciiString generate() {
        byte[] bytes = new byte[64];
        random.nextBytes(new byte[64]);
        final String salt = Base64.getEncoder().encodeToString(bytes);
        final Hashids hashids = new Hashids(salt, 16);
        final String id = hashids.encode(new Date().getTime(), random.nextInt(512));
        return AsciiString.of(id);
    }
}
