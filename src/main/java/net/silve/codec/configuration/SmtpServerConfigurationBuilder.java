package net.silve.codec.configuration;

import io.netty.handler.codec.smtp.SmtpResponse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

import static net.silve.codec.configuration.DefaultResponse.defaultResponsesMap;

public class SmtpServerConfigurationBuilder {

    private int port;
    private Map<String, SmtpResponse> responseMap = defaultResponsesMap;
    private String banner = "no-op ESMTP";
    private String hostname = "<hostname>";

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

    public String getBanner() {
        return banner;
    }

    public SmtpServerConfigurationBuilder setBanner(String banner) {
        this.banner = banner;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public SmtpServerConfigurationBuilder setHostname(String hostname) throws UnknownHostException {
        String hostName = hostname;

        if (Objects.isNull(hostName) || hostName.isBlank()) {
            hostName = InetAddress.getLocalHost().getHostName();
        }

        this.hostname = hostName;
        return this;
    }
}
