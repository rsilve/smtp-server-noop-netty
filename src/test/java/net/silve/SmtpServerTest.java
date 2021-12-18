package net.silve;

import net.silve.codec.configuration.SmtpServerConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmtpServerTest {

    @Test
    void shouldConfigure() {
        SmtpServer server = new SmtpServer();
        SmtpServerConfiguration configuration = server.configure();
        assertEquals(250, configuration.responses.responseHelo.code());
    }

}