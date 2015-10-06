package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientServeur implements Runnable {
	Socket sockets[];
	BufferedReader in;
	PrintWriter outs[];
    int numClient;
    int nbClient;
    String nomClient;
	
	public ChatClientServeur(Socket s[], int num, String nomC){
		sockets = s;
		numClient = num;
		nomClient = nomC;
		nbClient = s.length;
		outs = new PrintWriter[nbClient];
	}
	
	public void run() {
		try {
			String ligne;
			
			// recuperation de l'entrée du socket du client pour pouvoir lire
			in = new BufferedReader(new InputStreamReader(sockets[numClient].getInputStream()));
			
			// on récupère les sorties de toutes les socket client
			for(int i=0; i<nbClient; i++){
				outs[i] = new PrintWriter(sockets[i].getOutputStream());
			}
			
			// on informe les utilisateurs des joueurs connectés
			diffuserMessage(nomClient+" a rejoint le chat\n", false);
			
			while(true){
				// on récupère la ligne reçu
				ligne = in.readLine();
				
				// on diffuse le message à tous les autres clients
				diffuserMessage(ligne, true);
			}
		} catch (IOException e) {
			diffuserMessage(nomClient+" a quitté le chat", false);
			System.out.println(nomClient+" a quitté le chat");
			outs[numClient].close();
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	// diffuse la chaine s à tous les joueurs connectés
	// si le boolean est à true, le message vient d'un utilisateur
	// sinon il s'agit d'un message provenant du serveur
	public void diffuserMessage(String s, boolean b){
		String res="";
		for(int i=0; i<nbClient; i++){
			if(outs[i]!=null){
				// on ajoute le nom du joueur si il s'agit d'un message utilisateur
				if(b){
					res = nomClient+": ";
					if(i==numClient){
						res ="moi: ";
					}
				}
				// on ecrit la chaine
				outs[i].println(res+s);
				outs[i].flush();
				res = "";
			}
		}
	}

}
