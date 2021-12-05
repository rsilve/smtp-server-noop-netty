package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.command.CommandParser;

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
