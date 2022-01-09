package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableLastSmtpContent;
import net.silve.codec.request.RecyclableSmtpContent;
import net.silve.codec.request.RecyclableSmtpRequest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class SmtpRequestDecoderTest {

    private final AtomicBoolean contentExpected = new AtomicBoolean(false);
    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldDecodeRequest() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder(configuration, contentExpected));
        ByteBuf buf = Unpooled.copiedBuffer("MAIL FROM:<name@domain.tld>\r\n".getBytes(StandardCharsets.UTF_8));
        assertTrue(channel.writeInbound(buf));
        assertTrue(channel.finish());
        RecyclableSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.MAIL, req.command());
        assertEquals(AsciiString.of("name@domain.tld"), req.parameters().get(0));
    }

    @Test
    void shouldDecodeContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder(configuration, new AtomicBoolean(true)));
        assertTrue(channel.writeInbound(Unpooled.copiedBuffer("test".getBytes(StandardCharsets.UTF_8))));
        assertTrue(channel.finish());
        RecyclableSmtpContent content = channel.readInbound();
        assertEquals("test", content.content().toString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldDecodeLastContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder(configuration, new AtomicBoolean(true)));
        assertTrue(channel.writeInbound(Unpooled.wrappedBuffer(new byte[]{46, 13, 10})));
        assertTrue(channel.writeInbound(Unpooled.copiedBuffer("QUIT\r\n".getBytes(StandardCharsets.UTF_8))));
        assertTrue(channel.finish());
        RecyclableLastSmtpContent content = channel.readInbound();
        assertArrayEquals(new byte[]{46, 13, 10}, content.content().array());
        RecyclableSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.QUIT, req.command());
    }

    @Test
    void shouldThrowExceptionIfTooShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder(configuration, contentExpected));
        ByteBuf buf = Unpooled.copiedBuffer("MAI\r\n".getBytes(StandardCharsets.UTF_8));
        assertFalse(channel.writeInbound(buf));
        assertTrue(channel.finish());
        DefaultSmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseUnknownCommand, response);
    }

    @Test
    void shouldReturnResponseIfInvalid() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder(configuration, contentExpected));
        ByteBuf buf = Unpooled.copiedBuffer("MAIL FRO:<name@domain.tld>\r\n".getBytes(StandardCharsets.UTF_8));
        assertFalse(channel.writeInbound(buf));
        assertTrue(channel.finish());
        DefaultSmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseBadMailSyntax, response);
    }

    @Test
    void shouldDecodeUnknownCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestDecoder(configuration, contentExpected));
        ByteBuf buf = Unpooled.copiedBuffer("TITI FROM:<name@domain.tld>\r\n".getBytes(StandardCharsets.UTF_8));
        assertTrue(channel.writeInbound(buf));
        assertTrue(channel.finish());
        RecyclableSmtpRequest req = channel.readInbound();
        assertEquals(SmtpCommand.valueOf("TITI"), req.command());
        assertTrue(req.parameters().isEmpty());
    }

}