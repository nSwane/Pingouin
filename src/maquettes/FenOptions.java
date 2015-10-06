/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 20/05/2013	*
* 									*Date de modification :21/05/13	*
* Nom de la classe :FenOutils										*
* *******************************************************************
* Description : 													*
* Fenetre contenant les options du jeu								*
*********************************************************************/
package maquettes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import liaison.Liaison;
import ressourcesIHM.AvatarsJoueurs;
import ressourcesIHM.AvatarsJoueurs.Avatar;
import ressourcesIHM.BoutonImage;

@SuppressWarnings("serial")
public class FenOptions extends JFrame implements ActionListener, WindowListener{
	
	//variable
	private int hauteur = 590, largeur = 600,  largeurTitre = largeur / 4, espace = 10, hauteurElement = 80;
	private int emplacementHauteurObjet;
	private BoutonImage boutonValider, boutonAnnuler;
	private Liaison liaison;
	private FenPengouin fenPengouin;
	private PanSelectionJoueur[] panSelectionJoueur;
	private JLabel titreFenetre, optionAffichageAccessible, optionAffichageConfirmation, optionVitesseAnim,optionCommentJouer;
	private JPanel pan;
	private JCheckBox checkBoxCasesAccessibles, checkBoxAffichageConfirmation, checkBoxOptionCommentJouer;
	private JScrollBar barreVitesseAnimation;
	private PanContexte panCtxt;
	
	//constructeur
	public FenOptions(PanContexte panCtxt, FenPengouin fenPengouin, Liaison liaison){
		// Recuperation des parametres
		this.fenPengouin = fenPengouin;
		this.liaison = liaison;
		this.panCtxt = panCtxt;
		// Options de la fenetre
		this.configurationFenetre();
		// Ajout de tous les elements de la FenOptions
		this.initComponent();
			
		
	}//fin constructeur
	
	/**
	 *  Ajout de tous les elements de la FenOptions
	 */
	public void initComponent()
	{
		this.pan = new JPanel();
		this.pan.setBounds(0, 0,largeur, hauteur);
		this.pan.setBackground( new Color(60, 120, 192));
		this.pan.setLayout(null);
		//ajout des composants dans la fenetre options
		titreFenetre = new JLabel();
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(24);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		titreFenetre.setFont(font);
		titreFenetre.setForeground(Color.white);
		titreFenetre.setText("Options :");
		titreFenetre.setLocation(((largeur / 2 - largeurTitre / 4) ), 0);
		titreFenetre.setSize(largeurTitre, hauteurElement/2);
		pan.add(titreFenetre);
		//affichage des panel joueur pour les joueurs de la partie en cours
		affichageJoueur();
		//affichage des options concernant le gameplay
		affichageOptionsJeu();
		//affichage des options concernant les animations
		affichageOptionsAnimation();
		//Affichage des boutons valider et annuler
		affichageBoutons();
		getRootPane().setDefaultButton(boutonValider); 
		getRootPane().requestFocus(); 
		this.add(pan);
	}

	/**
	 * Parametres de configuration de la fenetre
	 */
	private void configurationFenetre()
	{
		this.setLayout(null);
		this.setSize(largeur, hauteur);							//Taille de la fenetre
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );	// option lors de la fermeture de la fenetre avec la croix
		this.setResizable(false);								//on empeche le redimensionnement de la fenetre
		this.setUndecorated(false);								//on enleve les contours de la fenetre
		this.setLocationRelativeTo(fenPengouin);
		this.setLocation(200, 100);
		this.addWindowListener(this);
		//fenetre visible
		this.setVisible(true);
		
	}//fin configuration de le fenetre
	
	private void affichageJoueur(){
		
		
		//initialisation de la taille du tableau de selection Joueur suivant le nombre de joueur actuelle dans la partie
		panSelectionJoueur = new PanSelectionJoueur[liaison.getDonnees().getNbJoueurs()];
		emplacementHauteurObjet = hauteur / 13;
		
		//ajout des panel de selectionJoueur au moment voulu
		for(int i = 0; i < liaison.getDonnees().getNbJoueurs(); i++)
		{
			panSelectionJoueur[i] = new PanSelectionJoueur(liaison, i, true);
			panSelectionJoueur[i].setLocation(espace, emplacementHauteurObjet);
			panSelectionJoueur[i].setOpaque(false);
			
			//chargement des informations dans les panSelectionJoueur
			panSelectionJoueur[i].setNomJoueur(liaison.getDonneesAff().getJoueurs()[i].getNom());
			panSelectionJoueur[i].setImageBoutonAvatarJoueur(liaison.getAvatars()[i].getImageAvatarJoueur());
			panSelectionJoueur[i].setTypeJoueur(liaison.getDonneesAff().getJoueurs()[i].getDifficulte());
			//rajouter pour afficher la difficulte choisi
			this.pan.add(panSelectionJoueur[i]);
			emplacementHauteurObjet = emplacementHauteurObjet + ((espace + hauteurElement));
			
		}
		
	}//fin affichage des panels joueur
	
	//affichage des options du jeu
	private void affichageOptionsJeu(){
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(14);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//ajout du label pour l'option
		optionAffichageAccessible = new JLabel(); 
		optionAffichageAccessible.setFont(font);
		optionAffichageAccessible.setForeground(Color.white);
		optionAffichageAccessible.setBounds(espace + 30, emplacementHauteurObjet + espace * 2,largeurTitre + (largeurTitre * 1)  , hauteurElement / 2);
		optionAffichageAccessible.setText("Afficher les cases accessibles");
		this.pan.add(optionAffichageAccessible);
		
		//ajout de la checkbox
		checkBoxCasesAccessibles = new JCheckBox();
		checkBoxCasesAccessibles.setBounds(espace, emplacementHauteurObjet + espace * 2 + espace /2, 30,30);
		checkBoxCasesAccessibles.setSelected(liaison.getDonneesAff().isOptionCasesAccessibles()); //on met dans l'etat dont l'option cases accessibles ce trouve.
		checkBoxCasesAccessibles.setOpaque(false);
		this.pan.add(checkBoxCasesAccessibles);
		
		//ajout du label pour l'option
		optionAffichageConfirmation = new JLabel(); 
		optionAffichageConfirmation.setFont(font);
		optionAffichageConfirmation.setForeground(Color.white);
		optionAffichageConfirmation.setBounds(espace + 30, emplacementHauteurObjet + espace * 4,largeurTitre + (largeurTitre * 1)  , hauteurElement / 2);
		optionAffichageConfirmation.setText("Afficher les messages de confirmation");
		this.pan.add(optionAffichageConfirmation);
		
		//ajout de la checkbox
		checkBoxAffichageConfirmation = new JCheckBox();
		checkBoxAffichageConfirmation.setBounds(espace, emplacementHauteurObjet + espace * 4 + espace /2, 30,30);
		checkBoxAffichageConfirmation.setSelected(liaison.getDonneesAff().isOptionAffichageConfirmation()); //on met dans l'etat dont l'option aff pop up se trouve.
		checkBoxAffichageConfirmation.setOpaque(false);
		this.pan.add(checkBoxAffichageConfirmation);
		
		//ajout du label pour l'option
		optionCommentJouer = new JLabel(); 
		optionCommentJouer.setFont(font);
		optionCommentJouer.setForeground(Color.white);
		optionCommentJouer.setBounds(espace+ 30 , emplacementHauteurObjet + espace * 6,largeurTitre + (largeurTitre * 1)  , hauteurElement / 2);
		optionCommentJouer.setText("Afficher les bulles d'information");
		this.pan.add(optionCommentJouer);
		
		//ajout de la checkbox
		checkBoxOptionCommentJouer = new JCheckBox();
		checkBoxOptionCommentJouer.setBounds(espace, emplacementHauteurObjet + espace * 6 + espace /2,30,30);
		checkBoxOptionCommentJouer.setSelected(liaison.getDonneesAff().isOptionCommentJouer()); //on met dans l'etat dont l'option aff pop up se trouve.
		checkBoxOptionCommentJouer.setOpaque(false);
		this.pan.add(checkBoxOptionCommentJouer);
		
	}//fin affichage optionsJeu
	//affichage des options concernant les animations
	private void affichageOptionsAnimation(){
		optionVitesseAnim = new JLabel();
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(14);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		optionVitesseAnim.setFont(font);
		optionVitesseAnim.setForeground(Color.white);
		optionVitesseAnim.setSize(largeurTitre*2, hauteurElement / 2);
		optionVitesseAnim.setLocation(espace,emplacementHauteurObjet + espace *9);
		optionVitesseAnim.setText("Vitesse de l'animation");
		this.pan.add(optionVitesseAnim);
		//affichage de la barre de selection de la vitesse d'animation
		barreVitesseAnimation = new JScrollBar(JScrollBar.HORIZONTAL);
		barreVitesseAnimation.setMinimum(0);
		barreVitesseAnimation.setMaximum(100);
		barreVitesseAnimation.setValue(liaison.getDonneesAff().getVitesseAnim());
		barreVitesseAnimation.setBounds(espace, emplacementHauteurObjet + espace *12 , hauteurElement *2, hauteurElement / 4);
		barreVitesseAnimation.setOpaque(false);
		this.pan.add(barreVitesseAnimation);
	}//fin affichage option animation

	//affichage des boutons valider et annuler
	private void affichageBoutons()
	{
		boutonValider = new BoutonImage("Valider");
		boutonValider.setBounds(largeur - (200 + espace *4), (hauteur / 2) - (hauteurElement + espace/2), 200, hauteurElement / 2 );
		boutonValider.addActionListener(this);
		boutonValider.setMnemonic(KeyEvent.VK_ENTER); //ajout d'un Mnemonic de clavier pour que lorsque l'on tape entree on effectue valider
		this.pan.add(boutonValider);
		boutonAnnuler = new BoutonImage("Retour");
		boutonAnnuler.setBounds(largeur - (200 + espace * 4), (hauteur / 2) + espace / 2, 200, hauteurElement / 2 );
		boutonAnnuler.addActionListener(this);
		this.pan.add(boutonAnnuler);
	}//fin affichage des boutons
	
	//fonction paint
	public void paintComponent(Graphics g){
		//super.paintComponents(g);
	}//fin paintcomponent
	
	@Override
	//fonction pour les actions des boutons
	public void actionPerformed(ActionEvent e)
	{
		//on sauvegarde les informations modifier lorsque l'on clique sur le bouton valider et on ferme la fenetre
		if(e.getSource() == boutonValider)
		{
			String[] nomJoueur = new String[liaison.getDonneesAff().getNbJoueurs()];
			int[] difficulte = new int[liaison.getDonneesAff().getNbJoueurs()];
			Avatar[] avatarJoueur = new Avatar[liaison.getDonneesAff().getNbJoueurs()];
			// On recupere tous les noms/difficultes/avatars des joueurs pour les passer a liaison
			for(int i = 0; i < liaison.getDonneesAff().getNbJoueurs(); i++){
				nomJoueur[i] = new String(panSelectionJoueur[i].getNomJoueur());
				difficulte[i] = panSelectionJoueur[i].getTypeJoueur();
				avatarJoueur[i] = liaison.getAvatars()[i].getAvatarJoueur();
				// On change immediatement le nom de tous les joueurs dans le panel Score
				if(this.panCtxt.getScoreJoueur() != null){
					this.panCtxt.getScoreJoueur().setLabelJoueur(nomJoueur[i], i);
				}
				// De meme pour le panel Pion
				if(this.panCtxt.getPionJoueur() != null){
					// On rafraichit l'affichage des noms
					this.panCtxt.getPionJoueur().setNomJoueurs(nomJoueur[i], i);
					// On recupere le tableau des pions
					BoutonImage[][] bP = this.panCtxt.getPionJoueur().getBoutonPion();
					// On change l'image des 4 pions pour chaque joueur
					for (int j = 0; j < liaison.getDonneesAff().getJoueurs()[i].getMaxPingouins(); j++) {
						bP[i][j].setImage(liaison.getAvatars()[i].getImageAvatarJoueur());
					}
					this.panCtxt.repaint();
					// On reaffecte le tableau des pions
					this.panCtxt.getPionJoueur().setBoutonPion(bP);
				}
			}
			//donne l'information concernant l'option des CasesAccessibles visibles ou non
			boolean optionCasesAccessibles = this.checkBoxCasesAccessibles.isSelected(); 
			//donne l'information concernant l'affichage des pop up
			boolean optionAffichageConfirmation = this.checkBoxAffichageConfirmation.isSelected(); 
			
			boolean optionCommentJouer = this.checkBoxOptionCommentJouer.isSelected();
			int vitesseAnim = barreVitesseAnimation.getValue();
			liaison.changerOption(nomJoueur,difficulte, avatarJoueur, optionCasesAccessibles, optionAffichageConfirmation, vitesseAnim, optionCommentJouer);
			// A l'ouverture de la FenOptions, le moteur a ete declare comme actif pour ne pas prendre en compte
			// de nouvelles requetes. Quand on quitte la fenetre, on met donc le moteur en inactif et on rappelle l'Observer
			// pour qu'il relance sa requete.
			liaison.setMoteurActif(false);
			liaison.notifierMiseAJour();
			this.dispose(); //on ferme la fenetre
		}
		//on ferme la fenetre sans rien faire si on appuie sur annuler
		else if(e.getSource() == boutonAnnuler)
		{
			AvatarsJoueurs[] avatarsJoueurs = liaison.getAvatars();
			for (int i = 0; i < liaison.getDonneesAff().getNbJoueurs(); i++)
			{
				avatarsJoueurs[i].setNumAvatar(liaison.getDonneesAff().getJoueurs()[i].getNumAvatar());
			}
			// A l'ouverture de la FenOptions, le moteur a ete declare comme actif pour ne pas prendre en compte
			// de nouvelles requetes. Quand on quitte la fenetre, on met donc le moteur en inactif et on rappelle l'Observer
			// pour qu'il relance sa requete.
			liaison.setMoteurActif(false);
			liaison.changerOption(null, null, null, liaison.getDonneesAff().isOptionCasesAccessibles(), liaison.getDonneesAff().isOptionAffichageConfirmation(), liaison.getDonneesAff().getVitesseAnim(), liaison.getDonneesAff().isOptionCommentJouer());
			liaison.notifierMiseAJour();
			this.dispose();	//on ferme la fenetre
		}
	}//fin action performed

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
		if(arg0.getSource() == this){
			AvatarsJoueurs[] avatarsJoueurs = liaison.getAvatars();
			for (int i = 0; i < liaison.getDonneesAff().getNbJoueurs(); i++)
			{
				avatarsJoueurs[i].setNumAvatar(liaison.getDonneesAff().getJoueurs()[i].getNumAvatar());
			}
			this.panCtxt.getPanelOutils().setFenOptionOuverte(false); // on autorise a reouvrir des fenOptions.
			// A l'ouverture de la FenOptions, le moteur a ete declare comme actif pour ne pas prendre en compte
			// de nouvelles requetes. Quand on quitte la fenetre, on met donc le moteur en inactif et on rappelle l'Observer
			// pour qu'il relance sa requete.
			liaison.setMoteurActif(false);
			liaison.changerOption(null, null, null, liaison.getDonneesAff().isOptionCasesAccessibles(), liaison.getDonneesAff().isOptionAffichageConfirmation(), liaison.getDonneesAff().getVitesseAnim(), liaison.getDonneesAff().isOptionCommentJouer());
			liaison.notifierMiseAJour();
		}
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}

	
}//fin classe
