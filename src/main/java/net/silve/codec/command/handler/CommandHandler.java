package net.silve.codec.command.handler;

import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface CommandHandler {

    CharSequence getName();


    @Nonnull
    HandlerResult handle(RecyclableSmtpRequest request, MessageSession session, SmtpServerConfiguration configuration) throws InvalidProtocolException;

    @Nonnull
    default HandlerResult response(RecyclableSmtpRequest request, MessageSession session, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        Objects.requireNonNull(request, "request object must be not null");
        Objects.requireNonNull(session, "session object must be not null");
        Objects.requireNonNull(configuration, "configuration object must be not null");
        return handle(request, session, configuration);
    }

}
