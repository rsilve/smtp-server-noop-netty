package net.silve.codec.request;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpRequest;
import io.netty.util.Recycler;
import io.netty.util.internal.ObjectUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecyclableSmtpRequest that = (RecyclableSmtpRequest) o;

        if (!handle.equals(that.handle)) return false;
        if (!command.equals(that.command)) return false;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = handle.hashCode();
        result = 31 * result + command.hashCode();
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
