package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HELOHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveName() {
        CharSequence name = new HELOHandler().getName();
        assertEquals(SmtpCommand.HELO.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        HandlerResult handle = new HELOHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.HELO), MessageSession.newInstance(), configuration);
        assertEquals(configuration.responses.responseHelo, handle.getResponse());
    }

}