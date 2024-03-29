package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

public class MailHandler implements CommandHandler {

    private static final MailHandler instance = new MailHandler();

    public static MailHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.MAIL.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, final MessageSession session, SmtpServerConfiguration configuration) throws InvalidProtocolException {
        if (session.isTransactionStarted()) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseSenderAlreadySpecified);
        }

        if (request.parameters().isEmpty()) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseSenderNeeded);
        }

        final CharSequence reversePath = request.parameters().get(0);
        if (reversePath.length() == 0) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseBadMailSyntax);
        }
        return HandlerResult.newInstance(configuration.responses.responseMailFromOk, MessageSession::setReversePath);

    }
}
