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

	private static ArrayList <ExistedClient> listLogin = new ArrayList<ExistedClient>();

	ExistedClient nico = new ExistedClient("Nico", "Nico", "192.168.108.3");
	ExistedClient vlado = new ExistedClient("Vlado", "Vlado", "192.168.108.1");
	
	
	
	public LoginClients () 
	{
		listLogin.add(nico);
		listLogin.add(vlado);
	}


	public static ArrayList <ExistedClient> getListLogin() {

		return listLogin;
	}
	
	public static int getSize() {
	
		return listLogin.size();
	}
	
	public static ExistedClient getClient(int index) {
		return listLogin.get(index);
	}
	
	public static int getIndex(ExistedClient client) {
		
		return listLogin.indexOf(client);
	}
	
	/**
	 * Boolean that return true if the client exists in the "listLogin"
	 */
	public boolean checkLogin(ExistedClient client) {
		if(listLogin.contains(client)) {
			return true;
		}
		return false;
	}
}