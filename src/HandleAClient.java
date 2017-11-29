import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JTextArea;

/**
 * Defines the thread class for handling a new connection... with its run
 * command process the commands sent by the client to encrypt and decrypt. It
 * starts the process by sending a NAME to the client.
 * 
 * @author Gladys Monagan... loosely based on code by Daniel Liang
 * @version March 31, 2017
 */

public class HandleAClient implements Runnable, Encryptable {
	// already opened socket
	private Socket socket;
	// place where the messages of the log are reported
	private JTextArea textAreaLog;
	// the server numbers the clients: report this number when outputting
	private int clientNumber;

	private DataInputStream fromClient;
	private DataOutputStream toClient;

	// Masks on the console the secret message sent to the Client
	// so that it is not "reported" on the textAreaLog
	private static final boolean MASKING_OUTPUT_SECRET_TEXT = true;

	/**
	 * Receives the open socket so that input/output streams can be attached and
	 * it receives the JTextArea where the messages will be logged. It keeps
	 * track of which client it's interacting with.
	 * 
	 * @param s
	 *            a socket that is opened already
	 * @param tA
	 *            a JTextArea that receives messages
	 * @param cN
	 *            the client number (used for reporting purposes)
	 */
	public HandleAClient(Socket s, JTextArea tA, int cN) {
		socket = s;
		textAreaLog = tA;
		clientNumber = cN;
	}

	/**
	 * Helper function to report messages to the textAreaLog which is an
	 * instance variable. It keeps the last appended line showing at the bottom.
	 */
	private void report(String direction, String msg) {
		textAreaLog.append(direction + " client " + clientNumber + ": ");
		// textAreaLog.append(Thread.currentThread().getName() + " : ");
		textAreaLog.append(msg + '\n');
		textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
	}

	/**
	 * Runs a thread: - set up the DataInputStream and the DataOutputStream -
	 * call for the commands from the client to be executed - clean up
	 */
	public void run() {
		try {
			try {
				// create the data input and output streams
				// we put them here instead of in the constructor so that
				// we do not have to put a try catch in the constructor
				fromClient = new DataInputStream(socket.getInputStream());
				toClient = new DataOutputStream(socket.getOutputStream());
				executeCmds();
			} finally // close may throw an Exception
			{
				socket.close();
			}
		} catch (Exception e) {
			// could be an IOException but also a NullPointerException
			// for clients' (students in the lab at Langara) to have a shorter
			// message
			report("ERROR ", e.getMessage());
			// report("in run of HandleClient", e.toString() + "\n");
			// e.printStackTrace(System.err);
		}
	} // run

	/**
	 * Execute all commands until the QUIT command is received from the client,
	 * i.e. continuously serve the client. If there is an unknown command, then
	 * stop, do not continue.
	 */
	private void executeCmds() throws IOException {
		// send the client its name (number)
		report("to    ", cmdToString(NAME) + " " + clientNumber);
		toClient.writeInt(NAME);
		toClient.writeInt(clientNumber);
		toClient.flush();

		// start listening to client's requests and respond to them
		boolean done = false;
		while (!done) {
			int cmd = fromClient.readInt();
			switch (cmd) {
			case QUIT:
				doQuit();
				done = true;
				break;
			case ENCRYPT:
				doEncrpytion(true);
				break;
			case DECRYPT:
				doEncrpytion(false);
				break;
			default:
				report("from", "unknown command " + String.valueOf(cmd) + " received");
				done = true;
			} // switch
		} // while
	} // executeCmds

	// reads a value from the client indicating the number
	// of characters that follow and then the characters are read
	private String charsFromClient() throws IOException {
		// indicates how many characters are going to be received
		int numChars = fromClient.readInt();
		String str = "";
		for (int i = 0; i < numChars; i++) {
			str += fromClient.readChar();
		}
		return str;
	}

	// sends a value to the client indicating the number
	// of characters that follow and then the characters are sent
	private void charsToClient(String text) throws IOException {
		// indicate how many characters are going to be sent
		int numChars = text.length();
		toClient.writeInt(numChars);
		toClient.flush();
		for (int i = 0; i < numChars; i++) {
			toClient.writeChar(text.charAt(i));
		}
		toClient.flush();
	}

	/**
	 * Reads the number of characters that are going to be read in the plain
	 * text coming from the client. Does the actual encryption. Sends the
	 * encrypted text to the client with the "DATA" message (command)
	 */
	private void doEncrpytion(boolean encryptionRequested) throws IOException {
		// get the characters into text
		String text = charsFromClient();

		// give feedback as to what was received
		report("from", text);

		// the answer to the decoding or the encoding
		String secretText;

		if (encryptionRequested) {
			secretText = Encrypter.encrypt(text);
			toClient.writeInt(ENCODED);
			toClient.flush();
		} else {
			secretText = Encrypter.decrypt(text);
			toClient.writeInt(PLAIN);
			toClient.flush();
		}

		// send the actual characters in the secretText
		charsToClient(secretText);

		// give feedback as to what was sent
		if (MASKING_OUTPUT_SECRET_TEXT) {
			report("to    ", "......");
		} else {
			report("to    ", secretText);
		}
	} // end doEncrpytion

	/**
	 * Processes a server quit by notifying the client with a DONE.
	 */
	private void doQuit() throws IOException {
		report("from", cmdToString(QUIT) + "  received");
		toClient.writeInt(DONE);
		toClient.flush();
	} // doQuit

} // end class HandleClient