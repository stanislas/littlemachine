package ch.ergon.littlemachine.server.businesslogic;

import java.io.File;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import ch.ergon.littlemachine.server.message.Message;

import com.lmax.disruptor.EventHandler;

public class MessagePersistanceHandler implements EventHandler<Message> {

	private final DB db = DBMaker.newFileDB(new File("/tmp/littlemachine")).closeOnJvmShutdown().make();
	private final BTreeMap<Long, byte[]> messages;

	public MessagePersistanceHandler() {
		messages = db.getTreeMap("messages");
	}

	@Override
	public void onEvent(Message event, long sequence, boolean endOfBatch) throws Exception {
		messages.put(sequence, event.rawMessage);
		db.commit();
	}

}
