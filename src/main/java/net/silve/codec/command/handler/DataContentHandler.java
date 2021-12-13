package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.DefaultSmtpResponse;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.request.RecyclableLastSmtpContent;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

public class DataContentHandler {

    private static final DataContentHandler instance = new DataContentHandler();

    public static DataContentHandler singleton() {
        return instance;
    }

    public HandlerResult handle(Object content, @NotNull MessageSession session) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_SENDER_NEEDED);
        }

        if (session.getForwardPath() == null || session.getForwardPath().isEmpty()) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_RECIPIENT_NEEDED);
        }

        if (content instanceof RecyclableLastSmtpContent) {
            session.completed();
            return new HandlerResult(
                    new DefaultSmtpResponse(250, String.format("Ok queued as %s", session.getId())),
                    MessageSession::completed);
        } else {
            return null;
        }
    }

}
