package net.silve.codec.command;

import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.concurrent.Future;
import net.silve.codec.session.MessageSession;
import net.silve.codec.SmtpRequest;

public abstract class CommandHandler {

    public abstract CharSequence getName();
    public abstract Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, MessageSession session) throws InvalidProtocolException;

    protected Future<SmtpResponse> promiseFrom(SmtpResponse response, ChannelHandlerContext ctx) {
       return ctx.executor().newSucceededFuture(response);
    }

}
