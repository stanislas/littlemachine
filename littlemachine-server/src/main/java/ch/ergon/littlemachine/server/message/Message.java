package ch.ergon.littlemachine.server.message;

import ch.ergon.littlemachine.server.businesslogic.MachineTransition;
import com.cognitect.transit.Reader;
import com.cognitect.transit.TransitFactory;
import com.cognitect.transit.Writer;
import com.lmax.disruptor.EventFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Message {

	public final static EventFactory<Message> MESSAGE_FACTORY = new EventFactory<Message>() {
		@Override
		public Message newInstance() {
			return new Message();
		}
	};

	public byte[] rawMessage;
	public UUID position;
	public MachineTransition transition;
	public long value;

	public Message() {

	}

	public Message(UUID position, MachineTransition transition, long value) {
		this.position = position;
		this.transition = transition;
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((transition == null) ? 0 : transition.hashCode());
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (transition != other.transition)
			return false;
		if (value != other.value)
			return false;
		return true;
	}
	
	public void decode() throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(rawMessage)) {
			Reader reader = TransitFactory.reader(TransitFactory.Format.MSGPACK, bais);
			position = reader.read();
			transition = MachineTransition.values()[((Long)reader.read()).intValue()];
			value = reader.read();
		}
	}
	
	public void encode() throws IOException {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream(30)) {
			Writer writer = TransitFactory.writer(TransitFactory.Format.MSGPACK, baos);
			writer.write(position);
			writer.write(transition.ordinal());
			writer.write(value);
			rawMessage = baos.toByteArray();
		}
	}

}
