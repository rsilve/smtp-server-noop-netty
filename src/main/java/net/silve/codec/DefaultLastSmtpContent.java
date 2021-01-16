package net.silve.codec;


import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;

import java.util.Objects;

public final class DefaultLastSmtpContent extends DefaultSmtpContent implements LastSmtpContent {

    private static final Recycler<DefaultLastSmtpContent> RECYCLER = new Recycler<>() {
        protected DefaultLastSmtpContent newObject(Recycler.Handle<DefaultLastSmtpContent> handle) {
            return new DefaultLastSmtpContent(handle);
        }
    };

    public static DefaultLastSmtpContent newInstance(ByteBuf data) {
        DefaultLastSmtpContent obj = RECYCLER.get();
        obj.content(data);
        return obj;
    }

    private final Recycler.Handle<DefaultLastSmtpContent> handle;

    private DefaultLastSmtpContent(Recycler.Handle<DefaultLastSmtpContent> handle) {
        super();
        this.handle = handle;
    }

    public void recycle() {
        this.release();
        handle.recycle(this);
    }


    public LastSmtpContent copy() {
        return (LastSmtpContent)super.copy();
    }

    public LastSmtpContent duplicate() {
        return (LastSmtpContent)super.duplicate();
    }

    public LastSmtpContent retainedDuplicate() {
        return (LastSmtpContent)super.retainedDuplicate();
    }

    public LastSmtpContent replace(ByteBuf content) {
        return DefaultLastSmtpContent.newInstance(content);
    }

    public DefaultLastSmtpContent retain() {
        super.retain();
        return this;
    }

    public DefaultLastSmtpContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    public DefaultLastSmtpContent touch() {
        super.touch();
        return this;
    }

    public DefaultLastSmtpContent touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultLastSmtpContent)) return false;
        if (!super.equals(o)) return false;
        DefaultLastSmtpContent that = (DefaultLastSmtpContent) o;
        return Objects.equals(handle, that.handle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), handle);
    }
}
