package net.silve.codec;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import org.junit.jupiter.api.Test;

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
        assertFalse(channel.writeInbound(DefaultSmtpRequest.newInstance(SmtpCommand.HELO)));
        response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_HELO, response);
    }

    @Test
    void shouldReturnResponseOnInvalidCommand() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpRequestHandler());
        SmtpResponse response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_GREETING, response);
        assertFalse(channel.writeInbound(DefaultSmtpRequest.newInstance(SmtpCommand.MAIL)));
        response = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_SENDER_NEEDED, response);
    }

}