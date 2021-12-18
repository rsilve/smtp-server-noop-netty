package net.silve.codec.configuration;

import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmtpServerConfigurationResponsesTest {

    SmtpServerConfigurationResponses responses = new SmtpServerConfigurationResponses(DefaultResponse.defaultResponsesMap);

    @Test
    void shouldReturnResponseUnknownCommand() {
        SmtpResponse response = responses.responseUnknownCommand;
        assertEquals(502, response.code());
        assertEquals(AsciiString.of("5.5.2 Error: command not recognized"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadSyntax() {
        SmtpResponse response = responses.responseBadSyntax;
        assertEquals(500, response.code());
        assertEquals(AsciiString.of("5.5.2 Error: bad syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseGreeting() {
        SmtpResponse response = responses.responseGreeting;
        assertEquals(220, response.code());
        assertEquals(AsciiString.of("silve.net ESMTP"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEHLO() {
        SmtpResponse response = responses.responseEhlo;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("<hostname>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseHelo() {
        SmtpResponse response = responses.responseHelo;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("<hostname>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEmpty() {
        SmtpResponse response = responses.responseEmpty;
        assertEquals(500, response.code());
        assertEquals(AsciiString.of("SMTP Command required"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseSenderAlreadySpecified() {
        SmtpResponse response = responses.responseSenderAlreadySpecified;
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("5.5.1 Sender already specified"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseSenderNeeded() {
        SmtpResponse response = responses.responseSenderNeeded;
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("5.5.1 Error: need MAIL command"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadSenderSyntax() {
        SmtpResponse response = responses.responseBadSenderSyntax;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.1.7 Bad sender address syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadMailSyntax() {
        SmtpResponse response = responses.responseBadMailSyntax;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.5.4 Syntax: MAIL FROM:<address>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseMailFromOk() {
        SmtpResponse response = responses.responseMailFromOk;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseTooManyRecipient() {
        SmtpResponse response = responses.responseTooManyRecipients;
        assertEquals(452, response.code());
        assertEquals(AsciiString.of("Error: too many recipients"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadRecipientSyntax() {
        SmtpResponse response = responses.responseBadRecipientSyntax;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.1.7 Bad recipient address syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadRcptSyntax() {
        SmtpResponse response = responses.responseBadRcptSyntax;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.5.4 Syntax: RCPT TO:<address>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRcptOk() {
        SmtpResponse response = responses.responseRcptOk;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRecipientNeeded() {
        SmtpResponse response = responses.responseRecipientNeeded;
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("Error: need RCPT command"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEndDataMessage() {
        SmtpResponse response = responses.responseEndDataMessage;
        assertEquals(354, response.code());
        assertEquals(AsciiString.of("End data with <CR><LF>.<CR><LF>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRsetOk() {
        SmtpResponse response = responses.responseRsetOk;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBye() {
        SmtpResponse response = responses.responseBye;
        assertEquals(221, response.code());
        assertEquals(AsciiString.of("2.0.0 Bye"), response.details().get(0));
    }

}