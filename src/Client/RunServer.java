package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RunServer implements Runnable{

	ServerSocket clientServerSocket;
	Socket clientSocket;
	InetAddress localaddress;

	
	
	
	

	
	@Override
	public void run() {
        // Déclaration des variables
 		InetAddress localAddress = null;
  		ServerSocket clientServerSocket;
  		
  		try {
  			// Affectation des variables
  			localAddress = InetAddress.getLocalHost();
  	 		clientServerSocket = new ServerSocket(47000, 10, localAddress);
  			
 			while(true)
 			{
 				Socket clientSocket = clientServerSocket.accept();
 	 		   	Thread t = new Thread(new AcceptClient(clientSocket));
 	 		   	
 	 		   	// Démarrage du Thread
 	 		   	t.start();
 			}
  		} 
  		catch 
  		(IOException ex) {
  			
//			ClientFrame.myLogger.setLevel(Level.WARNING);
//			ClientFrame.myLogger.severe("The port was busy");
 		} 
		
	}
	
	
	
}
