package net.silve.codec.command;

import net.silve.codec.command.handler.*;
import net.silve.codec.command.parsers.EHLOParser;
import net.silve.codec.command.parsers.MailParser;
import net.silve.codec.command.parsers.NoopParser;
import net.silve.codec.command.parsers.RcptParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandRegistryTest {

    @Test
    void shouldContainsAuth() {
        CommandRegistry command = CommandRegistry.AUTH;
        assertEquals(QUITHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsData() {
        CommandRegistry command = CommandRegistry.DATA;
        assertEquals(DataHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsEhlo() {
        CommandRegistry command = CommandRegistry.EHLO;
        assertEquals(EHLOHandler.singleton(), command.getHandler());
        assertEquals(EHLOParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsHelo() {
        CommandRegistry command = CommandRegistry.HELO;
        assertEquals(HELOHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsHelp() {
        CommandRegistry command = CommandRegistry.HELP;
        assertEquals(QUITHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsMail() {
        CommandRegistry command = CommandRegistry.MAIL;
        assertEquals(MailHandler.singleton(), command.getHandler());
        assertEquals(MailParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsNoop() {
        CommandRegistry command = CommandRegistry.NOOP;
        assertEquals(QUITHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsRcpt() {
        CommandRegistry command = CommandRegistry.RCPT;
        assertEquals(RcptHandler.singleton(), command.getHandler());
        assertEquals(RcptParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsRset() {
        CommandRegistry command = CommandRegistry.RSET;
        assertEquals(RSETHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsStartTls() {
        CommandRegistry command = CommandRegistry.STARTTLS;
        assertEquals(StartTlsHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsQuit() {
        CommandRegistry command = CommandRegistry.QUIT;
        assertEquals(QUITHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsVrfy() {
        CommandRegistry command = CommandRegistry.VRFY;
        assertEquals(QUITHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

    @Test
    void shouldContainsEXPN() {
        CommandRegistry command = CommandRegistry.EXPN;
        assertEquals(QUITHandler.singleton(), command.getHandler());
        assertEquals(NoopParser.singleton(), command.getParser());
    }

}