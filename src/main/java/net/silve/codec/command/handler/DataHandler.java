package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

import static net.silve.codec.MessageState.MESSAGE_COMPLETED;

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
            throw InvalidProtocolException.newInstance(configuration.responses.responseSenderNeeded);
        }

        if (session.needForward()) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseRecipientNeeded);
        }

        if (session.tooManyForward(configuration.maxRecipientSize)) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseTooManyRecipients);
        }

        return HandlerResult.newInstance(configuration.responses.responseEndDataMessage, (ctx, contentExpected) -> {
            ctx.fireChannelRead(MESSAGE_COMPLETED);
            contentExpected.set(true);
        });
    }

}
