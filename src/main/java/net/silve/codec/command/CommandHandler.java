package net.silve.codec.command;

import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface CommandHandler {

     CharSequence getName();

    @NotNull
    HandlerResult handle(@NotNull SmtpRequest request, @NotNull MessageSession session) throws InvalidProtocolException;

    @NotNull
    default HandlerResult response(@NotNull SmtpRequest request, @NotNull MessageSession session) throws InvalidProtocolException {
        Objects.requireNonNull(request, "request object must be not null");
        Objects.requireNonNull(session, "session object must be not null");
        HandlerResult result = handle(request, session);
        Objects.requireNonNull(result, "handler must not return null");
        return result;
    }

}
