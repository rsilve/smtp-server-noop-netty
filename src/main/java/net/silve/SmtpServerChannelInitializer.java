package net.silve;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import net.silve.codec.SmtpRequestHandler;
import net.silve.codec.SmtpRequestDecoder;
import net.silve.codec.SmtpResponseEncoder;

import java.util.concurrent.TimeUnit;

class SmtpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final ByteBuf CRLF_DELIMITER = Unpooled.wrappedBuffer(new byte[]{13, 10});

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                //.addLast(new ReadTimeoutHandler(1, TimeUnit.SECONDS))
                //.addLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                .addLast(new DelimiterBasedFrameDecoder(2000, false, CRLF_DELIMITER))
                .addLast(new SmtpRequestDecoder())
                .addLast(new SmtpResponseEncoder())
                .addLast(new SmtpRequestHandler());
    }
}
