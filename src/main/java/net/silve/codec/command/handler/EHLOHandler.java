package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

public class EHLOHandler implements CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.EHLO.name();
    }

    @Override
    public HandlerResult handle(@NotNull SmtpRequest request, @NotNull MessageSession session) {
        return HandlerResult.from(ConstantResponse.RESPONSE_EHLO);
    }
}
