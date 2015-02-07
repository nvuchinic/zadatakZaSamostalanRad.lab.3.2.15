package Server;
import java.awt.Dimension;
import java.awt.TextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerGUI {
	private JTextArea display;

	public ServerGUI() {
		JFrame window = new JFrame("Server");
		JPanel content = new JPanel();
		display = new JTextArea();
		// display.setPreferredSize(new Dimension(300, 200));
		display.setEditable(false);
		display.setLineWrap(true);
		JScrollPane areaScrollPane = new JScrollPane(display);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(390, 220));
		content.add(areaScrollPane);
		window.add(content);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(400, 300);
		window.setVisible(true);
	}

	public void logConnection(String ip, String name) {
		display.append("Connected: " + ip + " with name: " + name + "\n");
	}
}