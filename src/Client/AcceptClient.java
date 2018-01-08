package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;




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
			System.out.println("New client is now connected");
		
			message=(String)input.readObject();
		
			
			if(message.equals("download")) {
				System.out.println("Download request");
				//Get the path of the file to download
				String path =(String)input.readObject();
				System.out.println(path+" download");
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
