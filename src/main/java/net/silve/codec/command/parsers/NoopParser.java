package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;

public class NoopParser extends CommandParser {

    private static final NoopParser instance = new NoopParser();

    public static NoopParser singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.NOOP.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) {
        return EMTPY;
    }
}
