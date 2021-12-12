package net.silve.codec;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import net.silve.codec.command.handler.CommandHandler;
import net.silve.codec.command.CommandMap;
import net.silve.codec.command.handler.HandlerResult;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.session.MessageSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handles a server-side channel.
 */
public class SmtpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SmtpRequestHandler.class);

    private static final CommandMap commandMap = new CommandMap();

    private MessageSession messageSession;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        messageSession = MessageSession.newInstance();

        super.channelActive(ctx);
        logger.trace("[{}] connected", messageSession.getId());

        final SmtpResponse response = ConstantResponse.RESPONSE_GREETING;
        ctx.writeAndFlush(response);

    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof SmtpRequest) {
            readRequest(ctx, (SmtpRequest) msg);
        } else {
            readContent(ctx, msg);
        }
    }

    private void readContent(ChannelHandlerContext ctx, Object msg) {
        // ignore content
        if (msg instanceof LastSmtpContent) {
            ctx.writeAndFlush(new DefaultSmtpResponse(250, String.format("Ok queued as %s",
                    messageSession.getId()
            )));
            messageSession.completed();
        }
        ((SmtpContent) msg).recycle();
    }

    private void readRequest(ChannelHandlerContext ctx, SmtpRequest request) {
        final SmtpCommand command = request.command();
        final CommandHandler commandHandler = commandMap.getHandler(command.name());
        try {
            HandlerResult result = commandHandler.response(request, messageSession);
            messageSession.setLastCommand(command);
            result.getAction().execute(ctx);
            logger.trace("[{}] Request: {}, Response: {}", messageSession.getId(), request, result.getResponse());
            final ChannelFuture channelFuture = ctx.writeAndFlush(result.getResponse());
            if (result.getResponse().code() == 221 || result.getResponse().code() == 421) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (InvalidProtocolException e) {
            logger.error("Protocol error", e);
            ctx.writeAndFlush(e.getResponse());
        } catch (Exception e) {
            logger.error("error", e);
            ctx.writeAndFlush(e.getMessage());
        } finally {
            request.recycle();
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (messageSession.getCompletedAt() <= 0) {
            logger.warn("[{}] Last command was {}", messageSession.getId(), messageSession.getLastCommand());
        } else {
            logger.info("[{}] completed", messageSession.getId());
        }
        messageSession.recycle();
    }

}
