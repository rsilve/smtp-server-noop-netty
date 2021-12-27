package net.silve.codec.command.handler;

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
            throw InvalidProtocolException.newInstance(configuration.responses.responseSenderNeeded);
        }

        if (session.needForward()) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseRecipientNeeded);
        }

        if (content instanceof RecyclableLastSmtpContent) {
            session.completed();
            return HandlerResult.newInstance(
                    configuration.responses.responseDataOk,
                    MessageSession::completed);
        } else {
            return null;
        }
    }

}
