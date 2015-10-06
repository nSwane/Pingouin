package server;

import java.net.ServerSocket;

public class Partie implements Runnable{
	ServerSocket serverSocket;
	DonneesServeurChat donnneesServeurChat;
	int maxClients;
	
	public Partie(ServerSocket serverSocket, int maxClients, DonneesServeurChat donnees){
		this.serverSocket = serverSocket;
		this.maxClients = maxClients;
		this.donnneesServeurChat = donnees;
	}
	
	public void run(){
		//Initialisation des donnees
		Donnees d = new Donnees(maxClients);
		d.setSocketChat(donnneesServeurChat.getSocket());
		
		//Initialisation de la pool de threads
		//Un des thread permet l'initialisation de le partie
		Thread [] acceptC = new Thread[maxClients];
		
		
		for(int i = 0; i < maxClients; i++){
			acceptC[i] = new Thread(new AcceptClients(i, serverSocket, d));
		}
		
		//Initialisation du thread initPartie et ResetConnect
		Thread init = new Thread(new InitPartie(d));
		Thread reset = new Thread(new ResetConnect(d));
		
		//Lancement de la pool, du thread d'initialisation et de reinitialisation
		for(int i = 0; i < maxClients; i++){
			acceptC[i].start();
		}
		init.start();     
		reset.start();
		
		//Attente de terminaison des threads de la pool
		for(int i = 0; i < maxClients; i++){
			try {
				acceptC[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
