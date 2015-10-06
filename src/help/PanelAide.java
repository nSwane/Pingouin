/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 31/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :PanelAide										*
* *******************************************************************
* Description : 													*
*Ici on gere les images a afficher dans la fenetre d'aide			*
*********************************************************************/
package help;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelAide extends JPanel{
	
	//variable
	private BufferedImage imageAccueil, imagePrincipe, imageDeplacement, imagePrendreTuile, imageRegles;
	private Graphics2D dessin;
	private int hauteur = 690, largeur = 820;
	private boolean isQuestionPrincipe = false, isQuestionRegles = false, isQuestionDeplacement = false, isQuestionPrendreTuile = false;
	private PanelProf profTux;
	private PanelBulle bulleQuestionProf;
	//constructeur
	public PanelAide(){
		this.setLayout(null);
		initComponent();
	}
	/**
	 *  Ajout de tous les elements de la FenOptions
	 */
	public void initComponent()
	{
		this.setBounds(0, 0,largeur, hauteur);
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground( new Color(60, 120, 192));
		this.setVisible(true);
		ouvertureImage();
		ouvertureProf();
	}//fin initComponent
	
	/**
	 * ouverture des images de la fenetre aide.
	 */
	private void ouvertureImage(){
		try{
			imageAccueil = ImageIO.read(getClass().getResource("/Data/Help/Cours_Accueil.png"));
			imagePrincipe = ImageIO.read(getClass().getResource("/Data/Help/CoursPrincipe.png"));
			imageRegles = ImageIO.read(getClass().getResource("/Data/Help/CoursRegles.png"));
			imageDeplacement = ImageIO.read(getClass().getResource("/Data/Help/CoursDeplacerPingouin.png"));
			imagePrendreTuile = ImageIO.read(getClass().getResource("/Data/Help/CoursPrendreTuile.png"));
			}catch(IOException e){
		}
	}//fin ouverture image
	private void ouvertureProf(){
		//on cree la bulle pour les questions
		this.bulleQuestionProf = new PanelBulle(this);
		this.bulleQuestionProf.setLocation(240, 420);
		this.bulleQuestionProf.setSize(434 , 198);
		//on cree le prof
		profTux = new PanelProf(this, this.bulleQuestionProf);
		profTux.setLocation(616, 476);
		profTux.setSize(204, 204);
		this.add(profTux);
	}
	/** 
	 * paintComponent permet d'afficher les images suivant le cas voulu par l'utilisateur
	 */
	public void paintComponent(Graphics g){
		this.dessin = (Graphics2D) g;
		super.paintComponents(this.dessin);
		if(isQuestionPrincipe){	// affiche l'image des Principes du jeu
			this.dessin.drawImage(imagePrincipe, 0, 0, 814, 675, null);
		}else if(isQuestionRegles){	//affiche l'image des regles du jeu
			this.dessin.drawImage(imageRegles, 0, 0, 814, 675, null);
		}else if(isQuestionDeplacement){	//affiche l'image des deplacement du jeu
			this.dessin.drawImage(imageDeplacement, 0, 0, 814, 675, null);
		}else if(isQuestionPrendreTuile){	//affiche l'image de comment on prend les tuiles
			this.dessin.drawImage(imagePrendreTuile, 0, 0, 814, 675, null);
		}else{	//images par defaut pour l'accueil
			this.dessin.drawImage(imageAccueil, 0, 0, 814, 675, null);
		}	
		
	}

	//getter et setter
	/**
	 * @return the isQuestionPrincipe
	 */
	public boolean isQuestionPrincipe() {
		return isQuestionPrincipe;
	}
	/**
	 * @param isQuestionPrincipe the isQuestionPrincipe to set
	 */
	public void setQuestionPrincipe(boolean isQuestionPrincipe) {
		this.isQuestionPrincipe = isQuestionPrincipe;
	}
	/**
	 * @return the isQuestionRegles
	 */
	public boolean isQuestionRegles() {
		return isQuestionRegles;
	}
	/**
	 * @param isQuestionRegles the isQuestionRegles to set
	 */
	public void setQuestionRegles(boolean isQuestionRegles) {
		this.isQuestionRegles = isQuestionRegles;
	}
	/**
	 * @return the profTux
	 */
	public PanelProf getProfTux() {
		return profTux;
	}
	/**
	 * @param profTux the profTux to set
	 */
	public void setProfTux(PanelProf profTux) {
		this.profTux = profTux;
	}
	/**
	 * @return the isQuestionDeplacement
	 */
	public boolean isQuestionDeplacement() {
		return isQuestionDeplacement;
	}
	/**
	 * @param isQuestionDeplacement the isQuestionDeplacement to set
	 */
	public void setQuestionDeplacement(boolean isQuestionDeplacement) {
		this.isQuestionDeplacement = isQuestionDeplacement;
	}
	/**
	 * @return the isQuestionPrendreTuile
	 */
	public boolean isQuestionPrendreTuile() {
		return isQuestionPrendreTuile;
	}
	/**
	 * @param isQuestionPrendreTuile the isQuestionPrendreTuile to set
	 */
	public void setQuestionPrendreTuile(boolean isQuestionPrendreTuile) {
		this.isQuestionPrendreTuile = isQuestionPrendreTuile;
	}

	
}
