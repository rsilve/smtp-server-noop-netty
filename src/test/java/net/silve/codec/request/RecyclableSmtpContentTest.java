package net.silve.codec.request;

import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class RecyclableSmtpContentTest {

    @Test
    void shouldExists() {
        RecyclableSmtpContent instance = RecyclableSmtpContent.newInstance(Unpooled.copiedBuffer("DATA\r\n".getBytes(StandardCharsets.UTF_8)));
        assertNotNull(instance);
        assertEquals("DATA\r\n", instance.content().toString(StandardCharsets.UTF_8));
        assertEquals(1, instance.refCnt());
        instance.retain();
        assertEquals(2, instance.refCnt());
        instance.release();
        instance.recycle();
        assertEquals(0, instance.refCnt());
        assertThrows(IllegalReferenceCountException.class, () -> {
            instance.content();
        });
    }

}