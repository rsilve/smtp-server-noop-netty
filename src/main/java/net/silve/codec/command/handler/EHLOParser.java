package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import net.silve.codec.command.parsers.NoopParser;

public class EHLOParser extends NoopParser {

    @Override
    public CharSequence getName() {
        return SmtpCommand.EHLO.name();
    }

}

