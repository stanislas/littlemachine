package ch.ergon.littlemachine.server.incoming;

import com.lmax.disruptor.InsufficientCapacityException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ch.ergon.littlemachine.server.businesslogic.BusinessLogicService;

import com.google.inject.Inject;

public class DefaultIncomingMessageHandler extends ChannelInboundHandlerAdapter implements IncomingMessageHandler {

	private final BusinessLogicService businessLogicService;

	@Inject
	public DefaultIncomingMessageHandler(BusinessLogicService businessLogicService) {
		this.businessLogicService = businessLogicService;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws InsufficientCapacityException {
		byte[] message = (byte[]) msg;
		businessLogicService.publishEvent(message);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
