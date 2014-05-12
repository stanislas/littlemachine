package ch.ergon.littlemachine.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.fressian.FressianReader;
import org.fressian.FressianWriter;

public class Message {

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
		try (ByteArrayInputStream bais = new ByteArrayInputStream(rawMessage);
				FressianReader reader = new FressianReader(bais)) {
			position = (UUID) reader.readObject();
			transition = MachineTransition.values()[(int) reader.readInt()];
			value = reader.readInt();
		}
	}
	
	public void encode() throws IOException {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
				FressianWriter writer = new FressianWriter(baos)) {
			writer.writeObject(position);
			writer.writeInt(transition.ordinal());
			writer.writeInt(value);
			rawMessage = baos.toByteArray();
		}
	}

}
