package net.silve.codec.command;

import net.silve.codec.command.handler.*;
import net.silve.codec.command.parsers.MailParser;
import net.silve.codec.command.parsers.NoopParser;
import net.silve.codec.command.parsers.RcptParser;

public enum CommandRegistry {

    AUTH(new QUITHandler()),
    DATA(new DataHandler()),
    EHLO(new EHLOHandler(), new EHLOParser()),
    HELO(new HELOHandler() ),
    HELP(new QUITHandler()),
    MAIL(new MailHandler(), new MailParser()),
    NOOP(new QUITHandler()),
    QUIT(new QUITHandler()),
    RCPT(new RcptHandler(), new RcptParser()),
    RSET(new RSETHandler()),
    STARTTLS(new StartTlsHandler()),
    VRFY(new QUITHandler()),
    EXPN(new QUITHandler()),
    EMTPY(new EmptyHandler());

    private final CommandHandler command;
    private final CommandParser parser;

    CommandRegistry(CommandHandler cmd, CommandParser parser) {
        this.command = cmd;
        this.parser = parser;
    }

    CommandRegistry(CommandHandler cmd) {
        this.command = cmd;
        this.parser = new NoopParser();
    }

    public CommandHandler getHandler() {
        return this.command;
    }

    public CommandParser getParser() {
        return parser;
    }
}

