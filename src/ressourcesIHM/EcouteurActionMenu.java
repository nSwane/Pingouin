/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 30/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :EcouteurActionMenu								*
* *******************************************************************
* Description : 													*
* permet de realiser les actions du menu							*
*********************************************************************/
package ressourcesIHM;

import help.FenAide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import liaison.Liaison;
import liaison.ObserveurMoteur;
import maquettes.FenCharger;
import maquettes.FenPengouin;
import maquettes.PanContexte;
import maquettes.PanelDepart;
import maquettes.PanelPartiePerso;
import dialog.FenDialogConfirmationQuitter;
import dialog.FenDialogConfirmationRejouer;
import dialog.FenDialogSave;

public class EcouteurActionMenu implements ActionListener{

	private JFrame mainFen;
	private FenPengouin fenPengouin;
	private Liaison liaison;
	private PanContexte panCtxt;
	//constructeur panJeu
	public EcouteurActionMenu(JFrame mainFen,FenPengouin fenPengouin, Liaison liaison){
		this.mainFen = mainFen;
		this.liaison = liaison;
		this.fenPengouin = fenPengouin;
	}
		
	public void actionPerformed(ActionEvent e) {
		//action pour le menu Jeu
		if(e.getSource() == this.fenPengouin.getPartieRapide())
		{
			
			// Si le jeu n'est pas fini, on demande si on veut sauvegarder avant de rejouer
			if(!liaison.getDonneesAff().isJeuFini() && liaison.getDonneesAff().isOptionAffichageConfirmation())
				new FenDialogConfirmationRejouer(this.mainFen,this.fenPengouin, liaison, "Confirmation rejouer", true);
			else
				this.rejouer();
		}//fin action sur le menu rejouer
		
		//action pour le conseil
		if(e.getSource() == this.fenPengouin.getConseilCoup())
		{
			liaison.conseil();
		}
		if(e.getSource() == this.fenPengouin.getPartiePerso()){
		
			//liaison.supprimerSauvegarde("sauvegardeTemporaire");	//on supprime le fichier
			liaison.sauvegarder("sauvegardeTemporaire");
			liaison.deleteObservers();
			liaison.quitter();
			mainFen.getContentPane().removeAll();
			mainFen.add(new PanelPartiePerso(mainFen,fenPengouin, 1, liaison), 0); //ouverture de la fenetre pour choisir ces paramètres
			this.fenPengouin.getPanDepart().setEstOuvert(true);
			mainFen.setVisible(true);
		}
		//action si il s'agit du bouton charger pour charger une partie enregistree
		if(e.getSource() == this.fenPengouin.getCharger()){
			new FenCharger(this.mainFen, this.fenPengouin, liaison, null);
		}
		if(e.getSource() == this.fenPengouin.getSauvegarder()){
			new FenDialogSave(this.fenPengouin,this.liaison, "Sauvegarder", true);
		}//fin action sur sauvegarder
		if(e.getSource() == this.fenPengouin.getRegle()){
			new FenAide(this.fenPengouin);
		}
		
		//action si on appuie sur Menu	
		if(e.getSource() == this.fenPengouin.getMenu()){
			liaison.deleteObservers();
			liaison.quitter();
			this.mainFen.getContentPane().removeAll();
			mainFen.add(new PanelDepart(mainFen, fenPengouin)); //ouverture de la fenetre pour choisir ces paramètres
			this.fenPengouin.getPanDepart().setEstOuvert(true);
			mainFen.setVisible(true);
		}
		
		//action si on choisis de quitter		
		if(e.getSource() == this.fenPengouin.getQuitter()){
			quitterJeu();	//on quitte le jeu
		}//fin action pour le menu Jeu
	}//fin action performed
	
	//methode pour quitter le jeu
	private void quitterJeu(){
		if(liaison.getDonneesAff().isOptionAffichageConfirmation()){
			new FenDialogConfirmationQuitter(this.fenPengouin,"Confirmation quitter", true);	//on demande confirmation avant de quitter
		}else{
			System.exit(0);
		}
			
	}//fin quitterJeu
	
	private void rejouer()
	{
		liaison.quitter();
	  	// Initialisation de liaison
		liaison = new Liaison(false, false);
		
		// Changement de panel vers PanContexte
		mainFen.getContentPane().removeAll();
		this.panCtxt = new PanContexte(fenPengouin, liaison);
		mainFen.add(this.panCtxt); //a modifier
		mainFen.setVisible(true);
		liaison.deleteObservers();
		// Declaration de l'observer
//		ObserveurMoteur ca = new ObserveurMoteur(panCtxt, liaison);
		liaison.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
	}
}//fin classe
