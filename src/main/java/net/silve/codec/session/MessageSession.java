package net.silve.codec.session;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import io.netty.util.Recycler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageSession {

    private static final Recycler<MessageSession> RECYCLER = new Recycler<>() {
        protected MessageSession newObject(Recycler.Handle<MessageSession> handle) {
            return new MessageSession(handle);
        }
    };

    public static MessageSession newInstance() {
        MessageSession obj = RECYCLER.get();
        obj.reset();
        obj.startedAt = System.nanoTime();
        return obj;
    }

    private final Recycler.Handle<MessageSession> handle;

    private MessageSession(Recycler.Handle<MessageSession> handle) {
        this.handle = handle;
    }

    public void recycle() {
        this.reset();
        handle.recycle(this);
    }


    private AsciiString id = MessageSessionId.generate();
    private SmtpCommand lastCommand = null;
    private AsciiString reversePath = AsciiString.EMPTY_STRING;
    private List<AsciiString> forwardPath = new ArrayList<>();
    private boolean transactionStarted = false;
    private long startedAt = System.nanoTime();
    private long completedAt = 0l;
    private long size = 0l;

    private void reset() {
        this.id = MessageSessionId.generate();
        lastCommand = null;
        reversePath = AsciiString.EMPTY_STRING;
        forwardPath = new ArrayList<>();
        transactionStarted = false;
        startedAt = 0;
        completedAt = 0l;
        size = 0l;
    }

    public AsciiString getId() {
        return id;
    }

    public SmtpCommand getLastCommand() {
        return lastCommand;
    }

    public MessageSession setLastCommand(SmtpCommand lastCommand) {
        this.lastCommand = lastCommand;
        return this;
    }

    public AsciiString getReversePath() {
        return reversePath;
    }

    public MessageSession setReversePath(AsciiString reversePath) {
        this.reversePath = reversePath;
        return this;
    }

    public List<AsciiString> getForwardPath() {
        return forwardPath;
    }

    public MessageSession setForwardPath(List<AsciiString> forwardPath) {
        this.forwardPath = forwardPath;
        return this;
    }

    public List<AsciiString> addForwardPath(AsciiString path) {
        forwardPath.add(path);
        return forwardPath;
    }


    public boolean isTransactionStarted() {
        return transactionStarted;
    }

    public MessageSession setTransactionStarted(boolean transactionStarted) {
        this.transactionStarted = transactionStarted;
        return this;
    }

    public void completed() {
        this.completedAt = System.nanoTime();
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public long duration() {
        return this.completedAt - this.startedAt;
    }

    private double durationMillis(){
        final long microSeconds = duration() / 1000;
        return microSeconds / 1000d;
    }

    public long getSize() {
        return size;
    }

    public MessageSession setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageSession)) return false;
        MessageSession that = (MessageSession) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format(
                "MessageSession{id='%s', reversePath=%s, forwardPath=%s, duration=%.3f, size=%d}",
                id, reversePath, forwardPath, durationMillis(), size);
    }
}
