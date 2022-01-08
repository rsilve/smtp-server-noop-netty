package net.silve.codec.configuration;

public class SmtpServerConfiguration {

    public final SmtpServerConfigurationResponses responses;
    public final int maxRecipientSize;
    private final SmtpServerConfigurationTls tls;

    public SmtpServerConfiguration(SmtpServerConfigurationBuilder builder) {
        this.responses = new SmtpServerConfigurationResponses(builder.getResponseMap(), builder.getBanner(), builder.getHostname());
        this.tls = new SmtpServerConfigurationTls(builder.isTls(), builder.getTlsCert(), builder.getTlsKey());
        this.maxRecipientSize = builder.getRecipientMaxSize();
    }

    public SmtpServerConfigurationTls getTls() {
        return tls;
    }
}
