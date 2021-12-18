package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QUITHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = QUITHandler.singleton().getName();
        assertEquals(SmtpCommand.QUIT.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        HandlerResult handle = QUITHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.EHLO), MessageSession.newInstance());
        assertEquals(DefaultResponse.RESPONSE_BYE, handle.getResponse());
    }

}