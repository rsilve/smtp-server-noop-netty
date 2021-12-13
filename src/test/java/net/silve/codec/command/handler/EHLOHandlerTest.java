package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EHLOHandlerTest {
    @Test
    void shouldHaveName() {
        CharSequence name = new EHLOHandler().getName();
        assertEquals(SmtpCommand.EHLO.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        HandlerResult handle = new EHLOHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.HELO), MessageSession.newInstance());
        assertEquals(ConstantResponse.RESPONSE_EHLO, handle.getResponse());
    }

}