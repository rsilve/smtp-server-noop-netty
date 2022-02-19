package net.silve.codec.logger;

import io.micrometer.core.instrument.Metrics;
import net.silve.codec.session.MessageSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class SmtpLogger {

    private static final Logger logger = LoggerFactory.getLogger("smtp-noop");

    private SmtpLogger() {
        throw new IllegalStateException("Utility class");
    }

    public static void info(String s) {
        logger.info(s);
    }

    public static void info(String s, Object o) {
        logger.info(s, o);
    }

    public static void warn(String s, Throwable throwable) {
        logger.warn(s, throwable);
    }


    public static void info(MessageSession session) {
        Objects.requireNonNull(session, "session object is required for log");
        if (!session.isTransactionStarted()) {
            return;
        }

        if (session.isAccepted()) {
            Metrics.counter("smtp.received", "message", "received").increment();
            if (logger.isDebugEnabled()) {
                logger.debug("{} message accepted", session.getId());
            }
        } else {
            Metrics.counter("smtp.rejected", "message", "rejected").increment();
            String lastError = Objects.nonNull(session.lastError()) ? session.lastError() : "";
            logger.debug("{} message rejected ({})", session.getId(), lastError);
        }

    }

    public static void error(Throwable cause) {
        logger.error("unknown error", cause);
    }


}
