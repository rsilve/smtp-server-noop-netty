package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

public class EHLOHandler implements CommandHandler {

    private static final EHLOHandler instance = new EHLOHandler();

    public static EHLOHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.EHLO.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session) {
        if (session.isTlsEnabled()) {
            return HandlerResult.from(DefaultResponse.RESPONSE_EHLO_STARTTLS);
        } else {
            return HandlerResult.from(DefaultResponse.RESPONSE_EHLO);
        }
    }
}

