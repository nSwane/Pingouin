package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class ConnexionChat {
	Socket communication;
	int port;
	byte[] nomServeur = {(byte)192,(byte)168,22,10};
	BufferedReader in;
    PrintWriter out;
    String nomClient;
	
	public ConnexionChat(int p, String nomC){
		communication = null;
		port = p;
		in = null;
		out = null;
		nomClient = nomC;
	}
	
	public void Connexion(){
		try {
			// on créer la socket d'échange et on se connecte au serveur
			InetAddress ia = InetAddress.getByAddress(nomServeur);
			communication = new Socket(ia,port);
//			communication = new Socket(nomServeur, port);
			
			// on récupère les entrée/sortie de la socket
			in = new BufferedReader(new InputStreamReader(communication.getInputStream()));
			out = new PrintWriter(communication.getOutputStream());
			
			// on envoie le nom du joueur au serveur
			out.println(nomClient);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedReader getInput(){
		return in;
	}
	
	public PrintWriter getOutput(){
		return out;
	}
}
