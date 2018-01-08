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
	private String userName;
	private String passWord;
	private String ip;

	private File [] listFiles;
	

	

	public ExistedClient (String userName, String passWord, String ip) 
	{
		this.userName = userName;
		this.passWord = passWord;
		this.ip=ip;
		
	}
	
	
	
	public ExistedClient (String userName,  String ip, File[] files) 
	{
		this.userName = userName;
		this.ip=ip;
		this.listFiles=files;
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
		System.out.println("Files setted for "+ userName);
		this.listFiles = listFiles;
	}

	public String getip() {
		return ip;
	}

	public void setip(String ip) {
		this.ip = ip;
	}
	

	
	@Override
	public String toString() {
		return userName+" " +ip;
	}

	/**
	 * Method that tests the username
	 * AND the password 
	 */
//	@Override
//	public boolean equals(Object o) {
//		ExistedClient c = (ExistedClient) o;
//		if (this.userName.equals(c.userName) && this.passWord.equals(c.passWord)) { 
//			return true;
//		}
//		else
//			return false;
//	}
}