package net.silve.codec.command;


import io.netty.util.AsciiString;
import io.netty.util.Recycler;

import java.util.Objects;

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


    public static class Pair {

        private static final Recycler<Pair> RECYCLER = new Recycler<>() {
            protected Pair newObject(Recycler.Handle<Pair> handle) {
                return new Pair(handle);
            }
        };

        public static Pair newInstance(AsciiString token, AsciiString line) {
            final Pair pair = RECYCLER.get();

            pair.token = Objects.requireNonNull(token);
            pair.line = Objects.requireNonNull(line);
            return pair;
        }

        private final Recycler.Handle<Pair> handle;

        private Pair(Recycler.Handle<Pair> handle) {
            this.handle = handle;
        }

        public void recycle() {
            handle.recycle(this);
        }

        private AsciiString token;
        private AsciiString line;


        public AsciiString getToken() {
            return token;
        }

        public AsciiString getLine() {
            return line;
        }
    }

}
