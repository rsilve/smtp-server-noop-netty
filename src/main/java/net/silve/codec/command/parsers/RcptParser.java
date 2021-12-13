package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.command.handler.InvalidProtocolException;

public class RcptParser extends CommandParser {

    private static final RcptParser instance = new RcptParser();

    public static RcptParser singleton() {
        return instance;
    }

    private static final AsciiString PREFIX = AsciiString.of("TO:");

    @Override
    public CharSequence getName() {
        return SmtpCommand.RCPT.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) throws InvalidProtocolException {
        AsciiString args = AsciiString.of(line).trim(); // TO:<forward-path> extensions
        final Pair prefix = removePrefix(args.trim()); // return ['', '<forward-path> extensions']
        final Pair reversePath = extractForwardPath(prefix.getTail().trim()); // return ['forward-path', '> extensions']
        final CharSequence[] result = {reversePath.getHead()};
        prefix.recycle();
        reversePath.recycle();
        return result;
    }

    public static Pair removePrefix(AsciiString line) throws InvalidProtocolException {
        if (line.startsWith(PREFIX)) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(3));
        } else {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_BAD_RCPT_SYNTAX);
        }
    }

    public static Pair extractForwardPath(AsciiString path) throws InvalidProtocolException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_BAD_RECIPIENT_SYNTAX);
        }

    }


}
