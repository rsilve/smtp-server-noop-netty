package net.silve.tools;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.silve.codec.SmtpRequest;
import net.silve.codec.command.handler.CommandHandler;
import net.silve.codec.command.handler.HandlerResult;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.session.MessageSession;

import java.util.Objects;

public class TestInboundHandler extends SimpleChannelInboundHandler<SmtpRequest> {

    private final CommandHandler handler;
    private final MessageSession session;

    public TestInboundHandler(CommandHandler handler) {
        this(handler, MessageSession.newInstance());
    }

    public TestInboundHandler(CommandHandler handler, MessageSession session) {
        super();
        this.handler = handler;
        this.session = session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SmtpRequest request) throws InvalidProtocolException {
        Objects.requireNonNull(request);
        HandlerResult result = handler.response(request, session);
        ctx.writeAndFlush(result.getResponse()).addListener(ChannelFutureListener.CLOSE);
    }
}
