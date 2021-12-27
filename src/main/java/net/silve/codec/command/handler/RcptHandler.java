package net.silve.codec.command.handler;


import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

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
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session, SmtpServerConfiguration configuration) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseSenderNeeded);
        }
        if (session.tooManyForward(50)) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseTooManyRecipients);
        }
        if (request.parameters().isEmpty()) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseRecipientNeeded);
        }
        session.addForwardPath(AsciiString.of(request.parameters().get(0)));
        return new HandlerResult(configuration.responses.responseRcptOk, (MessageSession session1) -> session1.addForwardPath(AsciiString.of(request.parameters().get(0))));
    }

}
