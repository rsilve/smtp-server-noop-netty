package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpResponse;
import io.netty.util.Recycler;

import javax.annotation.Nonnull;


public class InvalidProtocolException extends Exception {

    private static final Recycler<InvalidProtocolException> RECYCLER = new Recycler<>() {
        protected InvalidProtocolException newObject(Recycler.Handle<InvalidProtocolException> handle) {
            return new InvalidProtocolException(handle);
        }
    };
    private final transient Recycler.Handle<InvalidProtocolException> handle;
    private transient SmtpResponse response;

    private InvalidProtocolException(Recycler.Handle<InvalidProtocolException> handle) {
        this.handle = handle;
    }

    public static InvalidProtocolException newInstance(@Nonnull SmtpResponse response) {
        InvalidProtocolException obj = RECYCLER.get();
        obj.response = response;
        return obj;
    }

    public void recycle() {
        this.response = null;
        handle.recycle(this);
    }

    public SmtpResponse getResponse() {
        return response;
    }


}
