package net.silve.codec.configuration;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import net.silve.codec.logger.LoggerFactory;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SmtpServerConfigurationTls {

    private static final Logger logger = LoggerFactory.getInstance();
    private boolean enabled;
    private SslContext sslCtx;

    public SmtpServerConfigurationTls(boolean enabled, String cert, String key) {
        this.enabled = enabled;
        if (enabled) {
            try {
                Objects.requireNonNull(cert, "Certificate file is required");
                Objects.requireNonNull(key, "Private key file is required");
                if (cert.isBlank()) {
                    throw new IllegalArgumentException("Certificate file must not be blank");
                }
                if (key.isBlank()) {
                    throw new IllegalArgumentException("Private key file must not be blank");
                }
                File certFile = new File(cert);
                File keyFile = new File(key);
                sslCtx = SslContextBuilder.forServer(certFile, keyFile).build();
                logger.log(Level.INFO, "SSL context initialized");
            } catch (Exception e) {
                this.enabled = false;
                logger.log(Level.WARNING, "SSL context initialization failed. StartTLS disabled", e);
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public SslContext getSslCtx() {
        return sslCtx;
    }
}
