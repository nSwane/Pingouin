package maquettes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelBackground extends JPanel{
	
	//variable
	Image fondEcran;
	Graphics2D dessin;
	//constructeur
	public PanelBackground(){
		// Charge les images de fond
		this.initImages();
	}//fin constructeur
	
	/**
	 * Charge les images de fond
	 */
	public void initImages()
	{
		//ouverture d'image
		try{
			fondEcran = ImageIO.read(getClass().getResource("/Data/Images/Fond/bgTest.jpg"));
		}catch(IOException e){
			System.out.println("[PanelBackground.initImages()] Erreur lors du chargement des images de fond");
		}
	}
	
	public void paintComponent(Graphics g){
		this.dessin = (Graphics2D) g;
		super.paintComponent(g);
		this.dessin.drawImage(fondEcran, 0, 0,  this.getWidth(), this.getHeight(), null);
	}

}//fin classe
