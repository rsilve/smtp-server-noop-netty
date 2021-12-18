package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.configuration.SmtpServerConfiguration;

import javax.annotation.Nonnull;

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
    public CharSequence[] parse(CharSequence line, @Nonnull SmtpServerConfiguration configuration) {
        return EMTPY;
    }
}
