package ch.ergon.littlemachine.server.businesslogic;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import static ch.ergon.littlemachine.server.businesslogic.MachineState.CLOSED_EXPORTED;
import static ch.ergon.littlemachine.server.businesslogic.MachineState.NULL;
import static ch.ergon.littlemachine.server.businesslogic.MachineState.OPENED;
import static ch.ergon.littlemachine.server.businesslogic.MachineTransition.ACK;
import static ch.ergon.littlemachine.server.businesslogic.MachineTransition.ADD;
import static ch.ergon.littlemachine.server.businesslogic.MachineTransition.CLOSE_EXPORT;
import static ch.ergon.littlemachine.server.businesslogic.MachineTransition.OPEN;
import static ch.ergon.littlemachine.server.businesslogic.MachineTransition.TIMEOUT;

public class DefaultTransitionTable implements TransitionTable {

	public final TransitionProcedure ignoreTransition = new TransitionProcedure() {
		@Override
		public boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer) {
			return true;
		}
	};

	public final TransitionProcedure impossibleTransition = new TransitionProcedure() {
		@Override
		public boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer) {
			return false;
		}
	};

	public final TransitionProcedure openTransition = new TransitionProcedure() {
		@Override
		public boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer) {
			PositionState state = new PositionState();
			state.state = OPENED;
			state.value = 0;
			positions.put(position, state);
			return true;
		}
	};

	public final TransitionProcedure addTransition = new TransitionProcedure() {
		@Override
		public boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer) {
			PositionState state = positions.get(position);
			state.value += incrementer;
			return true;
		}
	};

	public final TransitionProcedure closeExportTransition = new TransitionProcedure() {
		@Override
		public boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer) {
			PositionState state = positions.get(position);
			state.state = MachineState.CLOSED_EXPORTED;
			// Notify of Timeout wanted.
			return true;
		}
	};

	public final TransitionProcedure timeoutTransition = new TransitionProcedure() {
		@Override
		public boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer) {
			// log error
			positions.remove(position);
			return true;
		}
	};

	public final TransitionProcedure ackTransition = new TransitionProcedure() {
		@Override
		public boolean transition(Map<UUID, PositionState> positions, UUID position, long incrementer) {
			positions.remove(position);
			return true;
		}
	};

	public final EnumMap<MachineState, EnumMap<MachineTransition, TransitionProcedure>> transitions;

	@Inject
	public DefaultTransitionTable() {
		transitions = Maps.newEnumMap(MachineState.class);
		initTransitions();
		wireTransitions();
	}

	private void initTransitions() {
		for (MachineState ms : MachineState.values()) {
			EnumMap<MachineTransition, TransitionProcedure> map = Maps.newEnumMap(MachineTransition.class);
			transitions.put(ms, map);
		}
	}

	private void wireTransitions() {
		wire(NULL, OPEN, openTransition);
		wire(OPENED, ADD, addTransition);
		wire(OPENED, CLOSE_EXPORT, closeExportTransition);
		wire(CLOSED_EXPORTED, ACK, ackTransition);
		wire(CLOSED_EXPORTED, TIMEOUT, timeoutTransition);
		wire(NULL, TIMEOUT, ignoreTransition);
	}

	private void wire(MachineState state, MachineTransition transition, TransitionProcedure proc) {
		transitions.get(state).put(transition, proc);
	}

	@Override
	public TransitionProcedure nextTransition(MachineState currentState, MachineTransition transition) {
		TransitionProcedure proc = transitions.get(currentState).get(transition);
		return proc == null ? impossibleTransition : proc;
	}

}
