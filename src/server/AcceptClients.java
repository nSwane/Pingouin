package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import liaison.Coordonnees;
import liaison.DonneesPartagees;
import moteur.Joueur;
import moteur.Profile;

public class AcceptClients implements Runnable{
	ServerSocket serverSocket;
	int numeroClient;
	Donnees d;
	DonneesPartagees donneesS;
	ObjectInputStream in;
	Socket server;	
	
	public AcceptClients(int numeroClient, ServerSocket serverSocket, Donnees d){
		this.serverSocket = serverSocket;
		this.numeroClient = numeroClient;
		this.d = d;
		server = null;
	}
	
	public void run(){
		
		
		while(true){
			try {
				 System.out.println("Thread Client "+numeroClient+" is waiting for client");
				
				//En attente de connexion d'un client
				 
				server = serverSocket.accept();
				d.addAccept();
				
				if(!d.isPartieTerminee()){ //La partie a deja commencee
					//L'informer au client			
					
					//Recuperation du flux de donnees Server -> Client
					ObjectInputStream in = new ObjectInputStream(server.getInputStream());
					
					//Lecture du profile
					try {
						in.readObject();
					} catch (ClassNotFoundException e1) {
						System.out.println("Echec lecture du profile");
					}
					
					ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
					
					//Envoi
					out.writeObject(null);
					out.close();
	        		server.close();
	        		System.out.println("msg envoye!!");
	        		
					server = null;
					System.out.println("Partie en cours!");
					d.removeAccept();
				}
				else{
					//Correspondance entre numeroClient et numero du joueur
					d.setJoueurToClient(d.getNbClients(), numeroClient);
					
					//Correspondance entre numero du joueur et numeroClient  
					d.setClientToJoueur(numeroClient, d.getNbClients());
					System.out.println("client "+numeroClient+" --> "+d.getNbClients());
					
					d.addClient(); //Un client a traiter en plus
					
					//Recuperation des flux de donnees et de l'identifiant du client
					initClient();
					
					//Lancer la partie s'il s'agit du dernier joueur
					if(d.getNbAccept() == d.getMaxClients()){
						//Initialisation de la partie
			        	d.setInitStart(true);	
			        	d.notifyInit();
			        	d.setPartieTerminee(false);
					}
				}
		        
			}catch(SocketTimeoutException s){
//		        System.out.println("Thread Client "+numeroClient+" Socket timed out!");
		        System.out.println("Thread Client "+numeroClient+" Nombre clients connectes : "+d.getNbClients());
//		        System.out.println("Thread Client "+numeroClient+" not accepted");
			        	
		        if(d.getNbClients() < 2){ //Cas ou il y a au plus un joueur
		        	System.out.println("Thread Client "+numeroClient+"Pas assez de clients");

		        	if(d.getNbClients() == 1){
			        	//Recuperation flux de donnees Server -> Client
				    	ObjectOutputStream out = d.getOutFromServer(d.getJoueurToClient(0));
				    	Socket socketS = d.getServers(d.getJoueurToClient(0));
				    	d.setNbClients(0);
				    	d.setNbAccept(0);
				    	
				    	//Informer le client qu'il n y a pas de jeu possible
				    	try {
							if(socketS != null && out != null){
								
								if(!socketS.isClosed()){
						        	out.writeObject(null);
						        	socketS.close();
								}
					        				        	
								System.out.println("msg envoye!");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
		        	}
		        }
		        else{ //Le nombre de joueurs est suffisant
		        	
		        	if(d.isPartieTerminee()){
			        	System.out.println("Thread Client "+numeroClient+" -> initialisalisation!");
			        	
			        	//Initialisation de la partie
			        	d.setInitStart(true);	
			        	d.notifyInit();
			        	d.setPartieTerminee(false);
		        	}
		        	
		        }
		        server = null;
		    }
			catch(IOException s){
				System.out.println("-------> Thread Client "+numeroClient+": erreur accept");
				server = null;
			}
			
			//Le thread lanceur ne doit pas allez plus loin
			//De meme pour ceux dont la connexion avec un client a echouee
			if(server != null){
				//Attendre fin initialisation partie
				System.out.println("Wait Init");
				d.waitInit();
				
				//Lancer partie si possible
	    		lancerPartie();
	    		
	    		/*Reinitialiser les threads server:
	        	 *Fermeture de la socket cote server
	        	 *--> Retour a accept 
	        	 */
	    		System.out.println("Fin de partie!!");
	        	d.setReset(true);
	        	d.notifyReset();
	        	d.setPartieTerminee(true);
	        	
//	        	 Attendre que les autres threads joueurs soient prets
	        	 
//	        	d.setEstPret(d.getClientToJoueur(numeroClient), true);
//	        	d.notifyThreadsJoueurs();
//	        	
//	        	System.out.println("Attente des autres threads joueurs");
//	        	d.attendreThreadsJoueurs();
	        	
	    	}
		}
	}

	public void initClient(){
		//Un client s'est connecte
        System.out.println("Client : "+numeroClient+" just connected to "+ server.getRemoteSocketAddress());
    
		try {
			//Recuperation des flux de donnees
			//Client -> Server
			in = new ObjectInputStream(server.getInputStream());
			
			//Server ->Client
			d.setOutFromServer(numeroClient, new ObjectOutputStream(server.getOutputStream()));
			
			//Lecture du profile du client
			Profile profile = (Profile) in.readObject();
			System.out.println("Client : "+profile.getNom());
			
			//Mise a jour des donnees
			d.setNom(d.getClientToJoueur(numeroClient), profile.getNom());
			d.setAvatars(d.getClientToJoueur(numeroClient), profile.getNumeroAvatar());
			d.setEstOrdi(d.getClientToJoueur(numeroClient), profile.isEstOrdi());
			
//			System.out.println("d.setNom("+d.getClientToJoueur(numeroClient)+","+ profile.getNom()+")");
//			System.out.println("d.setEstOrdi("+d.getClientToJoueur(numeroClient)+","+ profile.isEstOrdi()+")");
//			System.out.println("d.setAvatars("+d.getClientToJoueur(numeroClient)+","+profile.getNumeroAvatar()+")");
			
			//Ajout du client pour initialisation de la partie
			d.setServers(numeroClient, server);			
			
		} catch (Exception e1) {
			System.out.println(e1);
			System.out.println("-------> AcceptClient : Recuperation des flux de donnees");
			System.out.println("-------> AcceptClient : Lecture de l'id du client ");
			System.out.println("-------> AcceptClient : Erreur ");

			//Mise a jour des donnees
			d.removeClient();
			
			//Fermeture de la socket cote server si possible et flux de donnees
			if(!server.isClosed()){
				try{
					server.close();
				}catch(Exception e){
					System.out.println(e);
				}
			}
			
		}
        /*****Phase d'initialisation du client terminee******/				
        System.out.println("Thread client "+numeroClient+" is waiting...");
        /*****Le client attend le debut de la partie*****/
	}
	
	public void lancerPartie(){
		donneesS = d.getDonneesP(); //Donnees initiales
		
		/*******************************/
//		Joueur [] tabj = donneesS.getJoueurs();	
//		for(int i = 0; i < d.getNbClients(); i++){
//			if(tabj[i] != null){
//				System.out.println(tabj[i].getNom()+" : "+tabj[i].toString());
//			}
//		}
//		System.out.println("Joueur courant : "+donneesS.getJoueurCourant());
		/*******************************/
        try {
        	ObjectOutputStream out = d.getOutFromServer(numeroClient);
        	
        	//Informer le client que la partie peut commencer
        	out.writeObject(donneesS);
			out.flush();
			out.reset();
          
        } catch (Exception e1) {
			System.out.println(e1);
			System.out.println("-------> Thread Client "+numeroClient+": Informer le client que la partie peut commencer");
			return;
        }
        
        System.out.println("*************************************");
        
//        System.out.println("Numero Client : "+numeroClient);
//        System.out.println("Id Client : "+tabj[d.getClientToJoueur(numeroClient)].getNumero());
//        System.out.println("Nom Client : "+tabj[d.getClientToJoueur(numeroClient)].getNom());
        
        //Lecture d'une requete puis envoie d'une reponse        
        readWrite(); 
    	
	}
	
	public void readWrite(){
		Socket [] servers = d.getServers();
		Joueur [] tabj = donneesS.getJoueurs();
		ObjectOutputStream [] outFromServer = d.getOutFromServer();
		
		DonneesPartagees dp;
		//Lecture des messages emis par le client
		try{
	        while((dp = (DonneesPartagees) in.readObject()) != null){		
	        	int jc = donneesS.getJoueurCourant();
	        	
				System.out.println("Parsing query...");
				
				//Verifier que c'est bien mon tour
				if(d.getJoueurToClient(jc) == numeroClient){					
					parseQuery(dp);
					System.out.println("Sending answer...");
				
					//Envoie d'une reponse
					for(int i = 0; i < d.getNbClients(); i++){
						if(servers[d.getJoueurToClient(i)] != null && !servers[d.getJoueurToClient(i)].isClosed() ){
							try {		
								if(outFromServer[d.getJoueurToClient(i)] != null){
									outFromServer[d.getJoueurToClient(i)].writeObject(donneesS);
									outFromServer[d.getJoueurToClient(i)].flush();
									outFromServer[d.getJoueurToClient(i)].reset();
									System.out.println("--> Envoyee a "+tabj[i].getNom());
								}
							} catch (IOException e) {
								System.out.println("-------> Le msg n'a pas pu etre envoye");
								break;
							}
						}
					}					
					System.out.println("Reponses envoyees...");
				}
				System.out.println("Joueur courant : "+donneesS.getJoueurCourant()+" : "+tabj[donneesS.getJoueurCourant()].getNom());
			}
	        //Fermeture flux de donnees Client -> Server	
			in.close();
		}catch(Exception e){
			System.out.println("-------> AcceptClient : erreur lecture/ecriture");
			System.out.println("-------> Le client s'est deconnecte");
			
			//Fermeture flux de donnees Client -> Server
			try {
				in.close();
			} catch (IOException e1) {
				System.out.println(e1);
			}			
			return;
		}        
	}
	
	private void parseQuery(DonneesPartagees donneesC){
		Joueur [] tabj = donneesS.getJoueurs();
		
		donneesS.setAction(donneesC.getAction());
		switch (donneesC.getAction()) {		
		case JouerCoup:
			Coordonnees cd = donneesC.getCoordPingouinDep();
			Coordonnees ca = donneesC.getCoordPingouinArr();
			
			donneesS.setCoordPingouinDep(new Coordonnees(cd.getI(), cd.getJ()));
			donneesS.setCoordPingouinArr(new Coordonnees(ca.getI(), ca.getJ()));
			
			//Deplacement du pengouin
			if(tabj[donneesS.getJoueurCourant()].deplacerPingouin(cd, ca) < 0){
				System.out.println(tabj[donneesS.getJoueurCourant()].getNom()+ " : Deplacement impossible");
				break;
			}			  	
			
			//Mise a jour du joueur courant    			
			if(donneesS.getJoueurCourant() == donneesS.getNbJoueurs() - 1){
				donneesS.setJoueurCourant(0);
			}
			else{
				donneesS.setJoueurCourant(donneesS.getJoueurCourant()+1);
			}
			
			// on v�rifie si le joueur suivant peut jouer, si ce n'est pas le cas il passe son tour
			// tant qu'on a pas trouv� un joueur pouvant jouer on passe au suivant
			// si tous les joueurs ne peuvent pas jouer c'est fini
			int n = donneesS.getNbJoueurs();
			while(donneesS.getJoueurs()[donneesS.getJoueurCourant()].getPingouinsRestants() <= 0 && n > 0 ){
				if(donneesS.getJoueurCourant() == donneesS.getNbJoueurs() - 1){
					donneesS.setJoueurCourant(0);
    			}else{
    				donneesS.setJoueurCourant(donneesS.getJoueurCourant()+1);
    			}
				n--;
			}

			// on v�rifie si tous les joueurs sont bloqu�s
			// si c'est le cas on ramasse les derni�res tuiles et trouve le gagnant
			if(n == 0){
				donneesS.getTerrain().ramasserTuilesFin(tabj);
				donneesS.setGagnant(donneesS.getTerrain().gagnantPartie(tabj));
				System.out.println("Le gagnant est: "+donneesS.getGagnant().getNom()+"!!!");
				donneesS.setJeuFini(true);
			}
			System.out.println("JC : "+tabj[donneesS.getJoueurCourant()].getNom());
			System.out.println("PR : "+tabj[donneesS.getJoueurCourant()].getPingouinsRestants());
			break;
			
		case PlacerPingouin:
			//Recuperation des coordonnees du pengouin
			Coordonnees ci = donneesC.getCoordPingouinInitial();
			
			donneesS.setCoordPingouinInitial(new Coordonnees(ci.getI(), ci.getJ()));
			
			//Placement du pengouin
			if( tabj[donneesS.getJoueurCourant()].ajouterPengouin(ci) < 0){
				System.out.println(tabj[donneesS.getJoueurCourant()].getNom()+" : Placement impossible");
				break;
			}  		
						
			//Si tous les placements sont termines, l'indiquer aux autres
			if(placementTermine()){
				donneesS.setPlacementPingouinTermine(placementTermine());
				donneesS.getTerrain().verifierBlocages(tabj);
			}
//			System.out.println(placementTermine());
			
			//Mise a jour du joueur courant
			if(donneesS.getJoueurCourant() == donneesS.getNbJoueurs() - 1){
				donneesS.setJoueurCourant(0);
			}
			else{
				donneesS.setJoueurCourant(donneesS.getJoueurCourant()+1);
			}
			break;
			
        case Quitter:
            break;
        
		default:
			break;
		}
		
	}
	
	//retourne vrai sil les pengouins de chaque joueurs sont places
	//Sino faux
	public boolean placementTermine()
	{
		boolean tousPlaces = true;
		Joueur [] tabj = donneesS.getJoueurs();
		
		for(int i = 0; i < donneesS.getNbJoueurs(); i++)
		{
			tousPlaces &= tabj[i].getPengouinsPlaces();
		}
		return tousPlaces;
	}
}
