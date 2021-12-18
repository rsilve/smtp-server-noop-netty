package net.silve.codec.command.handler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class DataHandler implements CommandHandler {

    private static final DataHandler instance = new DataHandler();

    public static DataHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.DATA.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session, SmtpServerConfiguration configuration) throws InvalidProtocolException {
        if (!session.isTransactionStarted()) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_SENDER_NEEDED);
        }

        if (session.needForward()) {
            throw new InvalidProtocolException(DefaultResponse.RESPONSE_RECIPIENT_NEEDED);
        }

        return HandlerResult.from(DefaultResponse.RESPONSE_END_DATA_MESSAGE);
    }

}
