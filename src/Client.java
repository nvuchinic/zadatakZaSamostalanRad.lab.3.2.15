//package ba.bitcamp.lab.Chat.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

//import GUI.ChatGUI;

public class Client {
	public static final int port = 1717;
	public static final String host = "localhost";
	//OutputStream os;
	
	
	public static void main(String[] args) {
		
		LoginGUI lgui=new LoginGUI(host,port);
	}
}
