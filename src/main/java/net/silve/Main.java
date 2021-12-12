package net.silve;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new SmtpServer()).execute(args);
        System.exit(exitCode);
    }
}
