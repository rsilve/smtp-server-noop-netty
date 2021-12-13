package net.silve.codec;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpRequest;
import io.netty.util.Recycler;
import io.netty.util.internal.ObjectUtil;

import java.util.Collections;
import java.util.List;

public final class RecyclableSmtpRequest implements SmtpRequest {

    private static final Recycler<RecyclableSmtpRequest> RECYCLER = new Recycler<>() {
        protected RecyclableSmtpRequest newObject(Recycler.Handle<RecyclableSmtpRequest> handle) {
            return new RecyclableSmtpRequest(handle);
        }
    };
    private final Recycler.Handle<RecyclableSmtpRequest> handle;
    private SmtpCommand command;
    private List<CharSequence> parameters;

    private RecyclableSmtpRequest(Recycler.Handle<RecyclableSmtpRequest> handle) {
        this.handle = handle;
    }

    public static RecyclableSmtpRequest newInstance(SmtpCommand command) {
        RecyclableSmtpRequest obj = RECYCLER.get();
        obj.command = ObjectUtil.checkNotNull(command, "command");
        obj.parameters = Collections.emptyList();
        return obj;
    }

    public static RecyclableSmtpRequest newInstance(SmtpCommand command, CharSequence... parameters) {
        RecyclableSmtpRequest obj = RECYCLER.get();
        obj.command = ObjectUtil.checkNotNull(command, "command");
        obj.parameters = SmtpUtils.toUnmodifiableList(parameters);
        return obj;
    }

    public void recycle() {
        this.command = null;
        this.parameters = null;
        handle.recycle(this);
    }

    public SmtpCommand command() {
        return this.command;
    }

    public List<CharSequence> parameters() {
        return this.parameters;
    }

    public int hashCode() {
        return this.command.hashCode() * 31 + this.parameters.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof io.netty.handler.codec.smtp.DefaultSmtpRequest)) {
            return false;
        } else if (o == this) {
            return true;
        } else {
            io.netty.handler.codec.smtp.DefaultSmtpRequest other = (io.netty.handler.codec.smtp.DefaultSmtpRequest) o;
            return this.command().equals(other.command()) && this.parameters().equals(other.parameters());
        }
    }

    public String toString() {
        return "DefaultSmtpRequest{command=" + this.command + ", parameters=" + this.parameters + '}';
    }
}
