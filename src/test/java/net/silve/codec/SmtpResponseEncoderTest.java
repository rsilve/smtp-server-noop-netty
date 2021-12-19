package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SmtpResponseEncoderTest {

    @Test
    void shouldEncodeResponse() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpResponseEncoder());
        assertTrue(channel.writeOutbound(new DefaultSmtpResponse(250, AsciiString.of("host"))));
        assertTrue(channel.finish());
        ByteBuf buf = channel.readOutbound();
        assertEquals("250 host\r\n", buf.toString(StandardCharsets.UTF_8));
        assertFalse(channel.isActive());
        assertFalse(channel.isOpen());
    }

}