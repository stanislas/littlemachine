package ch.ergon.littlemachine.server.message;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import ch.ergon.littlemachine.server.businesslogic.MachineTransition;

import com.pholser.junit.quickcheck.ForAll;

@RunWith(Theories.class)
public class MessageTest {

	public static final int MESSAGE_LENGTH = 29;

	@Theory
	public void encodeDecode(@ForAll(sampleSize = 100) MachineTransition transition,
			@ForAll(sampleSize = 100) long value) throws Exception {
		Message m = new Message(UUID.randomUUID(), transition, value);
		m.encode();
		Message m2 = new Message();
		m2.rawMessage = m.rawMessage;
		m2.decode();
		assertThat(m2, is(equalTo(m)));
	}

}
