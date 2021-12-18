package net.silve.codec.response;

import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantResponseTest {

    @Test
    void shouldReturnResponseUnknowCommand() {
        SmtpResponse response = ConstantResponse.RESPONSE_UNKNOWN_COMMAND;
        assertEquals(502, response.code());
        assertEquals(AsciiString.of("5.5.2 Error: command not recognized"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadSyntax() {
        SmtpResponse response = ConstantResponse.RESPONSE_BAD_SYNTAX;
        assertEquals(500, response.code());
        assertEquals(AsciiString.of("5.5.2 Error: bad syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseGreeting() {
        SmtpResponse response = ConstantResponse.RESPONSE_GREETING;
        assertEquals(220, response.code());
        assertEquals(AsciiString.of("silve.net ESMTP"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEHLO() {
        SmtpResponse response = ConstantResponse.RESPONSE_EHLO;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("<hostname>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseHelo() {
        SmtpResponse response = ConstantResponse.RESPONSE_HELO;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("<hostname>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEmpty() {
        SmtpResponse response = ConstantResponse.RESPONSE_EMPTY;
        assertEquals(500, response.code());
        assertEquals(AsciiString.of("SMTP Command required"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseSenderAlreadySpecified() {
        SmtpResponse response = ConstantResponse.RESPONSE_SENDER_ALREADY_SPECIFIED;
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("5.5.1 Sender already specified"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseSenderNeeded() {
        SmtpResponse response = ConstantResponse.RESPONSE_SENDER_NEEDED;
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("5.5.1 Error: need MAIL command"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadSenderSyntax() {
        SmtpResponse response = ConstantResponse.RESPONSE_BAD_SENDER_SYNTAX;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.1.7 Bad sender address syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadMailSyntax() {
        SmtpResponse response = ConstantResponse.RESPONSE_BAD_MAIL_SYNTAX;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.5.4 Syntax: MAIL FROM:<address>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseMailFromOk() {
        SmtpResponse response = ConstantResponse.RESPONSE_MAIL_FROM_OK;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseTooManyRecipient() {
        SmtpResponse response = ConstantResponse.RESPONSE_TOO_MANY_RECIPIENTS;
        assertEquals(452, response.code());
        assertEquals(AsciiString.of("Error: too many recipients"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadRecipientSyntax() {
        SmtpResponse response = ConstantResponse.RESPONSE_BAD_RECIPIENT_SYNTAX;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.1.7 Bad recipient address syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadRcptSyntax() {
        SmtpResponse response = ConstantResponse.RESPONSE_BAD_RCPT_SYNTAX;
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.5.4 Syntax: RCPT TO:<address>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRcptOk() {
        SmtpResponse response = ConstantResponse.RESPONSE_RCPT_OK;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRecipientNeeded() {
        SmtpResponse response = ConstantResponse.RESPONSE_RECIPIENT_NEEDED;
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("Error: need RCPT command"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEndDataMessage() {
        SmtpResponse response = ConstantResponse.RESPONSE_END_DATA_MESSAGE;
        assertEquals(354, response.code());
        assertEquals(AsciiString.of("End data with <CR><LF>.<CR><LF>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRsetOk() {
        SmtpResponse response = ConstantResponse.RESPONSE_RSET_OK;
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBye() {
        SmtpResponse response = ConstantResponse.RESPONSE_BYE;
        assertEquals(221, response.code());
        assertEquals(AsciiString.of("2.0.0 Bye"), response.details().get(0));
    }
}