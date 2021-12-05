package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;

public class EHLOParser extends NoopParser {

    @Override
    public CharSequence getName() {
        return SmtpCommand.EHLO.name();
    }

}

