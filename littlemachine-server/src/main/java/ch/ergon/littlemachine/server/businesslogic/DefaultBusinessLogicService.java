package ch.ergon.littlemachine.server.businesslogic;

import static ch.ergon.littlemachine.server.message.Message.MESSAGE_FACTORY;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.ergon.littlemachine.server.message.Message;

import com.google.inject.Inject;
import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DefaultBusinessLogicService implements BusinessLogicService {

	private final ExecutorService threadPool;
	private final Disruptor<Message> disruptor;
	private RingBuffer<Message> ringBuffer;


	@SuppressWarnings("unchecked")
	@Inject
	public DefaultBusinessLogicService(BusinessLogicProcessor businessLogicProcessor) {
		threadPool = Executors.newFixedThreadPool(4);
		disruptor = new Disruptor<>(MESSAGE_FACTORY, 1024, threadPool);
		MessagePersistanceHandler messagePersistanceHandler = new MessagePersistanceHandler();
		MessageDecodingHandler messageDecodingHandler = new MessageDecodingHandler();
		disruptor.handleEventsWith(messageDecodingHandler, messagePersistanceHandler);
		disruptor.after(
			messagePersistanceHandler,
			messageDecodingHandler).handleEventsWith(
			businessLogicProcessor);
	}

	@Override
	public void publishEvent(byte[] event) throws InsufficientCapacityException {
		long next = ringBuffer.tryNext();
		try {
			Message message = ringBuffer.get(next);
			message.rawMessage = event;
		} finally {
			ringBuffer.publish(next);
		}
	}

	@Override
	public void start() throws Exception {
		ringBuffer = disruptor.start();
	}

	@Override
	public void stop() {
		disruptor.shutdown();
		threadPool.shutdown();
	}

}
