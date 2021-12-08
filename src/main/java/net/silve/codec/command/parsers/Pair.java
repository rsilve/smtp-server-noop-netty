package net.silve.codec.command.parsers;

import io.netty.util.AsciiString;
import io.netty.util.Recycler;

import java.util.Objects;

public class Pair {

    private static final Recycler<Pair> RECYCLER = new Recycler<>() {
        protected Pair newObject(Handle<Pair> handle) {
            return new Pair(handle);
        }
    };

    public static Pair newInstance(AsciiString token, AsciiString line) {
        final Pair pair = RECYCLER.get();

        pair.head = Objects.requireNonNull(token);
        pair.tail = Objects.requireNonNull(line);
        return pair;
    }

    private final Recycler.Handle<Pair> handle;

    private Pair(Recycler.Handle<Pair> handle) {
        this.handle = handle;
    }

    public void recycle() {
        handle.recycle(this);
    }

    private AsciiString head;
    private AsciiString tail;


    public AsciiString getHead() {
        return head;
    }

    public AsciiString getTail() {
        return tail;
    }
}
