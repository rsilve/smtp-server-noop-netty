package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpResponse;


public class InvalidProtocolException extends Exception {

    private final transient SmtpResponse response;

    public InvalidProtocolException(SmtpResponse response) {
        super(response.toString());
        this.response = response;
    }


    public SmtpResponse getResponse() {
        return response;
    }
}
