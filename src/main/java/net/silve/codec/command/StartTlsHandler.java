package net.silve.codec.command;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.ConstantResponse;
import net.silve.codec.session.MessageSession;
import net.silve.codec.ssl.SslUtils;
import net.silve.codec.SmtpRequest;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.AsciiString;
import io.netty.util.concurrent.Future;

import javax.net.ssl.SSLEngine;

public class StartTlsHandler extends CommandHandler {
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
    public Future<SmtpResponse> execute(SmtpRequest request, ChannelHandlerContext ctx, MessageSession session) {
        final SSLEngine sslEngine = sslCtx.newEngine(ctx.channel().alloc());
        ctx.pipeline().addFirst(new SslHandler(sslEngine, true));
        return promiseFrom(ConstantResponse.RESPONSE_STARTTLS, ctx);
    }
}
