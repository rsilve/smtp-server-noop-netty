package net.silve;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.silve.codec.configuration.SmtpServerConfiguration;
import net.silve.codec.configuration.SmtpServerConfigurationBuilder;
import net.silve.codec.ssl.SslUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "smtp-noop", mixinStandardHelpOptions = true,
        description = "black hole email server")
public class SmtpServer implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SmtpServer.class);

    @CommandLine.Option(names = {"-p", "--port"}, paramLabel = "port",
            description = "listening port (default: ${DEFAULT-VALUE})")
    int port = 2525;

    @CommandLine.Option(names = {"--tls"}, paramLabel = "tls",
            description = "enable STARTTLS (default: ${DEFAULT-VALUE})")
    boolean tls = false;

    @CommandLine.Option(names = {"--banner"}, paramLabel = "banner",
            description = "set SMTP greeting banner (default: ${DEFAULT-VALUE})")
    String banner = "no-op ESMTP";

    @CommandLine.Option(names = {"--hostname"}, paramLabel = "hostname",
            description = "set hostname")
    String hostname;

    @Override
    public Integer call() throws Exception {
        run();
        return 0;
    }

    public void run() throws InterruptedException, UnknownHostException {
        SmtpServerConfiguration configuration = configure();
        SslUtils.initialize(this.tls);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new SmtpServerChannelInitializer(configuration))
                    .childOption(ChannelOption.AUTO_CLOSE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_LINGER, 0);


            ChannelFuture f = b.bind(this.port).sync();
            logger.info("Listening to {}", this.port);
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
        String hostName = hostname;

        if (Objects.isNull(hostName) || hostName.isBlank()) {
            hostName = InetAddress.getLocalHost().getHostName();
        }

        SmtpServerConfigurationBuilder builder = new SmtpServerConfigurationBuilder()
                .setBanner(banner)
                .setHostname(hostName);
        return new SmtpServerConfiguration(builder);
    }
}
