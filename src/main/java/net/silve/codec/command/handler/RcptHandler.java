package net.silve.codec.command.handler;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class RcptHandler implements CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.RCPT.name();
    }

    @Override
    public @NotNull HandlerResult handle(@NotNull SmtpRequest request, @NotNull MessageSession session) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_NEEDED);
        }

        if (session.getForwardPath().size() > 50) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_TOO_MANY_RECIPIENTS);
        }
        session.addForwardPath(AsciiString.of(request.parameters().get(0)));
        return HandlerResult.from(ConstantResponse.RESPONSE_RCPT_OK);
    }

}
