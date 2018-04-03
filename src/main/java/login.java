import javax.swing.*;

public class login {
	private static JFrame frame;

	private JPanel loginPanel;
	private JTextField usernameTextField;
	private JButton login;
	private JTextField passwordTextField;

	public login() {
		login.addActionListener(e -> {
			if(Mongo.CheckLogin(usernameTextField.getText(), passwordTextField.getText())) {
				frame.setTitle("RD Management");
				frame.setContentPane(new tabs().tabbedPanel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			} else
				JOptionPane.showMessageDialog(null, "Invalid username/password");
		});

	}

	public static void main(String[] args) {
		frame = new JFrame("RD Login");
//		frame.setSize(1500, 300);
		frame.setContentPane(new login().loginPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		new Mongo();
	}
}