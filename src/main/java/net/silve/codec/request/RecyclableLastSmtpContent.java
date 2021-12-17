package net.silve.codec.request;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.smtp.LastSmtpContent;
import io.netty.util.Recycler;

import javax.annotation.Nonnull;

public final class RecyclableLastSmtpContent extends RecyclableSmtpContent implements LastSmtpContent {

    private static final Recycler<RecyclableLastSmtpContent> RECYCLER = new Recycler<>() {
        protected RecyclableLastSmtpContent newObject(Recycler.Handle<RecyclableLastSmtpContent> handle) {
            return new RecyclableLastSmtpContent(handle);
        }
    };
    private final Recycler.Handle<RecyclableLastSmtpContent> handle;

    private RecyclableLastSmtpContent(Recycler.Handle<RecyclableLastSmtpContent> handle) {
        super();
        this.handle = handle;
    }

    public static RecyclableLastSmtpContent newInstance(@Nonnull ByteBuf data) {
        RecyclableLastSmtpContent obj = RECYCLER.get();
        obj.content(data);
        return obj;
    }

    @Override
    @SuppressWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
    public void recycle() {
        this.release();
        handle.recycle(this);
    }

    @Override
    public LastSmtpContent copy() {
        return (LastSmtpContent) super.copy();
    }

    @Override
    public LastSmtpContent duplicate() {
        return (LastSmtpContent) super.duplicate();
    }

    @Override
    public LastSmtpContent retainedDuplicate() {
        return (LastSmtpContent) super.retainedDuplicate();
    }

    @Override
    public LastSmtpContent replace(ByteBuf content) {
        return RecyclableLastSmtpContent.newInstance(content);
    }

    @Override
    public RecyclableLastSmtpContent retain() {
        super.retain();
        return this;
    }

    @Override
    public RecyclableLastSmtpContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override
    public RecyclableLastSmtpContent touch() {
        super.touch();
        return this;
    }

    @Override
    public RecyclableLastSmtpContent touch(Object hint) {
        super.touch(hint);
        return this;
    }

}
