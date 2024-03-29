package net.silve.codec.session;

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
    private final Recycler.Handle<MessageSession> handle;
    private AsciiString id = MessageSessionId.generate();
    private List<AsciiString> forwardPath = new ArrayList<>();
    private boolean transactionStarted = false;
    private long startedAt = System.nanoTime();
    private long completedAt = 0L;
    private boolean accepted = false;
    private List<CharSequence> details;

    private MessageSession(Recycler.Handle<MessageSession> handle) {
        this.handle = handle;
    }

    public static MessageSession newInstance() {
        MessageSession obj = RECYCLER.get();
        obj.reset();
        obj.startedAt = System.nanoTime();
        return obj;
    }

    public void recycle() {
        this.reset();
        handle.recycle(this);
    }

    public void reset() {
        this.id = MessageSessionId.generate();
        forwardPath = new ArrayList<>();
        transactionStarted = false;
        startedAt = 0;
        completedAt = 0L;
        accepted = false;
        details = null;
    }

    public AsciiString getId() {
        return id;
    }

    public MessageSession setReversePath() {
        this.transactionStarted = true;
        return this;
    }

    public MessageSession addForwardPath(AsciiString path) {
        forwardPath.add(path);
        return this;
    }

    public boolean needForward() {
        return Objects.isNull(forwardPath) || forwardPath.isEmpty();
    }

    public boolean tooManyForward(int limit) {
        return Objects.nonNull(forwardPath) && forwardPath.size() >= limit;
    }

    public boolean isTransactionStarted() {
        return transactionStarted;
    }

    public MessageSession completed() {
        this.completedAt = System.nanoTime();
        return this;
    }

    public long duration() {
        return this.completedAt - this.startedAt;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public MessageSession setAccepted(boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void lastError(List<CharSequence> details) {
        this.details = details;
    }

    public void lastError(CharSequence details) {
        ArrayList<CharSequence> list = new ArrayList<>();
        list.add(details);
        this.details = list;
    }

    public String lastError() {
        return String.join(", ", details);
    }


}
