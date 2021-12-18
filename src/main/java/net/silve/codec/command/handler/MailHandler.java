package net.silve.codec.command.handler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;


@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
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
    public HandlerResult handle(RecyclableSmtpRequest request, final MessageSession session) throws InvalidProtocolException {
        if (session.isTransactionStarted()) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_SENDER_ALREADY_SPECIFIED);
        }

        if (request.parameters().isEmpty()) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_SENDER_NEEDED);
        }

        final CharSequence reversePath = request.parameters().get(0);
        if (reversePath.length() == 0) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_BAD_MAIL_SYNTAX);
        }
        return new HandlerResult(DefaultResponse.RESPONSE_MAIL_FROM_OK, MessageSession::setReversePath);

    }
}
