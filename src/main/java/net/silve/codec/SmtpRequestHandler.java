package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.concurrent.Future;
import net.silve.codec.session.MessageSession;
import net.silve.codec.command.CommandHandler;
import net.silve.codec.command.CommandMap;
import net.silve.codec.command.InvalidProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handles a server-side channel.
 */
public class SmtpRequestHandler extends ChannelInboundHandlerAdapter { // (1)

    private static final Logger logger = LoggerFactory.getLogger(SmtpRequestHandler.class);

    private static final CommandMap commandMap = new CommandMap();

    private MessageSession messageSession;

    private ByteBuf buffer = null;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception { // (1)
        messageSession = MessageSession.newInstance();

        super.channelActive(ctx);
        logger.trace("[{}] connected", messageSession.getId());

        final SmtpResponse response = ConstantResponse.RESPONSE_GREETING;
        ctx.writeAndFlush(response);

    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
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

    private void readRequest(ChannelHandlerContext ctx, SmtpRequest msg) {
        final SmtpRequest request = msg;
        final SmtpCommand command = request.command();
        final CommandHandler commandHandler = commandMap.getHandler(command.name());
        try {
            final Future<SmtpResponse> response = commandHandler.execute(request, ctx, messageSession);
            messageSession.setLastCommand(command);
            response.addListener(future -> {
                assert response == future;
                if (future.isSuccess()) {
                    final SmtpResponse object = (SmtpResponse) future.get();
                    logger.trace("[{}] Request: {}, Response: {}", messageSession.getId(), request, object);
                    final ChannelFuture channelFuture = ctx.writeAndFlush(object);
                    if (SmtpCommand.QUIT.equals(command)) {
                        channelFuture.addListener(ChannelFutureListener.CLOSE);
                    }

                } else {
                    final ChannelFuture channelFuture = ctx.writeAndFlush(ConstantResponse.RESPONSE_BYE);
                    channelFuture.addListener(ChannelFutureListener.CLOSE);
                }
                request.recycle();
            });
        } catch (InvalidProtocolException e) {
            logger.error("Protocol error", e);
            ctx.writeAndFlush(e.getResponse()).addListener(ChannelFutureListener.CLOSE);
            request.recycle();
        } catch (Exception e) {
            logger.error("error", e);
            ctx.writeAndFlush(e.getMessage()).addListener(ChannelFutureListener.CLOSE);
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        logger.warn("[{}] Last command was {}", messageSession.getId(), messageSession.getLastCommand());
        ctx.close();
    }


}
