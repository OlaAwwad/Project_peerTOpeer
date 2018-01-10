package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

import javax.swing.JOptionPane;


/**
 * Project name : PeerToPeer Class : ThreadDownload
 *
 * Date of creation : 28.12.2017
 * 
 * Description : Thread to download the file from the client
 * 
 * @author Vlado Mitrovic
 */

public class ThreadDownload implements Runnable {

	@Override
	public void run() {
		try {
			Socket MySocket = null;
			InetAddress serverAddress = null;

			// Get ip from server
			serverAddress = InetAddress.getByName(((DistantClient) ClientFrame.clientsList.getSelectedItem()).getip());

			// Connecting to the server
			MySocket = new Socket(serverAddress, 47000);

			// Send the file name to the client
			ObjectOutputStream output = new ObjectOutputStream(MySocket.getOutputStream());

			// Send the command

			ClientFrame.ClientLogs.newLog(Level.INFO,
					"Send command download for " + ClientFrame.listServerFiles.getSelectedValue().getAbsolutePath());
			output.writeObject("download");
			output.flush();

			// Send the path of the file
			output.writeObject(ClientFrame.listServerFiles.getSelectedValue().getAbsolutePath());
			output.flush();

			// Create objectOutputstream to get info from the server
			ObjectInputStream input = new ObjectInputStream(MySocket.getInputStream());
			int fileSize = (int) input.readLong();


			// Get the file
			File file = new File(ClientFrame.clientFolder.getAbsolutePath() + "/"
					+ ClientFrame.listServerFiles.getSelectedValue().getName());
			FileOutputStream fileOutput = new FileOutputStream(file);

			byte[] data = new byte[1024];
			int nbBytes;

			// downloading the file
			ClientFrame.ClientLogs.newLog(Level.INFO,
					"Starting download of" + ClientFrame.listServerFiles.getSelectedValue().getName());

			while ((nbBytes = input.read(data)) > -1) {
				fileOutput.write(data, 0, nbBytes);
				fileOutput.flush();
			}
			
			
			
			ClientFrame.modelClient.addElement(new File(ClientFrame.clientFolder.getAbsolutePath() + "/" +ClientFrame.listServerFiles.getSelectedValue().getName()));

			ClientFrame.ClientLogs.newLog(Level.INFO, "Download finish");
			JOptionPane.showMessageDialog(null, "Download Succesful of : "+ ClientFrame.listServerFiles.getSelectedValue().getName());
			
			MySocket.close();
			fileOutput.close();
		} catch (UnknownHostException e) {
			ClientFrame.ClientLogs.newLog(Level.SEVERE, "UnknownHostException : " + e.toString());

		} catch (IOException e) {

			ClientFrame.ClientLogs.newLog(Level.SEVERE, "IOException : " + e.toString());
		}
		
		
		

	}

}
