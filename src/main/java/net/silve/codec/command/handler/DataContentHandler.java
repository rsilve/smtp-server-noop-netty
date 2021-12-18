package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableLastSmtpContent;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

public class DataContentHandler {

    private static final DataContentHandler instance = new DataContentHandler();

    public static DataContentHandler singleton() {
        return instance;
    }

    public HandlerResult handle(Object content, MessageSession session, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(configuration.responses.responseSenderNeeded);
        }

        if (session.needForward()) {
            throw new InvalidProtocolException(configuration.responses.responseRecipientNeeded);
        }

        if (content instanceof RecyclableLastSmtpContent) {
            session.completed();
            return new HandlerResult(
                    new DefaultSmtpResponse(250, String.format("2.0.0 Ok: queued as %s", session.getId())),
                    MessageSession::completed);
        } else {
            return null;
        }
    }

}
