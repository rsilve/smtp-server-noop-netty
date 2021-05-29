package net.silve.codec.command;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.concurrent.Future;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;

public class RSETHandler extends  CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.RSET.name();
    }

    @Override
    public Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, MessageSession session) {
        return promiseFrom(ConstantResponse.RESPONSE_RSET_OK, ctx);
    }
}

