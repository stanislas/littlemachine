package ch.ergon.littlemachine.server.businesslogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import ch.ergon.littlemachine.server.LittleMachineModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.InRange;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Theories.class)
public class DefaultTransitionTableTest {

	public static final int RUN_LENGTH = 2000;
	public static final Random RANDOM = new Random();
	private Injector injector;
	
	@Before
	public void setUp() {
		injector = Guice.createInjector(new LittleMachineModule());
	}

	@Theory
	public void randomTransitions(@ForAll(sampleSize = 1000) @InRange(minInt = 1, maxInt = 10) int nOfPos) {
		Map<UUID, PositionState> positions = new HashMap<>(nOfPos);
		UUID[] uuids = new UUID[nOfPos];
		for (int i = 0; i < uuids.length; i++) {
			uuids[i] = UUID.randomUUID();
		}
		TransitionTable t = injector.getInstance(TransitionTable.class);
		int i = 0;
		for (; i < RUN_LENGTH; i++) {
			UUID position = uuids[RANDOM.nextInt(nOfPos)];
			MachineState currentState = DefaultBusinessLogicProcessor.getCurrentState(positions, position);
			MachineTransition transition = randomTransition();
			TransitionProcedure proc = t.nextTransition(currentState, transition);
			proc.transition(positions, position, RANDOM.nextLong());
		}

		assertThat(i, is(equalTo(RUN_LENGTH)));
	}

	private static MachineTransition randomTransition() {
		MachineTransition[] values = MachineTransition.values();
		int i = RANDOM.nextInt(values.length);
		return values[i];
	}

}
