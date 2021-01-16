package net.silve.codec;

import io.netty.handler.codec.smtp.SmtpCommand;

import java.util.List;

public interface SmtpRequest extends SmtpObject {
    SmtpCommand command();
    List<CharSequence> parameters();
    void recycle();
}
