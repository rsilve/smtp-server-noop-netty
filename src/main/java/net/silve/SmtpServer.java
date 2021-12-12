package net.silve;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.silve.codec.ssl.SslUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "smtp-noop", mixinStandardHelpOptions = true,
        description = "black hole email server")
public class SmtpServer implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SmtpServer.class);

    @CommandLine.Option(names = {"-p", "--port"}, paramLabel = "port",
            description = "listening port (default: ${DEFAULT-VALUE})")
    int port = 2525;

    @Override
    public Integer call() throws Exception {
        run();
        return 0;
    }

    public void run() throws InterruptedException {

        SslUtils.initialize();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new SmtpServerChannelInitializer())
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
}
