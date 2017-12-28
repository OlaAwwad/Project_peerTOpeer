package Client;

import java.io.File;

public class Client {

	private String name;
	private String ip;
	private File[] listOfFiles;
	
	
	public Client(String name, String ip, File[] listOfFiles)
	{
		this.name = name;
		this.ip = ip;
		this.listOfFiles = listOfFiles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public File[] getListOfFiles() {
		return listOfFiles;
	}

	public void setListOfFiles(File[] listOfFiles) {
		this.listOfFiles = listOfFiles;
	}

	
	
}
