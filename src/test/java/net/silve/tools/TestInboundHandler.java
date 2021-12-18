package net.silve.tools;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.silve.codec.command.handler.CommandHandler;
import net.silve.codec.command.handler.HandlerResult;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import java.util.Objects;

public class TestInboundHandler extends SimpleChannelInboundHandler<RecyclableSmtpRequest> {

    private final CommandHandler handler;
    private final MessageSession session;

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    public TestInboundHandler(CommandHandler handler) {
        this(handler, MessageSession.newInstance());
    }

    public TestInboundHandler(CommandHandler handler, MessageSession session) {
        super();
        this.handler = handler;
        this.session = session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RecyclableSmtpRequest request) throws InvalidProtocolException {
        Objects.requireNonNull(request);
        HandlerResult result = handler.response(request, session, configuration);
        ctx.writeAndFlush(result.getResponse()).addListener(ChannelFutureListener.CLOSE);
    }
}
