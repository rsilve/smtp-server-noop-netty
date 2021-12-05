package net.silve.codec.command;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.DefaultSmtpRequest;
import net.silve.codec.command.handler.HELOHandler;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HELOHandlerTest {
    @Test
    void shouldHaveName() {
        CharSequence name = new HELOHandler().getName();
        assertEquals(SmtpCommand.HELO.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        HandlerResult handle = new HELOHandler().handle(DefaultSmtpRequest.newInstance(SmtpCommand.HELO), MessageSession.newInstance());
        assertEquals(ConstantResponse.RESPONSE_HELO, handle.getResponse());
    }

}