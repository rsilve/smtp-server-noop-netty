package net.silve.codec.command;

import io.netty.handler.codec.smtp.SmtpResponse;

public class HandlerResult {


    private static final ChannelHandlerContextAction NOOP = ctx -> { /* do nothing */ };

    public static HandlerResult from(SmtpResponse response) {
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
