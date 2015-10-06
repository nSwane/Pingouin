package maquettes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import liaison.Liaison;
import ressourcesIHM.AvatarsJoueurs;
import ressourcesIHM.BoutonImage;

@SuppressWarnings("serial")
public class PanSelectionJoueur extends JPanel implements ActionListener {

	//variable
	private JTextField nomJoueur;
	private JComboBox<String> typeJoueur;
	private final static int hauteurPan = 80, largeurPan = 270, espacement = 10, tailleImage = 60;
	private final static int tailleTexteField = 130, tailleComboBox = 130, largeurElement = 25, tailleNomMax = 20;
	private BoutonImage  boutonAvatarJoueur, boutonSupprimer;
	private Liaison liaison;
	private AvatarsJoueurs avatarJoueur, avatarTmp;
	private int numJoueur;
	private PanelPartiePerso panPartiePerso;
	//constructeur

	public PanSelectionJoueur(Liaison liaison, int numJoueur,boolean isOption){
		this.liaison = liaison;
		this.numJoueur = numJoueur;
		configurationFenetre();		
		//affichage de l'avatar du joueur
		affichageAvatar();
		//affichage nom joueur
		affichageNomJoueur();
		//affichage du comboBox avec les type de Joueur
		affichageTypeJoueur();
		//affichage du bouton supprimer si est seulement si on a plus de 2 joueurs
		if(!isOption){
			affichageBoutonSupprimer();
		}
	}//fin constructeur pour les options
	public PanSelectionJoueur(Liaison liaison, int numJoueur,boolean isOption, PanelPartiePerso panPartiePerso){
		this.liaison = liaison;
		this.panPartiePerso = panPartiePerso;
		this.numJoueur = numJoueur;
		configurationFenetre();		
		//affichage de l'avatar du joueur
		affichageAvatar();
		//affichage nom joueur
		affichageNomJoueur();
		//affichage du comboBox avec les type de Joueur
		affichageTypeJoueur();
		//affichage du bouton supprimer si est seulement si on a plus de 2 joueurs
		if(!isOption){
			affichageBoutonSupprimer();
		}
	}//fin constructeur pour les options
	/**
	 * Parametres de configuration de la fenetre
	 */
	private void configurationFenetre()
	{
		this.setLayout(null);
		this.setSize(largeurPan, hauteurPan);
		this.setOpaque(true);
		this.setBackground(Color.white);
	}
	private void affichageAvatar(){ 
		avatarJoueur = liaison.getAvatars()[numJoueur];
		avatarTmp = new AvatarsJoueurs(avatarJoueur.getNumeroJoueur(), avatarJoueur.getNumAvatar(), liaison);
		this.boutonAvatarJoueur = new BoutonImage("",new Point(espacement,espacement),tailleImage,tailleImage,avatarJoueur.getImageAvatarJoueur());
		this.boutonAvatarJoueur.addActionListener(this);
		this.add(boutonAvatarJoueur);
	}//fin affichage de l'avatar
	
	private void affichageNomJoueur(){
		nomJoueur = new JTextField();
		nomJoueur.setBounds((espacement * 2) + tailleImage, espacement, tailleTexteField, largeurElement);
		nomJoueur.setEditable(true);
		nomJoueur.setSelectionEnd(tailleNomMax);
		this.add(nomJoueur);
	}
	//affichage du type de joueur
	private void affichageTypeJoueur(){
		typeJoueur = new JComboBox<String>();
		typeJoueur.setBackground(Color.white);
		typeJoueur.addItem("Humain");
		typeJoueur.addItem("Ordinateur - Facile");
		typeJoueur.addItem("Ordinateur - Moyen");
		typeJoueur.addItem("Ordinateur - Difficile");
		typeJoueur.setEditable(false);
		typeJoueur.setBounds((espacement * 2) + tailleImage ,(espacement * 2) + 25  ,tailleComboBox,largeurElement);
		this.add(typeJoueur);
	}//fin affichage de la comboBox
	
	//fonction d'affichage du bouton supprimer lorsque on le desire
	private void affichageBoutonSupprimer(){
		
		boutonSupprimer = new BoutonImage("Croix");
		boutonSupprimer.setBounds((espacement * 3) + tailleImage + tailleTexteField, espacement ,31,31);
		boutonSupprimer.addActionListener(this);
		this.add(boutonSupprimer);
	}//fin fonction affichage bouton supprimer
	
	//fonction de dessin de la fenetre
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		typeJoueur.validate();
		
	}// fin fonction de dessin de la fenetre
	
	//fonction d'action sur les elements de la fenetre
	public void actionPerformed(ActionEvent e) {
		//action lors du clic sur le bouton supprimer
		
		if(e.getSource() == this.boutonSupprimer){
			panPartiePerso.getPanJoueur().remove(this);
			this.setVisible(false);
			//liaison.getDonneesAff().setNbJoueurs(liaison.getDonneesAff().getNbJoueurs()-1);
			panPartiePerso.setNbJoueurs(panPartiePerso.getNbJoueurs() - 1);
			panPartiePerso.setBoutonAjouterJoueur(numJoueur);
			panPartiePerso.repaint();
		}
		else if(e.getSource() == this.boutonAvatarJoueur){
			// Il faut que l'on cree un avatar temporaire parce que la on change directement dans 
			// celui du joueur, donc si on appuie sur annuler, l'avatar est quand meme change !
			// Ou alors qu'on se base sur celui qui est dans liaison
			avatarJoueur.next();
			this.boutonAvatarJoueur.setImage(avatarJoueur.getImageAvatarJoueur());
		}
	}//fin gestion des actions des boutons
	
	//getter setter des elements de la fenetre
	/**
	 * @return the avatarJoueur
	 */
	public BoutonImage getBoutonAvatarJoueur() {
		return boutonAvatarJoueur;
	}
	/**
	 * @param avatarJoueur the avatarJoueur to set
	 */
	public void setImageBoutonAvatarJoueur(Image avatarJoueur) {
		this.boutonAvatarJoueur.setImage(avatarJoueur);
	}
	/**
	 * @return the nomJoueur
	 */
	public String getNomJoueur() {
		return nomJoueur.getText();
	}
	/**
	 * @param nomJoueur the nomJoueur to set
	 */
	public void setNomJoueur(String nomJoueur) {
		this.nomJoueur.setText(nomJoueur);
	}
	/**
	 * @return the typeJoueur
	 */
	public int getTypeJoueur() {
		return typeJoueur.getSelectedIndex() ;
	}
	/**
	 * @param typeJoueur the typeJoueur to set
	 */
	public void setTypeJoueur(int typeJoueur) {
		this.typeJoueur.setSelectedIndex(typeJoueur);
	}
	/**
	 * @return the boutonSupprimer
	 */
	public BoutonImage getBoutonSupprimer() {
		return boutonSupprimer;
	}
	/**
	 * @param boutonSupprimer the boutonSupprimer to set
	 */
	public void setBoutonSupprimer(BoutonImage boutonSupprimer) {
		this.boutonSupprimer = boutonSupprimer;
	}
	public void isBoutonSupprimerVisible(boolean visible){
		this.boutonSupprimer.setVisible(visible);
		repaint();
	}
	//fin des getters setters
}//fin classe
