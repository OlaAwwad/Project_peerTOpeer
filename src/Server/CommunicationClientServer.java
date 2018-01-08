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
	private Socket socket;
	private String clientIp;
	private File[] listOfFiles;
	private ExistedClient NewClient;

	public CommunicationClientServer (Socket socket) 
	{
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			ous = new ObjectOutputStream(socket.getOutputStream());
			
			while(true) {
				message_distant = (String) ois.readObject();
				System.out.println(message_distant);
				
				// Login
				if(message_distant.equals("login")) {
					
					userName = (String) ois.readObject();
					System.out.println("username "+ userName);

					passWord = (String) ois.readObject();
					System.out.println("pwd "+ passWord);

					clientIp = (String) ois.readObject();
					System.out.println("IP :"+clientIp);
					
					ExistedClient tempClient = new ExistedClient(userName, passWord, clientIp);

					
					// If the user exists, we allow the connection
					if(Main_ServerCo.listFilesOfClient.checkLogin(tempClient)) {
						ous.writeObject(true);
						ous.flush();
						
						NewClient = tempClient;
				
					} else
						ous.writeObject(false);
						ous.flush();
				}		

				
				if(message_distant.equals("post"))	
				{
					/*Lecture des infos du client*/
					listenInfo();
				}
				
				if(message_distant.equals("get"))
				{
					
					
					/*Envoi du nombres de clients disponibles*/
					
					ous.writeObject(Server.LoginClients.getSize());
					ous.flush();
	
					
					for(ExistedClient client : Server.LoginClients.getListLogin())
					{
						System.out.println(client.getUserName()+" send to client");
						ous.writeObject(client.getUserName());
						ous.flush();
						
						System.out.println("IP is : "+ client.getip());
						ous.writeObject(client.getip());
						ous.flush();
						
						ous.writeObject(client.getListFiles());
						System.out.println(client.getListFiles().length+" files to client");
						ous.flush();
					}		
				}
				if(message_distant.equals("logout"))
				{
					/*Receive logout message*/
				
					ois.close();
					ous.close();
					socket.close();
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

	public void listenInfo() throws IOException, ClassNotFoundException
	{
		System.out.println("Add file from "+ userName);
		/*Réception de la liste de fichiers du client */
		listOfFiles = (File[]) ois.readObject();
		
		
		for(File file : listOfFiles)
		{
			
			System.out.println("File add : "+ file.getName());
// 		   	Server.newlog.setLevel(Level.INFO);
//			Server.myLogger.info("received : "+file.getName());
		}
		
		
		
		Server.LoginClients.getClient(Server.LoginClients.getIndex(NewClient)).setListFiles(listOfFiles);
		
	
		
	}


}