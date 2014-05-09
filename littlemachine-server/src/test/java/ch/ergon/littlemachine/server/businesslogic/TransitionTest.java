package ch.ergon.littlemachine.server.businesslogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.InRange;

import static org.junit.Assume.assumeThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Theories.class)
public class TransitionTest {

	public static final int RUN_LENGTH = 1000;
	public static final Random RANDOM = new Random();

	@Theory
	public void randomTransitions(@ForAll(sampleSize = 100) @InRange(minInt = 1, maxInt = 10) int nOfPos) {
		Map<UUID, PositionState> positions = new HashMap<>(nOfPos);
		UUID[] uuids = new UUID[nOfPos];
		for (int i = 0; i < uuids.length; i++) {
			uuids[i] = UUID.randomUUID();
		}
		TransitionTable t = new TransitionTable();
		int i = 0;
		for (; i < RUN_LENGTH; i++) {
			UUID position = uuids[RANDOM.nextInt(nOfPos)];
			MachineState currentState = getCurrentState(positions, position);
			MachineTransition transition = randomTransition();
			TransitionProcedure proc = t.nextTransition(currentState, transition);
			proc.transition(positions, position, RANDOM.nextLong());
		}

		assumeThat(i, is(equalTo(RUN_LENGTH)));
	}

	private static MachineState getCurrentState(Map<UUID, PositionState> positions, UUID position) {
		PositionState state = positions.get(positions);
		return state == null ? MachineState.NULL : state.state;
	}

	private static MachineTransition randomTransition() {
		MachineTransition[] values = MachineTransition.values();
		int i = RANDOM.nextInt(values.length);
		return values[i];
	}

}
