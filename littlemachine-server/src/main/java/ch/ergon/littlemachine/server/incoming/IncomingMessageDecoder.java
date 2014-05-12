package ch.ergon.littlemachine.server.incoming;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Partially decodes a fressian message that has been encoded prepended with the
 * magic number 'F' and a 32-bit integer length prefix (see BigIntegerDecoder of
 * the Fibonnacci example).
 * 
 */
public class IncomingMessageDecoder extends ByteToMessageDecoder {

	private static final int MAGIC_NUMBER = 'F';

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 5) {
			return;
		}

		in.markReaderIndex();

		int magicNumber = in.readUnsignedByte();
		if (magicNumber != MAGIC_NUMBER) {
			in.resetReaderIndex();
			throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
		}

		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] decoded = new byte[dataLength];
		in.readBytes(decoded);
		out.add(decoded);
	}

}
