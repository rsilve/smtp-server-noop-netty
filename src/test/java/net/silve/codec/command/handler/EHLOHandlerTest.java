package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.configuration.SmtpServerConfigurationTls;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EHLOHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveName() {
        CharSequence name = new EHLOHandler().getName();
        assertEquals(SmtpCommand.EHLO.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        HandlerResult handle = new EHLOHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.HELO), MessageSession.newInstance(), configuration);
        assertEquals(configuration.responses.responseEhlo, handle.getResponse());
    }

    @Test
    void shouldReturnResponseWithTLS() {
        SmtpServerConfiguration spy = spy(configuration);
        SmtpServerConfigurationTls mock = mock(SmtpServerConfigurationTls.class);
        when(mock.isEnabled()).thenReturn(true);
        when(spy.getTls()).thenReturn(mock);
        HandlerResult handle = new EHLOHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.HELO), MessageSession.newInstance(), spy);
        assertEquals(configuration.responses.responseEhloStarttls, handle.getResponse());
    }

}