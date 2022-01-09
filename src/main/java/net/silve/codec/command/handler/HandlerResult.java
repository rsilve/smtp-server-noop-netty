package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.Recycler;

public class HandlerResult {

    private static final ChannelHandlerContextAction NOOP = (ctx, contentExpected) -> { /* do nothing */ };
    private static final MessageSessionAction DEFAULT = session -> { /* do nothing */ };
    private static final Recycler<HandlerResult> RECYCLER = new Recycler<>() {
        protected HandlerResult newObject(Handle<HandlerResult> handle) {
            return new HandlerResult(handle);
        }
    };
    private final Recycler.Handle<HandlerResult> handle;
    private SmtpResponse response;
    private ChannelHandlerContextAction action = NOOP;
    private MessageSessionAction sessionAction = DEFAULT;

    private HandlerResult(Recycler.Handle<HandlerResult> handle) {
        this.handle = handle;
    }
    
    public static HandlerResult newInstance(SmtpResponse response) {
        final HandlerResult obj = RECYCLER.get();
        obj.response = response;
        return obj;
    }

    public static HandlerResult newInstance(SmtpResponse response, ChannelHandlerContextAction action) {
        final HandlerResult obj = RECYCLER.get();
        obj.response = response;
        obj.action = action;
        return obj;
    }

    public static HandlerResult newInstance(SmtpResponse response, MessageSessionAction sessionAction) {
        final HandlerResult obj = RECYCLER.get();
        obj.response = response;
        obj.sessionAction = sessionAction;
        return obj;
    }

    public void recycle() {
        this.response = null;
        this.action = NOOP;
        this.sessionAction = DEFAULT;
        handle.recycle(this);
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
