import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Client for the Encryption and Decryption program. <br />
 * Send commands to the Server in response to the GUI. <br />
 * Written for CpSc 1181. <br />
 * based on http://www.cs.armstrong.edu/liang/intro9e/book/Client.java
 * 
 * @author ******** your name ********
 * @version November 24, 2017
 */
public class Client extends JFrame implements Runnable, Encryptable {

	// the instance variables socket, toServer, and fromServer are
	// needed because of the interface Runnable that has a method run
	// that takes no arguments
	private Socket socket;
	private DataOutputStream toServer;
	private DataInputStream fromServer;

	// display the blank as a dot
	private char BLANK = '.';

	JTextField f1;
	JTextField f2;

	/**
	 * Set up the GUI, connect to the server and decrypt and encrypt.
	 * 
	 * @param args
	 *            the line commands -- not used
	 */
	public static void main(String[] args) {
		Client c = new Client(HOST);
		Thread thread = new Thread(c);

		thread.start();

	}

	/**
	 * Sets up the Graphical User Interface s and it sets ups the I/O streams
	 * from a socket. It then starts a thread if the socket to the server
	 * worked.
	 * 
	 * @param serverHost
	 *            IPAddress of server that is running already
	 */
	public Client(String serverHost) {
		buildGUI();

	} // Client

	/**
	 * Continues sending and receiving data as long as the server does not send
	 * a DONE
	 */
	@Override
	public void run() {
		openConnection("10.65.32.155");

		try {

			int n = fromServer.readInt();
			System.out.println("Command: " + n + " - " + cmdToString(n));
			System.out.println("Your number is: " + fromServer.readInt());
			n = fromServer.readInt();
			System.out.println("Command: " + n + " - " + cmdToString(n));
			System.out.println("Server is sending " + fromServer.readInt() + " characters");
			for (int i = 0; i < 5; i++) {
				System.out.println(fromServer.readChar());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
   * 
   */
	public void buildGUI() {
		final int FRAME_WIDTH = 1200;
		final int FRAME_HEIGHT = 300;

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(2, 2));

		f1 = new JTextField("This is my message!");
		f2 = new JTextField(" ");

		JButton decrypt = new JButton("Decrypt");
		ActionListener listener = new DecryptButton();
		decrypt.addActionListener(listener);

		JButton encrypt = new JButton("Encrypt");

		myPanel.add(f1);
		myPanel.add(decrypt);

		myPanel.add(f2);
		myPanel.add(encrypt);

		add(myPanel);

		setTitle("Encoder and Decoder");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	} // buildGUI

	class DecryptButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String msg = f1.getText();
			System.out.println(msg);

			try {
				toServer.writeInt(ENCRYPT);
				toServer.writeInt(msg.length());
				for (int i = 0; i < msg.length(); i++) {
					toServer.writeChar(msg.charAt(i));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// toServer.writeChar('T');
			// toServer.writeChar('E');
			// toServer.writeChar('H');
			// toServer.writeChar('E');
			// toServer.writeChar('E');
			// toServer.flush();
		}
	}

	/**
	 * Creates a socket with the GAME_PORT and opens its input and output
	 * streams called fromServer and toServer.
	 */
	private void openConnection(String serverHost) {
		try {
			this.socket = new Socket(serverHost, PORT);
			this.fromServer = new DataInputStream(socket.getInputStream());
			this.toServer = new DataOutputStream(socket.getOutputStream());
		} catch (SecurityException e) {
			System.err.print("a security manager exists: ");
			System.err.println("its checkConnect doesn't allow the connection");
			System.err.println("without a SERVER, I'm toast ... no point going on so bye, bye");
		} catch (UnknownHostException e) {
			System.err.println("the IP address of the host could not be found...cannot go on, bye");
		} catch (IOException e) {
			System.err.println("cannot seem to be able to connect to the server \"" + serverHost + "\"");
			System.err.println("without a SERVER, I'm toast ... no point going on so bye, bye");
		}

	} // openConnection

} // Client 