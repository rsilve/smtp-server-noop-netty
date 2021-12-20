package net.silve.codec.command.handler;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static net.silve.codec.command.handler.StartTlsHandler.STARTTLS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StartTlsHandlerTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveName() {
        CharSequence name = new StartTlsHandler().getName();
        assertEquals(STARTTLS.name(), name);
    }

    @Test
    void shouldExecute() {
        HandlerResult handle = new StartTlsHandler().handle(RecyclableSmtpRequest.newInstance(STARTTLS),
                MessageSession.newInstance(), configuration);
        assertEquals(configuration.responses.responseStarttls, handle.getResponse());
        ChannelPipeline pipeline = mock(ChannelPipeline.class);
        ChannelHandlerContext mock = mock(ChannelHandlerContext.class);
        when(mock.pipeline()).thenReturn(pipeline);
        handle.getAction().execute(mock);
        verifyNoInteractions(pipeline);
    }

    @Test
    void shouldExecuteWithTls() {
        String crt = getClass().getResource("/cert.pem").getFile();
        String key = getClass().getResource("/privkey.pem").getFile();
        SmtpServerConfiguration configurationWithTls = new SmtpServerConfiguration(
                new SmtpServerConfigurationBuilder()
                        .setTls(true)
                        .setTlsKey(key)
                        .setTlsCert(crt)
        );
        HandlerResult handle = new StartTlsHandler().handle(RecyclableSmtpRequest.newInstance(STARTTLS),
                MessageSession.newInstance(), configurationWithTls);
        assertEquals(configurationWithTls.responses.responseStarttls, handle.getResponse());
        ChannelPipeline pipeline = mock(ChannelPipeline.class);
        Channel channel = mock(Channel.class);
        ByteBufAllocator alloc = mock(ByteBufAllocator.class);
        ChannelHandlerContext mock = mock(ChannelHandlerContext.class);
        when(mock.pipeline()).thenReturn(pipeline);
        when(channel.alloc()).thenReturn(alloc);
        when(mock.channel()).thenReturn(channel);
        handle.getAction().execute(mock);
        verify(pipeline).addFirst(any());
    }

}