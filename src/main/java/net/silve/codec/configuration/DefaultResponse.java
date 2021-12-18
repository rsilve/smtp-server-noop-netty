package net.silve.codec.configuration;

import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;

import java.util.HashMap;
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
    public static final String RESPONSE_TOO_MANY_RECIPIENTS_NAME = "RESPONSE_TOO_MANY_RECIPIENTS_NAME";
    public static final String RESPONSE_BAD_RECIPIENT_SYNTAX_NAME = "RESPONSE_BAD_RECIPIENT_SYNTAX_NAME";
    public static final String RESPONSE_BAD_RCPT_SYNTAX_NAME = "RESPONSE_BAD_RCPT_SYNTAX_NAME";
    public static final String RESPONSE_RECIPIENT_NEEDED_NAME = "RESPONSE_RECIPIENT_NEEDED_NAME";
    public static final String RESPONSE_END_DATA_MESSAGE_NAME = "RESPONSE_END_DATA_MESSAGE_NAME";
    public static final String RESPONSE_BYE_NAME = "RESPONSE_BYE_NAME";

    public static final AsciiString HOSTNAME = AsciiString.of("<hostname>");
    public static final Map<String, SmtpResponse> defaultResponsesMap = new HashMap() {{
        put(RESPONSE_UNKNOWN_COMMAND_NAME, new DefaultSmtpResponse(502, AsciiString.of("5.5.2 Error: command not recognized")));
        put(RESPONSE_BAD_SYNTAX_NAME, new DefaultSmtpResponse(500, AsciiString.of("5.5.2 Error: bad syntax")));
        put(RESPONSE_GREETING_NAME, new DefaultSmtpResponse(220, AsciiString.of("silve.net ESMTP")));
        put(RESPONSE_EHLO_NAME, new DefaultSmtpResponse(250, HOSTNAME, AsciiString.of("SIZE 20480000")));
        put(RESPONSE_EHLO_STARTTLS_NAME, new DefaultSmtpResponse(250, HOSTNAME, AsciiString.of("SIZE 20480000"), AsciiString.of("STARTTLS")));
        put(RESPONSE_HELO_NAME, new DefaultSmtpResponse(250, HOSTNAME));
        put(RESPONSE_STARTTLS_NAME, new DefaultSmtpResponse(220, AsciiString.of("Ready to start TLS")));
        put(RESPONSE_EMPTY_NAME, new DefaultSmtpResponse(500, AsciiString.of("SMTP Command required")));
        put(RESPONSE_SENDER_ALREADY_SPECIFIED_NAME, new DefaultSmtpResponse(503, AsciiString.of("5.5.1 Sender already specified")));
        put(RESPONSE_SENDER_NEEDED_NAME, new DefaultSmtpResponse(503, AsciiString.of("5.5.1 Error: need MAIL command")));
        put(RESPONSE_BAD_SENDER_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.1.7 Bad sender address syntax")));
        put(RESPONSE_BAD_MAIL_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.5.4 Syntax: MAIL FROM:<address>")));
        put(RESPONSE_MAIL_FROM_OK_NAME, new DefaultSmtpResponse(250, AsciiString.of("2.5.1 Ok")));
        put(RESPONSE_TOO_MANY_RECIPIENTS_NAME, new DefaultSmtpResponse(452, AsciiString.of("Error: too many recipients")));
        put(RESPONSE_BAD_RECIPIENT_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.1.7 Bad recipient address syntax")));
        put(RESPONSE_BAD_RCPT_SYNTAX_NAME, new DefaultSmtpResponse(501, AsciiString.of("5.5.4 Syntax: RCPT TO:<address>")));
        put(RESPONSE_RECIPIENT_NEEDED_NAME, new DefaultSmtpResponse(503, AsciiString.of("Error: need RCPT command")));
        put(RESPONSE_END_DATA_MESSAGE_NAME, new DefaultSmtpResponse(354, AsciiString.of("End data with <CR><LF>.<CR><LF>")));
        put(RESPONSE_BYE_NAME, new DefaultSmtpResponse(221, AsciiString.of("2.0.0 Bye")));
    }};

    public static final SmtpResponse RESPONSE_BYE = defaultResponsesMap.get(RESPONSE_BYE_NAME);
    public static final SmtpResponse RESPONSE_END_DATA_MESSAGE = defaultResponsesMap.get(RESPONSE_END_DATA_MESSAGE_NAME);
    public static final SmtpResponse RESPONSE_RECIPIENT_NEEDED = defaultResponsesMap.get(RESPONSE_RECIPIENT_NEEDED_NAME);
    public static final SmtpResponse RESPONSE_BAD_RCPT_SYNTAX = defaultResponsesMap.get(RESPONSE_BAD_RCPT_SYNTAX_NAME);
    public static final SmtpResponse RESPONSE_BAD_RECIPIENT_SYNTAX = defaultResponsesMap.get(RESPONSE_BAD_RECIPIENT_SYNTAX_NAME);
    public static final SmtpResponse RESPONSE_TOO_MANY_RECIPIENTS = defaultResponsesMap.get(RESPONSE_TOO_MANY_RECIPIENTS_NAME);
    public static final SmtpResponse RESPONSE_MAIL_FROM_OK = defaultResponsesMap.get(RESPONSE_MAIL_FROM_OK_NAME);
    public static final SmtpResponse RESPONSE_RCPT_OK = RESPONSE_MAIL_FROM_OK;
    public static final SmtpResponse RESPONSE_RSET_OK = RESPONSE_MAIL_FROM_OK;
    public static final SmtpResponse RESPONSE_BAD_MAIL_SYNTAX = defaultResponsesMap.get(RESPONSE_BAD_MAIL_SYNTAX_NAME);
    public static final SmtpResponse RESPONSE_BAD_SENDER_SYNTAX = defaultResponsesMap.get(RESPONSE_BAD_SENDER_SYNTAX_NAME);
    public static final SmtpResponse RESPONSE_SENDER_NEEDED = defaultResponsesMap.get(RESPONSE_SENDER_NEEDED_NAME);
    public static final SmtpResponse RESPONSE_SENDER_ALREADY_SPECIFIED = defaultResponsesMap.get(RESPONSE_SENDER_ALREADY_SPECIFIED_NAME);
    public static final SmtpResponse RESPONSE_EMPTY = defaultResponsesMap.get(RESPONSE_EMPTY_NAME);
    public static final SmtpResponse RESPONSE_STARTTLS = defaultResponsesMap.get(RESPONSE_STARTTLS_NAME);
    public static final SmtpResponse RESPONSE_HELO = defaultResponsesMap.get(RESPONSE_HELO_NAME);
    public static final SmtpResponse RESPONSE_EHLO_STARTTLS = defaultResponsesMap.get(RESPONSE_EHLO_STARTTLS_NAME);
    public static final SmtpResponse RESPONSE_EHLO = defaultResponsesMap.get(RESPONSE_EHLO_NAME);
    public static final SmtpResponse RESPONSE_GREETING = defaultResponsesMap.get(RESPONSE_GREETING_NAME);
    public static final SmtpResponse RESPONSE_BAD_SYNTAX = defaultResponsesMap.get(RESPONSE_BAD_SYNTAX_NAME);
    public static final SmtpResponse RESPONSE_UNKNOWN_COMMAND = defaultResponsesMap.get(RESPONSE_UNKNOWN_COMMAND_NAME);


    private DefaultResponse() {
    }

}
