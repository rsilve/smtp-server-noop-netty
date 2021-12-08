package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;


public class MailParser extends CommandParser {

    private static final MailParser instance = new MailParser();

    public static MailParser singleton() {
        return instance;
    }

    private static final InvalidSyntaxException FROM_REQUIRED_EXCEPTION = new InvalidSyntaxException("'MAIL FROM:' required");

    @Override
    public CharSequence getName() {
        return SmtpCommand.MAIL.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) throws InvalidSyntaxException {

        AsciiString args = AsciiString.of(line).trim();
        final Pair noPrefix = removePrefix(args.trim());
        final Pair reversePath = extractReversePath(noPrefix.getTail().trim());
        final CharSequence[] result = {reversePath.getHead()};
        noPrefix.recycle();
        reversePath.recycle();
        return result;
    }

    public static Pair removePrefix(AsciiString line) throws InvalidSyntaxException {
        if (line.startsWith("FROM:")) {
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
