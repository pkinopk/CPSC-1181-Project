import javax.swing.JFrame;

public class PasswordViewer {
	public static void main(String[] args) {

		JFrame controller = new ControllerFrame();
		controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controller.setTitle("Controller");
		controller.setLocationRelativeTo(controller);
		controller.setVisible(true);
	}
}
