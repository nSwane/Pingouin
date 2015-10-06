	/********************************************************************
	* Nom du codeur : Michael OMER		*Date de création : 17/05/2013	*
	* 									*Date de modification :			*
	* Nom de la classe :PanelDepart										*
	* *******************************************************************
	* Description : 													*
	* Panel contenant Le Menu au lancement du jeu						*
	* c'est a dire les boutons :										*
	* 						Partie rapide								*
	* 						Partie perso								*
	* 						charger										*
	*						quitter										*
	*********************************************************************/

package maquettes;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import liaison.Liaison;
import liaison.ObserveurMoteur;
import ressourcesIHM.BoutonImage;
import Son.BanqueDeSon;
import dialog.FenDialogReseau;

@SuppressWarnings("serial")
public class PanelDepart extends JPanel implements ActionListener
{
	
	//variables
		private BoutonImage boutonPartieRapide, boutonPerso, boutonCharger, boutonQuitter;
		private BufferedImage imageLogoJeu, imageFondEcran;
		private JFrame mainFen;
		private FenPengouin fenPengouin;
		private final static int hauteurFenetre = 740, largeurFenetre = 1024, hauteurBouton = 60;
		private final static int largeurBouton = (largeurFenetre / 4), hauteurLogo = 200, largeurLogo = 600, espace = (hauteurBouton / 3);
		private Liaison liaison;
		private ObserveurMoteur obM;
		private PanContexte panCtxt;
		private boolean estOuvert;
		private BoutonImage boutonLigne;
	
	//constructeurs
	public PanelDepart(JFrame mainFen, FenPengouin fenPengouin){
		
		// Recuperation des parametres
		this.mainFen = mainFen;
		this.fenPengouin = fenPengouin;
		estOuvert = true;
		// Options de la fenetre
		this.setLayout(null);
		this.setSize(largeurFenetre, hauteurFenetre);
		this.setVisible(true);
		
		// Ajout des elements du panelDepart
		this.initComponent();
	}
	
	/**
	 * Ajout des elements du panel de depart
	 */
	public void initComponent()
	{
		ajoutDesImages();
		ajoutDesBoutons();
				
	}//fin initcomponent
	//ajout des images du logo et du fond ecran
	private void ajoutDesImages(){
		
		//ouverture des images des boutons
		try{
			 imageLogoJeu = ImageIO.read(getClass().getResource("/Data/Images/Fond/Titre_Pingouins.png"));
			 imageFondEcran = ImageIO.read(getClass().getResource("/Data/Images/Fond/Fond_Ecran_Depart.jpg"));
		}catch(IOException e){
		}
//		
	}//fin ajout des images
	
	//ajout des boutons du menu
	private void ajoutDesBoutons(){
		//ouverture des bouton et ajout a la fenetre
		boutonPartieRapide = new BoutonImage("PartieRapide");
		boutonPartieRapide.setSize(largeurBouton,hauteurBouton);
		boutonPartieRapide.setLocation(getWidth()/2 - largeurBouton / 2, (getHeight()/3 - hauteurLogo)/2 + hauteurLogo + espace);
		boutonPartieRapide.addActionListener(this);
		this.add(boutonPartieRapide);
		//creation du bouton Partie Perso
		boutonPerso = new BoutonImage("PartiePerso");
		boutonPerso.setSize(largeurBouton, hauteurBouton);
		boutonPerso.setLocation(getWidth()/2 - largeurBouton / 2, (getHeight()/3 - hauteurLogo)/2 + hauteurLogo + (espace * 2) + hauteurBouton);
		boutonPerso.addActionListener(this);
		this.add(boutonPerso);
		//creation du bouton Partie en Ligne
		boutonLigne = new BoutonImage("PartieEnLigne");
		boutonLigne.setSize(largeurBouton, hauteurBouton);
		boutonLigne.setLocation(getWidth()/2 - largeurBouton / 2, (getHeight()/3 - hauteurLogo)/2 + hauteurLogo + (espace * 3) + 2*hauteurBouton);
		boutonLigne.addActionListener(this);
		this.add(boutonLigne);
		//creation du bouton charger	
		boutonCharger = new BoutonImage("Charger");
		boutonCharger.setSize(largeurBouton, hauteurBouton);
		boutonCharger.setLocation(getWidth()/2 - largeurBouton / 2, (getHeight()/3 - hauteurLogo)/2 + hauteurLogo + (espace * 4) + 3 * hauteurBouton);
		boutonCharger.addActionListener(this);
		this.add(boutonCharger);
		//creation du bouton quitter
		boutonQuitter = new BoutonImage("Quitter");
		boutonQuitter.setSize(largeurBouton, hauteurBouton);
		boutonQuitter.setLocation(getWidth()/2 - largeurBouton / 2, (getHeight()/3 - hauteurLogo)/2 + hauteurLogo + (espace * 8) + 4 * hauteurBouton);
		boutonQuitter.addActionListener(this);
		this.add(boutonQuitter);
	}//fin ajout des boutons du menu
	
	//fonction de dessin du panel
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(imageFondEcran,0,-50,null);
		g.drawImage(imageLogoJeu,(getWidth() - largeurLogo)/2 + espace, (getHeight()/3 - hauteurLogo)/2, null);
	}

	/**
	 * @return the boutonPartieRapide
	 */
	public BoutonImage getBoutonPartieRapide() {
		return boutonPartieRapide;
	}

	/**
	 * @param boutonPartieRapide the boutonPartieRapide to set
	 */
	public void setBoutonPartieRapide(BoutonImage boutonPartieRapide) {
		this.boutonPartieRapide = boutonPartieRapide;
	}

	/**
	 * @return the boutonPerso
	 */
	public BoutonImage getBoutonPerso() {
		return boutonPerso;
	}

	/**
	 * @param boutonPerso the boutonPerso to set
	 */
	public void setBoutonPerso(BoutonImage boutonPerso) {
		this.boutonPerso = boutonPerso;
	}

	/**
	 * @return the boutonCharger
	 */
	public BoutonImage getBoutonCharger() {
		return boutonCharger;
	}

	/**
	 * @param boutonCharger the boutonCharger to set
	 */
	public void setBoutonCharger(BoutonImage boutonCharger) {
		this.boutonCharger = boutonCharger;
	}

	/**
	 * @return the boutonQuitter
	 */
	public BoutonImage getBoutonQuitter() {
		return boutonQuitter;
	}

	/**
	 * @param boutonQuitter the boutonQuitter to set
	 */
	public void setBoutonQuitter(BoutonImage boutonQuitter) {
		this.boutonQuitter = boutonQuitter;
	}
	

	/**
	 * @return the estOuvert
	 */
	public boolean isEstOuvert() {
		return estOuvert;
	}

	/**
	 * @param estOuvert the estOuvert to set
	 */
	public void setEstOuvert(boolean estOuvert) {
		this.estOuvert = estOuvert;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
//		if (liaison.getDonneesAff().isSpeakerMute())
			BanqueDeSon.CLIC.jouerSon(false);
		//action pour le menu de depart
		//Action si il s'agit du bouton Partie rapide
		if(e.getSource() == this.boutonPartieRapide)
		{
			// Initialisation de liaison
			liaison = new Liaison(false,false);
			
			// Changement de panel vers PanContexte
			mainFen.getContentPane().removeAll();
			this.panCtxt = new PanContexte(fenPengouin, liaison);
			this.estOuvert = false;
			mainFen.add(this.panCtxt);//a modifier
			mainFen.setVisible(true);
			// Declaration de l'observer
//			obM = new ObserveurMoteur(panCtxt, liaison);
			liaison.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
		}
		// Action si il s'agit du bouton perso
		else if(e.getSource() == this.boutonPerso)
		{
			// Initialisation de liaison
			liaison = new Liaison(false,true);
			mainFen.getContentPane().removeAll();
			mainFen.add(new PanelPartiePerso(mainFen, fenPengouin, 0, liaison), 0); //ouverture de la fenetre pour choisir ces paramètres
			this.estOuvert = true;
			mainFen.setVisible(true);
		}
		//Action s'il s'agit du bouton jouer en ligne
		else if(e.getSource() == this.boutonLigne){
			Liaison liaison = new Liaison(true, false);
			
			new FenDialogReseau(fenPengouin,"Tentative de Connexion",liaison ,true);
			liaison.attendreFinConnexion();
			
			if(liaison.isConnexionEtablie()){
				
				// Changement de panel vers PanContexte
				mainFen.getContentPane().removeAll();
				this.panCtxt = new PanContexte(fenPengouin, liaison);
				this.estOuvert = false;
				mainFen.add(this.panCtxt);//a modifier
				mainFen.setVisible(true);
				// Declaration de l'observer
				liaison.nouvellePartieReseau(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
			}
		}
		//action si il s'agit du bouton charger pour charger une partie enregistree
		else if(e.getSource() == this.boutonCharger)
		{
			// Initialisation de liaison
			liaison = new Liaison(false,false);
			this.panCtxt = new PanContexte(fenPengouin, liaison);
			// Declaration de l'observer
//			obM = new ObserveurMoteur(panCtxt, liaison);
			// Initialise le moteur
			liaison.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
			
			this.estOuvert = true;
			new FenCharger(this.mainFen, this.fenPengouin, liaison, obM);
		}
		//Action si il s'agit de quitter
		else if(e.getSource() == this.boutonQuitter){
			this.estOuvert = false;
			System.exit(0);	//on quitte le jeu
		}
		
		//fin action pour le menu depart
			
	}//fin action performed

}
