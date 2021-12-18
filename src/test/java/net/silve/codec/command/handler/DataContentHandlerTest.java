package net.silve.codec.command.handler;

import io.netty.buffer.Unpooled;
import io.netty.util.AsciiString;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableLastSmtpContent;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DataContentHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldThrowExceptionIfSenderNeeded() {
        InvalidProtocolException exception = assertThrows(InvalidProtocolException.class, () -> DataContentHandler.singleton().handle(null, MessageSession.newInstance(), configuration));

        assertEquals(configuration.responses.responseSenderNeeded, exception.getResponse());
    }

    @Test
    void shouldThrowExceptionIfRecipientNeeded() {
        InvalidProtocolException exception = assertThrows(InvalidProtocolException.class, () -> DataContentHandler.singleton().handle(null, MessageSession.newInstance().setReversePath(), configuration));

        assertEquals(configuration.responses.responseRecipientNeeded, exception.getResponse());
    }


    @Test
    void shouldIgnoreContent() throws InvalidProtocolException {
        HandlerResult result = DataContentHandler.singleton().handle(null, MessageSession.newInstance().setReversePath().addForwardPath(AsciiString.EMPTY_STRING), configuration);
        assertNull(result);
    }

    @Test
    void shouldNotIgnoreLastContent() throws InvalidProtocolException {
        MessageSession session = MessageSession.newInstance().setReversePath().addForwardPath(AsciiString.EMPTY_STRING);
        HandlerResult result = DataContentHandler.singleton().handle(RecyclableLastSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8))), session, configuration);
        assertNotNull(result);
        assertEquals(250, result.getResponse().code());
        result.getSessionAction().execute(session);
        assertTrue(session.duration() > 0);
    }

}