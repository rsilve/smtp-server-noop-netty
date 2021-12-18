package net.silve.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableLastSmtpContent;
import net.silve.codec.request.RecyclableSmtpContent;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.ssl.SslUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SmtpRequestHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldReturnGreeting() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        assertTrue(channel.finish());
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
    }

    @Test
    void shouldReturnResponseOnCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.HELO)));
        response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_HELO, response);
    }

    @Test
    void shouldReturnResponseOnCommand002() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.EHLO)));
        response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_EHLO, response);
    }


    @Test
    void shouldReturnResponseOnCommand003() {
        SslUtils.initialize(true);
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.EHLO)));
        response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_EHLO_STARTTLS, response);
    }

    @Test
    void shouldReturnResponseOnInvalidCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL)));
        response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_SENDER_NEEDED, response);
    }

    @Test
    void shouldReturnResponseOnInvalidCommand002() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_SENDER_NEEDED, response);
    }

    @Test
    void shouldReturnResponseOnUnknownCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.valueOf("FOO"))));
        response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_UNKNOWN_COMMAND, response);
    }

    @Test
    void shouldReturnResponseOnLastContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableLastSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        channel.readOutbound();
        channel.readOutbound();
        response = channel.readOutbound();
        assertEquals(250, response.code());
        assertTrue(response.details().get(0).toString().startsWith("2.0.0 Ok: queued as"));
    }

    @Test
    void shouldNotReturnResponseOnContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(DefaultResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        channel.readOutbound();
        channel.readOutbound();
        response = channel.readOutbound();
        assertNull(response);
    }
}