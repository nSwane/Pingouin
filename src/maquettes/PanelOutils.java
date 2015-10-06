/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 20/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :PanelOutils										*
* *******************************************************************
* Description : 													*
* Panneau contenant les boutons d'outils pour le joueur				*
*********************************************************************/
package maquettes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import liaison.Liaison;
import liaison.ObserveurMoteur;
import ressourcesIHM.BoutonImage;
import Son.BanqueDeSon;
import dialog.FenDialogConfirmationRejouer;

@SuppressWarnings("serial")
public class PanelOutils extends JPanel implements ActionListener {
	
	//variable
	private BoutonImage boutonConseil, boutonAnnuler, boutonRefaire, boutonOptions, boutonRejouer, boutonSpeaker, boutonMusic, boutonControle;
	private int hauteurBouton = 40, largeurBouton = 200, espace = 5;
	private FenPengouin fenPengouin;
	private Liaison liaison;
	private JFrame mainFen;
	private PanContexte panCtxt;
	private boolean fenOptionOuverte;
	
	//constructeur
	public PanelOutils(PanContexte panCtxt, JFrame mainFen,FenPengouin fenPengouin, Liaison liaison){
		
		// Recuperation des parametres
		this.panCtxt = panCtxt;
		this.fenPengouin = fenPengouin;
		this.liaison = liaison;
		this.mainFen = mainFen;
		
		// Options de la fenetre
		this.setOpaque(false); // on permet de voir le fond du panneau contexte
		this.setLayout(null);
		
		// Ajout de tous les elements du panelOutils
		this.initComponent();
		
	}//fin constructeur
	
	/**
	 *  Ajout de tous les elements du panelOutils
	 */
	public void initComponent()
	{
		//mise en place des boutons du panneau outils
		boutonConseil = new BoutonImage("Conseil");
		boutonConseil.setBounds(espace, espace * 5, largeurBouton, hauteurBouton);
		boutonConseil.addActionListener(this);
		this.add(boutonConseil);
		boutonAnnuler = new BoutonImage("Annuler");
		boutonAnnuler.addActionListener(this);
		boutonAnnuler.setEnabled(liaison.getDonneesAff().isAnnulerPossible());
		boutonAnnuler.setBounds(espace, espace * 6 + hauteurBouton, largeurBouton, hauteurBouton);
		this.add(boutonAnnuler);
		boutonRefaire = new BoutonImage("Retablir");
		boutonRefaire.addActionListener(this);
		boutonRefaire.setEnabled(liaison.getDonneesAff().isRefairePossible());
		boutonRefaire.setBounds(espace, espace * 7 + hauteurBouton * 2 , largeurBouton, hauteurBouton);
		this.add(boutonRefaire);
		boutonRejouer = new BoutonImage("Rejouer");
		boutonRejouer.addActionListener(this);
		boutonRejouer.setBounds(espace, espace * 8 + hauteurBouton * 3, largeurBouton, hauteurBouton);
		this.add(boutonRejouer);
		boutonOptions = new BoutonImage("Options");
		boutonOptions.setBounds(espace,espace * 9 + hauteurBouton * 4, largeurBouton, hauteurBouton);
		boutonOptions.addActionListener(this);
		this.add(boutonOptions);
		boutonSpeaker = new BoutonImage("Speaker", liaison.getDonneesAff().isSpeakerActif());
		boutonSpeaker.setBounds(espace+100,espace * 10 + hauteurBouton * 5, 50, 50);
		boutonSpeaker.addActionListener(this);
		this.add(boutonSpeaker);
		boutonMusic = new BoutonImage("Music", liaison.getDonneesAff().isMusicActive());
		boutonMusic.setBounds(espace + 150,espace * 10 + hauteurBouton * 5, 50, 50);
		boutonMusic.addActionListener(this);
		this.add(boutonMusic);
		boutonControle = new BoutonImage("Pause", true);
		boutonControle.setBounds(espace,espace * 10 + hauteurBouton * 5, 50, 50);
		boutonControle.addActionListener(this);
		boutonControle.setActif(false);
		liaison.setOrdiActif(false);
		boutonControle.setToolTipText("Permet de mettre en pause l'ordinateur pour observer ses deplacements avec Annuler sans qu'il rejoue immediatement.");
		this.add(boutonControle);
	}
	
	public BoutonImage getOptions(){
		return this.boutonOptions;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (liaison.getDonneesAff().isSpeakerActif())
			BanqueDeSon.CLIC.jouerSon(false);
		if(e.getSource() == this.boutonOptions)
		{
			if(!fenOptionOuverte){

				// A l'appui sur le bouton controle, on inverse isActif
				liaison.setOrdiActif(false);
				boutonControle.setActif(false);
				// A l'ouverture de la fenêtre, on declare le moteur comme actif pour qu'il ne prenne pas d'action en compte
				// On le met en inactif a la fermeture de la fenetre
				liaison.setMoteurActif(true);
				new FenOptions(this.panCtxt, this.fenPengouin, liaison);
				fenOptionOuverte = true;
			}
		}
		else if(e.getSource() == this.boutonConseil)
		{
			if (liaison.isOrdiActif() && !liaison.getDonneesAff().joueurCourant().estOrdi())
				liaison.conseil();
		}
		else if(e.getSource() == this.boutonAnnuler)
		{
			// A l'appui sur le bouton controle, on inverse isActif
			liaison.setOrdiActif(false);
			boutonControle.setActif(false);
			liaison.annuler();
		}
		else if(e.getSource() == this.boutonRefaire)
		{
			// A l'appui sur le bouton controle, on inverse isActif
			liaison.setOrdiActif(false);
			boutonControle.setActif(false);
			liaison.refaire();
		}
		else if(e.getSource() == this.boutonRejouer)
		{
			// Si le jeu n'est pas fini, on demande si on veut sauvegarder avant de rejouer
			if(!liaison.getDonneesAff().isJeuFini() && liaison.getDonneesAff().isOptionAffichageConfirmation())
				new FenDialogConfirmationRejouer(this.mainFen,this.fenPengouin, liaison, "Confirmation rejouer", true);
			else
				this.rejouer();
		}
		else if(e.getSource() == this.boutonSpeaker)
		{
			liaison.muteSound(!liaison.getDonneesAff().isSpeakerActif(), liaison.getDonneesAff().isMusicActive());				
			System.out.println("[PanelOutils - " + Thread.currentThread().getId()+"] spearkeactif : "+ liaison.getDonneesAff().isSpeakerActif());
			boutonSpeaker.setActif(liaison.getDonneesAff().isSpeakerActif());
		}
		else if(e.getSource() == this.boutonMusic)
		{
			if (liaison.getDonneesAff().isMusicActive())
				BanqueDeSon.MUSIQUE.Stop();
			else
				BanqueDeSon.MUSIQUE.jouerSon(true);
			liaison.muteSound(liaison.getDonneesAff().isSpeakerActif(), !liaison.getDonneesAff().isMusicActive());
			boutonMusic.setActif(liaison.getDonneesAff().isMusicActive());
		}
		// Si on appuie sur le bouton play/pause de l'ordi
		if(e.getSource() == this.boutonControle)
		{
			System.out.println("[PanelOutils] l'ordi etait " + liaison.isOrdiActif() + ", il est maintenant : " + !liaison.isOrdiActif());
			
			// Si l'ordi etait en pause, 
			if (!liaison.isOrdiActif())
			{
				if (!liaison.getDonneesAff().isJeuFini() && liaison.getDonneesAff().joueurCourant().estOrdi())
				{
					if (liaison.getDonneesAff().isPlacementTermine())
					{
						System.out.println("[PanelOutils] jouerCoup ordi");
						liaison.jouerCoup(null, null);
					}
					else
					{
						liaison.placerPingouin(null);
					}
				}
			}
			// A l'appui sur le bouton controle, on inverse isActif
			liaison.setOrdiActif(!liaison.isOrdiActif());
			boutonControle.setActif(!boutonControle.isActif());
		}
	}//fin action performed

	private void rejouer()
	{
		liaison.quitter();
	  	// Initialisation de liaison
		liaison = new Liaison(false, liaison.getDonneesAff().isPartiePerso());
		
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
	
	public BoutonImage getBoutonAnnuler() {
		return boutonAnnuler;
	}

	public BoutonImage getBoutonRefaire() {
		return boutonRefaire;
	}
	public void setFenOptionOuverte(boolean b){
		this.fenOptionOuverte = b;
	}

	public boolean isConseil() {
		// TODO Auto-generated method stub
		return true; // A completer pour renvoyer si c'est un conseil
	}

	public BoutonImage getBoutonControle() {
		return boutonControle;
	}

	public void setBoutonControle(BoutonImage boutonControle) {
		this.boutonControle = boutonControle;
	}

	public BoutonImage getBoutonConseil() {
		return boutonConseil;
	}

	public void setBoutonConseil(BoutonImage boutonConseil) {
		this.boutonConseil = boutonConseil;
	}

}//fin classe
