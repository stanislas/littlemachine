package ch.ergon.littlemachine.server.businesslogic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BusinessLogicProcessor {
	
	public final Map<UUID, PositionState> positions = new HashMap<>(100000);
	
	public final TransitionTable transitionTable = new TransitionTable();

}
