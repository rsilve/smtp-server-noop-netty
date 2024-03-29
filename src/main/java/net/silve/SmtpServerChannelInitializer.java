package net.silve;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import net.silve.codec.SmtpRequestDecoder;
import net.silve.codec.SmtpRequestHandler;
import net.silve.codec.SmtpResponseEncoder;
import net.silve.codec.configuration.SmtpServerConfiguration;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class SmtpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final ByteBuf CRLF_DELIMITER = Unpooled.wrappedBuffer(new byte[]{13, 10});
    private static final SmtpResponseEncoder RESPONSE_ENCODER = new SmtpResponseEncoder();
    private final SmtpServerConfiguration configuration;

    public SmtpServerChannelInitializer(@Nonnull SmtpServerConfiguration configuration) {
        Objects.requireNonNull(configuration, "server configuration is required");
        this.configuration = configuration;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        AtomicBoolean contentExpected = new AtomicBoolean(false);
        ch.pipeline()
                .addLast(new ReadTimeoutHandler(1, TimeUnit.MINUTES))
                .addLast(new WriteTimeoutHandler(1, TimeUnit.MINUTES))
                .addLast(new DelimiterBasedFrameDecoder(2000, false, CRLF_DELIMITER))
                .addLast(RESPONSE_ENCODER)
                .addLast(new SmtpRequestDecoder(configuration, contentExpected))
                .addLast(new SmtpRequestHandler(configuration, contentExpected));
    }
}
