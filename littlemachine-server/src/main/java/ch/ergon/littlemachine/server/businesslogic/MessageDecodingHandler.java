package ch.ergon.littlemachine.server.businesslogic;

import ch.ergon.littlemachine.server.message.Message;

import com.lmax.disruptor.EventHandler;

public class MessageDecodingHandler implements EventHandler<Message> {

	@Override
	public void onEvent(Message event, long sequence, boolean endOfBatch) throws Exception {
		event.decode();
	}

}
