package net.silve.codec.command;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import net.silve.codec.ConstantResponse;
import net.silve.codec.DefaultSmtpRequest;
import net.silve.codec.SmtpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RSETHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = new RSETHandler().getName();
        assertEquals(SmtpCommand.RSET.name(), name);
    }

    @Test
    void shouldExecute() {
        SimpleChannelInboundHandler<SmtpRequest> handler = new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, SmtpRequest request) {
                RSETHandler rsetHandler = new RSETHandler();
                rsetHandler.execute(request, ctx, null).addListener(future -> ctx.writeAndFlush(future.get()).addListener(ChannelFutureListener.CLOSE));
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        assertFalse(channel.writeInbound(DefaultSmtpRequest.newInstance(SmtpCommand.EHLO)));
        channel.finish();
        SmtpResponse outbound = channel.readOutbound();
        assertEquals(ConstantResponse.RESPONSE_RSET_OK, outbound);
    }

}