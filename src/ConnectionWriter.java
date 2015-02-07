//package ba.bitcamp.nermin.chatApp.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ConnectionWriter extends Thread {

	public static HashMap<String, OutputStream> connections = new HashMap<String, OutputStream>();
	private Set<String> keys = connections.keySet();

	@Override
	public void run() {

		while (true) {

			if (Message.hasNext()) {
				System.out.println("Writer: " + Message.hasNext());
				Message msg = Message.next();
				byte[] messageToSend = ((msg.getSender() + ": " + msg.getContent()).getBytes()); 
				Iterator<String> it = keys.iterator();

				while (it.hasNext()) {

					String current = it.next();

					if (!current.equals(msg.getSender())) {
						OutputStream os = connections.get(current);

						try {

							os.write(messageToSend);
							os.flush();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}

		}

	}

}