/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 20/05/2013	*
* 									*Date de modification :21/05/13	*
* Nom de la classe :PanelContexte									*
* *******************************************************************
* Description : 													*
* Panneau principal regroupant tous les panneaux du jeu pour faire 	*
* l'interface														*
*********************************************************************/
package maquettes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import liaison.Liaison;
import ressourcesIHM.EcouteurActionMenu;
import Son.BanqueDeSon;

@SuppressWarnings("serial")
public class PanContexte extends JPanel {
	//variable
	private Tux animateurJeu;
	private JFrame mainFen;
	private FenPengouin fenPengouin;
	private PanelJeu plateauDeJeu;
	private PanelScore scoreJoueur;
	private PanelOutils outilsJoueur;
	private PanelChat chatJoueur;
	private PanelBackground fondEcran;
	private PanelPion pionJoueur;
	private Liaison liaison;

	//constructeur
	public PanContexte(FenPengouin fenPengouin, Liaison liaison){
		//Recuperation des parametres
		this.fenPengouin = fenPengouin;
		this.mainFen = this.fenPengouin.getFenetrePrincipale();
		this.liaison = liaison;
		this.setBackground( new Color(60, 120, 192));
		
		// options de la fenetre
		this.setLayout(null);
		// Ajout de tous les elements du panel Contexte
		this.initComponent();
		
	}//fin constructeur
	
	/**
	 *  Ajout de tous les elements du panel Contexte
	 */
	public void initComponent()
	{
		if (liaison.getDonneesAff().isMusicActive())
			BanqueDeSon.MUSIQUE.jouerSon(true);
		//creation de la barre de menu
		this.fenPengouin.ajoutMenuPrincipal(new EcouteurActionMenu(this.mainFen, this.fenPengouin, this.liaison));
		
		// on definit les panel pion et score
		affichagePion();
		affichageScore();
		//mais on les affiche seulement dans la bonne phase de jeu
		if(!liaison.getDonneesAff().isPlacementTermine()){
			this.add(pionJoueur);
		}else{
			this.add(scoreJoueur);
		}
		//ajout du plateau DeJeu dans la fenetre
		affichagePlateau();
		//creation de l'animateur qui va expliquer et afficher les infos bulles
		affichageCompagnon();
		//affichage du fond d'ecran ce trouvant derriere le plateau
		affichageBackground();
		
		if(liaison.isEnLigne()){
			affichageChat();
		}else{
			affichageOutils();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		/** Version avec Tux qui change :  1/5 trucs a changer 
		if(!liaison.getDonneesAff().isJeuFini())
		{
			// Recuperation de l'avatar du joueur courant
			AvatarsJoueurs avatarCourant = liaison.getAvatars()[liaison.getDonneesAff().getJoueurCourant()];
			animateurJeu.setImage(avatarCourant);
		}*/
	}//fin paintComponent
	
	/**
	 * Affichage de Tux en bas à gauche
	 */
	private void affichageCompagnon(){
		// Recuperation de l'avatar du joueur courant
		/** Version avec Tux qui change :  2/5 trucs a changer 
		AvatarsJoueurs avatarCourant = liaison.getAvatars()[liaison.getDonneesAff().getJoueurCourant()];
		animateurJeu = new Tux(avatarCourant);*/
		animateurJeu = new Tux();
		animateurJeu.setBounds(0,500,200,204);
		animateurJeu.setCommentJouer(liaison.getDonneesAff().isOptionCommentJouer());
		animateurJeu.setBienvenue(liaison.getDonneesAff().isOptionCommentJouer());
		animateurJeu.parler(this, liaison);
		this.add(animateurJeu);
	}//fin affichage du compagnon
	
	/**
	 * Affichage de la grille d'hexagones
	 */
	private void affichagePlateau()
	{
		plateauDeJeu = new PanelJeu(liaison, this);
		plateauDeJeu.setBounds(150, 40, 650, 650);
		plateauDeJeu.setOpaque(false);
		this.add(plateauDeJeu, 0);
		
	}//fin affichagePlateau de jeu
	
	/**
	 * Affichage du panneau des Scores
	 */
	private void affichageScore(){

		System.out.println("[panctxt affichage score] thread : " + Thread.currentThread().getId());
		scoreJoueur = new PanelScore(liaison);	//a modifier avec le fichier de lien suivant le nombre de joueur
		int taillePanel = liaison.getDonneesAff().getNbJoueurs() * 60 + 75;
		scoreJoueur.setLocation(798, 768 / 2  - (taillePanel + 50));
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(18);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scoreJoueur.setBorder(BorderFactory.createTitledBorder(null, "Score : ", 1, 1, font, Color.white));	//creation de la bordure autour du score des joueurs
	}//fin affichageScore
	
	/**
	 * Affichage du panneau des Outils
	 */
	private void affichageOutils(){
		outilsJoueur = new PanelOutils(this, mainFen, this.fenPengouin, liaison);
		outilsJoueur.setBounds(798, 768 / 2 - 35,210,310);
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(18);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outilsJoueur.setBorder(BorderFactory.createTitledBorder(null, "Outils : ", 1, 1, font, Color.white));
		this.add(outilsJoueur);
	}//fin affichageOutils
	
	/**
	 * Affichage de l'image de fond
	 */
	private void affichageBackground(){
		fondEcran = new PanelBackground();
		fondEcran.setBounds(50,20,748,638);
		this.add(fondEcran);
		
	}//fin affichageBackground
	/**
	 * Affichage du panneau de chat
	 */
	private void affichageChat(){
		chatJoueur = new PanelChat(this, mainFen, this.fenPengouin, liaison);
		chatJoueur.setBounds(798, 768 / 2 + 10,210,290);
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(18);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel titre = new JLabel("Conversation: ");
		titre.setForeground(Color.white);
		titre.setFont(font);
		titre.setBounds(807, 768/2-6, 210, 20);
		
		this.add(titre);
		this.add(chatJoueur);
	}
	/**
	 * Affichage du tableau des pions (pendant la phase de placement)
	 */
	private void affichagePion(){
		pionJoueur = new PanelPion(liaison);
		int taillePanel = liaison.getDonneesAff().getNbJoueurs() * 60 + 75;
		pionJoueur.setLocation(798, 768 / 2  - (taillePanel + 50));

		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(20);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Border border = BorderFactory.createTitledBorder(null, "Placez vos pingouins : ", 1, 1, font, Color.white);
		pionJoueur.setBorder(border);	//creation de la bordure autour du panel des pions
	}//fin affichagePion
	
	/**
	 * Enleve le panel de placement des Pions et ajoute le panel des Score
	 */
	public void switchPionScore()
	{
		System.out.println("[panctxt switch pion] thread : " + Thread.currentThread().getId());
		this.remove(pionJoueur);
		this.affichageScore();
		this.add(scoreJoueur);
		animateurJeu.setBienvenue(false);	
		animateurJeu.setAlive(false);
		this.repaint();
		if(liaison.getDonneesAff().isOptionCommentJouer())
			animateurJeu.setAlive(true);
		animateurJeu.parler(this,liaison);
		this.repaint();
	}
	
	/**
	 * Enleve le panel des Score et ajoute le panel de placement des Pions
	 */
	public void switchScorePion()
	{
		this.remove(scoreJoueur);
		this.affichagePion();
		this.add(pionJoueur);
		this.repaint();
	}

	public PanelScore getScoreJoueur() {
		return scoreJoueur;
	}

	public void setScoreJoueur(PanelScore scoreJoueur) {
		this.scoreJoueur = scoreJoueur;
	}
	
	public PanelPion getPionJoueur() {
		return pionJoueur;
	}

	public void setPionJoueur(PanelPion pionJoueur) {
		this.pionJoueur = pionJoueur;
	}
	
	public PanelOutils getOutilsJoueur() {
		return outilsJoueur;
	}

	public FenPengouin getFenPengouin() {
		return fenPengouin;
	}

	public void setFenPengouin(FenPengouin fenPengouin) {
		this.fenPengouin = fenPengouin;
	}
	public Tux getAnimateurJeu(){
		return this.animateurJeu;
	}
	
	public PanelOutils getPanelOutils(){
		return this.outilsJoueur;
	}

	public PanelJeu getPlateauDeJeu() {
		return plateauDeJeu;
	}

	public void setPlateauDeJeu(PanelJeu plateauDeJeu) {
		this.plateauDeJeu = plateauDeJeu;
	}
}//fin classe
