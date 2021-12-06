package net.silve.codec.command.handler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;


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

    @Override
    public @NotNull HandlerResult handle(@NotNull SmtpRequest request, @NotNull final MessageSession session) throws InvalidProtocolException {
        if (session.isTransactionStarted()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_ALREADY_SPECIFIED);
        }

        if (request.parameters().isEmpty()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_NEEDED);
        }

        final CharSequence reversePath = request.parameters().get(0);
        session.setReversePath(AsciiString.of(reversePath));
        session.setTransactionStarted(true);
        return HandlerResult.from(ConstantResponse.RESPONSE_MAIL_FROM_OK);

    }
}
