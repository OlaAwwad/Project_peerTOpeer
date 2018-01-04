package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RunServer implements Runnable{

	private ServerSocket clientServerSocket;
	private Socket clientSocket;
	private InetAddress localaddress;

	
	
	
	

	
	@Override
	public void run() {
		try {
			clientSocket = null;
			localaddress = InetAddress.getLocalHost();
			clientServerSocket = new ServerSocket(47000, 10, localaddress);
			
			while(true) {
				//accept the connection
				clientSocket = clientServerSocket.accept();
				new Thread(new AcceptClient(clientSocket)).start();
			}
			
		} catch (UnknownHostException e) {
			
		} catch (IOException e) {
			
		}
		
	}
	
	
	
}
