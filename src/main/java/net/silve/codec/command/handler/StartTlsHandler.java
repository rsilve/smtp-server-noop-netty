package net.silve.codec.command.handler;


import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.AsciiString;
import net.silve.codec.ConstantResponse;
import net.silve.codec.SmtpRequest;
import net.silve.codec.session.MessageSession;
import net.silve.codec.ssl.SslUtils;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLEngine;

public class StartTlsHandler implements CommandHandler {
    public static final SmtpCommand STARTTLS = SmtpCommand.valueOf(AsciiString.cached("STAR"));
    private final SslContext sslCtx;

    public StartTlsHandler() {
        this.sslCtx = SslUtils.getSslCtx();
    }

    @Override
    public CharSequence getName() {
        return STARTTLS.name();
    }

    @Override
    public @NotNull HandlerResult handle(@NotNull SmtpRequest request, @NotNull MessageSession session) {
        return new HandlerResult(ConstantResponse.RESPONSE_STARTTLS, ctx1 -> {
            final SSLEngine sslEngine = sslCtx.newEngine(ctx1.channel().alloc());
            ctx1.pipeline().addFirst(new SslHandler(sslEngine, true));
        });
    }
}
