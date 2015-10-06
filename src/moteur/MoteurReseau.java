package moteur;

import java.io.*;
import java.net.SocketException;

import liaison.*;

public class MoteurReseau implements Runnable
{
	private Liaison liaison;
	private DonneesPartagees d;
    private boolean continuer, verbose;
    private String debug;
    
    private ObjectOutputStream out;
    
	public MoteurReseau(Liaison l)
	{
		this.liaison = l;
		this.verbose = true;
		this.d = l.getDonnees();	
		
        this.continuer = true;
		
		d.setJoueurCourant(0);
		
		//Init flux pour communication client <-> server
		try{			
			//->Ecriture
			out = l.getOut();
		}catch(Exception e){
			System.out.println(e);
			System.out.println("Client : erreur recuperation flux de donnees");
			System.exit(-1);
		}
	}

	public void run() {
		//Desarmement du timeout
		try {
			liaison.getClient().setSoTimeout(0);
		} catch (SocketException e1) {
			System.out.println(e1);
			System.out.println("Client : Le Desarmement du timeout a echoue");
		}
		
		//Init Thread lecture
		Thread t = new Thread(new LectureClient(liaison));
		t.start();
				
        do
        {   
    		System.out.println("entree dans attendre");
        	liaison.attendre();
        	
        	d = liaison.getDonnees();
        	
    		liaison.setActionPrec(d.getAction());
    		switch (d.getAction()) {	
    		case CaseAccessibles:  
    			debug = verbose?"[Moteur] CaseAccessibles\n":""; System.out.print(debug);  			
    			
    			//Recuperation des coordonnees du pengouin
    			Coordonnees c = d.getCoordPingouinAcc();
    			
    			//Mise a jour des cases accessibles
    			d.getTerrain().casesAccessibles(c);
    			liaison.notifierMiseAJour();
    			break;
    			
    		case AnnulerCaseAccessibles:  
    			debug = verbose?"[Moteur] AnnulerCaseAccessibles\n":""; System.out.print(debug);  			
    			//Recuperation des coordonnees du pengouin
    			Coordonnees cA = d.getCoordPingouinAcc();
    			
    			//Mise a jour des cases accessibles
    			d.getTerrain().annulerCasesAccessibles(cA);
    			liaison.notifierMiseAJour();
    			break;
    			
    		case JouerCoup:
    		case PlacerPingouin:    
    			
    			envoyerRequete(d);
    			liaison.setMsgEnvoye(true);
    			System.out.println("J'attend reponse!!!!");
//    			liaison.attendreReponse();
    			liaison.setMsgEnvoye(false);
    			break;
    			
            case Quitter:
    			debug = verbose?"[Moteur] Quitter\n":""; System.out.print(debug);
            	System.out.println("[Moteur] quitter()");
                continuer = false;
            
    		default:
    			break;
    		}

        } while(continuer);

        System.out.println("Le moteur s'est arrete!!");
        
        //Fin de communication -> Fermeture de la socket cote client
        envoyerRequete(null);
  		try {
  			liaison.getClient().close();
  		} catch (IOException e1) {
  			System.out.println("Echec fermeture socket client");
  		}
  		
  		//Retour au menu principal
	}
	
	//Client -> Server
	public void envoyerRequete(DonneesPartagees d){
		try {
				out.writeObject(d);
				out.flush();
				out.reset();
		} catch (IOException e) {
			System.out.println("Client : erreur ecriture");
			System.out.println(e);
		}
	}
}
