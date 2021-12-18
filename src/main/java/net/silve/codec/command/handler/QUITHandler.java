package net.silve.codec.command.handler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class QUITHandler implements CommandHandler {

    private static final QUITHandler instance = new QUITHandler();

    public static QUITHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.QUIT.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session) {
        return HandlerResult.from(DefaultResponse.RESPONSE_BYE);
    }
}
