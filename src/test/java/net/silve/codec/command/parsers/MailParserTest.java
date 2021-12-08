package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailParserTest {

    @Test
    void shouldHaveAName() {
        CharSequence name = MailParser.singleton().getName();
        assertEquals(SmtpCommand.MAIL.name(), name);
    }

    @Test
    void shouldParseFrom() throws InvalidSyntaxException {
        CharSequence[] parsed = MailParser.singleton().parse("FROM:<name@domain.tld>");
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldParseFromWithExtension() throws InvalidSyntaxException {
        CharSequence[] parsed = MailParser.singleton().parse("FROM:<name@domain.tld> extension");
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldPThrowExceptionIfInvalidCommand() {
        try {
            MailParser.singleton().parse("FRO:<name@domain.tld> extension");
            fail();
        } catch (InvalidSyntaxException e) {
            assertEquals("'MAIL FROM:' required", e.getMessage());
        }

    }

    @Test
    void shouldPThrowExceptionIfInvalidReversePath() {
        try {
            MailParser.singleton().parse("FROM:<name@domain");
            fail();
        } catch (InvalidSyntaxException e) {
            assertEquals("'<reverse-path>' required in '<name@domain'", e.getMessage());
        }

    }

}