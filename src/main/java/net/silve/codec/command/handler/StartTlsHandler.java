package net.silve.codec.command.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.AsciiString;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLEngine;

public class StartTlsHandler implements CommandHandler {

    public static final SmtpCommand STARTTLS = SmtpCommand.valueOf(AsciiString.cached("STAR"));
    private static final StartTlsHandler instance = new StartTlsHandler();

    public static StartTlsHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return STARTTLS.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session, SmtpServerConfiguration configuration) {
        return HandlerResult.newInstance(configuration.responses.responseStarttls, (ChannelHandlerContext ctx1) -> {
            if (configuration.getTls().isEnabled()) {
                final SSLEngine sslEngine = configuration.getTls().getSslCtx().newEngine(ctx1.channel().alloc());
                ctx1.pipeline().addFirst(new SslHandler(sslEngine, true));
            }
        });
    }
}
