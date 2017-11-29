import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControllerFrame extends JFrame {
	private JTextField input;
	private PasswordFrame game;
	private int action;
	private int password;

	private static final int FRAME_WIDTH = 450;
	private static final int FRAME_HEIGHT = 150;

	public ControllerFrame() {
		password = 123;

		game = new PasswordFrame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setTitle("Guess the password");
		game.setLocationRelativeTo(null);
		game.setVisible(true);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2, 3));

		input = new JTextField("");
		input.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		add(input, BorderLayout.NORTH);

		JButton setPlayer = new JButton("SET PLAYER");
		setPlayer.addActionListener(new buttonSetPlayerListener());

		JButton setTurnPlayer = new JButton("SET TURN PLAYER");
		setTurnPlayer.addActionListener(new buttonSetTurnListener());

		JButton zeroCorrect = new JButton("ZERO CORRECT");
		zeroCorrect.addActionListener(new buttonZeroListener());

		JButton oneCorrect = new JButton("ONE CORRECT");
		oneCorrect.addActionListener(new buttonOneListener());

		JButton twoCorrect = new JButton("TWO CORRECT");
		twoCorrect.addActionListener(new buttonTwoListener());

		JButton threeCorrect = new JButton("THREE CORRECT");
		threeCorrect.addActionListener(new buttonThreeListener());

		buttons.add(setPlayer);
		buttons.add(setTurnPlayer);
		buttons.add(zeroCorrect);
		buttons.add(oneCorrect);
		buttons.add(twoCorrect);
		buttons.add(threeCorrect);
		add(buttons, BorderLayout.CENTER);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);

	}

	class buttonSetPlayerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int player;
			try {
				player = Integer.parseInt(input.getText());
				game.setiAmPlayer(player);
				game.setMsg("You are player: " + player, Color.BLACK);
			} catch (Exception e) {
			}

		}
	}

	class buttonSetTurnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				int player = Integer.parseInt(input.getText());
				game.setTurn(player);
				if (game.getiAmPlayer() == player) {
					game.setMsg("Your turn to play", Color.BLUE);
				} else
					game.setMsg("Your opponent's turn to play", Color.BLUE);
			} catch (Exception e) {
			}
		}
	}

	class buttonZeroListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			game.setMsg("You didn't get any of the digits correct (Your guess: " + game.getTryPass() + ")", Color.RED);
			game.clearDisplay();
			if (game.getiAmPlayer() == 1) {
				game.setTurn(2);
			} else
				game.setTurn(1);
		}
	}

	class buttonOneListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			game.setMsg("You got one digit correct (Your guess: " + game.getTryPass() + ")", Color.ORANGE);
			game.clearDisplay();
			if (game.getiAmPlayer() == 1) {
				game.setTurn(2);
			} else
				game.setTurn(1);
		}
	}

	class buttonTwoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			game.setMsg("You got two digits correct. You are almost there! (Your guess: " + game.getTryPass() + ")",
					Color.YELLOW);
			game.clearDisplay();
			if (game.getiAmPlayer() == 1) {
				game.setTurn(2);
			} else
				game.setTurn(1);
		}
	}

	class buttonThreeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			game.setMsg("You guessed the combination. Congratulations you won! (Your guess: " + game.getTryPass() + ")",
					Color.GREEN);
			game.clearDisplay();
			if (game.getiAmPlayer() == 1) {
				game.setTurn(2);
			} else
				game.setTurn(1);
		}
	}

}
