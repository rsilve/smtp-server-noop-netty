package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MailParserTest {

    SmtpServerConfiguration configuration = new SmtpServerConfiguration(new SmtpServerConfigurationBuilder());

    @Test
    void shouldHaveAName() {
        CharSequence name = MailParser.singleton().getName();
        assertEquals(SmtpCommand.MAIL.name(), name);
    }

    @Test
    void shouldParseFrom() throws InvalidProtocolException {
        CharSequence[] parsed = MailParser.singleton().parse("FROM:<name@domain.tld>", configuration);
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldParseFromWithExtension() throws InvalidProtocolException {
        CharSequence[] parsed = MailParser.singleton().parse("FROM:<name@domain.tld> extension", configuration);
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldPThrowExceptionIfInvalidCommand() {
        try {
            MailParser.singleton().parse("FRO:<name@domain.tld> extension", configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseBadMailSyntax, e.getResponse());
        }

    }

    @Test
    void shouldPThrowExceptionIfInvalidReversePath() {
        try {
            MailParser.singleton().parse("FROM:<name@domain", configuration);
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(configuration.responses.responseBadSenderSyntax, e.getResponse());
        }

    }

}