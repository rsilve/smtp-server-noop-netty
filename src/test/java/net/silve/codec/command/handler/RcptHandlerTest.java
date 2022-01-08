package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RcptHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveName() {
        CharSequence name = RcptHandler.singleton().getName();
        assertEquals(SmtpCommand.RCPT.name(), name);
    }

    @Test
    void shouldThrowExceptionIfTransactionNotStarted() {
        try {
            RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT), MessageSession.newInstance(), configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseSenderNeeded, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionIfTooManyRecipient() {
        try {
            MessageSession session = MessageSession.newInstance().setReversePath();
            IntStream.range(0, 51).forEach(value -> session.addForwardPath(AsciiString.of(String.valueOf(value))));
            RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT), session, configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseTooManyRecipients, e.getResponse());
        }
    }


    @Test
    void shouldThrowExceptionIfNoRecipient() {
        try {
            RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT), MessageSession.newInstance().setReversePath(), configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseRecipientNeeded, e.getResponse());
        }
    }

    @Test
    void shouldReturnResponse() throws InvalidProtocolException {
        HandlerResult handle = RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "recipient"), MessageSession.newInstance().setReversePath(), configuration);
        assertEquals(configuration.responses.responseRcptOk, handle.getResponse());
    }

    @Test
    void shouldReturnResponseMultipleRecipient() throws InvalidProtocolException {
        MessageSession session = MessageSession.newInstance().setReversePath();
        IntStream.range(0, 50).forEach(value -> session.addForwardPath(AsciiString.of(String.valueOf(value))));
        HandlerResult handle = RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "recipient"), session, configuration);
        assertEquals(configuration.responses.responseRcptOk, handle.getResponse());
        handle.getSessionAction().execute(session);
        assertTrue(session.tooManyForward(50));
    }


}