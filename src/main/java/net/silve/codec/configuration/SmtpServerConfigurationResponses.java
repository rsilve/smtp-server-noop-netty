package net.silve.codec.configuration;

import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

import static net.silve.codec.configuration.DefaultResponse.*;

public class SmtpServerConfigurationResponses {

    public final SmtpResponse responseServerError;
    public final SmtpResponse responseBye;
    public final SmtpResponse responseEndDataMessage;
    public final SmtpResponse responseRecipientNeeded;
    public final SmtpResponse responseBadRcptSyntax;
    public final SmtpResponse responseBadRecipientSyntax;
    public final SmtpResponse responseTooManyRecipients;
    public final SmtpResponse responseMailFromOk;
    public final SmtpResponse responseRcptOk;
    public final SmtpResponse responseRsetOk;
    public final SmtpResponse responseBadMailSyntax;
    public final SmtpResponse responseBadSenderSyntax;
    public final SmtpResponse responseSenderNeeded;
    public final SmtpResponse responseSenderAlreadySpecified;
    public final SmtpResponse responseStarttls;
    public final SmtpResponse responseHelo;
    public final SmtpResponse responseEhloStarttls;
    public final SmtpResponse responseEhlo;
    public final SmtpResponse responseGreeting;
    public final SmtpResponse responseBadSyntax;
    public final SmtpResponse responseUnknownCommand;

    public SmtpServerConfigurationResponses(@Nonnull Map<String, SmtpResponse> map, @Nonnull String banner, @Nonnull String hostname) {
        Objects.requireNonNull(map, "responses map is required");
        Objects.requireNonNull(banner, "banner is required");
        Objects.requireNonNull(hostname, "hostname is required");
        responseServerError = map.get(RESPONSE_SERVER_ERROR_NAME);
        responseBye = map.get(RESPONSE_BYE_NAME);
        responseEndDataMessage = map.get(RESPONSE_END_DATA_MESSAGE_NAME);
        responseRecipientNeeded = map.get(RESPONSE_RECIPIENT_NEEDED_NAME);
        responseBadRcptSyntax = map.get(RESPONSE_BAD_RCPT_SYNTAX_NAME);
        responseBadRecipientSyntax = map.get(RESPONSE_BAD_RECIPIENT_SYNTAX_NAME);
        responseTooManyRecipients = map.get(RESPONSE_TOO_MANY_RECIPIENTS_NAME);
        responseMailFromOk = map.get(RESPONSE_MAIL_FROM_OK_NAME);
        responseRcptOk = responseMailFromOk;
        responseRsetOk = responseMailFromOk;
        responseBadMailSyntax = map.get(RESPONSE_BAD_MAIL_SYNTAX_NAME);
        responseBadSenderSyntax = map.get(RESPONSE_BAD_SENDER_SYNTAX_NAME);
        responseSenderNeeded = map.get(RESPONSE_SENDER_NEEDED_NAME);
        responseSenderAlreadySpecified = map.get(RESPONSE_SENDER_ALREADY_SPECIFIED_NAME);
        responseStarttls = map.get(RESPONSE_STARTTLS_NAME);
        responseBadSyntax = map.get(RESPONSE_BAD_SYNTAX_NAME);
        responseUnknownCommand = map.get(RESPONSE_UNKNOWN_COMMAND_NAME);

        responseGreeting = new DefaultSmtpResponse(220, AsciiString.of(banner));
        responseHelo = new DefaultSmtpResponse(250, AsciiString.of(hostname));
        responseEhloStarttls = new DefaultSmtpResponse(250, AsciiString.of(hostname), SIZE, AsciiString.of("STARTTLS"));
        responseEhlo = new DefaultSmtpResponse(250, AsciiString.of(hostname), SIZE);
    }


}
