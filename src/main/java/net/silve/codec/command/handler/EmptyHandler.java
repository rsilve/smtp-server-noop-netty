package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

public class EmptyHandler implements CommandHandler {

    private static final EmptyHandler instance = new EmptyHandler();

    public static EmptyHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.EMPTY.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session, SmtpServerConfiguration configuration) {
        return HandlerResult.from(configuration.responses.responseEmpty);
    }


}
