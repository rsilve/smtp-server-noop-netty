package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

public class RSETHandler implements CommandHandler {

    private static final RSETHandler instance = new RSETHandler();

    public static RSETHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.RSET.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session, SmtpServerConfiguration configuration) {
        return new HandlerResult(configuration.responses.responseRsetOk, MessageSession::reset);
    }
}

