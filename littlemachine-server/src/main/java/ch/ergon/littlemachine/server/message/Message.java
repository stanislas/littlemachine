package ch.ergon.littlemachine.server.message;

import java.io.IOException;
import java.util.UUID;

import org.fressian.Reader;
import org.fressian.Writer;

import ch.ergon.littlemachine.server.businesslogic.MachineTransition;

public class Message {

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

	public static void write(Message message, Writer writer) throws IOException {
		writer.writeObject(message.position);
		writer.writeInt(message.transition.ordinal());
		writer.writeInt(message.value);
	}

	public static Message read(Reader reader) throws Exception {
		UUID position = (UUID) reader.readObject();
		int transitionIdx = (int) reader.readInt();
		long value = reader.readInt();
		return new Message(position, MachineTransition.values()[transitionIdx], value);
	}

}
