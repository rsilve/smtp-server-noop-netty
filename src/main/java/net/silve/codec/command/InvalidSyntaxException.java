package net.silve.codec.command;

public class InvalidSyntaxException extends Exception {

    public InvalidSyntaxException(String message) {
        super(message);
    }

    public InvalidSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
}
