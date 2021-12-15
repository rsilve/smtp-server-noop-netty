package net.silve.codec.request;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.handler.codec.smtp.SmtpRequest;
import io.netty.util.Recycler;
import io.netty.util.internal.ObjectUtil;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public final class RecyclableSmtpRequest implements SmtpRequest {

    public static final String COMMAND_SHOULD_NOT_BE_NULL = "command should not be null";
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

    public static RecyclableSmtpRequest newInstance(@Nonnull SmtpCommand command) {
        RecyclableSmtpRequest obj = RECYCLER.get();
        obj.command = ObjectUtil.checkNotNull(command, COMMAND_SHOULD_NOT_BE_NULL);
        obj.parameters = Collections.emptyList();
        return obj;
    }

    public static RecyclableSmtpRequest newInstance(@Nonnull SmtpCommand command, CharSequence... parameters) {
        RecyclableSmtpRequest obj = RECYCLER.get();
        obj.command = ObjectUtil.checkNotNull(command, COMMAND_SHOULD_NOT_BE_NULL);
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

}
