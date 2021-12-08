package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;

public class RcptParser extends CommandParser {

    private static final RcptParser instance = new RcptParser();

    public static RcptParser singleton() {
        return instance;
    }

    private static final AsciiString PREFIX = AsciiString.of("TO:");

    private static final InvalidSyntaxException REQUIRED_COMMAND_EXCEPTION = new InvalidSyntaxException("'RCPT TO:' command required");

    @Override
    public CharSequence getName() {
        return SmtpCommand.RCPT.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) throws InvalidSyntaxException {
        AsciiString args = AsciiString.of(line).trim(); // TO:<forward-path> extensions
        final Pair prefix = removePrefix(args.trim()); // return ['', '<forward-path> extensions']
        final Pair reversePath = extractForwardPath(prefix.getTail().trim()); // return ['forward-path', '> extensions']
        final CharSequence[] result = {reversePath.getHead()};
        prefix.recycle();
        reversePath.recycle();
        return result;
    }

    public static Pair removePrefix(AsciiString line) throws InvalidSyntaxException {
        if (line.startsWith(PREFIX)) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(3));
        } else {
            throw REQUIRED_COMMAND_EXCEPTION;
        }
    }

    public static Pair extractForwardPath(AsciiString path) throws InvalidSyntaxException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw new InvalidSyntaxException(String.format("'<forward-path>' required in '%s'", path), e);
        }

    }


}
