package server;

import liaison.DonneesPartagees;

public class InitPartie implements Runnable{
	Donnees d;
	
	public InitPartie(Donnees d){
		this.d = d;
	}
	
	public void run(){
		while(true){
			System.out.println("Thread init is waiting...");
			d.attendreConnexion();
			System.out.println("Thread init is starting...");
			
			// on peut maintenant lancer le serveur de chat
			Thread t = new Thread(new ServeurChat(d.getNbClients(), d.getSocketChat()));
			t.start();
			
			//Tous les client sont connectes
			System.out.println("Initialisation OK!");
			
			//Verifier qu'il n y ai pas plusieurs joueurs avec le meme avatar
			String [] lesNoms = d.getLesNoms();
			int maxClients = d.getMaxClients();
			int maxAvatars = d.getMaxAvatars();
			int [] lesAvatars = d.getLesAvatars();			
			boolean [] avatarsPris = d.getAvatarPris();
			
			for(int i = 0; i < maxClients; i++){
				
				if(lesNoms[i] != null){
					//Si l'avatar du joueur i est pris alors lui en affecter un autre
					if(lesAvatars[i] == -1){
						int x = 0;
						while(0 <= x && x < maxAvatars && avatarsPris[x]){
							x++;
						}
						//L'avatar x n'est pas pris
						lesAvatars[i] = x;
						avatarsPris[x] = true;
					}
				}
			}
			
			d.setDonneesP(new DonneesPartagees(d.getNbClients(), d.getLesNoms(), d.getLesAvatars(), d.getEstOrdi()));
			
			d.setInitStart(false);
			d.setPoolStart(true);
			d.notifyPool(); //Debloquer threads de la pool
			System.out.println("Debloquer thread de la pool");
		}
	}
}
