package net.silve.codec.command.handler;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelHandlerContextAction {
    void execute(ChannelHandlerContext ctx);
}
