package ch.ergon.littlemachine.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		msg.encode();
		out.writeByte((byte) 'F');
		out.writeInt(msg.rawMessage.length);
		out.writeBytes(msg.rawMessage);
	}

}
