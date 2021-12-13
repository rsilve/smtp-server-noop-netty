package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import net.silve.codec.response.ConstantResponse;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SmtpResponseEncoderTest {

    @Test
    void shouldEncodeResponse() {
        EmbeddedChannel channel = new EmbeddedChannel(new SmtpResponseEncoder());
        assertTrue(channel.writeOutbound(ConstantResponse.RESPONSE_HELO));
        assertTrue(channel.finish());
        ByteBuf buf = channel.readOutbound();
        assertEquals("250 <hostname>\r\n", buf.toString(StandardCharsets.UTF_8));
        assertFalse(channel.isActive());
        assertFalse(channel.isOpen());
    }

}