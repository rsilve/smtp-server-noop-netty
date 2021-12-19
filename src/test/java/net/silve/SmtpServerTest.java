package net.silve;

import net.silve.codec.configuration.SmtpServerConfiguration;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmtpServerTest {

    @Test
    void shouldConfigure() throws UnknownHostException {
        SmtpServer server = new SmtpServer();
        SmtpServerConfiguration configuration = server.configure();
        assertEquals(250, configuration.responses.responseHelo.code());
    }

}