package net.silve.codec;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import net.silve.codec.command.CommandMap;
import net.silve.codec.command.handler.CommandHandler;
import net.silve.codec.command.handler.DataContentHandler;
import net.silve.codec.command.handler.HandlerResult;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.logger.SmtpLogger;
import net.silve.codec.request.RecyclableSmtpContent;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Handles a server-side channel.
 */
public class SmtpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final CommandMap commandMap = CommandMap.getInstance();
    private final SmtpServerConfiguration configuration;
    private final AtomicBoolean contentExpected;

    private MessageSession messageSession;

    public SmtpRequestHandler(@Nonnull SmtpServerConfiguration configuration, @Nonnull AtomicBoolean contentExpected) {
        super();
        Objects.requireNonNull(configuration, "server configuration is required");
        this.configuration = configuration;
        Objects.requireNonNull(configuration, "content expected shared properties required");
        this.contentExpected = contentExpected;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        messageSession = MessageSession.newInstance();
        super.channelActive(ctx);
        final SmtpResponse response = configuration.responses.responseGreeting;
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RecyclableSmtpRequest) {
            readRequest(ctx, (RecyclableSmtpRequest) msg);
        } else {
            readContent(ctx, msg);
        }
    }

    private void readContent(ChannelHandlerContext ctx, Object msg) {
        DataContentHandler contentHandler = DataContentHandler.singleton();
        try {
            HandlerResult result = contentHandler.handle(msg, messageSession, configuration);
            if (!Objects.isNull(result)) {
                ctx.writeAndFlush(result.getResponse());
                result.recycle();
            }
        } catch (InvalidProtocolException e) {
            ctx.writeAndFlush(e.getResponse());
            e.recycle();
        } finally {
            if (msg instanceof RecyclableSmtpContent) {
                ((RecyclableSmtpContent) msg).recycle();
            }
        }
    }

    private void readRequest(ChannelHandlerContext ctx, RecyclableSmtpRequest request) {
        try {
            final SmtpCommand command = request.command();
            final CommandHandler commandHandler = commandMap.getHandler(command.name());
            HandlerResult result = commandHandler.response(request, messageSession, configuration);
            if (request.command().equals(SmtpCommand.RSET) || request.command().equals(SmtpCommand.QUIT)) {
                SmtpLogger.info(messageSession);
            }
            result.getSessionAction().execute(messageSession);
            result.getAction().execute(ctx, contentExpected);
            final ChannelFuture channelFuture = ctx.writeAndFlush(result.getResponse());

            if (result.getResponse().code() == 221 || result.getResponse().code() == 421) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
            result.recycle();
        } catch (InvalidProtocolException e) {
            messageSession.lastError(e.getResponse().details());
            ctx.writeAndFlush(e.getResponse());
            e.recycle();
        } catch (Exception e) {
            messageSession.lastError(e.getMessage());
            ctx.writeAndFlush(configuration.responses.responseUnknownCommand);
        } finally {
            request.recycle();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (Objects.nonNull(messageSession)) {
            messageSession.recycle();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        SmtpLogger.error(cause);
        ctx.writeAndFlush(configuration.responses.responseServerError).addListener(ChannelFutureListener.CLOSE);
    }
}
