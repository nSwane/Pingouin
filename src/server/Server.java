package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	public static void main(String [] args){
		
		int maxParties = 1;
		Thread [] parties = new Thread[maxParties];
		
		int maxClients = 4;
		ServerSocket serverSocket = null;
		int port = 1076;
		//création de la socket du serveur chat
		DonneesServeurChat donnesChat = new DonneesServeurChat();
		  
		//Creation du port
		try {
			serverSocket = new ServerSocket(port);
			
			//Le server n'attendra plus de client au bout de 30000ms (30s)
			serverSocket.setSoTimeout(10000);
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Creation du port "+port+" impossible");			
			System.exit(-1);
		}
		
		System.out.println("Server is waiting for client on port " +serverSocket.getLocalPort() + "...");		                        
		System.out.println("*************************************");
		
		//Initialisation des parties
		for(int i = 0; i < maxParties; i++){
			parties[i] = new Thread(new Partie(serverSocket, maxClients, donnesChat));
		}
		
		//Lancement des parties
		for(int i = 0; i < maxParties; i++){
			parties[i].start();
		}
		
		//Attente de terminaison des parties
		try {
			for(int i = 0; i < maxParties; i++){
				parties[i].join();
			}
		} catch (InterruptedException e) {
			System.out.println(e);
			System.out.println("Server : erreur join");
		}
		
		// fermeture de la socket de chat
		donnesChat.closeSocket();
		System.out.println("Server exited");			  
	}
}