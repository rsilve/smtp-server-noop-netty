package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
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
        HandlerResult handle = new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "test@domain.tld"), MessageSession.newInstance());
        assertEquals(DefaultResponse.RESPONSE_MAIL_FROM_OK, handle.getResponse());
    }

    @Test
    void shouldThrowExceptionIfTransactionAlreadStarted() {
        try {
            HandlerResult handle = new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "test@domain.tld"), MessageSession.newInstance().setReversePath());
            assertEquals(DefaultResponse.RESPONSE_MAIL_FROM_OK, handle.getResponse());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_SENDER_ALREADY_SPECIFIED, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionMailIsEmtpy() {
        try {
            new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, ""), MessageSession.newInstance());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_BAD_MAIL_SYNTAX, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionRequestParameterIsEmpty() {
        try {
            HandlerResult handle = new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL), MessageSession.newInstance());
            assertEquals(DefaultResponse.RESPONSE_MAIL_FROM_OK, handle.getResponse());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_SENDER_NEEDED, e.getResponse());
        }
    }

}