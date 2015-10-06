package maquettes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import liaison.Liaison;

@SuppressWarnings("serial")
public class Tux extends JPanel {
	//variable
	private BufferedImage imageNormal, imageVictoire, imageDefaite, tux, imageProf;
	private Graphics2D dessinTux;
	private boolean isCommentJouer,isBienvenue = true, isAlive;
//	private AvatarsJoueurs avatarTux;
	//constructeur

	/** Version avec Tux qui change :  4/5 trucs a changer */
	//public Tux(AvatarsJoueurs avatarTux)
	public Tux()
	{
//		this.avatarTux = avatarTux;
		this.setOpaque(false);
		//Chargement des images
		initImages();
		choixAvatar();

	}
	/**
	 * Initialise les images
	 */
	public void initImages()
	{
		try {
			/** Version avec Tux qui change :  5/5 trucs a changer 
			imageNormal = ImageIO.read(getClass().getResource("/Data/Images/Tux/"+ this.avatarTux.getAvatarJoueur().toString()+".png")); */
			
			imageNormal = ImageIO.read(getClass().getResource("/Data/Images/Tux/tux_normal.png"));
		 	imageVictoire = ImageIO.read(getClass().getResource("/Data/Images/Utile/Tux_Victoire.png"));
		 	imageDefaite = ImageIO.read(getClass().getResource("/Data/Images/Utile/Tux_Defaite.png"));
		 	imageProf = ImageIO.read(getClass().getResource("/Data/Help//Prof.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tux = imageNormal;
	}
	/**
	 * choix avatar suivant la selection du joueur (pour quand on mettra un panel de choix entre les avatars)
	 */
	private void choixAvatar(){
		
	}
	//actions
	public void paintComponent(Graphics g){
		this.dessinTux = (Graphics2D) g;
		super.paintComponent(g);
		if(!isCommentJouer){
			tux = imageNormal;
			this.dessinTux.drawImage(tux, 0, 0, null);
		}else{
			this.dessinTux.drawImage(tux, 0, 20, 180, 180, null);
		}
		repaint();
	}
	//actions affichant l'infobulle voulu
	public void parler(PanContexte panCtxt, Liaison liaison)
	{	
		PanelPresentation panelPresentation = new PanelPresentation(this,isBienvenue);
		
		if(liaison.getDonneesAff().isJeuFini())
		{
			
			//on change le tux suivant l'etat de fin de partie
			if(!liaison.getDonneesAff().tousOrdis() && liaison.getDonneesAff().getGagnant().estOrdi()){
				tux = imageDefaite;
			}else{
				tux = imageVictoire;
			}
			//on affiche la bulle de victoire
			panCtxt.add(new PanelVictoire(panCtxt.getFenPengouin().getFenetrePrincipale(),liaison, panCtxt.getFenPengouin()), 0);
		}else if(isCommentJouer){
				tux = imageProf;
				if(isBienvenue){
					isAlive = true;
					panCtxt.add(panelPresentation,0);
					isBienvenue = false;
				}else{
					panCtxt.add(panelPresentation,0);
				}
				repaint();			
		}else{
			tux = imageNormal;	
		}
	}
	/**
	 * @return the isCommentJouer
	 */
	public boolean isCommentJouer() {
		return isCommentJouer;
	}
	/**
	 * @param isCommentJouer the isCommentJouer to set
	 */
	public void setCommentJouer(boolean isCommentJouer) {
		this.isCommentJouer = isCommentJouer;
	}
	/**
	 * @return the isBienvenue
	 */
	public boolean isBienvenue() {
		return isBienvenue;
	}
	/**
	 * @param isBienvenue the isBienvenue to set
	 */
	public void setBienvenue(boolean isBienvenue) {
		this.isBienvenue = isBienvenue;
	}
	/**
	 * @return the isAlive
	 */
	public boolean isAlive() {
		return isAlive;
	}
	/**
	 * @param isAlive the isAlive to set
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
}
