package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;


public class MailParser extends CommandParser {

    private static final MailParser instance = new MailParser();

    public static MailParser singleton() {
        return instance;
    }

    private static final InvalidSyntaxException FROM_REQUIRED_EXCEPTION = new InvalidSyntaxException("'MAIL FROM:' required");
    private static final AsciiString PREFIX = AsciiString.of("FROM:");

    @Override
    public CharSequence getName() {
        return SmtpCommand.MAIL.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) throws InvalidSyntaxException {
        AsciiString args = AsciiString.of(line).trim(); // FROM:<reverse-path> extensions
        final Pair noPrefix = removePrefix(args.trim());// get ['', '<reverse-path> extensions']
        final Pair reversePath = extractReversePath(noPrefix.getTail().trim()); // get ['reverse-path', '> extensions']
        final CharSequence[] result = {reversePath.getHead()};
        noPrefix.recycle();
        reversePath.recycle();
        return result;
    }

    public static Pair removePrefix(AsciiString line) throws InvalidSyntaxException {
        if (line.startsWith(PREFIX)) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(5));
        } else {
            throw FROM_REQUIRED_EXCEPTION;
        }
    }

    public static Pair extractReversePath(AsciiString path) throws InvalidSyntaxException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw new InvalidSyntaxException(String.format("'<reverse-path>' required in '%s'", path), e);
        }
    }


}
