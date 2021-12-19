package net.silve.codec.configuration;

import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;

import java.util.Map;

class DefaultResponse {

    public static final String RESPONSE_UNKNOWN_COMMAND_NAME = "RESPONSE_UNKNOWN_COMMAND_NAME";
    public static final String RESPONSE_BAD_SYNTAX_NAME = "RESPONSE_BAD_SYNTAX_NAME";
    public static final String RESPONSE_GREETING_NAME = "RESPONSE_GREETING_NAME";
    public static final String RESPONSE_EHLO_NAME = "RESPONSE_EHLO_NAME";
    public static final String RESPONSE_EHLO_STARTTLS_NAME = "RESPONSE_EHLO_STARTTLS_NAME";
    public static final String RESPONSE_HELO_NAME = "RESPONSE_HELO_NAME";
    public static final String RESPONSE_STARTTLS_NAME = "RESPONSE_STARTTLS_NAME";
    public static final String RESPONSE_EMPTY_NAME = "RESPONSE_EMPTY_NAME";
    public static final String RESPONSE_SENDER_ALREADY_SPECIFIED_NAME = "RESPONSE_SENDER_ALREADY_SPECIFIED_NAME";
    public static final String RESPONSE_SENDER_NEEDED_NAME = "RESPONSE_SENDER_NEEDED_NAME";
    public static final String RESPONSE_BAD_SENDER_SYNTAX_NAME = "RESPONSE_BAD_SENDER_SYNTAX_NAME";
    public static final String RESPONSE_BAD_MAIL_SYNTAX_NAME = "RESPONSE_BAD_MAIL_SYNTAX_NAME";
    public static final String RESPONSE_MAIL_FROM_OK_NAME = "RESPONSE_MAIL_FROM_OK_NAME";
    public static final String RESPONSE_RCPT_OK_NAME = "RESPONSE_RCPT_OK_NAME";
    public static final String RESPONSE_RSET_OK_NAME = "RESPONSE_RSET_OK_NAME";
    public static final String RESPONSE_TOO_MANY_RECIPIENTS_NAME = "RESPONSE_TOO_MANY_RECIPIENTS_NAME";
    public static final String RESPONSE_BAD_RECIPIENT_SYNTAX_NAME = "RESPONSE_BAD_RECIPIENT_SYNTAX_NAME";
    public static final String RESPONSE_BAD_RCPT_SYNTAX_NAME = "RESPONSE_BAD_RCPT_SYNTAX_NAME";
    public static final String RESPONSE_RECIPIENT_NEEDED_NAME = "RESPONSE_RECIPIENT_NEEDED_NAME";
    public static final String RESPONSE_END_DATA_MESSAGE_NAME = "RESPONSE_END_DATA_MESSAGE_NAME";
    public static final String RESPONSE_BYE_NAME = "RESPONSE_BYE_NAME";

    public static final AsciiString HOSTNAME = AsciiString.of("<hostname>");

    public static final String OK = "2.5.1 Ok";

    public static final AsciiString SIZE = AsciiString.of("SIZE 20480000");
    public static final Map<String, SmtpResponse> defaultResponsesMap = Map.ofEntries(
            Map.entry(RESPONSE_UNKNOWN_COMMAND_NAME, new DefaultSmtpResponse(502, AsciiString.of("5.5.2 Error: command not recognized"))),
            Map.entry(RESPONSE_BAD_SYNTAX_NAME, new DefaultSmtpResponse(500, AsciiString.of("5.5.2 Error: bad syntax"))),
            Map.entry(RESPONSE_GREETING_NAME, new DefaultSmtpResponse(220, AsciiString.of("silve.net ESMTP"))),
            Map.entry(RESPONSE_EHLO_NAME, new DefaultSmtpResponse(250, HOSTNAME, SIZE)),
            Map.entry(RESPONSE_EHLO_STARTTLS_NAME, new DefaultSmtpResponse(250, HOSTNAME, SIZE, AsciiString.of("STARTTLS"))),
            Map.entry(RESPONSE_HELO_NAME, new DefaultSmtpResponse(250, HOSTNAME)),
            Map.entry(RESPONSE_STARTTLS_NAME, new DefaultSmtpResponse(220, AsciiString.of("Ready to start TLS"))),
            Map.entry(RESPONSE_EMPTY_NAME, new DefaultSmtpResponse(500, AsciiString.of("SMTP Command required"))),
            Map.entry(RESPONSE_SENDER_ALREADY_SPECIFIED_NAME, new DefaultSmtpResponse(503, AsciiString.of("5.5.1 Sender already specified"))),
            Map.entry(RESPONSE_SENDER_NEEDED_NAME, new DefaultSmtpResponse(503, AsciiString.of("5.5.1 Error: need MAIL command"))),
            Map.entry(RESPONSE_BAD_SENDER_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.1.7 Bad sender address syntax"))),
            Map.entry(RESPONSE_BAD_MAIL_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.5.4 Syntax: MAIL FROM:<address>"))),
            Map.entry(RESPONSE_MAIL_FROM_OK_NAME, new DefaultSmtpResponse(250, AsciiString.of(OK))),
            Map.entry(RESPONSE_TOO_MANY_RECIPIENTS_NAME, new DefaultSmtpResponse(452, AsciiString.of("Error: too many recipients"))),
            Map.entry(RESPONSE_BAD_RECIPIENT_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.1.7 Bad recipient address syntax"))),
            Map.entry(RESPONSE_BAD_RCPT_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.5.4 Syntax: RCPT TO:<address>"))),
            Map.entry(RESPONSE_RECIPIENT_NEEDED_NAME, new DefaultSmtpResponse(503, AsciiString.of("Error: need RCPT command"))),
            Map.entry(RESPONSE_RCPT_OK_NAME, new DefaultSmtpResponse(250, AsciiString.of(OK))),
            Map.entry(RESPONSE_END_DATA_MESSAGE_NAME, new DefaultSmtpResponse(354, AsciiString.of("End data with <CR><LF>.<CR><LF>"))),
            Map.entry(RESPONSE_RSET_OK_NAME, new DefaultSmtpResponse(250, AsciiString.of(OK))),
            Map.entry(RESPONSE_BYE_NAME, new DefaultSmtpResponse(221, AsciiString.of("2.0.0 Bye")))
    );

    private DefaultResponse() {
    }

}