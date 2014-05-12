package ch.ergon.littlemachine.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ch.ergon.littlemachine.server.incoming.IncomingMessageDecoder;
import ch.ergon.littlemachine.server.incoming.IncomingMessageHandler;

import com.google.common.base.Strings;

public class LittleMachine {

	private static final String PORT = "LITTLE_MACHINE_PORT";

	private final int port;

	public LittleMachine(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new IncomingMessageDecoder(), new IncomingMessageHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		String portEnv = System.getenv(PORT);
		int port;
		if (Strings.isNullOrEmpty(portEnv)) {
			port = 8080;
		} else {
			port = Integer.parseInt(args[0]);
		}
		new LittleMachine(port).run();
	}

}
