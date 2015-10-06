package dialog;

import javax.swing.JFrame;
import javax.swing.Timer;

import liaison.Liaison;

public class Connexion implements Runnable{
	Liaison liaison;
	Timer timer;
	JFrame mainFen;
	
	public Connexion(JFrame parent, Timer timer, Liaison liaison){
		this.liaison = liaison;
		this.timer = timer;
		mainFen = parent;
	}
	
	public void run(){
		if(liaison.connect() == 0){
			//Fermeture de la fenetre de dialog reseau
			timer.start(); 
			
			//Lancer Partie
			liaison.setConnexionEtablie(true);
			liaison.notifyConnexion();
			
		}
		else{
			//Fermeture de la fenetre de dialog reseau
			timer.start();
			liaison.notifyConnexion();
			if(!liaison.isAnnuler()){
				new FenDialogReseauEchec(mainFen,"Tentative de Connexion",  true);
			}
		}
		
		
		
	}

}
