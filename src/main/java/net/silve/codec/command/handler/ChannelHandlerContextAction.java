package net.silve.codec.command.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicBoolean;

public interface ChannelHandlerContextAction {
    void execute(ChannelHandlerContext ctx, AtomicBoolean contentExpected);
}
