package net.silve.codec.command;

import net.silve.codec.command.handler.*;
import net.silve.codec.command.parsers.*;

public enum CommandRegistry {

    AUTH(QUITHandler.singleton()),
    DATA(DataHandler.singleton()),
    EHLO(EHLOHandler.singleton(), EHLOParser.singleton()),
    HELO(HELOHandler.singleton()),
    HELP(QUITHandler.singleton()),
    MAIL(MailHandler.singleton(), MailParser.singleton()),
    NOOP(QUITHandler.singleton()),
    QUIT(QUITHandler.singleton()),
    RCPT(RcptHandler.singleton(), RcptParser.singleton()),
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
        this.parser = NoopParser.singleton();
    }

    public CommandHandler getHandler() {
        return this.command;
    }

    public CommandParser getParser() {
        return parser;
    }
}

