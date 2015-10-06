/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 17/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :BoutonImage										*
* *******************************************************************
* Description : 													*
* Classe pour les boutons du jeu pour pouvoir gerer des images		*
*																	*
*********************************************************************/
//nom du package
package ressourcesIHM;

//nom des librairies utilisé
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

//Classe BoutonImage
@SuppressWarnings("serial")
public class BoutonImage extends JButton implements MouseListener{
	
	//variable
	Image imageBouton;
	Image normal, surbrillance, appuye, grise, normalMute, surbrillanceMute, appuyeMute;
	String name;
	boolean isEntered, isPressed, isPion, isSound, isActif;
	
	//constructeur pion
	public BoutonImage(String name, Point position, int width, int height, Image image){
		this.name = name;
		this.normal = image;
		this.initImagesPion();
		this.isPion = true;
		this.isSound = false;
		this.addMouseListener(this);
		this.isEntered = false; // Image de la tuile a 1 poisson
		//declaration de l'emplacement du bouton dans la fenetre
		super.setBounds((int)position.getX(), (int)position.getY(), width, height);
		this.setContentAreaFilled(false);
		this.setBorder(null);	//on enleve le cadre autour du bouton
	}
	
	// Constructeur Bouton avec appuie et surbrillance (plus grise si c'est Annuler, Refaire ou Retablir)
	public BoutonImage(String name){
		this.name = name;
		if(name.equals("Annuler") || name.equals("Retablir") || name.equals("Refaire"))
		{
			this.initImagesBouton(true);
//			this.setDisabledIcon(new ImageIcon( grise));
		}
		else
			this.initImagesBouton(false);
		this.isPion = false;
		this.isSound = false;
//		this.setIcon(new ImageIcon(normal));
//		this.setPressedIcon(new ImageIcon( appuye));
//		this.setRolloverIcon(new ImageIcon( surbrillance));
		this.addMouseListener(this);
		this.setContentAreaFilled(false);	//on permet d'avoir un fond transparent.
		this.setBorder(null);	//on enleve le cadre autour du bouton
	}
	
	/**
	 * Constructeur pour les boutons de sons
	 * @param name : nom du bouton pour les images
	 * @param actif : indique si le bouton est actif ou pas (pour les images des trucs barres)
	 */
	public BoutonImage(String name, boolean actif)
	{
		this.name = name;
		this.isActif = actif;
		this.isPion = false;
		this.isSound = true;
		this.initImagesSon();
		this.addMouseListener(this);
		this.setContentAreaFilled(false);	//on permet d'avoir un fond transparent.
		this.setBorder(null);	//on enleve le cadre autour du bouton
	}
	
	public BoutonImage(String name, Image image){
		this.name = name;
		this.imageBouton = image;	
		this.isPion = false;
		this.isSound = false;
		this.initImagesPion();
		this.addMouseListener(this);
		this.setContentAreaFilled(false);	//on permet d'avoir un fond transparent.
		this.setBorder(null);	//on enleve le cadre autour du bouton
	}
	
	public void initImagesPion()
	{
		try {
			this.surbrillance = ImageIO.read(getClass().getResource("/Data/Images/Plateau/Surbrillance_bouton.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialise les images des boutons
	 * @param doOuUndo : si le bouton est Annuler, Refaire ou Retablir
	 */
	public void initImagesBouton(boolean doOuUndo)
	{
		try {
			this.normal = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+".png"));
			this.surbrillance = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"_S.png"));
			this.appuye = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"_A.png"));
			if (doOuUndo)
				this.grise = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"_G.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialise les images des boutons de son
	 * @param doOuUndo : si le bouton est Annuler, Refaire ou Retablir
	 */
	public void initImagesSon()
	{
		try {
			this.normal = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+".png"));
			this.normalMute = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"Mute.png"));
			this.surbrillance = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"_S.png"));
			this.surbrillanceMute = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"Mute_S.png"));
			this.appuye = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"_A.png"));
			this.appuyeMute = ImageIO.read(getClass().getResource("/Data/Images/Boutons/bouton"+name+"Mute_A.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);

		if(isPion)
			g.drawImage(normal, 0, 0, this.getWidth(), this.getHeight(), null);
		else if(!isSound && this.isEnabled())
		{
			if(isPressed)
				g.drawImage(appuye, 0, 0, this.getWidth(), this.getHeight(), null);
			else if(isEntered)
			{
				g.drawImage(surbrillance, 0, 0, this.getWidth(), this.getHeight(), null);
			}
			else
				g.drawImage(normal, 0, 0, this.getWidth(), this.getHeight(), null);
			isEntered = false;
			isPressed = false;
		}
		else if (isSound)
		{
			if(isPressed)
			{
				if (isActif)
					g.drawImage(appuye, 0, 0, this.getWidth(), this.getHeight(), null);
				else
					g.drawImage(appuyeMute, 0, 0, this.getWidth(), this.getHeight(), null);
			}
			else if(isEntered)
			{
				if (isActif)
					g.drawImage(surbrillance, 0, 0, this.getWidth(), this.getHeight(), null);
				else
					g.drawImage(surbrillanceMute, 0, 0, this.getWidth(), this.getHeight(), null);
			}
			else
			{
				if (isActif)
					g.drawImage(normal, 0, 0, this.getWidth(), this.getHeight(), null);
				else
					g.drawImage(normalMute, 0, 0, this.getWidth(), this.getHeight(), null);
			}
			isEntered = false;
//			isPressed = false;
		}
		else
			g.drawImage(grise, 0, 0, this.getWidth(), this.getHeight(), null);
		
	}// Fin de paintComponent

	
	public void setImage(Image image){
		this.normal = image;
		repaint();
	}
	public Image getImage()
	{
		return this.normal;
	}

	//gestion des images lorsque la souris passe sur le bouton.
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		isEntered = true;
		this.repaint();
	}
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		this.repaint();
	}
	@Override
	public void mousePressed(MouseEvent arg0)
	{
		isPressed = true;
		this.repaint();
	}
	@Override
	public void mouseReleased(MouseEvent arg0)
	{

		isPressed = false;
		this.repaint();
	}

	public boolean isActif() {
		return isActif;
	}

	public void setActif(boolean isActif) {
		this.isActif = isActif;
	}

	

}//Fin de la classe
