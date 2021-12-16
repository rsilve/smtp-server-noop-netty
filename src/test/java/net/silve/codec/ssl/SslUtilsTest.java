package net.silve.codec.ssl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class SslUtilsTest {

    @Test
    void shouldInitialize() {
        SslUtils.initialize(false);
        assertFalse(SslUtils.isTlsEnabled());
        assertNull(SslUtils.getSslCtx());
    }

}