package ch.ergon.littlemachine.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.fressian.FressianReader;

public class FressianClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf m = (ByteBuf) msg; // (1)
		try (ByteBufInputStream is = new ByteBufInputStream(m);
				FressianReader reader = new FressianReader(is)) {
			long readInt = reader.readInt();
			boolean readBoolean = reader.readBoolean();
			double readDouble = reader.readDouble();
			System.out.println(String.format("int: %s, boolean: %s, double: %s", readInt, readBoolean,
					readDouble));
			ctx.close();
		} finally {
			m.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
