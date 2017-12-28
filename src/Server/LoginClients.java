package Server;

/**
*  	Project name : PeerToPeer
*	Class : ListClients
*
* 	Date of creation : 28.12.2017
* 	
*	Description :
*	Class who add two valide login (name, pwd, ip).
* 
* @author Nicolas Piguet
*/

import java.util.ArrayList;

public class LoginClients {

	private ArrayList <ExistedClient> listLogin = new ArrayList<ExistedClient>();

	public LoginClients () 
	{
		listLogin.add(new ExistedClient("Nico", "Nico", "192.168.108.1"));
		listLogin.add(new ExistedClient("Vlado", "Vlado", "192.168.108.10"));
	}


	public ArrayList <ExistedClient> getListLogin() {
		return listLogin;
	}
	
	public int getSize() {
		return listLogin.size();
	}
	
	public ExistedClient getClient(int index) {
		return listLogin.get(index);
	}
	
	public int getIndex(ExistedClient client) {
		return listLogin.indexOf(client);
	}
	
	/**
	 * Boolean that return true if the client exists in the "listLogin"
	 */
	public boolean checkLogin(ExistedClient client) {
		if(listLogin.contains(client))
			return true;
		return false;
	}
}