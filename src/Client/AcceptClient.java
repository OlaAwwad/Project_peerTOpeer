package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Project name : PeerToPeer Class : AcceptClient
 *
 * Date of creation : 28.12.2017
 * 
 * Description : Thread used to upload the file to the other client
 * 
 * @author Vlado Mitrovic
 */


public class AcceptClient implements Runnable {

	private Socket clientSocket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String message;
	
	public AcceptClient(Socket socket){
		this.clientSocket=socket;
	}
	
	
	@Override
	public void run() {
	
		try {
			input=new ObjectInputStream(clientSocket.getInputStream());
			output= new ObjectOutputStream(clientSocket.getOutputStream());
			message=(String)input.readObject();
		
			
			if(message.equals("download")) {
				//Get the path of the file to download
				String path =(String)input.readObject();
				File dwnlFile = new File(path);
				FileInputStream fileInput = new FileInputStream(dwnlFile);
				
				byte[] data = new byte[1024];
				int nbBytes;
				
				while ((nbBytes = fileInput.read(data)) > -1) {
					output.write(data, 0, nbBytes);
					output.flush();
					
				}
				
				output.close();
				input.close();
				fileInput.close();
				clientSocket.close();
				
				
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
