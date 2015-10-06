package server;

import java.io.IOException;
import java.net.ServerSocket;

public class DonneesServeurChat {
	private ServerSocket socketEcoute;
	
	public DonneesServeurChat(){
		try {
			socketEcoute = new ServerSocket(1234);
		} catch (IOException e) {
			System.out.println("[DonneesServeurChat] Erreur lors de la création du socket");
		}
	}
	
	public ServerSocket getSocket(){
		return socketEcoute;
	}
	
	public void closeSocket(){
		try {
			socketEcoute.close();
		} catch (IOException e) {
			System.out.println("[DonneesServeurChat] Erreur lors de la fermeture du socket");
		}
	}
	
}
