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
import net.silve.codec.request.RecyclableSmtpContent;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import net.silve.codec.ssl.SslUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;


/**
 * Handles a server-side channel.
 */
public class SmtpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SmtpRequestHandler.class);

    private static final CommandMap commandMap = new CommandMap();
    private final SmtpServerConfiguration configuration;

    private MessageSession messageSession;

    public SmtpRequestHandler(@Nonnull SmtpServerConfiguration configuration) {
        super();
        Objects.requireNonNull(configuration, "server configuration is required");
        this.configuration = configuration;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        messageSession = MessageSession.newInstance();
        if (SslUtils.isTlsEnabled()) {
            messageSession.tlsEnabled();
        }

        super.channelActive(ctx);
        logger.trace("[{}] connected", messageSession.getId());

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
            HandlerResult result = contentHandler.handle(msg, messageSession);
            if (!Objects.isNull(result)) {
                ctx.writeAndFlush(result.getResponse());
            }
        } catch (InvalidProtocolException e) {
            ctx.writeAndFlush(e.getResponse());
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
            result.getSessionAction().execute(messageSession);
            result.getAction().execute(ctx);
            logger.trace("[{}] Request: {}, Response: {}", messageSession.getId(), request, result.getResponse());
            final ChannelFuture channelFuture = ctx.writeAndFlush(result.getResponse());
            if (result.getResponse().code() == 221 || result.getResponse().code() == 421) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (InvalidProtocolException e) {
            ctx.writeAndFlush(e.getResponse());
        } catch (Exception e) {
            ctx.writeAndFlush(configuration.responses.responseUnknownCommand);
        } finally {
            request.recycle();
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        messageSession.recycle();
    }

}
