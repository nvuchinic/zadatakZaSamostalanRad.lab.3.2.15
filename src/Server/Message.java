package Server;
//package ba.bitcamp.nermin.chatApp.server;

import java.util.LinkedList;
import java.util.Queue;

public class Message {

	private String sender;
	private String content;
	private static volatile Queue<Message> messageQueue = new LinkedList<Message>();

	public Message(String sender, String content) {
		this.sender = sender;
		this.content = content + "\n";
		messageQueue.add(this);
	}

	public static boolean hasNext() {

		return !messageQueue.isEmpty();

	}

	/**
	 * @return a message from the messageQueue and removes it
	 */
	public static Message next() {
		return messageQueue.poll();
	}

	public String getSender() {
		return sender;
	}

	public String getContent() {
		return content;
	}

}
