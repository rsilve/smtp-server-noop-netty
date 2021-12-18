package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RcptHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = RcptHandler.singleton().getName();
        assertEquals(SmtpCommand.RCPT.name(), name);
    }

    @Test
    void shouldThrowExceptionIfTransactionNotStarted() {
        try {
            RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT), MessageSession.newInstance());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_SENDER_NEEDED, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionIfTooManyRecipient() {
        try {
            MessageSession session = MessageSession.newInstance().setReversePath();
            IntStream.range(0, 51).forEach(value -> session.addForwardPath(AsciiString.of(String.valueOf(value))));
            RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT), session);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_TOO_MANY_RECIPIENTS, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionIfNoRecipient() {
        try {
            RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT), MessageSession.newInstance().setReversePath());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_RECIPIENT_NEEDED, e.getResponse());
        }
    }

    @Test
    void shouldReturnResponse() throws InvalidProtocolException {
        HandlerResult handle = RcptHandler.singleton().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "recipient"), MessageSession.newInstance().setReversePath());
        assertEquals(DefaultResponse.RESPONSE_RCPT_OK, handle.getResponse());
    }


}