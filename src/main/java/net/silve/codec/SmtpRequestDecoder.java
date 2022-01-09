package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import net.silve.codec.command.CommandMap;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.command.parsers.CommandParser;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.request.RecyclableLastSmtpContent;
import net.silve.codec.request.RecyclableSmtpContent;
import net.silve.codec.request.RecyclableSmtpRequest;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class SmtpRequestDecoder extends SimpleChannelInboundHandler<ByteBuf> {

    private static final ByteBuf DOT_CRLF_DELIMITER = Unpooled.wrappedBuffer(new byte[]{46, 13, 10});
    private static final CharSequence[] EMPTY_CHAR_SEQUENCE = {};
    private static final CommandMap commandMap = CommandMap.getInstance();
    private final SmtpServerConfiguration configuration;

    private final AtomicBoolean contentExpected;

    public SmtpRequestDecoder(@Nonnull SmtpServerConfiguration configuration, AtomicBoolean contentExpected) {
        super(true);
        Objects.requireNonNull(configuration, "configuration is required");
        this.configuration = configuration;
        Objects.requireNonNull(configuration, "content expected shared properties required");
        this.contentExpected = contentExpected;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf frame) {
        if (this.contentExpected.get()) {
            readContent(ctx, frame);
        } else {
            readRequest(ctx, frame);
        }

    }

    private void readContent(ChannelHandlerContext ctx, ByteBuf frame) {
        RecyclableSmtpContent result;
        if (DOT_CRLF_DELIMITER.equals(frame)) {
            this.contentExpected.set(false);
            result = RecyclableLastSmtpContent.newInstance(frame.retain());
        } else {
            result = RecyclableSmtpContent.newInstance(frame.retain());
        }
        ctx.fireChannelRead(result);
    }

    private void readRequest(ChannelHandlerContext ctx, ByteBuf frame) {
        try {
            RecyclableSmtpRequest request = parseLine(frame);
            ctx.fireChannelRead(request);
        } catch (InvalidProtocolException e) {
            ctx.writeAndFlush(e.getResponse());
            e.recycle();
        }
    }

    private RecyclableSmtpRequest parseLine(ByteBuf frame) throws InvalidProtocolException {
        if (!frame.isReadable()) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseBadSyntax);
        }
        int readable = frame.readableBytes();
        if (readable < 6) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseUnknownCommand);
        }
        final CharSequence line = frame.toString(CharsetUtil.US_ASCII);
        final CharSequence command = extractCommand(line);
        return RecyclableSmtpRequest.newInstance(SmtpCommand.valueOf(command), extractParameters(line, command));
    }

    private CharSequence extractCommand(CharSequence line) {
        return AsciiString.of(line.subSequence(0, 4)).toUpperCase();
    }

    private CharSequence[] extractParameters(CharSequence line, CharSequence command) throws InvalidProtocolException {
        if (line.length() == 6) {
            return EMPTY_CHAR_SEQUENCE;
        }
        CommandParser parser = commandMap.getParser(command);
        if (Objects.isNull(parser)) {
            return EMPTY_CHAR_SEQUENCE;
        }
        return parser.parse(line.subSequence(4, line.length()), configuration);
    }
}
