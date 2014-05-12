package ch.ergon.littlemachine.server.message;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.fressian.FressianReader;
import org.fressian.FressianWriter;
import org.fressian.Reader;
import org.fressian.Writer;
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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Writer w = new FressianWriter(baos);
		Message.write(m, w);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		Reader r = new FressianReader(bais);
		Message m2 = Message.read(r);
		assertThat(m2, is(equalTo(m)));
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}
