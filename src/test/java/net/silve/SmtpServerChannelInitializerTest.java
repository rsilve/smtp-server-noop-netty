package net.silve;

import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SmtpServerChannelInitializerTest {

    @Test
    void shouldUseConfiguration() {
        SmtpServerChannelInitializer initializer = new SmtpServerChannelInitializer(new SmtpServerConfiguration(new SmtpServerConfigurationBuilder()));
        assertNotNull(initializer);
    }
}