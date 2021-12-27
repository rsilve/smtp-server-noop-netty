package net.silve.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.configuration.SmtpServerConfigurationTls;
import net.silve.codec.request.RecyclableLastSmtpContent;
import net.silve.codec.request.RecyclableSmtpContent;
import net.silve.codec.request.RecyclableSmtpRequest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmtpRequestHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldReturnGreeting() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        assertTrue(channel.finish());
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
    }

    @Test
    void shouldReturnResponseOnCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.HELO)));
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseHelo, response);
    }

    @Test
    void shouldReturnResponseOnCommand002() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.EHLO)));
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseEhlo, response);
    }


    @Test
    void shouldReturnResponseOnCommand003() {
        SmtpServerConfiguration spy = spy(configuration);
        SmtpServerConfigurationTls mock = mock(SmtpServerConfigurationTls.class);
        when(mock.isEnabled()).thenReturn(true);
        when(spy.getTls()).thenReturn(mock);

        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(spy));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.EHLO)));
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseEhloStarttls, response);
    }

    @Test
    void shouldReturnResponseOnInvalidCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL)));
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseSenderNeeded, response);
    }

    @Test
    void shouldReturnResponseOnInvalidCommand002() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseSenderNeeded, response);
    }

    @Test
    void shouldReturnResponseOnUnknownCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertTrue(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.valueOf("FOO"))));
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseUnknownCommand, response);
    }

    @Test
    void shouldReturnResponseOnLastContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableLastSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        channel.readOutbound();
        channel.readOutbound();
        response = channel.readOutbound();
        assertEquals(250, response.code());
        assertTrue(response.details().get(0).toString().startsWith("2.0.0 Ok queued"));
    }

    @Test
    void shouldNotReturnResponseOnContent() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        channel.readOutbound();
        channel.readOutbound();
        response = channel.readOutbound();
        assertNull(response);
    }

    @Test
    void shouldReturnRecipientNeeded() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "rctp@domain.tld")));
        assertFalse(channel.writeInbound(RecyclableSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        channel.readOutbound();
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseRecipientNeeded, response);
    }

    @Test
    void shouldReturnFromNeeded() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler(configuration));
        SmtpResponse response = channel.readOutbound();
        assertEquals(configuration.responses.responseGreeting, response);
        assertFalse(channel.writeInbound(RecyclableSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)))));
        response = channel.readOutbound();
        assertEquals(configuration.responses.responseSenderNeeded, response);
    }

}