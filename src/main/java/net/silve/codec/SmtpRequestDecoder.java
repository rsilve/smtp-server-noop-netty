package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import net.silve.codec.command.CommandParser;
import net.silve.codec.command.InvalidSyntaxException;
import net.silve.codec.command.CommandMap;

import java.util.Objects;


public class SmtpRequestDecoder extends SimpleChannelInboundHandler<ByteBuf> {

    private static final ByteBuf DOT_CRLF_DELIMITER = Unpooled.wrappedBuffer(new byte[]{46, 13, 10});
    private static final CharSequence[] EMPTY_CHAR_SEQUENCE = new CharSequence[]{};

    private static final CommandMap commandMap = new CommandMap();

    private boolean contentExpected = false;

    public SmtpRequestDecoder() {
        super(true);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf frame) throws Exception {

        if (!this.contentExpected) {
            readRequest(ctx, frame);
        } else {
            readContent(ctx, frame);
        }

    }

    private void readContent(ChannelHandlerContext ctx, ByteBuf frame) {
        SmtpContent result = null;
        try {
            if (frame.equals(DOT_CRLF_DELIMITER)) {
                this.contentExpected = false;
                result = DefaultLastSmtpContent.newInstance(frame.retain());
            } else {
                result = DefaultSmtpContent.newInstance(frame.retain());
            }
            ctx.fireChannelRead(result);
        } catch (Exception e) {
            if (!Objects.isNull(result)) {
                result.recycle();
            }
        }
    }

    private void readRequest(ChannelHandlerContext ctx, ByteBuf frame) {
        SmtpRequest result = null;
        try {
            int readable = frame.readableBytes();
            int readerIndex = frame.readerIndex();
            if (readable < 5) {
                throw newDecoderException(frame, readerIndex, readable);
            }

            CharSequence detail = frame.isReadable() ? frame.toString(CharsetUtil.US_ASCII) : null;
            if (Objects.isNull(detail)) {
                result = DefaultSmtpRequest.newInstance(SmtpCommand.EMPTY);
            } else {
                final CharSequence command = getCommand(detail);
                final CharSequence[] parameters = getParameters(detail, command);
                result = DefaultSmtpRequest.newInstance(SmtpCommand.valueOf(command), parameters);
                if (SmtpCommand.DATA.equals(result.command())) {
                    this.contentExpected = true;
                }
            }
            ctx.fireChannelRead(result);
        } catch (Exception e) {
            if (!Objects.isNull(result)) {
                result.recycle();
            }
        }
    }

    private static DecoderException newDecoderException(ByteBuf buffer, int readerIndex, int readable) {
        return new DecoderException("Received invalid line: '" + buffer.toString(readerIndex, readable, CharsetUtil.US_ASCII) + '\'');
    }

    private CharSequence getCommand(CharSequence line) {
        ObjectUtil.checkNotNull(line, "Invalid protocol: null line");
        if (line.length() < 4) {
            throw new IllegalArgumentException(String.format("Invalid protocol: less than 4 char '%s'", line));
        }
        final CharSequence command = line.subSequence(0, 4);
        return AsciiString.of(command).toUpperCase();
    }

    private CharSequence[] getParameters(CharSequence line, CharSequence command) {
        ObjectUtil.checkNotNull(line, "Invalid protocol: null line");
        if (line.length() == 4) {
            return EMPTY_CHAR_SEQUENCE;
        }

        CommandParser parser = commandMap.getParser(command);

        if (Objects.isNull(parser)) {
            return EMPTY_CHAR_SEQUENCE;
        }

        try {
            return parser.parse(line.subSequence(4, line.length()));
        } catch (InvalidSyntaxException e) {
            throw new IllegalArgumentException("Invalid protocol: " + e.getMessage(), e);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
    }
}
