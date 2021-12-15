package net.silve.codec.request;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecyclableSmtpRequestTest {

    @Test
    void shouldExists() {
        RecyclableSmtpRequest instance = RecyclableSmtpRequest.newInstance(SmtpCommand.HELO);
        assertNotNull(instance);
        assertEquals(SmtpCommand.HELO, instance.command());
        assertTrue(instance.parameters().isEmpty());
        instance.recycle();
        assertNull(instance.command());
        assertNull(instance.parameters());

    }

    @Test
    void shouldAvoidNullCommand() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> RecyclableSmtpRequest.newInstance(null));
    }

    @Test
    void shouldExistsWithParameters() {
        RecyclableSmtpRequest instance = RecyclableSmtpRequest.newInstance(SmtpCommand.HELO, AsciiString.of("test"));
        assertNotNull(instance);
        assertEquals(SmtpCommand.HELO, instance.command());
        assertEquals(1, instance.parameters().size());
        assertEquals(AsciiString.of("test"), instance.parameters().get(0));
    }

    @Test
    void shouldAvoidNullCommandWithParameters() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> RecyclableSmtpRequest.newInstance(null, AsciiString.EMPTY_STRING));
    }


}