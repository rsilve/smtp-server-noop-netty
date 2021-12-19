package net.silve.codec.ssl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import net.silve.codec.logger.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SslUtils {

    private static final Logger logger = LoggerFactory.getInstance();

    private static SslContext sslCtx;
    private static boolean tlsEnabled;

    private SslUtils() {
    }

    public static void initialize(boolean useTls) {
        tlsEnabled = useTls;
        if (tlsEnabled) {
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
                logger.log(Level.INFO, "SSL context initialized DN: ''{0}''", ssc.cert().getIssuerDN());
            } catch (CertificateException e) {
                logger.log(Level.SEVERE, "Failed to initialize self signed certificate", e);
            } catch (SSLException e) {
                logger.log(Level.SEVERE, "Failed to initialize TLS context", e);
            }
        } else {
            sslCtx = null;
        }
    }

    public static boolean isTlsEnabled() {
        return tlsEnabled;
    }

    public static SslContext getSslCtx() {
        return sslCtx;
    }
}
