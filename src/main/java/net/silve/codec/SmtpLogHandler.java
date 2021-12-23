package net.silve.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.silve.codec.logger.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Handles a server-side channel.
 */
//@ChannelHandler.Sharable
public class SmtpLogHandler extends SimpleChannelInboundHandler<MessageState> {

    private static final Logger logger = LoggerFactory.getInstance();


    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.log(Level.INFO, "Connected from [{0}]", ctx.channel().remoteAddress());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.log(Level.INFO, "disconnect from [{0}]", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageState messageState) {
        switch (messageState) {
            case MESSAGE_COMPLETED:
                logger.log(Level.INFO, "message completed");
                break;
            case COMMAND_QUIT:
                logger.log(Level.INFO, "Server close connection");
                break;
            default:
                break;
        }
    }
}
