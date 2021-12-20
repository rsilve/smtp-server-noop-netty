package net.silve.codec.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmtpServerConfigurationTlsTest {

    @Test
    void shouldInitializeWithEnabled() {
        String crt = getClass().getResource("/cert.pem").getFile();
        String key = getClass().getResource("/privkey.pem").getFile();
        SmtpServerConfigurationTls configurationTls = new SmtpServerConfigurationTls(true, crt, key);
        assertTrue(configurationTls.isEnabled());
        assertTrue(configurationTls.getSslCtx().isServer());
    }

    @Test
    void shouldInitializeWithDisabled() {
        String crt = getClass().getResource("/cert.pem").getFile();
        String key = getClass().getResource("/privkey.pem").getFile();
        SmtpServerConfigurationTls configurationTls = new SmtpServerConfigurationTls(false, crt, key);
        assertFalse(configurationTls.isEnabled());
        assertNull(configurationTls.getSslCtx());
    }

    @Test
    void shouldNotEnableIfNull() {
        SmtpServerConfigurationTls configurationTls = new SmtpServerConfigurationTls(false, null, null);
        assertFalse(configurationTls.isEnabled());
        assertNull(configurationTls.getSslCtx());
    }

    @Test
    void shouldNotEnableIfNull002() {
        String crt = getClass().getResource("/cert.pem").getFile();
        SmtpServerConfigurationTls configurationTls = new SmtpServerConfigurationTls(true, crt, null);
        assertFalse(configurationTls.isEnabled());
        assertNull(configurationTls.getSslCtx());
    }

    @Test
    void shouldNotEnableIfBlank() {
        SmtpServerConfigurationTls configurationTls = new SmtpServerConfigurationTls(false, " ", null);
        assertFalse(configurationTls.isEnabled());
        assertNull(configurationTls.getSslCtx());
    }

    @Test
    void shouldNotEnableIfBlank002() {
        String crt = getClass().getResource("/cert.pem").getFile();
        SmtpServerConfigurationTls configurationTls = new SmtpServerConfigurationTls(true, crt, " ");
        assertFalse(configurationTls.isEnabled());
        assertNull(configurationTls.getSslCtx());
    }

}