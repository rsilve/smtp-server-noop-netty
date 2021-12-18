package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MailHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveName() {
        CharSequence name = new MailHandler().getName();
        assertEquals(SmtpCommand.MAIL.name(), name);
    }

    @Test
    void shouldReturnResponse() throws InvalidProtocolException {
        HandlerResult handle = new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "test@domain.tld"), MessageSession.newInstance(), configuration);
        assertEquals(configuration.responses.responseMailFromOk, handle.getResponse());
    }

    @Test
    void shouldThrowExceptionIfTransactionAlreadStarted() {
        try {
            HandlerResult handle = new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, "test@domain.tld"), MessageSession.newInstance().setReversePath(), configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseSenderAlreadySpecified, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionMailIsEmtpy() {
        try {
            new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL, ""), MessageSession.newInstance(), configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseBadMailSyntax, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionRequestParameterIsEmpty() {
        try {
            HandlerResult handle = new MailHandler().handle(RecyclableSmtpRequest.newInstance(SmtpCommand.MAIL), MessageSession.newInstance(), configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseSenderNeeded, e.getResponse());
        }
    }

}