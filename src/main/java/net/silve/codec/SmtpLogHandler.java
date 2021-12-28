package net.silve.codec;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.silve.codec.logger.SmtpLogger;


/**
 * Handles a server-side channel.
 */
@ChannelHandler.Sharable
public class SmtpLogHandler extends SimpleChannelInboundHandler<MessageState> {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        SmtpLogger.info("Connected from [{}]", ctx.channel().remoteAddress());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        SmtpLogger.info("disconnect from [{}]", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageState messageState) {
        switch (messageState) {
            case MESSAGE_COMPLETED:
                SmtpLogger.info("message completed");
                break;
            case COMMAND_QUIT:
                SmtpLogger.info("Server close connection");
                break;
            case FATAL_ERROR:
                SmtpLogger.info("Server error. Close connection");
                break;
            case ERROR:
                SmtpLogger.info("Server error");
                break;
            default:
                break;
        }
    }
}
