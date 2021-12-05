package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class HandlerResult {


    private static final ChannelHandlerContextAction NOOP = ctx -> { /* do nothing */ };

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull HandlerResult from(@NotNull SmtpResponse response) {
        return new HandlerResult(response);
    }

    private final SmtpResponse response;
    private final ChannelHandlerContextAction action;

    public HandlerResult(SmtpResponse response) {
        this(response, NOOP);
    }

    public HandlerResult(SmtpResponse response, ChannelHandlerContextAction action) {
        this.response = response;
        this.action = action;
    }


    public SmtpResponse getResponse() {
        return response;
    }

    public ChannelHandlerContextAction getAction() {
        return action;
    }
}
