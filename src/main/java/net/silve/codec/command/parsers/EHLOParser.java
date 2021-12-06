package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;

public class EHLOParser extends NoopParser {

    private static final EHLOParser instance = new EHLOParser();

    public static EHLOParser singleton() {
        return instance;
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.EHLO.name();
    }

}

