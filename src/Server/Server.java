package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Server {

	public static final int port = 1717;

	public static void serverStart() throws IOException {
		ServerSocket server = new ServerSocket(port);
		ConnectionWriter cw = new ConnectionWriter();
		cw.start();

		while (true) {
			String str = "waiting for connection";
			System.out.println(str);
			Socket client;

			try {
				client = server.accept();

				String clientName = handShake(client.getInputStream());

				if (clientName != null) {

					while (ConnectionWriter.connections.containsKey(clientName)) {

						clientName += new Random().nextInt(1000);
					}

					ConnectionWriter.connections.put(clientName,
							client.getOutputStream());
					ConnectionListener connListener = new ConnectionListener(
							client.getInputStream(), clientName);
					connListener.start();
					new Message("%server%", "join%" + clientName);

				} else {
					client.getOutputStream().write(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static String handShake(InputStream is) throws IOException,
			TransformerException, NoSuchAlgorithmException {

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String name = br.readLine();
		name = name.replaceAll("%", ""); // da ne bi client mogao dodati '%' zna
										// kao svoj userName
		String pass = br.readLine();
		int result = XmlConnection.userLogin(name, pass);
		/*if (result != 0) {
			return null;
		}*/
		if (result == 0) {
			JOptionPane
					.showMessageDialog(
							null,
							"Ne postoji korisnik sa tim username-om i/ili passwordom!\nUspjesno ste registrovani kao novi korisnik!");
		} else if (result == -4) {
			JOptionPane.showMessageDialog(null,"Ne postoji korisnik sa tim username-om i/ili passwordom!" +
					"\nPrilikom vase registracije kao novog korisnika "
									+ "doslo je do greske!");
		}
		else if(result==-1){
			JOptionPane.showMessageDialog(null,"Unijeli ste ispravan username. ali pogresan password!");
		}
		else if(result==-3){
			JOptionPane.showMessageDialog(null,"Doslo je do greske prilikom citanaj XML file-a!");
		}

		return name;

	}

	public static void main(String[] args) {
		try {
			new XmlConnection();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			serverStart();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
