package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class HandlerResult {


    private static final ChannelHandlerContextAction NOOP = ctx -> { /* do nothing */ };
    private static final MessageSessionAction DEFAULT = session -> { /* do nothing */ };
    private final SmtpResponse response;
    private final ChannelHandlerContextAction action;
    private final MessageSessionAction sessionAction;

    public HandlerResult(SmtpResponse response) {
        this(response, NOOP, DEFAULT);
    }

    public HandlerResult(SmtpResponse response, ChannelHandlerContextAction action) {
        this(response, action, DEFAULT);
    }

    public HandlerResult(SmtpResponse response, MessageSessionAction sessionAction) {
        this(response, NOOP, sessionAction);
    }

    public HandlerResult(SmtpResponse response, ChannelHandlerContextAction action, MessageSessionAction sessionAction) {
        this.response = response;
        this.action = action;
        this.sessionAction = sessionAction;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull HandlerResult from(@NotNull SmtpResponse response) {
        return new HandlerResult(response);
    }

    public SmtpResponse getResponse() {
        return response;
    }

    public ChannelHandlerContextAction getAction() {
        return action;
    }

    public MessageSessionAction getSessionAction() {
        return sessionAction;
    }
}
