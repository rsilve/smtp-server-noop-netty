package net.silve.codec.command.handler;


import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.command.CommandHandler;
import net.silve.codec.command.HandlerResult;
import net.silve.codec.command.InvalidProtocolException;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

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
