package net.silve.codec.configuration;

public class SmtpServerConfiguration {

    public final SmtpServerConfigurationResponses responses;

    public SmtpServerConfiguration(SmtpServerConfigurationBuilder builder) {
        this.responses = new SmtpServerConfigurationResponses(builder.getResponseMap(), builder.getBanner(), builder.getHostname());
    }

}
