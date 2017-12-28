package Server;

/**
*  	Project name : PeerToPeer
*	Class : ServerCo
*
* 	Date of creation : 28.12.2017
* 	
*	Description :
*	MAIN class with the frame of the server & the creation
*	of the connection.
* 
* @author Nicolas Piguet
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Main_ServerCo {
	
	public static LoginClients listFilesOfClient = new LoginClients();

	public static void main(String[] args) {
		
		initialize();
		
		ServerSocket serverSocket;
		Socket socket = null;
		InetAddress localAddress = null;
		String interfaceName = "eth0";

		try {
			NetworkInterface ni = NetworkInterface.getByName(interfaceName);
			Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				InetAddress ia = inetAddresses.nextElement();
				if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress()) 
				{
					System.out.println(ni.getName() + "->IP: " + ia.getHostAddress());
					localAddress = ia;
				}
			}
		} 
		catch (SocketException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		}

		try {
			serverSocket = new ServerSocket(65535, 10, localAddress);
			new CustomFormatter().newLog(Level.INFO, "Server launched");
			
			// Wait for a client connection and start a thread with the class "CommunicationClientServer"
			while (true) {
				socket = serverSocket.accept();
				new CustomFormatter().newLog(Level.INFO, "Client with IP " + socket.getInetAddress().getHostAddress() + " connected");
				Thread t = new Thread(new CommunicationClientServer(socket));
				t.start();
			}
		} catch (SocketException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		} catch (IOException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		}
	}
	
	/**
	 * Method that initialize the "interface"
	 */
	public static void initialize() {
		JFrame frameServer = new JFrame();
		frameServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameServer.setPreferredSize(new Dimension(300, 80));
		frameServer.setUndecorated(true);
		frameServer.setLocation(50, 100);
		frameServer.setAlwaysOnTop(true);
		
		JButton close = new JButton("Shutdown the server");
		close.addActionListener(new Close());
		close.setPreferredSize(new Dimension(300, 80));
		close.setBackground(new Color(255, 100, 100));
		close.setFocusPainted(false);
		close.setFont(new Font("Arial", 0, 24));
		
		frameServer.add(close);
		frameServer.setVisible(true);
		frameServer.pack();
	}
	
	/**
	 * Close the server when you press the button
	 */
	private static class Close implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new CustomFormatter().newLog(Level.INFO, "Server shutdown !!!");
			System.exit(0);
		}	
	}
}