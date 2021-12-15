package net.silve.codec.command.handler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class EHLOHandler implements CommandHandler {

    private static final EHLOHandler instance = new EHLOHandler();

    public static EHLOHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.EHLO.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session) {
        return HandlerResult.from(ConstantResponse.RESPONSE_EHLO);
    }
}

