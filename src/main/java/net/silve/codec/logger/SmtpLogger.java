package net.silve.codec.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
}
