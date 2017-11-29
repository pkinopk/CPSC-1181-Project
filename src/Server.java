import java.awt.BorderLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * Server for the Encryption and Decryption of messages <br />
 * Written for CpSc 1181 <br />
 * inspired losely by programs in Y. D. Liang's book on Java
 * http://www.cs.armstrong.edu/liang/intro9e/book/MultiThreadServer.java
 *
 * @author G. Monagan
 * @version April 1, 2017
 */
public class Server extends JFrame implements Encryptable {
	// dimensions of frame
	public static final int FRAME_WIDTH = 500;
	public static final int FRAME_HEIGHT = 400;

	// to format the date 12-Nov-2016 10:15 PM
	private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

	// display the messages in this JTextArea
	private JTextArea textAreaLog;

	/**
	 * Starts the server which creates a log window, opens one socket per client
	 * and starts an encryption / decryption (in its own thread). The Server
	 * begins the converstation by giving a client a name (a client number).
	 * 
	 * @param args
	 *            line arguments -- not used
	 */
	public static void main(String[] args) {
		new Server();
	}

	/**
	 * Writes onto the textAreaLog a string and adds a carriage return. Scroll
	 * down so that the bottom of the textArea is always shown, i.e. the last
	 * line is always displayed
	 * 
	 * @param msg
	 *            the message to display.
	 */
	private void report(String msg) {
		textAreaLog.append(msg + '\n');
		textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
	} // report

	/**
	 * Builds an extended frame that has a text area to capture the messages
	 * e.g. the log of connections and when a game begins.
	 */
	public void buildLogFrame() {
		// a text area made up of many lines
		textAreaLog = new JTextArea();
		// so that the last line is always shown....
		DefaultCaret caret = (DefaultCaret) textAreaLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		// adjust the frame size ad other properties
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setTitle("Log of Activities");
		add(new JScrollPane(textAreaLog), BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	} // buildLogFrame

	/**
	 * Reports the server's IP address. <br>
	 * It has nothing to do with the clients. Gives current time.
	 * 
	 * @return gives the date and time nicely formatter
	 * @throws UnknownHostException
	 *             due to not a proper server
	 */
	private String reportStatsOnServer() throws UnknownHostException {
		report("This server's computer name is " + InetAddress.getLocalHost().getHostName());
		report("This server's IP address is " + InetAddress.getLocalHost().getHostAddress() + "\n");
		return LocalDateTime.now().format(FORMATTER);
	} // reportStatsOnServer

	/**
	 * Reports the clients's domain name and IP address. <br />
	 * .... just for fun
	 * 
	 * @param socket
	 *            an open socket
	 * @param n
	 *            the client's number (starting with 1)
	 */
	private void reportStatsOnClient(Socket socket, int n) {

		InetAddress addr = socket.getInetAddress();
		report("client " + n + "'s host name is " + addr.getHostName());
		report("client " + n + "'s IP Address is " + addr.getHostAddress());
		report("starting thread for client " + n + " at " + LocalDateTime.now().format(FORMATTER));
	} // reportStatsOnClient

	/**
	 * Builds a reporting window. It opens a server socket. It connects through
	 * a socket to a client. It provides encrypting and decrypting for that
	 * client (handles the client) It continues on and on.
	 */
	public Server() {
		// to report the server's error messages
		buildLogFrame();

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			String nowStr = reportStatsOnServer();
			report("The server, port " + serverSocket.getLocalPort() + ", started on " + nowStr);

			int clientNumber = 0;
			while (true) {
				clientNumber++;
				// listen for a new connection request
				Socket socket = serverSocket.accept();
				reportStatsOnClient(socket, clientNumber);

				// create a client thread for the connection
				Runnable service = new HandleAClient(socket, textAreaLog, clientNumber);
				new Thread(service).start();
			} // end while true
		} // end try
		catch (IOException e) {
			report("problems in server " + e.toString());
			e.printStackTrace(System.err);
		} // catch IOException
	} // Server
}
