package ch.ergon.littlemachine.server.businesslogic;

import ch.ergon.littlemachine.server.message.Message;

import com.lmax.disruptor.EventHandler;

public interface BusinessLogicProcessor extends EventHandler<Message> {

}
