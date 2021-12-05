package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.DefaultSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MailHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = new MailHandler().getName();
        assertEquals(SmtpCommand.MAIL.name(), name);
    }

    @Test
    void shouldReturnResponse() throws InvalidProtocolException {
        HandlerResult handle = new MailHandler().handle(DefaultSmtpRequest.newInstance(SmtpCommand.MAIL, "test@domain.tld"), MessageSession.newInstance());
        assertEquals(ConstantResponse.RESPONSE_MAIL_FROM_OK, handle.getResponse());
    }

    @Test
    void shouldThrowExceptionIfTransactionAlreadStarted()  {
        try {
            HandlerResult handle = new MailHandler()
                    .handle(DefaultSmtpRequest.newInstance(SmtpCommand.MAIL, "test@domain.tld"),
                            MessageSession.newInstance().setTransactionStarted(true));
            assertEquals(ConstantResponse.RESPONSE_MAIL_FROM_OK, handle.getResponse());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_SENDER_ALREADY_SPECIFIED, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionRequestParameterIsEmpty()  {
        try {
            HandlerResult handle = new MailHandler()
                    .handle(DefaultSmtpRequest.newInstance(SmtpCommand.MAIL),
                            MessageSession.newInstance());
            assertEquals(ConstantResponse.RESPONSE_MAIL_FROM_OK, handle.getResponse());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_SENDER_NEEDED, e.getResponse());
        }
    }

}