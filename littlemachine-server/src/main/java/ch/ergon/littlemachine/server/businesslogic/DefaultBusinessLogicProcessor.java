package ch.ergon.littlemachine.server.businesslogic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ch.ergon.littlemachine.server.message.Message;

import com.google.inject.Inject;

public class DefaultBusinessLogicProcessor implements BusinessLogicProcessor {

	public final Map<UUID, PositionState> positions = new HashMap<>(100000);

	public final TransitionTable transitionTable;

	@Inject
	public DefaultBusinessLogicProcessor(TransitionTable transitionTable) {
		this.transitionTable = transitionTable;
	}

	@Override
	public void onEvent(Message m, long sequence, boolean endOfBatch) throws Exception {
		MachineState state = getCurrentState(positions, m.position);
		TransitionProcedure proc = transitionTable.nextTransition(state, m.transition);
		boolean successful = proc.transition(positions, m.position, m.value);
		if (!successful) {
			// Log back.
		}
	}

	public static MachineState getCurrentState(Map<UUID, PositionState> positions, UUID position) {
		PositionState state = positions.get(positions);
		return state == null ? MachineState.NULL : state.state;
	}

}
