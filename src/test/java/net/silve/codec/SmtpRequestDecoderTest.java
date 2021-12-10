package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SmtpRequestDecoderTest {

    @Test
    void shouldDecodeRequest() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        ByteBuf buf = Unpooled.copiedBuffer("MAIL FROM:<name@domain.tld>\r\n".getBytes(StandardCharsets.UTF_8));
        assertTrue(channel.writeInbound(buf));
        assertTrue(channel.finish());
        DefaultSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.MAIL, req.command());
        assertEquals(AsciiString.of("name@domain.tld"), req.parameters().get(0));
    }

    @Test
    void shouldDecodeContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        assertTrue(channel.writeInbound(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8))));
        assertTrue(channel.writeInbound(Unpooled.copiedBuffer("test".getBytes(StandardCharsets.UTF_8))));
        assertTrue(channel.finish());
        DefaultSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.DATA, req.command());
        DefaultSmtpContent content = channel.readInbound();
        assertEquals("test", content.content().toString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldDecodeLastContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        assertTrue(channel.writeInbound(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8))));
        assertTrue(channel.writeInbound(Unpooled.wrappedBuffer(new byte[]{46, 13, 10})));
        assertTrue(channel.writeInbound(Unpooled.copiedBuffer("QUIT\r\n".getBytes(StandardCharsets.UTF_8))));
        assertTrue(channel.finish());
        DefaultSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.DATA, req.command());
        DefaultLastSmtpContent content = channel.readInbound();
        assertArrayEquals(new byte[]{46, 13, 10}, content.content().array());
        req = channel.readInbound();
        assertEquals(SmtpCommand.QUIT, req.command());
    }

    @Test
    void shouldThrowExceptionIfTooShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        ByteBuf buf = Unpooled.copiedBuffer("MAI\r\n".getBytes(StandardCharsets.UTF_8));
        assertFalse(channel.writeInbound(buf));
        assertTrue(channel.finish());
        DefaultSmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_UNKNOWN_COMMAND, response);
    }

    @Test
    void shouldReturnResponseIfInvalid() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        ByteBuf buf = Unpooled.copiedBuffer("MAIL FRO:<name@domain.tld>\r\n".getBytes(StandardCharsets.UTF_8));
        assertFalse(channel.writeInbound(buf));
        assertTrue(channel.finish());
        DefaultSmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_BAD_MAIL_SYNTAX, response);
    }

    @Test
    void shouldDecodeUnknownCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder());
        ByteBuf buf = Unpooled.copiedBuffer("TITI FROM:<name@domain.tld>\r\n".getBytes(StandardCharsets.UTF_8));
        assertTrue(channel.writeInbound(buf));
        assertTrue(channel.finish());
        DefaultSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.valueOf("TITI"), req.command());
        assertTrue(req.parameters().isEmpty());
    }

}