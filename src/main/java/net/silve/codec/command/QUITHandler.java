package net.silve.codec.command;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.session.MessageSession;
import net.silve.codec.SmtpRequest;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QUITHandler extends CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(QUITHandler.class);

    @Override
    public CharSequence getName() {
        return SmtpCommand.QUIT.name();
    }

    @Override
    public Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, MessageSession session) {
        logger.debug("Session was {}", session);
        return promiseFrom(ConstantResponse.RESPONSE_BYE, ctx);
    }
}
