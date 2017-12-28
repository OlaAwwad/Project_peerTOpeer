package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame{
	
	private JLabel lblUser = new JLabel("Username");
	private JLabel lblPassword = new JLabel("Password");
	private JTextField txtUser = new JTextField();
	private JPasswordField txtPassword = new JPasswordField();
	private Button btnCancel = new Button("Cancel");
	private Button btnConnect = new Button("Connect");
	private JPanel pnlButtons = new JPanel();
	private JPanel pnlForm = new JPanel();
	private Font font25 = new Font ("Century Gothic",Font.BOLD, 25);
	private Font font15 = new Font ("Century Gothic",Font.PLAIN, 20);
	
	public LoginFrame () {
		
		
		//Basic configuration
		super("P2P Nicolas Vlado");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 200));
		setSize(620, 414);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(0,0));
		setBackground(new Color(255, 128, 0));
		setResizable(false);
		
		btnConnect.setmouseColor(229, 255, 204);
		btnCancel.setmouseColor(255, 163, 153);
		
		
		pnlForm.setLayout(new GridLayout(4, 1));
		pnlButtons.setLayout(new FlowLayout());
		
		txtUser.setColumns(15);
		txtPassword.setColumns(15);

		lblUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		txtUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		txtPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		

		txtUser.setSize(new Dimension(300, 40));
		
		
		pnlForm.add(lblUser);
		pnlForm.add(txtUser);

		pnlForm.add(lblPassword);
		pnlForm.add(txtPassword);
		
		lblUser.setFont(font25);
		lblPassword.setFont(font25);
		txtUser.setFont(font15);
		txtPassword.setFont(font15);
		

		pnlButtons.add(btnConnect);
		pnlButtons.add(btnCancel);

		add(pnlForm);
		add(pnlButtons, BorderLayout.SOUTH);
		
		
		btnConnect.addActionListener(new Connect_Click());
		btnCancel.addActionListener(new Cancel_Click());
		
		
		
	}
	
	
	class Cancel_Click implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	class Connect_Click implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
}
