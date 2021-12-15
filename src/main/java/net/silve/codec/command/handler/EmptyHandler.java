package net.silve.codec.command.handler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.session.MessageSession;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class EmptyHandler implements CommandHandler {

    private static final EmptyHandler instance = new EmptyHandler();

    public static EmptyHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.EMPTY.name();
    }

    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session) {
        return HandlerResult.from(ConstantResponse.RESPONSE_EMPTY);
    }


}
