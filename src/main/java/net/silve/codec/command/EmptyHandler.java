package net.silve.codec.command;

import net.silve.codec.ConstantResponse;
import net.silve.codec.session.MessageSession;
import net.silve.codec.SmtpRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.concurrent.Future;

public class EmptyHandler extends CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.EMPTY.name();
    }

    @Override
    public Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, MessageSession session) {
        return promiseFrom(ConstantResponse.RESPONSE_EMPTY, ctx);
    }


}
