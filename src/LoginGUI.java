//package ba.bitcamp.lab.Chat.Client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

public class LoginGUI {

	private JTextArea userNameArea;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton quitButton;
	private String host;
	private int port;

	public LoginGUI(String host, int port) {
		this.host = host;
		this.port = port;
		JFrame window = new JFrame("Login");
		window.setResizable(false);
		window.setSize(300, 300);
		JPanel content = new JPanel();
		loginButton = new JButton("LOGIN");
		quitButton = new JButton("QUIT");
		userNameArea = new JTextArea();
		userNameArea.setPreferredSize(new Dimension(250, 20));
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(250, 20));
		JLabel userNameLabel = new JLabel("Enter username:");
		JLabel passwordLabel = new JLabel("Enter password:");
		content.add(userNameLabel);
		content.add(userNameArea);
		content.add(passwordLabel);
		content.add(passwordField);
		content.add(loginButton);
		content.add(quitButton);
		ButtonHandler bh = new ButtonHandler();
		loginButton.addActionListener(bh);
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(content);
		window.setVisible(true);

	}

	private static void showError(String message) {

		JOptionPane.showMessageDialog(null, message, "ERROR",
				JOptionPane.WARNING_MESSAGE);

	}

	public class ButtonHandler implements ActionListener {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {

			String userName = userNameArea.getText();
			String password = new String(passwordField.getPassword());
			userName = userName.replaceAll(" ", "");
			password = password.replaceAll(" ", "");
			System.out.println(userName);
			System.out.println(password);
			if (userName.equals("") || password.equals("")) {
				showError("Unesite username i password!");
				return;
			}

			String passwordToHash = password;
			String hashedPassword = null;
			try {
				// Create MessageDigest instance for MD5
				MessageDigest md = MessageDigest.getInstance("MD5");
				// Add password bytes to digest
				md.update(passwordToHash.getBytes());
				// Get the hash's bytes
				byte[] bytes = md.digest();
				// This bytes[] has bytes in decimal format;
				// Convert it to hexadecimal format
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
							.substring(1));
				}
				// Get complete hashed password in hex format
				hashedPassword = sb.toString();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}

			Socket client;
			try {
				client = new Socket(host, port);
				OutputStream os = client.getOutputStream();
				InputStream is = client.getInputStream();
				os.write((userName + "\n").getBytes());
				os.write((hashedPassword + "\n").getBytes());
				int result = is.read();
				if (result == 0) {
					ChatGui cgui = new ChatGui(client);
					new Thread(cgui).start();
				} else
					System.out.println("Error!");
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/*
			 * try { ChatGUI gui = new ChatGUI(client); } catch (IOException e1)
			 * { // TODO Auto-generated catch block e1.printStackTrace(); } new
			 * Thread(gui).start();
			 */
		}

	}
}
