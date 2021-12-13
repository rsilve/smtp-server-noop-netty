package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.command.handler.InvalidProtocolException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailParserTest {

    @Test
    void shouldHaveAName() {
        CharSequence name = MailParser.singleton().getName();
        assertEquals(SmtpCommand.MAIL.name(), name);
    }

    @Test
    void shouldParseFrom() throws InvalidProtocolException {
        CharSequence[] parsed = MailParser.singleton().parse("FROM:<name@domain.tld>");
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldParseFromWithExtension() throws InvalidProtocolException {
        CharSequence[] parsed = MailParser.singleton().parse("FROM:<name@domain.tld> extension");
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldPThrowExceptionIfInvalidCommand() {
        try {
            MailParser.singleton().parse("FRO:<name@domain.tld> extension");
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_BAD_MAIL_SYNTAX, e.getResponse());
        }

    }

    @Test
    void shouldPThrowExceptionIfInvalidReversePath() {
        try {
            MailParser.singleton().parse("FROM:<name@domain");
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_BAD_SENDER_SYNTAX, e.getResponse());
        }

    }

}