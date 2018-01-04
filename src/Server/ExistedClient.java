package Server;

/**
*  	Project name : PeerToPeer
*	Class : ExistedClient
*
* 	Date of creation : 28.12.2017
* 	
*	Description :
*	Object used to store a client with his username,
*	his password and his port. The port is set when
*	we log in.
* 
* @author Nicolas Piguet
*/

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class ExistedClient implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String passWord;
	private String ip;
	private int port;
	private File [] listFiles;

	public ExistedClient (String userName, String passWord, String ip) 
	{
		this.userName = userName;
		this.passWord = passWord;
	
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassword(String passWord) {
		this.passWord = passWord;
	}

	public File[] getListFiles() {
		return listFiles;
	}

	public void setListFiles(File[] listFiles) {
		this.listFiles = listFiles;
	}

	public String getip() {
		return ip;
	}

	public void setip(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString() {
		return "RegisteredClient [userName=" + userName + ", passWord=" + passWord + ", ip=" + ip + ", listFiles="
				+ Arrays.toString(listFiles) + "]";
	}

	/**
	 * Method that tests the username
	 * AND the password 
	 */
	@Override
	public boolean equals(Object o) {
		System.out.println("Check login");
		ExistedClient c = (ExistedClient) o;
		if (this.userName.equals(c.userName) && this.passWord.equals(c.passWord)) { 
			System.out.println("OK");
			return true;
		}
		else
			return false;
	}
}