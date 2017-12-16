package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Downloading implements Runnable{

	private Socket socket;
	private OutputStream output;
	private InputStream intput;
	private InetAddress address;
	private FileOutputStream fileoutput;
	private File file;
	
	
	
	
	@Override
	public void run() {
		
		
	}

}
