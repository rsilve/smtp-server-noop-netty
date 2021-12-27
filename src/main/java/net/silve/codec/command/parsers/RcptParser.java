package net.silve.codec.command.parsers;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.command.handler.InvalidProtocolException;
import net.silve.codec.configuration.SmtpServerConfiguration;

import javax.annotation.Nonnull;

public class RcptParser extends CommandParser {

    private static final RcptParser instance = new RcptParser();
    private static final AsciiString PREFIX = AsciiString.of("TO:");

    public static RcptParser singleton() {
        return instance;
    }

    public static Pair removePrefix(AsciiString line, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        if (line.startsWith(PREFIX)) {
            return Pair.newInstance(AsciiString.EMPTY_STRING, line.subSequence(3));
        } else {
            throw InvalidProtocolException.newInstance(configuration.responses.responseBadRcptSyntax);
        }
    }

    public static Pair extractForwardPath(AsciiString path, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        try {
            return parsePath(path);
        } catch (InvalidSyntaxException e) {
            throw InvalidProtocolException.newInstance(configuration.responses.responseBadRecipientSyntax);
        }

    }

    @Override
    public CharSequence getName() {
        return SmtpCommand.RCPT.name();
    }

    @Override
    public CharSequence[] parse(CharSequence line, @Nonnull SmtpServerConfiguration configuration) throws InvalidProtocolException {
        AsciiString args = AsciiString.of(line).trim(); // TO:<forward-path> extensions
        final Pair prefix = removePrefix(args.trim(), configuration); // return ['', '<forward-path> extensions']
        final Pair reversePath = extractForwardPath(prefix.getTail().trim(), configuration); // return ['forward-path', '> extensions']
        final CharSequence[] result = {reversePath.getHead()};
        prefix.recycle();
        reversePath.recycle();
        return result;
    }


}
