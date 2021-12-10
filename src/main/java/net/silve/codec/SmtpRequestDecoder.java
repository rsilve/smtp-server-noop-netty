package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import net.silve.codec.command.CommandMap;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.command.parsers.CommandParser;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class SmtpRequestDecoder extends SimpleChannelInboundHandler<ByteBuf> {

    private static final ByteBuf DOT_CRLF_DELIMITER = Unpooled.wrappedBuffer(new byte[]{46, 13, 10});
    private static final CharSequence[] EMPTY_CHAR_SEQUENCE = {};
    private static final InvalidProtocolException EXCEPTION_BAD_SYNTAX = new InvalidProtocolException(ConstantResponse.RESPONSE_BAD_SYNTAX);
    private static final InvalidProtocolException EXCEPTION_UNKNOWN_COMMAND = new InvalidProtocolException(ConstantResponse.RESPONSE_UNKNOWN_COMMAND);

    private static final CommandMap commandMap = new CommandMap();

    private boolean contentExpected = false;

    public SmtpRequestDecoder() {
        super(true);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf frame) {

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
        try {
            DefaultSmtpRequest request = parseLine(frame);
            if (SmtpCommand.DATA.equals(request.command())) {
                this.contentExpected = true;
            }
            ctx.fireChannelRead(request);
        } catch (InvalidProtocolException e) {
            ctx.writeAndFlush(e.getResponse());
        }
    }

    private DefaultSmtpRequest parseLine(ByteBuf frame) throws InvalidProtocolException {
        int readable = frame.readableBytes();
        if (readable < 5) {
            throw EXCEPTION_UNKNOWN_COMMAND;
        }
        CharSequence detail = frame.isReadable() ? frame.toString(CharsetUtil.US_ASCII) : null;
        if (Objects.isNull(detail)) {
            throw EXCEPTION_BAD_SYNTAX;
        }

        final CharSequence command = getCommand(detail);
        final CharSequence[] parameters = getParameters(detail, command);
        return DefaultSmtpRequest.newInstance(SmtpCommand.valueOf(command), parameters);
    }

    private CharSequence getCommand(@NotNull CharSequence line) throws InvalidProtocolException {
        if (line.length() < 4) {
            throw EXCEPTION_UNKNOWN_COMMAND;
        }
        final CharSequence command = line.subSequence(0, 4);
        return AsciiString.of(command).toUpperCase();
    }

    private CharSequence[] getParameters(@NotNull CharSequence line, @NotNull CharSequence command) throws InvalidProtocolException {
        ObjectUtil.checkNotNull(line, "Invalid protocol: null line");
        if (line.length() == 4) {
            return EMPTY_CHAR_SEQUENCE;
        }
        CommandParser parser = commandMap.getParser(command);
        if (Objects.isNull(parser)) {
            return EMPTY_CHAR_SEQUENCE;
        }
        return parser.parse(line.subSequence(4, line.length()));
    }
}
