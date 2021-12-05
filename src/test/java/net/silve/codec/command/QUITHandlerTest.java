package net.silve.codec.command;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import net.silve.codec.ConstantResponse;
import net.silve.codec.DefaultSmtpRequest;
import net.silve.codec.SmtpRequest;
import net.silve.tools.TestInboundHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class QUITHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = new QUITHandler().getName();
        assertEquals(SmtpCommand.QUIT.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        SimpleChannelInboundHandler<SmtpRequest> handler = new TestInboundHandler(new QUITHandler());
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        assertFalse(channel.writeInbound(DefaultSmtpRequest.newInstance(SmtpCommand.EHLO)));
        channel.finish();
        SmtpResponse outbound = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_BYE, outbound);
    }

}