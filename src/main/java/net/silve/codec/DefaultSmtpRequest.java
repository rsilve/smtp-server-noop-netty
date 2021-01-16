package net.silve.codec;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.Recycler;
import io.netty.util.internal.ObjectUtil;

import java.util.Collections;
import java.util.List;

public final class DefaultSmtpRequest implements SmtpRequest {

    private static final Recycler<DefaultSmtpRequest> RECYCLER = new Recycler<>() {
        protected DefaultSmtpRequest newObject(Recycler.Handle<DefaultSmtpRequest> handle) {
            return new DefaultSmtpRequest(handle);
        }
    };

    public static DefaultSmtpRequest newInstance(SmtpCommand command) {
        DefaultSmtpRequest obj = RECYCLER.get();
        obj.command = ObjectUtil.checkNotNull(command, "command");
        obj.parameters = Collections.emptyList();
        return obj;
    }

    public static DefaultSmtpRequest newInstance(SmtpCommand command, CharSequence... parameters) {
        DefaultSmtpRequest obj = RECYCLER.get();
        obj.command = ObjectUtil.checkNotNull(command, "command");
        obj.parameters = SmtpUtils.toUnmodifiableList(parameters);
        return obj;
    }

    private final Recycler.Handle<DefaultSmtpRequest> handle;

    private DefaultSmtpRequest(Recycler.Handle<DefaultSmtpRequest> handle) {
        this.handle = handle;
    }

    public void recycle() {
        this.command = null;
        this.parameters = null;
        handle.recycle(this);
    }


    private SmtpCommand command;
    private List<CharSequence> parameters;

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
            io.netty.handler.codec.smtp.DefaultSmtpRequest other = (io.netty.handler.codec.smtp.DefaultSmtpRequest)o;
            return this.command().equals(other.command()) && this.parameters().equals(other.parameters());
        }
    }

    public String toString() {
        return "DefaultSmtpRequest{command=" + this.command + ", parameters=" + this.parameters + '}';
    }
}
