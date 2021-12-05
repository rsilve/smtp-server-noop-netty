package net.silve.codec.command;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelHandlerContextAction {
    void execute(ChannelHandlerContext ctx);
}
