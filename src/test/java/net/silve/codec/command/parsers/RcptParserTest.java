package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.response.DefaultResponse;
import net.silve.codec.command.handler.InvalidProtocolException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RcptParserTest {

    @Test
    void shouldHaveAName() {
        CharSequence name = RcptParser.singleton().getName();
        assertEquals(SmtpCommand.RCPT.name(), name);
    }

    @Test
    void shouldParseFrom() throws InvalidProtocolException {
        CharSequence[] parsed = RcptParser.singleton().parse("TO:<name@domain.tld>");
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldParseFromWithExtension() throws InvalidProtocolException {
        CharSequence[] parsed = RcptParser.singleton().parse("TO:<name@domain.tld> extension");
        assertEquals(1, parsed.length);
        assertEquals(AsciiString.of("name@domain.tld"), parsed[0]);
    }

    @Test
    void shouldPThrowExceptionIfInvalidCommand() {
        try {
            RcptParser.singleton().parse("T:<name@domain.tld> extension");
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_BAD_RCPT_SYNTAX, e.getResponse());
        }

    }

    @Test
    void shouldPThrowExceptionIfInvalidForwardPath() {
        try {
            RcptParser.singleton().parse("TO:<name@domain");
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(DefaultResponse.RESPONSE_BAD_RECIPIENT_SYNTAX, e.getResponse());
        }

    }

}