package net.silve.codec.command;

import net.silve.codec.command.handler.*;
import net.silve.codec.command.parsers.*;

public enum CommandRegistry {

    AUTH(QUITHandler.singleton()),
    DATA(DataHandler.singleton()),
    EHLO(EHLOHandler.singleton(), new EHLOParser()),
    HELO(HELOHandler.singleton()),
    HELP(QUITHandler.singleton()),
    MAIL(MailHandler.singleton(), new MailParser()),
    NOOP(QUITHandler.singleton()),
    QUIT(QUITHandler.singleton()),
    RCPT(RcptHandler.singleton(), new RcptParser()),
    RSET(RSETHandler.singleton()),
    STARTTLS(StartTlsHandler.singleton()),
    VRFY(QUITHandler.singleton()),
    EXPN(QUITHandler.singleton()),
    EMTPY(EmptyHandler.singleton());

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

