package net.silve.codec.command;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.silve.codec.ConstantResponse;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RSETHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = new RSETHandler().getName();
        assertEquals(SmtpCommand.RSET.name(), name);
    }

    @Test
    void shouldExecute() throws InterruptedException, ExecutionException {
        ChannelHandlerContext context = new EmbeddedChannel().pipeline().lastContext();
        Future<SmtpResponse> execute = new RSETHandler().execute(null, context, null);
        SmtpResponse smtpResponse = execute.await().get();
        assertEquals(ConstantResponse.RESPONSE_RSET_OK, smtpResponse);
    }

}