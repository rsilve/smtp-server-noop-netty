package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.configuration.SmtpServerConfiguration;

import javax.annotation.Nonnull;


public class MailParser extends CommandParser {

    private static final MailParser instance = new MailParser();
    private static final AsciiString PREFIX = AsciiString.of("FROM:");

    public static MailParser singleton() {
        return instance;
    }

    public static Pair removePrefix(AsciiString line, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        if (line.startsWith(PREFIX)) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(5));
        } else {
            throw new InvalidProtocolException(configuration.responses.responseBadMailSyntax);
        }
    }

    public static Pair extractReversePath(AsciiString path, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw new InvalidProtocolException(configuration.responses.responseBadSenderSyntax);
        }
    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.MAIL.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        AsciiString args = AsciiString.of(line).trim(); // FROM:<reverse-path> extensions
        final Pair noPrefix = removePrefix(args.trim(), configuration); // return ['', '<reverse-path> extensions']
        final Pair reversePath = extractReversePath(noPrefix.getTail().trim(), configuration); // return ['reverse-path', '> extensions']
        final CharSequence[] result = {reversePath.getHead()};
        noPrefix.recycle();
        reversePath.recycle();
        return result;
    }


}
