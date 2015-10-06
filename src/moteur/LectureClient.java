package moteur;

import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;

import dialog.FenDialogConnexionInterrompue;

import liaison.DonneesPartagees;
import liaison.DonneesPartagees.Action;
import liaison.Liaison;

public class LectureClient implements Runnable{
	Liaison liaison;
	ObjectInputStream in;
	
	public LectureClient(Liaison l){
		liaison = l;
		in = l.getIn();
	}
	
	public void run(){
		try{
			DonneesPartagees dp;
			while((dp = (DonneesPartagees) in.readObject()) != null){				
				
				//Mise a jour des donnees
				liaison.setDonnees(dp);				
				liaison.setActionPrec(dp.getAction());
				
				if(liaison.isMsgEnvoye()){
					//J'ai envoye un message et je suis en attente de reception
					liaison.setMsgRecu(true);
					liaison.informerMoteur();
					liaison.setMsgRecu(false);
				}
				
				liaison.notifierMiseAJour();				
			}
			
			//Le client a recu un message de terminaison
			System.out.println("La partie a ete interrompue!!");
			new FenDialogConnexionInterrompue(null,"Fin de la partie",  true);
			
		}catch (ClassNotFoundException e) {			
			System.out.println("Client : erreur lecture Objet");
			System.out.println(e);
			
		}
		catch(SocketTimeoutException e){
			System.out.println("Je suis reste trop longtemps inactif");
		}
		catch (java.io.IOException e) {
			System.out.println("Client : erreur lecture - connexion interrompue");
			System.out.println(e);
		}		
		
		//Fin du moteur
		liaison.setActionPrec(Action.Quitter);
		liaison.quitter();
		
			liaison.setMsgRecu(true);
			liaison.informerMoteur();
		
		System.out.println("End of Lecture Client!!!");
//		liaison.notifierMiseAJour();
		
	}
}
