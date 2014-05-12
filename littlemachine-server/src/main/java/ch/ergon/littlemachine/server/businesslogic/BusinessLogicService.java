package ch.ergon.littlemachine.server.businesslogic;

import ch.ergon.littlemachine.server.Service;

public interface BusinessLogicService extends Service {

	boolean publishEvent(byte[] event);

}
