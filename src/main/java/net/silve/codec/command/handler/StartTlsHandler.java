package net.silve.codec.command.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.AsciiString;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.session.MessageSession;
import net.silve.codec.ssl.SslUtils;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLEngine;

public class StartTlsHandler implements CommandHandler {

    public static final SmtpCommand STARTTLS = SmtpCommand.valueOf(AsciiString.cached("STAR"));
    private static final StartTlsHandler instance = new StartTlsHandler();
    private final SslContext sslCtx;

    public StartTlsHandler() {
        this.sslCtx = SslUtils.getSslCtx();
    }

    public static StartTlsHandler singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return STARTTLS.name();
    }

    @Nonnull
    @Override
    public HandlerResult handle(RecyclableSmtpRequest request, MessageSession session) {
        return new HandlerResult(ConstantResponse.RESPONSE_STARTTLS, (ChannelHandlerContext ctx1) -> {
            final SSLEngine sslEngine = sslCtx.newEngine(ctx1.channel().alloc());
            ctx1.pipeline().addFirst(new SslHandler(sslEngine, true));
        });
    }
}
