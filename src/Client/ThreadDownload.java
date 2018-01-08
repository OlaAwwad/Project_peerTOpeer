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

import javax.swing.JProgressBar;

import Server.ExistedClient;

public class ThreadDownload implements Runnable{

	@Override
	public void run() {
		try {
			Socket MySocket = null;
			InetAddress serverAddress = null;
			
			//Get ip from server
			serverAddress = InetAddress.getByName(((ExistedClient)ClientFrame.clientsList.getSelectedItem()).getip());
			
			//Connecting to the server
			MySocket = new Socket(serverAddress, 47000);
			
			//Send the file name to the client
			ObjectOutputStream output = new ObjectOutputStream(MySocket.getOutputStream());
			
			//Send the command 
			System.out.println("Sending download to "+ serverAddress);
			output.writeObject("download");
			output.flush();
			
			//Send the path of the file
			System.out.println("Sending the path");
			output.writeObject(ClientFrame.listServerFiles.getSelectedValue().getAbsolutePath());
			output.flush();
			
			
			
			//Create objectOutputstream to get info from the server
			ObjectInputStream input = new ObjectInputStream(MySocket.getInputStream());
			int fileSize = (int)input.readLong();
			int progress = 0;
			JProgressBar bar = new JProgressBar(0, fileSize);
			
			//Get the file
			File file = new File(ClientFrame.clientFolder.getAbsolutePath()+"/"+ClientFrame.listServerFiles.getSelectedValue().getName());
			FileOutputStream fileOutput = new FileOutputStream(file);
			byte[] data = new byte[1024];
			int nbBytes;
			
	
			
			//downloading the file
			while((nbBytes = input.read(data)) > -1) {
				progress+=nbBytes;
				fileOutput.write(data, 0, nbBytes);
				fileOutput.flush();
				bar.setValue(progress);
			}			
			
			MySocket.close();
			fileOutput.close();
		} catch (UnknownHostException e) {
			
		} catch (IOException e) {
			
		}
		
	}

}
