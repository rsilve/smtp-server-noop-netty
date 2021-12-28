package net.silve;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.logger.SmtpLogger;
import picocli.CommandLine;

import javax.annotation.Nonnull;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;


@CommandLine.Command(name = "smtp-noop", mixinStandardHelpOptions = true,
        description = "black hole email server")
public class SmtpServer implements Callable<Integer> {

    @CommandLine.Option(names = {"-p", "--port"}, paramLabel = "port",
            description = "listening port (default: ${DEFAULT-VALUE})")
    int port = 2525;

    @CommandLine.Option(names = {"--tls"}, paramLabel = "tls",
            description = "enable STARTTLS (default: ${DEFAULT-VALUE})")
    boolean tls = false;

    @CommandLine.Option(names = {"--tls-cert"}, paramLabel = "tls-cert",
            description = "set certificate file for TLS")
    String tlsCert;

    @CommandLine.Option(names = {"--tls-key"}, paramLabel = "tls-key",
            description = "set key file for TLS")
    String tlsKey;

    @CommandLine.Option(names = {"--banner"}, paramLabel = "banner",
            description = "set SMTP greeting banner (default: ${DEFAULT-VALUE})")
    String banner = "no-op ESMTP";

    @CommandLine.Option(names = {"--hostname"}, paramLabel = "hostname",
            description = "set hostname")
    String hostname;

    @CommandLine.Option(names = {"--thread"}, paramLabel = "thread",
            description = "fix number of thread (0 for automatic setting)")
    int thread = 0;

    @Override
    public Integer call() throws Exception {
        run();
        return 0;
    }

    public void run() throws InterruptedException, UnknownHostException {
        SmtpServerConfiguration configuration = configure();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(thread);

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(workerGroup, bossGroup));

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new SmtpServerChannelInitializer(configuration))
                    .childOption(ChannelOption.AUTO_CLOSE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_LINGER, 0);


            ChannelFuture f = b.bind(this.port).sync();
            SmtpLogger.info("Listening to {}", this.port);
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Nonnull
    public SmtpServerConfiguration configure() throws UnknownHostException {
        SmtpServerConfigurationBuilder builder = new SmtpServerConfigurationBuilder()
                .setBanner(banner)
                .setHostname(hostname)
                .setTls(tls)
                .setTlsCert(tlsCert)
                .setTlsKey(tlsKey);
        return new SmtpServerConfiguration(builder);
    }

    private static class ShutdownThread extends Thread {
        private final EventLoopGroup workerGroup;
        private final EventLoopGroup bossGroup;

        public ShutdownThread(EventLoopGroup workerGroup, EventLoopGroup bossGroup) {
            this.workerGroup = workerGroup;
            this.bossGroup = bossGroup;
        }

        @Override
        public void run() {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            SmtpLogger.info("termination signal receive: stop process");
        }
    }
}
