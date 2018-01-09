package Client;

/**
 * Project name : PeerToPeer Class : DistantClient
 *
 * Date of creation : 28.12.2017
 * 
 * Description : Class that contains data of one other client
 * 
 * @author Vlado Mitrovic
 */

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class DistantClient implements Serializable {
	private String userName;
	private String passWord;
	private String ip;

	private File[] listFiles;

	public DistantClient(String userName, String passWord, String ip) {
		this.userName = userName;
		this.passWord = passWord;
		this.ip = ip;

	}

	public DistantClient(String userName, String ip, File[] files) {
		this.userName = userName;
		this.ip = ip;
		this.listFiles = files;
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

	@Override
	public String toString() {
		return userName + " " + ip;
	}

}