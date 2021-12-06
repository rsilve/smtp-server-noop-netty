package net.silve.codec.command;

import net.silve.codec.command.handler.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    @Test
    void shouldContains() {
        CommandRegistry command = CommandRegistry.AUTH;
        assertEquals(QUITHandler.singleton(), command.getHandler());

        command = CommandRegistry.DATA;
        assertEquals(DataHandler.singleton(), command.getHandler());

        command = CommandRegistry.EHLO;
        assertEquals(EHLOHandler.singleton(), command.getHandler());

        command = CommandRegistry.HELO;
        assertEquals(HELOHandler.singleton(), command.getHandler());

        command = CommandRegistry.HELP;
        assertEquals(QUITHandler.singleton(), command.getHandler());

        command = CommandRegistry.MAIL;
        assertEquals(MailHandler.singleton(), command.getHandler());

        command = CommandRegistry.NOOP;
        assertEquals(QUITHandler.singleton(), command.getHandler());

        command = CommandRegistry.RCPT;
        assertEquals(RcptHandler.singleton(), command.getHandler());

        command = CommandRegistry.RSET;
        assertEquals(RSETHandler.singleton(), command.getHandler());

        command = CommandRegistry.STARTTLS;
        assertEquals(StartTlsHandler.singleton(), command.getHandler());

        command = CommandRegistry.QUIT;
        assertEquals(QUITHandler.singleton(), command.getHandler());

        command = CommandRegistry.VRFY;
        assertEquals(QUITHandler.singleton(), command.getHandler());

        command = CommandRegistry.EXPN;
        assertEquals(QUITHandler.singleton(), command.getHandler());

        command = CommandRegistry.EMTPY;
        assertEquals(EmptyHandler.singleton(), command.getHandler());

    }

}