package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RSETHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = new RSETHandler().getName();
        assertEquals(SmtpCommand.RSET.name(), name);
    }

    @Test
    void shouldExecute() {
        HandlerResult handle = new RSETHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.EHLO), MessageSession.newInstance());
        assertEquals(ConstantResponse.RESPONSE_RSET_OK, handle.getResponse());
    }

}