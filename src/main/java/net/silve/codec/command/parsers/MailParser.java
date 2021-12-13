package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.command.handler.InvalidProtocolException;


public class MailParser extends CommandParser {

    private static final MailParser instance = new MailParser();

    public static MailParser singleton() {
        return instance;
    }

    private static final AsciiString PREFIX = AsciiString.of("FROM:");

    @Override
    public CharSequence getName() {
        return SmtpCommand.MAIL.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line) throws InvalidProtocolException {
        AsciiString args = AsciiString.of(line).trim(); // FROM:<reverse-path> extensions
        final Pair noPrefix = removePrefix(args.trim()); // return ['', '<reverse-path> extensions']
        final Pair reversePath = extractReversePath(noPrefix.getTail().trim()); // return ['reverse-path', '> extensions']
        final CharSequence[] result = {reversePath.getHead()};
        noPrefix.recycle();
        reversePath.recycle();
        return result;
    }

    public static Pair removePrefix(AsciiString line) throws InvalidProtocolException {
        if (line.startsWith(PREFIX)) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(5));
        } else {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_BAD_MAIL_SYNTAX);
        }
    }

    public static Pair extractReversePath(AsciiString path) throws InvalidProtocolException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw new InvalidProtocolException(ConstantResponse.RESPONSE_BAD_SENDER_SYNTAX);
        }
    }


}
