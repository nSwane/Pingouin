package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*Permet de reinitialiser le server dans le cas ou il n y a qu'un joueur de connecte*/
public class ResetConnect implements Runnable{
	Donnees d;
	
	public ResetConnect(Donnees d){
		this.d = d;
	}
	
	public void run(){
		while(true){
			//Attendre qu'on est besoin de moi
			d.attendreReset();
			
			System.out.println("Reset!");
			
			//Reinitialisation
			for(int i = 0; i < d.getMaxClients(); i++){
				//Recuperation flux de donnees Server -> Client
		    	ObjectOutputStream out = d.getOutFromServer(i);
		    	Socket socketS = d.getServers(i);
		    	
		    	//Informer le client que la partie va etre interrompue
		    	try {
					if(socketS != null){
						
						if(!socketS.isClosed() && out != null){
							out.writeObject(null);
			        		socketS.close();
			        		System.out.println("msg envoye a "+i+"!!");
			        	}			      						
					}
				} catch (IOException e) {
					System.out.println("-------> "+e);
					System.out.println("-------> Thread reset : erreur writeObject "+i);
				}		    	
			}
			
			//Mise a jour des donnees
			d.reset();
		}
	}
}
