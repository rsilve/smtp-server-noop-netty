package net.silve.codec;


import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;
import net.silve.codec.utils.RecyclableByteBufHolder;

import java.util.Objects;


public class DefaultSmtpContent extends RecyclableByteBufHolder implements SmtpContent {

    private static final Recycler<DefaultSmtpContent> RECYCLER = new Recycler<>() {
        protected DefaultSmtpContent newObject(Recycler.Handle<DefaultSmtpContent> handle) {
            return new DefaultSmtpContent(handle);
        }
    };

    public static DefaultSmtpContent newInstance(ByteBuf data) {
        DefaultSmtpContent obj = RECYCLER.get();
        obj.content(data);
        return obj;
    }

    private Recycler.Handle<DefaultSmtpContent> handle;

    protected DefaultSmtpContent() {}
    protected DefaultSmtpContent(Recycler.Handle<DefaultSmtpContent> handle) {
        this.handle = handle;
    }

    public void recycle() {
        this.release();
        handle.recycle(this);
    }



    public SmtpContent copy() {
        return (SmtpContent)super.copy();
    }

    public SmtpContent duplicate() {
        return (SmtpContent)super.duplicate();
    }

    public SmtpContent retainedDuplicate() {
        return (SmtpContent)super.retainedDuplicate();
    }

    public SmtpContent replace(ByteBuf content) {
        return DefaultSmtpContent.newInstance(content);
    }

    public SmtpContent retain() {
        super.retain();
        return this;
    }

    public SmtpContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    public SmtpContent touch() {
        super.touch();
        return this;
    }

    public SmtpContent touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultSmtpContent)) return false;
        if (!super.equals(o)) return false;

        DefaultSmtpContent that = (DefaultSmtpContent) o;
        return Objects.equals(handle, that.handle);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (handle != null ? handle.hashCode() : 0);
        return result;
    }
}
