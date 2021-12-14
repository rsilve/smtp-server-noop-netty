package net.silve.codec.response;

import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;

public class ConstantResponse {

    public static final SmtpResponse RESPONSE_UNKNOWN_COMMAND = new DefaultSmtpResponse(502,
            AsciiString.of("5.5.2 Error: command not recognized"));
    public static final SmtpResponse RESPONSE_BAD_SYNTAX = new DefaultSmtpResponse(500,
            AsciiString.of("5.5.2 Error: bad syntax"));
    public static final SmtpResponse RESPONSE_GREETING = new DefaultSmtpResponse(220,
            AsciiString.of("silve.net ESMTP"));
    public static final SmtpResponse RESPONSE_EHLO = new DefaultSmtpResponse(250,
            AsciiString.of("<hostname>"),
            AsciiString.of("SIZE 20480000"), AsciiString.of("STARTTLS"));
    public static final SmtpResponse RESPONSE_HELO = new DefaultSmtpResponse(250,
            AsciiString.of("<hostname>"));
    public static final SmtpResponse RESPONSE_STARTTLS = new DefaultSmtpResponse(220,
            AsciiString.of("Ready to start TLS"));
    public static final SmtpResponse RESPONSE_EMPTY = new DefaultSmtpResponse(500,
            AsciiString.of("SMTP Command required"));
    public static final SmtpResponse RESPONSE_SENDER_ALREADY_SPECIFIED =
            new DefaultSmtpResponse(503,
                    AsciiString.of("5.5.1 Sender already specified"));
    public static final SmtpResponse RESPONSE_SENDER_NEEDED =
            new DefaultSmtpResponse(503,
                    AsciiString.of("5.5.1 Error: need MAIL command"));
    public static final SmtpResponse RESPONSE_BAD_SENDER_SYNTAX = new DefaultSmtpResponse(501,
            AsciiString.of("5.1.7 Bad sender address syntax"));
    public static final SmtpResponse RESPONSE_BAD_MAIL_SYNTAX = new DefaultSmtpResponse(501,
            AsciiString.of("5.5.4 Syntax: MAIL FROM:<address>"));
    public static final SmtpResponse RESPONSE_MAIL_FROM_OK =
            new DefaultSmtpResponse(250,
                    AsciiString.of("2.5.1 Ok"));
    public static final SmtpResponse RESPONSE_TOO_MANY_RECIPIENTS =
            new DefaultSmtpResponse(452,
                    AsciiString.of("Error: too many recipients"));
    public static final SmtpResponse RESPONSE_BAD_RECIPIENT_SYNTAX = new DefaultSmtpResponse(501,
            AsciiString.of("5.1.7 Bad recipient address syntax"));
    public static final SmtpResponse RESPONSE_BAD_RCPT_SYNTAX = new DefaultSmtpResponse(501,
            AsciiString.of("5.5.4 Syntax: RCPT TO:<address>"));
    public static final SmtpResponse RESPONSE_RCPT_OK = RESPONSE_MAIL_FROM_OK;
    public static final SmtpResponse RESPONSE_RECIPIENT_NEEDED =
            new DefaultSmtpResponse(503, AsciiString.of("Error: need RCPT command"));
    public static final SmtpResponse RESPONSE_END_DATA_MESSAGE =
            new DefaultSmtpResponse(354, AsciiString.of("End data with <CR><LF>.<CR><LF>"));
    public static final SmtpResponse RESPONSE_RSET_OK = RESPONSE_MAIL_FROM_OK;
    public static final SmtpResponse RESPONSE_BYE = new DefaultSmtpResponse(221,
            AsciiString.of("Bye"));

    private ConstantResponse() {
    }

}
