package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.command.CommandHandler;
import net.silve.codec.command.HandlerResult;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

public class EmptyHandler implements CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.EMPTY.name();
    }

    @Override
    public @NotNull HandlerResult handle(@NotNull SmtpRequest request, @NotNull MessageSession session) {
        return HandlerResult.from(ConstantResponse.RESPONSE_EMPTY);
    }


}
