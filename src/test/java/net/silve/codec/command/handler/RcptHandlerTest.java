package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.ConstantResponse;
import net.silve.codec.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RcptHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = RcptHandler.singleton().getName();
        assertEquals(SmtpCommand.RCPT.name(), name);
    }

    @Test
    void shouldThrowExceptionIfTransactionNotStarted() {
        try {
            RcptHandler.singleton()
                    .handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT), MessageSession.newInstance());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_SENDER_NEEDED, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionIfTooManyRecipient() {
        try {
            ArrayList<AsciiString> list = new ArrayList<>(51);
            for (int i = 0; i < 51; i++) {
                list.add(AsciiString.of(String.valueOf(i)));
            }
            RcptHandler.singleton()
                    .handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT),
                            MessageSession.newInstance().setTransactionStarted(true).setForwardPath(list));
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_TOO_MANY_RECIPIENTS, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionIfNoRecipient() {
        try {
            RcptHandler.singleton()
                    .handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT),
                            MessageSession.newInstance().setTransactionStarted(true));
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_RECIPIENT_NEEDED, e.getResponse());
        }
    }

    @Test
    void shouldReturnResponse() throws InvalidProtocolException {
        HandlerResult handle = RcptHandler.singleton()
                .handle(RecyclableSmtpRequest.newInstance(SmtpCommand.RCPT, "recipient"),
                        MessageSession.newInstance().setTransactionStarted(true));
        assertEquals(ConstantResponse.RESPONSE_RCPT_OK, handle.getResponse());
    }


}