package net.silve.codec.configuration;

import io.netty.handler.codec.smtp.SmtpResponse;

import java.util.Map;

import static net.silve.codec.configuration.DefaultResponse.defaultResponsesMap;

public class SmtpServerConfigurationBuilder {

    private int port;
    private Map<String, SmtpResponse> responseMap = defaultResponsesMap;

    public SmtpServerConfigurationBuilder() {
        /* empty */
    }

    public int getPort() {
        return port;
    }

    public SmtpServerConfigurationBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public Map<String, SmtpResponse> getResponseMap() {
        return responseMap;
    }

    public SmtpServerConfigurationBuilder setResponseMap(Map<String, SmtpResponse> responseMap) {
        this.responseMap = responseMap;
        return this;
    }
}
