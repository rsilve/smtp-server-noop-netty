package net.silve.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SmtpRequestHandlerTest {

    @Test
    void shouldReturnGreeting() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        assertTrue(channel.finish());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
    }

    @Test
    void shouldReturnResponseOnCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.HELO)));
        response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_HELO, response);
    }

    @Test
    void shouldReturnResponseOnInvalidCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL)));
        response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_SENDER_NEEDED, response);
    }

    @Test
    void shouldReturnResponseOnInvalidCommand002() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_SENDER_NEEDED, response);
    }

    @Test
    void shouldReturnResponseOnUnknownCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.valueOf("FOO"))));
        response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_UNKNOWN_COMMAND, response);
    }

    @Test
    void shouldReturnResponseOnLastContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableLastSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        channel.readOutbound();
        channel.readOutbound();
        response = channel.readOutbound();
        assertEquals(250, response.code());
        assertTrue(response.details().get(0).toString().startsWith("Ok queued"));
    }

    @Test
    void shouldNotReturnResponseOnContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        channel.readOutbound();
        channel.readOutbound();
        response = channel.readOutbound();
        assertNull(response);
    }
}