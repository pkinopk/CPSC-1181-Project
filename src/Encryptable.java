/**
 * Protocol for the server / client encryption and decryption programs.
 * 
 * @author G. Monagan
 * @version November 24, 2017
 */
public interface Encryptable {
	/**
	 * port for encryption
	 */
	int PORT = 2017;

	/**
	 * Server's IP address ... super funny that it would be here and that it is
	 * not a line argument but your instructor did not get around to covering
	 * section 11.3 of the textbook which is on line arguments
	 */
	// String HOST = "localhost";
	String HOST = "127.0.0.1";

	/**
	 * message sent by the <b>server</b> to the client <br>
	 * NAME takes one integer argument
	 * <p>
	 * NAME <em>n</em>
	 * </p>
	 * where <em>n</em> is the name (number) of the client.
	 */
	int NAME = 100;

	/**
	 * response sent by the <b>server</b> to the client <br>
	 * ENCODED takes one integer argument followed by n characters
	 * <p>
	 * ENCODED
	 * <em>n char<sub>0</sub> char<sub>1</sub> ... char<sub>n-1</sub> </em>
	 * </p>
	 * where <em>n</em> is the number of characters that will follow after the
	 * integer <em>n</em>. <br>
	 * ENCODED is to be interpreted as a response by the <b>server</b> to the
	 * client with the encoded text (based on the plain text that the client had
	 * sent) and the encoded text consists of <em>n</em> (encrypted) characters.
	 */
	int ENCODED = 101;

	/**
	 * response sent by the <b>server</b> to the client <br>
	 * PLAIN takes one integer argument followed by n characters
	 * <p>
	 * PLAIN
	 * <em>n char<sub>0</sub> char<sub>1</sub> ... char<sub>n-1</sub> </em>
	 * </p>
	 * where <em>n</em> is the number of characters that will follow after the
	 * integer <em>n</em>. <br>
	 * PLAIN is to be interpreted as a response by the <b>server</b> to the
	 * client with the plain text (based on the encrypted text that the client
	 * had sent) and the plain text consists of <em>n</em> (decrypted)
	 * characters.
	 */
	int PLAIN = 102;

	/**
	 * response sent by the <b>server</b> to the client <br>
	 * DONE does not have arguments
	 * <p>
	 * DONE
	 * </p>
	 * DONE means I received the request that the client is quitting so I'm
	 * crossing the client off my list of active clients.
	 */
	int DONE = 103;

	/**
	 * request sent by the <b>client</b> to the server <br>
	 * ENCRYPT takes one integer argument followed by n characters
	 * <p>
	 * ENCRYPT
	 * <em>n char<sub>0</sub> char<sub>1</sub> ... char<sub>n-1</sub> </em>
	 * </p>
	 * where <em>n</em> is the number of characters that will follow after the
	 * integer <em>n</em>. <br>
	 * ENCRYPT is to be interpreted as a request by the <b>client</b> to the
	 * server to encrypt the <em>n</em> characters that will follow the integer
	 * <em>n</em>.
	 */
	int ENCRYPT = 1000;

	/**
	 * request sent by the <b>client</b> to the server <br>
	 * DECRYPT takes one integer argument followed by n characters
	 * <p>
	 * DECRYPT
	 * <em>n char<sub>0</sub> char<sub>1</sub> ... char<sub>n-1</sub> </em>
	 * </p>
	 * where <em>n</em> is the number of characters that will follow after the
	 * integer <em>n</em>. <br>
	 * DECRYPT is to be interpreted as a request by the <b>client</b> to the
	 * server to decrypt (decipher) the <em>n</em> characters that will follow
	 * the integer <em>n</em>.
	 */
	int DECRYPT = 1001;

	/**
	 * request sent by the <b>client</b> to the server <br>
	 * QUIT does not have arguments
	 * <p>
	 * QUIT
	 * </p>
	 * QUIT tells the server "bye server, hasta la vista" I'm not going to send
	 * you any more requests.
	 */
	int QUIT = 1002;

	/**
	 * Converts an integer command cmd to its string representation.
	 * <p>
	 * Supported commands are <br>
	 * NAME server &rarr; client, one int <br>
	 * ENCODED server &rarr; client, one int followed by the charcters <br>
	 * PLAIN server &rarr; client, one int followed by the characters<br>
	 * DONE server &rarr; client, no arguments ENCRYPT client &rarr; server, one
	 * int followed by the charcters <br>
	 * DECRYPT client &rarr; server, one int followed by the charcters <br>
	 * QUIT client &rarr; server, no arguments
	 * </p>
	 * A command that is not suppored returns the string
	 * "UNRECOGNIZABLE COMMAND".
	 * 
	 * @param cmd
	 *            an integer corresponding to a command
	 * @return String the textual representation of the command cmd
	 */
	default String cmdToString(int cmd) {
		String cmdString;
		switch (cmd) {
		case NAME:
			cmdString = "NAME";
			break;
		case ENCODED:
			cmdString = "ENCODED";
			break;
		case PLAIN:
			cmdString = "PLAIN";
			break;
		case DONE:
			cmdString = "DONE";
			break;
		case ENCRYPT:
			cmdString = "ENCRYPT";
			break;
		case DECRYPT:
			cmdString = "DECRYPT";
			break;
		case QUIT:
			cmdString = "QUIT";
			break;
		default:
			cmdString = "UNRECOGNIZABLE COMMAND";
		} // switch
		return cmdString;
	} // cmdToString
}
