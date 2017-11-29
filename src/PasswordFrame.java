import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PasswordFrame extends JFrame implements Runnable, Encryptable {
	private JLabel display;
	private String tryPass;
	private JLabel msg;
	private boolean myTurn;
	private int iAmPlayer;

	private Socket socket;
	private DataOutputStream toServer;
	private DataInputStream fromServer;

	private static final int FRAME_WIDTH = 450;
	private static final int FRAME_HEIGHT = 300;
	private static final int NUMBER_OF_DIGITS = 3;

	public static void main(String[] args) {

		PasswordFrame game = new PasswordFrame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setTitle("Guess the password");
		game.setLocationRelativeTo(null);
		game.setVisible(true);

		PasswordFrame player = new PasswordFrame();
		Thread thread = new Thread(player);

		thread.start();

	}

	@Override
	public void run() {
		openConnection(HOST);

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
			e.printStackTrace();
		}

	}

	public PasswordFrame() {
		tryPass = "";
		myTurn = false;
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2, 1));
		display = new JLabel(" ");
		display.setHorizontalAlignment((int) CENTER_ALIGNMENT);

		msg = new JLabel();
		msg.setHorizontalAlignment((int) CENTER_ALIGNMENT);

		topPanel.add(display);
		topPanel.add(msg);
		add(topPanel, BorderLayout.NORTH);
		createButtonPanel();
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}

	private void createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 3));

		buttonPanel.add(makeDigitButton("7"));
		buttonPanel.add(makeDigitButton("8"));
		buttonPanel.add(makeDigitButton("9"));
		buttonPanel.add(makeDigitButton("4"));
		buttonPanel.add(makeDigitButton("5"));
		buttonPanel.add(makeDigitButton("6"));
		buttonPanel.add(makeDigitButton("1"));
		buttonPanel.add(makeDigitButton("2"));
		buttonPanel.add(makeDigitButton("3"));
		buttonPanel.add(makeQuitButton("Quit"));
		buttonPanel.add(makeDigitButton("0"));
		buttonPanel.add(makeSubmitButton("Submit"));

		add(buttonPanel, BorderLayout.CENTER);
	}

	class DigitButtonListener implements ActionListener {
		private String digit;

		public DigitButtonListener(String aDigit) {
			digit = aDigit;
		}

		public void actionPerformed(ActionEvent event) {
			if (tryPass == "" || tryPass.length() < NUMBER_OF_DIGITS) // TODO
																		// here
																		// check
																		// for
																		// max
																		// digits
				tryPass += digit;

			display.setText(tryPass);
		}
	}

	class SubmitButtonListener implements ActionListener {

		public SubmitButtonListener() {

		}

		public void actionPerformed(ActionEvent event) {
			if (myTurn) {
				if (tryPass.length() < 3) {
					setMsg("Passwords must be " + NUMBER_OF_DIGITS + " digits", Color.GRAY);
				} else {
					setMsg("Password sent to server", Color.BLACK);
				}
			} else {
				setMsg("Not your turn", Color.GRAY);
				clearDisplay();
			}
		}
	}

	class QuitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// DO SOMETHING
			System.exit(0);
		}
	}

	public JButton makeDigitButton(String digit) {

		JButton button = new JButton(digit);

		ActionListener listener = new DigitButtonListener(digit);
		button.addActionListener(listener);
		return button;
	}

	public JButton makeSubmitButton(String submit) {
		JButton button = new JButton("Submit");
		ActionListener listener = new SubmitButtonListener();
		button.addActionListener(listener);
		return button;
	}

	public JButton makeQuitButton(String quit) {
		JButton button = new JButton(quit);

		ActionListener listener = new QuitButtonListener();
		button.addActionListener(listener);
		return button;
	}

	public void setMsg(String text, Color color) {
		msg.setForeground(color);
		msg.setText(text);
	}

	public void setiAmPlayer(int player) {
		iAmPlayer = player;
	}

	public void setTurn(int player) {
		if (iAmPlayer == player) {
			myTurn = true;
		} else
			myTurn = false;
	}

	public int getiAmPlayer() {
		return iAmPlayer;
	}

	public String getTryPass() {
		return tryPass;
	}

	public void clearDisplay() {
		this.tryPass = "";
		this.display.setText("");
	}

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
}
