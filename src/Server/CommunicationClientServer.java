package Server;

/**
*  	Project name : PeerToPeer
*	Class : CommunicationClientServer
*
* 	Date of creation : 28.12.2017
* 	
*	Description :
*	Class for communicating between client & server
* 
* @author Nicolas Piguet
*/

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;

public class CommunicationClientServer implements Runnable 
{
	private ObjectInputStream ois;
	private ObjectOutputStream ous;
	
	private String message_distant;
	private String userName;
	private String passWord;
	private String ip;
	private Socket socket;
	private int port;
	private ExistedClient tempClient;

	public CommunicationClientServer (Socket socket) 
	{
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println("Thread started");
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			ous = new ObjectOutputStream(socket.getOutputStream());
			
			while(true) {
				message_distant = (String) ois.readObject();
				System.out.println("Reciev : "+ message_distant);
				// Login
				if(message_distant.equals("login")) {
					
					userName = (String) ois.readObject();
					System.out.println("Reciev : "+ userName);
					passWord = (String) ois.readObject();
					System.out.println("Reciev : "+ passWord);
					tempClient = new ExistedClient(userName, passWord);
					System.out.println("Test login");
					ous.writeObject(true);
					// If the user exists, we allow the connection
					if(Main_ServerCo.listFilesOfClient.checkLogin(tempClient)) {
						System.out.println("LOGIN OK !");
						ous.writeObject(true);
						Main_ServerCo.listFilesOfClient.getClient(Main_ServerCo.listFilesOfClient.getIndex(tempClient)).setPort(port);
					} else
						ous.writeObject(false);
				}		
				// Send
				else if(message_distant.equals("send")) {
					File[] files = (File[])ois.readObject();
					System.out.println(Arrays.toString(files));					
					Main_ServerCo.listFilesOfClient.getClient(Main_ServerCo.listFilesOfClient.getIndex(tempClient)).setListFiles(files);
					for (ExistedClient client : Main_ServerCo.listFilesOfClient.getListLogin()) {
						System.out.println(client.toString());
					}
				}
				// Display files of a client
				else if(message_distant.equals("display")) {
					ous.writeObject(Main_ServerCo.listFilesOfClient.getSize());
					ous.flush();
					for (int i=0 ; i<Main_ServerCo.listFilesOfClient.getSize(); i++) {
						
						// Send & flush the informations about each file
						System.out.println("TEST :" + Arrays.toString(Main_ServerCo.listFilesOfClient.getClient(i).getListFiles()));
						ous.writeObject("IP: "+Main_ServerCo.listFilesOfClient.getClient(i).getip()+
								"\n PORT: "+Main_ServerCo.listFilesOfClient.getClient(i).getPort()+
								"\n FILE: "+Main_ServerCo.listFilesOfClient.getClient(i).getListFiles()+"\n");
						ous.flush();
					}
				}
			}
		} 
		catch (IOException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		} 
		catch (ClassNotFoundException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		}
	}
}