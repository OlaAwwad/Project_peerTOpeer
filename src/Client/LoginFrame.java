package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Server.CustomFormatter;

public class LoginFrame extends JFrame implements KeyListener{
	
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
	
	private ImageIcon snowman = new ImageIcon("Icon/snowman.png");
	private JLabel piclabel = new JLabel(snowman);

	
	protected static Socket MySocket;
	protected static ObjectInputStream input;
	protected static ObjectOutputStream output;
	private String serverName = "192.168.108.10";
	private InetAddress serverAddress = null;
	
	public LoginFrame () {
		
		
		//Basic configuration
		super("Snowman Client");
		
		pnlForm.setBackground(new Color(255,204,153));
		pnlButtons.setBackground(new Color(255,204,153));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 200));
		setSize(620, 500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(0,0));
		setBackground(new Color(255, 128, 0));
		setResizable(false);
		
		btnConnect.setmouseColor(229, 255, 204);
		btnCancel.setmouseColor(255, 163, 153);
		
		
		pnlForm.setLayout(new GridLayout(5, 1));
		pnlButtons.setLayout(new FlowLayout());
		
		txtUser.setColumns(15);
		txtPassword.setColumns(15);

		lblUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		txtUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		txtPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		
		txtUser.setSize(new Dimension(500,80));
		txtPassword.setSize(new Dimension(500,80));

		txtUser.setSize(new Dimension(300, 40));
		
		pnlForm.add(piclabel);
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
		
			String username = txtUser.getText();
			String password = new String(txtPassword.getPassword());
		
			if(username.equals("") || password.isEmpty()) {
				txtUser.setText("");
				JOptionPane.showMessageDialog(null, "Username and password can't be empty !");
				txtPassword.selectAll();	
			}
			else {
			try {
				
				//Get the server address
				serverAddress = InetAddress.getByName(serverName);

				//Connection to the server
				MySocket = new Socket(serverAddress, 65535);
				output = new ObjectOutputStream(MySocket.getOutputStream());
				input = new ObjectInputStream(MySocket.getInputStream());



				
				//Send the login details
				output.writeObject("login");
				System.out.println("Send login");
				output.flush();
				output.writeObject(username);
				System.out.println("username");
				output.flush();
				output.writeObject(password);
				System.out.println("pwd");
				output.flush();
				output.writeObject(MySocket.getLocalAddress().getHostAddress());
				output.flush();
				
				System.out.println("CHECKING LOGIN ....");
				if((boolean)input.readObject()) {
					System.out.println("LOGIN OK");
					dispose();
					ClientFrame clientFrame = new ClientFrame(username, MySocket.getLocalAddress().getHostAddress(), input, output, MySocket);
					clientFrame.setVisible(true);
					new CustomFormatter().newLog(Level.INFO, "Correct login");
				} else {
					JOptionPane.showMessageDialog(null, "Incorrect login");
					txtPassword.selectAll();
					new CustomFormatter().newLog(Level.WARNING, "Incorrect login");
				}

			} catch (UnknownHostException e1) {
				new CustomFormatter().newLog(Level.SEVERE, e1.toString());
			} catch (IOException e1) {
				new CustomFormatter().newLog(Level.SEVERE, e1.toString());
			} catch (ClassNotFoundException e1) {
				new CustomFormatter().newLog(Level.SEVERE, e1.toString());
			}
			
			}
		
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
            btnConnect.doClick();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
