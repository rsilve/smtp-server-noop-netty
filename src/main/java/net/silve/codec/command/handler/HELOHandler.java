package net.silve.codec.command.handler;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import org.jetbrains.annotations.NotNull;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
public class HELOHandler implements CommandHandler {

    private static final HELOHandler instance = new HELOHandler();

    public static HELOHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.HELO.name();
    }

    @Override
    public @NotNull HandlerResult handle(@NotNull SmtpRequest request, @NotNull MessageSession session) {
        return HandlerResult.from(ConstantResponse.RESPONSE_HELO);
    }
}

