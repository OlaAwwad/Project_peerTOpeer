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

import Client.DistantClient;

public class CommunicationClientServer implements Runnable {
	private ObjectInputStream ois;
	private ObjectOutputStream ous;

	private String message_distant;
	private String userName;
	private String passWord;
	private Socket socket;
	private String clientIp;
	private File[] listOfFiles;
	private ExistedClient NewClient;
	public static CustomFormatter serverLogs = new CustomFormatter();;

	public CommunicationClientServer(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			ois = new ObjectInputStream(socket.getInputStream());
			ous = new ObjectOutputStream(socket.getOutputStream());

			while (true) {
				message_distant = (String) ois.readObject();
				CommunicationClientServer.serverLogs.newLog(Level.INFO,
						"Command " + message_distant + " by " + socket.getInetAddress().getHostAddress());
				// Login
				if (message_distant.equals("login")) {

					userName = (String) ois.readObject();

					passWord = (String) ois.readObject();

					clientIp = (String) ois.readObject();

					CommunicationClientServer.serverLogs.newLog(Level.INFO, "Attempt to login : " + userName);

					ExistedClient tempClient = new ExistedClient(userName, passWord, clientIp);

					// If the user exists, we allow the connection
					if (Main_ServerCo.listFilesOfClient.checkLogin(tempClient)) {
						CommunicationClientServer.serverLogs.newLog(Level.INFO, "User " + userName + " connected");
						ous.writeObject(true);
						ous.flush();

						NewClient = tempClient;

					} else
						CommunicationClientServer.serverLogs.newLog(Level.WARNING, "Wrong username or password");
					ous.writeObject(false);
					ous.flush();
				}

				// Listen and setting the list of files for a client
				if (message_distant.equals("post")) {
					/* Lecture des infos du client */
					listenInfo();
				}

				// Sending to client the other client avalaible and files
				if (message_distant.equals("get")) {

			

					int nbClients =Server.LoginClients.getSize();
					
					ous.writeObject(nbClients);
					ous.flush();

					for (ExistedClient client : Server.LoginClients.getListLogin()) {
						CommunicationClientServer.serverLogs.newLog(Level.INFO,
								"Send data from : " + client.getUserName());
						ous.writeObject(client.getUserName());
						ous.flush();

						ous.writeObject(client.getip());
						ous.flush();

						ous.writeObject(client.getListFiles());
						ous.flush();
					}
				}

				// Logout of a client
				if (message_distant.equals("logout")) {
					/* Receive logout message */
					CommunicationClientServer.serverLogs.newLog(Level.WARNING,
							"Closing socket : " + socket.getInetAddress().getHostAddress());
					ois.close();
					ous.close();
					socket.close();
				}

			}
		} catch (IOException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		} catch (ClassNotFoundException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		}
	}

	// Listen and setting the list of files for a client
	public void listenInfo() throws IOException, ClassNotFoundException {

		listOfFiles = (File[]) ois.readObject();

		for (File file : listOfFiles) {
			CommunicationClientServer.serverLogs.newLog(Level.INFO, "New file :" + file.getName());
		}

		Server.LoginClients.getClient(Server.LoginClients.getIndex(NewClient)).setListFiles(listOfFiles);

	}

}