package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * Project name : PeerToPeer Class : RunServer
 *
 * Date of creation : 28.12.2017
 * 
 * Description : Thread to upload the file to the client
 * 
 * @author Vlado Mitrovic
 */

public class RunServer implements Runnable {

	ServerSocket clientServerSocket;
	Socket clientSocket;
	InetAddress localaddress;

	@Override
	public void run() {

		InetAddress localAddress = null;
		ServerSocket clientServerSocket;

		try {

			localAddress = InetAddress.getLocalHost();
			clientServerSocket = new ServerSocket(47000, 10, localAddress);

			while (true) {
				Socket clientSocket = clientServerSocket.accept();

				ClientFrame.ClientLogs.newLog(Level.INFO,
						"Client with IP " + clientSocket.getInetAddress().getHostAddress() + " connected");
				Thread t = new Thread(new AcceptClient(clientSocket));

				t.start();
			}
		} catch (IOException e) {

			ClientFrame.ClientLogs.newLog(Level.SEVERE, "IOException : " + e.toString());

		}

	}

}
