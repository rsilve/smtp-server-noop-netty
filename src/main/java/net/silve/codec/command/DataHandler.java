package net.silve.codec.command;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.concurrent.Future;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;

public class DataHandler extends CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.DATA.name();
    }

    @Override
    public Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, MessageSession session) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_NEEDED);
        }

        if (session.getForwardPath() == null || session.getForwardPath().isEmpty()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_RECIPIENT_NEEDED);
        }

        return promiseFrom(ConstantResponse.RESPONSE_END_DATA_MESSAGE, ctx);
    }
}
