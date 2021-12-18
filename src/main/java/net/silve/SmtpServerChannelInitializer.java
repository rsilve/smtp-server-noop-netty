package net.silve;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import net.silve.codec.SmtpRequestDecoder;
import net.silve.codec.SmtpRequestHandler;
import net.silve.codec.SmtpResponseEncoder;
import net.silve.codec.configuration.SmtpServerConfiguration;

import javax.annotation.Nonnull;
import java.util.Objects;

class SmtpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final ByteBuf CRLF_DELIMITER = Unpooled.wrappedBuffer(new byte[]{13, 10});
    private final SmtpServerConfiguration configuration;

    public SmtpServerChannelInitializer(@Nonnull SmtpServerConfiguration configuration) {
        Objects.requireNonNull(configuration, "server configuration is required");
        this.configuration = configuration;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline()
                //.addLast(new ReadTimeoutHandler(1, TimeUnit.SECONDS))
                //.addLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                .addLast(new DelimiterBasedFrameDecoder(2000, false, CRLF_DELIMITER))
                .addLast(new SmtpResponseEncoder())
                .addLast(new SmtpRequestDecoder(configuration))
                .addLast(new SmtpRequestHandler(configuration));
    }
}
