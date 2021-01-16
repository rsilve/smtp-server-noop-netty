package net.silve.codec.command;

import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.AsciiString;
import io.netty.util.concurrent.Future;


public class MailHandler extends CommandHandler {

    @Override
    public CharSequence getName() {
        return SmtpCommand.MAIL.name();
    }

    @Override
    public Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, final MessageSession session) throws InvalidProtocolException {
        if (session.isTransactionStarted()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_ALREADY_SPECIFIED);
        }

        final CharSequence reversePath = request.parameters().get(0);
        session.setReversePath(AsciiString.of(reversePath));
        session.setTransactionStarted(true);
        return promiseFrom(ConstantResponse.RESPONSE_MAIL_FROM_OK, ctx);

    }
}
