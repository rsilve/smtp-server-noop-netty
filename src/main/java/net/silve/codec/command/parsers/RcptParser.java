package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;

public class RcptParser extends CommandParser {

    @Override
    public CharSequence getName() {
        return SmtpCommand.RCPT.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) throws InvalidSyntaxException {
        AsciiString args = AsciiString.of(line).trim();
        final Pair prefix = parsePrefix(args.trim());
        final Pair reversePath = parseForwardPath(prefix.getLine().trim());
        final CharSequence[] result = {reversePath.getToken()};
        prefix.recycle();
        reversePath.recycle();
        return result;
    }

    public static Pair parsePrefix(AsciiString line) throws InvalidSyntaxException {
        if (line.startsWith("TO:")) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(3));
        } else {
            throw new InvalidSyntaxException("'RCPT TO:' command required");
        }
    }

    public static Pair parseForwardPath(AsciiString path) throws InvalidSyntaxException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw new InvalidSyntaxException(String.format("'RCPT TO:<forward-path>' required in '%s'", path), e);
        }

    }




}
