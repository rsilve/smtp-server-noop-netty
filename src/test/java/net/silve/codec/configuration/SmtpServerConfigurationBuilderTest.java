package net.silve.codec.configuration;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmtpServerConfigurationBuilderTest {

    @Test
    void shouldHaveHostnameMethod() throws UnknownHostException {
        SmtpServerConfigurationBuilder builder = new SmtpServerConfigurationBuilder();
        builder.setHostname("host");
        assertEquals("host", builder.getHostname());
        builder.setHostname(null);
        assertEquals(InetAddress.getLocalHost().getHostName(), builder.getHostname());
        builder.setHostname(null);
        assertEquals(InetAddress.getLocalHost().getHostName(), builder.getHostname());
        builder.setHostname(" ");
        assertEquals(InetAddress.getLocalHost().getHostName(), builder.getHostname());
        builder.setRecipientMaxSize(30);
        assertEquals(30, builder.getRecipientMaxSize());
    }

    @Test
    void shouldHaveDefaultMaxRecipientSize() {
        SmtpServerConfigurationBuilder builder = new SmtpServerConfigurationBuilder();
        assertEquals(50, builder.getRecipientMaxSize());

    }
}