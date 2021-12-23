package net.silve;

import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT [%4$s] %5$s%6$s%n");

        int exitCode = new CommandLine(new SmtpServer()).execute(args);
        System.exit(exitCode);
    }
}
