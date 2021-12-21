package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RSETHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveName() {
        CharSequence name = new RSETHandler().getName();
        assertEquals(SmtpCommand.RSET.name(), name);
    }

    @Test
    void shouldExecute() {
        HandlerResult handle = new RSETHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.EHLO), MessageSession.newInstance(), configuration);
        assertEquals(configuration.responses.responseRsetOk, handle.getResponse());
        MessageSession mock = mock(MessageSession.class);
        handle.getSessionAction().execute(mock);
        verify(mock).reset();
    }

}