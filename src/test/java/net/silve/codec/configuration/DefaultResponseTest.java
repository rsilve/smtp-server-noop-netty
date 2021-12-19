package net.silve.codec.configuration;

import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import static net.silve.codec.configuration.DefaultResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultResponseTest {

    @Test
    void shouldReturnResponseUnknowCommand() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_UNKNOWN_COMMAND_NAME);
        assertEquals(502, response.code());
        assertEquals(AsciiString.of("5.5.2 Error: command not recognized"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadSyntax() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_BAD_SYNTAX_NAME);
        assertEquals(500, response.code());
        assertEquals(AsciiString.of("5.5.2 Error: bad syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseGreeting() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_GREETING_NAME);
        assertEquals(220, response.code());
        assertEquals(AsciiString.of("silve.net ESMTP"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEHLO() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_EHLO_NAME);
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("<hostname>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseHelo() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_HELO_NAME);
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("<hostname>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEmpty() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_EMPTY_NAME);
        assertEquals(500, response.code());
        assertEquals(AsciiString.of("SMTP Command required"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseSenderAlreadySpecified() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_SENDER_ALREADY_SPECIFIED_NAME);
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("5.5.1 Sender already specified"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseSenderNeeded() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_SENDER_NEEDED_NAME);
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("5.5.1 Error: need MAIL command"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadSenderSyntax() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_BAD_SENDER_SYNTAX_NAME);
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.1.7 Bad sender address syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadMailSyntax() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_BAD_MAIL_SYNTAX_NAME);
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.5.4 Syntax: MAIL FROM:<address>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseMailFromOk() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_MAIL_FROM_OK_NAME);
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseTooManyRecipient() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_TOO_MANY_RECIPIENTS_NAME);
        assertEquals(452, response.code());
        assertEquals(AsciiString.of("Error: too many recipients"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadRecipientSyntax() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_BAD_RECIPIENT_SYNTAX_NAME);
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.1.7 Bad recipient address syntax"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBadRcptSyntax() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_BAD_RCPT_SYNTAX_NAME);
        assertEquals(501, response.code());
        assertEquals(AsciiString.of("5.5.4 Syntax: RCPT TO:<address>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRcptOk() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_RCPT_OK_NAME);
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRecipientNeeded() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_RECIPIENT_NEEDED_NAME);
        assertEquals(503, response.code());
        assertEquals(AsciiString.of("Error: need RCPT command"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseEndDataMessage() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_END_DATA_MESSAGE_NAME);
        assertEquals(354, response.code());
        assertEquals(AsciiString.of("End data with <CR><LF>.<CR><LF>"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseRsetOk() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_RSET_OK_NAME);
        assertEquals(250, response.code());
        assertEquals(AsciiString.of("2.5.1 Ok"), response.details().get(0));
    }

    @Test
    void shouldReturnResponseBye() {
        SmtpResponse response = defaultResponsesMap.get(RESPONSE_BYE_NAME);
        assertEquals(221, response.code());
        assertEquals(AsciiString.of("2.0.0 Bye"), response.details().get(0));
    }
}