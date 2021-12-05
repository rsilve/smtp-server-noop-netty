package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

public class DataHandler implements CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.DATA.name();
    }

    @Override
    public HandlerResult handle(@NotNull SmtpRequest request, @NotNull MessageSession session) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_NEEDED);
        }

        if (session.getForwardPath() == null || session.getForwardPath().isEmpty()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_RECIPIENT_NEEDED);
        }

        return HandlerResult.from(ConstantResponse.RESPONSE_END_DATA_MESSAGE);
    }

}
