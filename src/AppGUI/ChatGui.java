package AppGUI;
//package ba.bitcamp.nerminV.ChatAppGUI;

import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatGui implements Runnable {

	private JTextArea display;
	private TextField inputMsg;
	private Socket connection;
	private InputStream is;
	private OutputStream os;

	public ChatGui(final Socket connection) throws IOException {

		this.connection = connection;
		this.is = connection.getInputStream();
		this.os = connection.getOutputStream();

		JFrame window = new JFrame("MSN");
		JPanel content = new JPanel();
		JButton buttonSend = new JButton("SEND");

		display = new JTextArea();
		display.setEditable(false);

		inputMsg = new TextField();
		inputMsg.setPreferredSize(new Dimension(200, 20));

		buttonSend.addActionListener(new MessageHandler());
		inputMsg.addKeyListener(new MessageHandler());
		display.setLineWrap(true);

		JScrollPane areaScrollPane = new JScrollPane(display);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(390, 220));

		content.add(areaScrollPane);
		content.add(inputMsg);
		content.add(buttonSend);

		window.add(content);
window.addWindowListener(new WindowAdapter() {
	public void windowClosed(WindowEvent e){
		try {
			connection.shutdownOutput();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection.close();
			System.exit(0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
});
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					connection.shutdownOutput();
					connection.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		window.setSize(400, 300);
		window.setVisible(true);

	}

	public void listenForNetwork() throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(is));

		String line = null;
		while ((line = input.readLine()) != null) {
			
			if (!line.equals("")) {
				String[] arrayFirst = line.split(":");
				
				if (arrayFirst[0].equals("%server%")) {
					String[] arraySecond = arrayFirst[1].split("%");
					
					if (arraySecond[0].equals(" join")) {
						display.append(arraySecond[1] + " has joined this chat\n");

					} else if (arraySecond[0].equals(" left")) {
						display.append(arraySecond[1] + " has left the chat\n");
					}
				} else {
					display.append(line + "\n");
				}
				line = null;
			}
		}
	}

	private class MessageHandler extends KeyAdapter implements ActionListener {

		private void sendMessage() {
			String str = inputMsg.getText();
			if (!str.equals("")) {
				str += "\n";
				display.append("Me: " + str);
				try {
					os.write(str.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				inputMsg.setText(null);

			}

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessage();
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10)
				sendMessage();
		}
	}

	@Override
	public void run() {
		try {
			listenForNetwork();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}