package ch.ergon.littlemachine.server.businesslogic;

import java.util.Map;
import java.util.UUID;

public interface TransitionProcedure {

	boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer);

}
