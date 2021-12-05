package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.DefaultSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmptyHandlerTest {
    @Test
    void shouldHaveName() {
        CharSequence name = new EmptyHandler().getName();
        assertEquals(SmtpCommand.EMPTY.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        HandlerResult handle = new EmptyHandler().handle(DefaultSmtpRequest.newInstance(SmtpCommand.HELO), MessageSession.newInstance());
        assertEquals(ConstantResponse.RESPONSE_EMPTY, handle.getResponse());
    }

}