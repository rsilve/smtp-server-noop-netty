package net.silve.codec.command.handler;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class RcptHandler implements CommandHandler {

    private static final RcptHandler instance = new RcptHandler();

    public static RcptHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.RCPT.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_SENDER_NEEDED);
        }
        if (session.tooManyForward(50)) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_TOO_MANY_RECIPIENTS);
        }
        if (request.parameters().isEmpty()) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_RECIPIENT_NEEDED);
        }
        session.addForwardPath(AsciiString.of(request.parameters().get(0)));
        return new HandlerResult(DefaultResponse.RESPONSE_RCPT_OK, (MessageSession session1) -> session1.addForwardPath(AsciiString.of(request.parameters().get(0))));
    }

}
