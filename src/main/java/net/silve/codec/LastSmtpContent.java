package net.silve.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public interface LastSmtpContent extends SmtpContent {
    LastSmtpContent EMPTY_LAST_CONTENT = new LastSmtpContent() {
        @Override
        public void recycle() {
            this.release();
        }

        public LastSmtpContent copy() {
            return this;
        }

        public LastSmtpContent duplicate() {
            return this;
        }

        public LastSmtpContent retainedDuplicate() {
            return this;
        }

        public LastSmtpContent replace(ByteBuf content) {
            return DefaultLastSmtpContent.newInstance(content);
        }

        public LastSmtpContent retain() {
            return this;
        }

        public LastSmtpContent retain(int increment) {
            return this;
        }

        public LastSmtpContent touch() {
            return this;
        }

        public LastSmtpContent touch(Object hint) {
            return this;
        }

        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }

        public int refCnt() {
            return 1;
        }

        public boolean release() {
            return false;
        }

        public boolean release(int decrement) {
            return false;
        }
    };

    LastSmtpContent copy();

    LastSmtpContent duplicate();

    LastSmtpContent retainedDuplicate();

    LastSmtpContent replace(ByteBuf var1);

    LastSmtpContent retain();

    LastSmtpContent retain(int var1);

    LastSmtpContent touch();

    LastSmtpContent touch(Object var1);
}
