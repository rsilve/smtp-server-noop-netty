package net.silve.codec.command;

import io.netty.handler.codec.smtp.SmtpCommand;

public class NoopParser extends CommandParser {

    @Override
    public CharSequence getName() {
        return SmtpCommand.NOOP.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) {
        return EMTPY;
    }
}
