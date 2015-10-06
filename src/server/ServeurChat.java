package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurChat implements Runnable {
	int nbClient;
	ServerSocket socketEcoute;
	
	public ServeurChat(int nb, ServerSocket s){
		nbClient = nb;
		socketEcoute = s;
	}
	
	public void run() {
		try {
			// on stocke les sockets d'echange dans un tableau de socket
			Socket communication[] = new Socket[nbClient];
			
			// on créer un tableau de thread. Chaque thread gère un client
			Thread t[] = new Thread[nbClient];
			
			BufferedReader in;
			String noms[] = new String[nbClient];
			
			// connexion avec chaque client
			for(int i=0; i<nbClient; i++){
				System.out.println("attente de connexion du client "+i+"...");
				communication[i] = socketEcoute.accept();
				
				// on recupere le nom du client
				in = new BufferedReader(new InputStreamReader(communication[i].getInputStream()));
				noms[i] = in.readLine();
				System.out.println("Client "+noms[i]+" connecté");
			}

			System.out.println("La communication peut commencer");
			
			// on lance les threads serveur qui vont gérer les clients
			for(int i=0; i<nbClient; i++){
				t[i] = new Thread(new ChatClientServeur(communication, i, noms[i]));
				t[i].start();
			}
			
			// on attend que les threads serveurs soit terminés pour stopper le serveur
			for(int i=0; i<nbClient; i++){
				try {
					t[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("arret du serveur chat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
