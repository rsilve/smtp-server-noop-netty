package net.silve.codec.command;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.session.MessageSession;
import net.silve.codec.SmtpRequest;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;
import io.netty.util.concurrent.Future;

public class RcptHandler extends CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.RCPT.name();
    }

    @Override
    public Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, MessageSession session) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_NEEDED);
        }

        if (session.getForwardPath().size() > 50) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_TOO_MANY_RECIPIENTS);
        }
        session.addForwardPath(AsciiString.of(request.parameters().get(0)));
        return promiseFrom(ConstantResponse.RESPONSE_RCPT_OK, ctx);
    }

}
