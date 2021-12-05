package net.silve.tools;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.silve.codec.SmtpRequest;
import net.silve.codec.command.CommandHandler;
import net.silve.codec.command.InvalidProtocolException;
import net.silve.codec.session.MessageSession;

public class TestInboundHandler extends SimpleChannelInboundHandler<SmtpRequest> {

    private final CommandHandler handler;
    private MessageSession session;

    public TestInboundHandler(CommandHandler handler) {
        super();
        this.handler = handler;
    }

    public TestInboundHandler(CommandHandler handler, MessageSession session) {
        this(handler);
        this.session = session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SmtpRequest request) throws InvalidProtocolException {
        handler.execute(request, ctx, session).addListener(future -> ctx.writeAndFlush(future.get()).addListener(ChannelFutureListener.CLOSE));
    }
}
