package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SmtpRequestDecoderTest {

    @Test
    void shouldDecodeRequest() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        ByteBuf buf = Unpooled.copiedBuffer("MAIL FROM:<name@domain.tld>".getBytes(StandardCharsets.UTF_8));
        assertTrue(channel.writeInbound(buf));
        assertTrue(channel.finish());
        DefaultSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.MAIL, req.command());
        assertEquals(AsciiString.of("name@domain.tld"), req.parameters().get(0));
    }

    @Test
    void shouldThrowExceptionIfTooShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        ByteBuf buf = Unpooled.copiedBuffer("MAIL".getBytes(StandardCharsets.UTF_8));
        try {
            channel.writeInbound(buf);
            fail();
        } catch (DecoderException e) {
            assertEquals("Received invalid line: 'MAIL'", e.getMessage());
        }
    }

    @Test
    void shouldThrowExceptionIfInvalid() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        ByteBuf buf = Unpooled.copiedBuffer("MAIL FRO:<name@domain.tld>".getBytes(StandardCharsets.UTF_8));
        try {
            channel.writeInbound(buf);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid protocol: 'MAIL FROM:' command required", e.getMessage());
        }
    }

}