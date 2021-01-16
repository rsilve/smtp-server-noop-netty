package net.silve.codec.command;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;


public class MailParser extends CommandParser {

    private static InvalidSyntaxException FROM_REQUIRRED_EXCEPTION = new InvalidSyntaxException("'MAIL FROM:' required");
    @Override
    public CharSequence getName() {
        return SmtpCommand.MAIL.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) throws InvalidSyntaxException {

        AsciiString args = AsciiString.of(line).trim();
        final Pair prefix = parsePrefix(args.trim());
        final Pair reversePath = parseReversePath(prefix.getLine().trim());
        final CharSequence[] result = {reversePath.getToken()};
        prefix.recycle();
        reversePath.recycle();
        return result;
    }

    public static Pair parsePrefix(AsciiString line) throws InvalidSyntaxException {
        if (line.startsWith("FROM:")) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(5));
        } else {
            throw FROM_REQUIRRED_EXCEPTION;
        }
    }

    public static Pair parseReversePath(AsciiString path) throws InvalidSyntaxException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw new InvalidSyntaxException(String.format("'MAIL FROM:<forward-path>' required in '%s'", path), e);
        }
    }




}
