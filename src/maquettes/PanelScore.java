/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 20/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :PanelScore										*
* *******************************************************************
* Description : 													*
* Panneau d'affichage des scores des joueurs						*
*********************************************************************/
package maquettes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import liaison.Liaison;
import moteur.Joueur;

@SuppressWarnings("serial")
public class PanelScore extends JPanel{
	
	//variable
	private JLabel[] labelJoueur = new JLabel[4];
	private JLabel[] pointPoisson = new JLabel[4];
	private JLabel[] pointTuiles = new JLabel[4];
	private Image imagePoisson, imageTuile, surbrillanceScore;
	int largeurDepart = 5, hauteurDepart = 25, espaceEntreLabel = 10;
	private Liaison liaison;
	//constructeur
	public PanelScore(Liaison liaison){
//		System.out.println("[PANEL SCORE]");
		this.setOpaque(false); //on permet de voir le fond du panneau contexte
		this.setLayout(null);
		this.liaison = liaison;
		this.setSize(210, liaison.getDonneesAff().getNbJoueurs() * 60 + 20);
		// Ajout de tous les elements du panelScore
		this.initComponent();
	}//fin constructeur

	/**
	 *  Ajout de tous les elements du panelScore
	 */
	public void initComponent()
	{
		for(int i = 0 ; i < liaison.getDonneesAff().getNbJoueurs() ; i++)
		{
			Font font = null;
			try {
				font = liaison.getDonneesAff().getFont(18);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ajout des label Joueur
			labelJoueur[i] = new JLabel();
			labelJoueur[i].setText(liaison.getDonneesAff().getJoueurs()[i].getNom() + " : ");
			labelJoueur[i].setBounds(largeurDepart,hauteurDepart - 8, this.getWidth() - largeurDepart + espaceEntreLabel, 30);
			labelJoueur[i].setForeground(Color.white);
			labelJoueur[i].setFont(font);
			this.add(labelJoueur[i]);
			//ajout des labels des points Poisson
			pointPoisson[i] = new JLabel();
			pointPoisson[i].setText("" + liaison.getDonneesAff().getJoueurs()[i].getNombrePoissons());
			pointPoisson[i].setBounds(40, hauteurDepart + 15,25,30);
			pointPoisson[i].setForeground(Color.white);
			pointPoisson[i].setFont(font);
			this.add(pointPoisson[i]);
			//ajout des labels des points Tuile
			pointTuiles[i] = new JLabel();
			pointTuiles[i].setText("" + liaison.getDonneesAff().getJoueurs()[i].getNombreTuiles());
			pointTuiles[i].setBounds(115, hauteurDepart + 15,25,30);
			pointTuiles[i].setForeground(Color.white);
			pointTuiles[i].setFont(font);
			this.add(pointTuiles[i]);
			hauteurDepart = hauteurDepart + 55;
			ouvertureImage();
		
		}
	}
	
	public void setScore(int numJoueurCourant, Joueur joueurCourant)
	{
//		System.out.println("[PanelScore] Score joueur "+(numJoueurCourant+1)+" : " + joueurCourant.getNombrePoissons());
		this.pointPoisson[numJoueurCourant].setText(""+joueurCourant.getNombrePoissons());
		this.pointTuiles[numJoueurCourant].setText(""+joueurCourant.getNombreTuiles());
		
	}
	
	public void setLabelJoueur(String nomJoueur, int numeroJoueur){
		labelJoueur[numeroJoueur].setText(nomJoueur + " : ");
	}
	
	private void ouvertureImage(){
		try{
			 imagePoisson = ImageIO.read(getClass().getResource("/Data/Images/Utile/poisson.png"));
			 imageTuile = ImageIO.read(getClass().getResource("/Data/Images/Utile/tuile.png"));
			 surbrillanceScore = ImageIO.read(getClass().getResource("/Data/Images/Plateau/Surbrillance_score.png"));
			}catch(IOException e){
		}
	}
	public void paintComponent(Graphics g){
//		System.out.println("[panel score] paint component");
		super.paintComponent(g);
		g.drawImage(surbrillanceScore, 3, 25 + 55 * liaison.getDonnees().getJoueurCourant(), null);
		hauteurDepart = 25;
		for(int i = 0; i < liaison.getDonneesAff().getNbJoueurs(); i++){
			Image imageBouton = liaison.getAvatars()[i].getImageAvatarJoueur();
			g.drawImage(imageBouton,155, hauteurDepart, 50, 50, null);
			g.drawImage(imagePoisson,largeurDepart, hauteurDepart + 15, 30, 34, null);
			g.drawImage(imageTuile, 75, hauteurDepart + 15, 30, 35, null);
			
			hauteurDepart = hauteurDepart + 55;
		}
	}

}//fin classe
