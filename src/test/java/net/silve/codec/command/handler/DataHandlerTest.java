package net.silve.codec.command.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DataHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveName() {
        CharSequence name = DataHandler.singleton().getName();
        assertEquals(SmtpCommand.DATA.name(), name);
    }

    @Test
    void shouldReturnResponse() throws InvalidProtocolException {
        MessageSession session = MessageSession.newInstance().setReversePath().addForwardPath(AsciiString.of("ee"));
        HandlerResult result = DataHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA),
                session, configuration);
        assertNotNull(result);
        assertEquals(configuration.responses.responseEndDataMessage, result.getResponse());
        ChannelHandlerContext mock = mock(ChannelHandlerContext.class);
        AtomicBoolean contentExpected = new AtomicBoolean(false);
        result.getAction().execute(mock, contentExpected);
        assertTrue(contentExpected.get());
        assertTrue(session.isAccepted());
    }


    @Test
    void shouldThrowExceptionIfSenderNeeded() {
        try {
            DataHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA), MessageSession.newInstance(), configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseSenderNeeded, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionIfTransactionAlreadStarted() {
        try {
            DataHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA), MessageSession.newInstance().setReversePath(), configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseRecipientNeeded, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionRequestParameterIsEmpty() throws InvalidProtocolException {

        HandlerResult handle = DataHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA), MessageSession.newInstance().setReversePath().addForwardPath(AsciiString.of("ee")), configuration);
        assertEquals(configuration.responses.responseEndDataMessage, handle.getResponse());
    }

    @Test
    void shouldThrowExceptionTooManyRecipients() {
        MessageSession session = MessageSession.newInstance().setReversePath();
        IntStream.range(0, 51).forEach(value -> session.addForwardPath(AsciiString.of("ee")));
        assertThrows(InvalidProtocolException.class, () -> DataHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA), session, configuration));

    }

}