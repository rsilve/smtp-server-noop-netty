package net.silve.codec.command.parsers;


import io.netty.util.AsciiString;

public abstract class CommandParser {

    protected static final CharSequence[] EMTPY = {};

    public abstract CharSequence getName();
    public abstract CharSequence[] parse(CharSequence line) throws InvalidSyntaxException;

    public static Pair parsePath(AsciiString path) throws InvalidSyntaxException {
        AsciiString next = path;
        if (path.indexOf("<") == 0) {
            final int end = path.indexOf(">");
            if (end < 0) {
                throw new InvalidSyntaxException("'<path>' required");
            }
            path = path.subSequence(1, end);
            // spaces within the <> are also possible, Postfix apparently
            // trims these away:
            return Pair.newInstance(path.trim(), next.subSequence(end));
        }
        throw new InvalidSyntaxException(String.format("'<path>' required in '%s'", next));
    }


}
