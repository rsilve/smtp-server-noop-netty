package net.silve.codec.ssl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class SslUtils {

    private static final Logger logger = LoggerFactory.getLogger(SslUtils.class);

    private static SslContext sslCtx;

    public static void initialize() {
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            logger.info("SSL context initialized DN: '{}'", ssc.cert().getIssuerDN());
        } catch (CertificateException e) {
            logger.error("Failed to initialize self signed certificate", e);
        } catch (SSLException e) {
            logger.error("Failed to initialize TLS context", e);
        }
    }

    public static SslContext getSslCtx() {
        return sslCtx;
    }

    private SslUtils() {
    }
}
