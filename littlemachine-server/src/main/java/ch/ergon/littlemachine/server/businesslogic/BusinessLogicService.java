package ch.ergon.littlemachine.server.businesslogic;

import ch.ergon.littlemachine.server.Service;
import com.lmax.disruptor.InsufficientCapacityException;

public interface BusinessLogicService extends Service {

	void publishEvent(byte[] event) throws InsufficientCapacityException;

}
