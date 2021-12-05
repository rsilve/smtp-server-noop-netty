package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.DefaultSmtpRequest;
import net.silve.codec.command.HandlerResult;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QUITHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = new QUITHandler().getName();
        assertEquals(SmtpCommand.QUIT.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        HandlerResult handle = new QUITHandler().handle(DefaultSmtpRequest.newInstance(SmtpCommand.EHLO), MessageSession.newInstance());
        assertEquals(ConstantResponse.RESPONSE_BYE, handle.getResponse());
    }

}