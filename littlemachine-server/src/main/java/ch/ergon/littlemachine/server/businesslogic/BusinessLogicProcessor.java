package ch.ergon.littlemachine.server.businesslogic;

import ch.ergon.littlemachine.server.message.Message;

public interface BusinessLogicProcessor {

	public abstract void processMessage(Message m);

}
