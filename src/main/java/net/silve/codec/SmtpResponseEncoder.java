package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.smtp.SmtpResponse;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class SmtpResponseEncoder extends MessageToMessageEncoder<SmtpResponse> {

    private static final char SEPARATOR_LAST = '-';
    private static final char SEPARATOR = ' ';
    private static final Map<SmtpResponse, byte[]> encodedResponse = new ConcurrentHashMap<>(20);

    @Override
    public boolean acceptOutboundMessage(Object msg) {
        return msg instanceof SmtpResponse;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SmtpResponse smtpResponse, List<Object> list) {
        ByteBuf buffer = ctx.alloc().buffer();
        boolean release = true;
        try {
            byte[] bytes = memoize(smtpResponse);
            buffer.writeBytes(bytes);
            list.add(buffer);
            release = false;
        } finally {
            if (release) {
                buffer.release();
            }
        }
    }

    private byte[] memoize(SmtpResponse smtpResponse) {
        if (!encodedResponse.containsKey(smtpResponse)) {
            byte[] bytes = encodeResponse(smtpResponse);
            encodedResponse.put(smtpResponse, bytes);
        }
        return encodedResponse.get(smtpResponse);
    }

    private byte[] encodeResponse(SmtpResponse smtpResponse) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] codeBytes = Integer.toString(smtpResponse.code()).getBytes(StandardCharsets.US_ASCII);
        final int size = smtpResponse.details().size();
        for (int i = 0; i < size; i++) {
            byte[] seq = smtpResponse.details().get(i).toString().getBytes(StandardCharsets.US_ASCII);
            char separator = i < size - 1 ? SEPARATOR_LAST : SEPARATOR;
            stream.writeBytes(codeBytes);
            stream.write((byte) separator);
            stream.writeBytes(seq);
            stream.writeBytes(new byte[]{13, 10});
        }
        return stream.toByteArray();
    }


}
