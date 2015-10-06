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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import liaison.DonneesPartagees;
import liaison.Liaison;
import moteur.Joueur;
import ressourcesIHM.BoutonImage;
import Son.BanqueDeSon;

@SuppressWarnings("serial")
public class PanelPion extends JPanel implements ActionListener{
	
	//variable
	private Image[][] imagePion;

	private Liaison liaison;
	private int nbJoueurs, nbPingouins;
	@SuppressWarnings("unused")
	private int iPingSelect, jPingSelect;
	private int largeurBouton = 50, hauteurBouton = 50;
	private int hauteurDepart = 25, espaceEntreLabel = 10;
	private BoutonImage[][] boutonPion;
	private JLabel[] nomJoueurs;
	private boolean isSelected;

	private Image surbrillance;

	//constructeur
	public PanelPion(Liaison liaison){
		// Recuperation des parametres et initialisation des constantes
		this.liaison = liaison;
		this.isSelected = true;
		this.nbJoueurs = liaison.getDonneesAff().getNbJoueurs();
			// nbPingouins vaut 4 s'il y a 2 joueurs, 3 si 3 joueurs, 2 si 4 joueurs, 0 si y'a un probleme
			nbPingouins = (nbJoueurs==2) ? 4 : ((nbJoueurs==3) ? 3 : ((nbJoueurs==4) ? 2 : 0));
		this.imagePion = new Image[nbJoueurs][nbPingouins];
		this.boutonPion = new BoutonImage[nbJoueurs][nbPingouins];
		
		// Options de la fenetre
		this.configurationFenetre();
		// Charge toutes les images des pions
		this.initImages();
		// Ajout de tous les elements de la FenCharger
		this.initComponent();
		
	}//fin constructeur

	/**
	 *  Ajout de tous les elements de la FenCharger
	 */
	public void initComponent()
	{
		DonneesPartagees donnees = liaison.getDonneesAff();
		int nbPingPlaces, nbPingMax;
		//mise en place du nom des joueurs
		nomJoueurs = new JLabel[nbJoueurs];
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(18);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mise en place des images
		int largeurImage, hauteurImage = 40, largeurNom = 10, hauteurNom = 20;
		for(int i = 0 ; i < nbJoueurs ; i++)
		{
			hauteurImage = hauteurNom + espaceEntreLabel *2;
			nomJoueurs[i] = new JLabel();
			nomJoueurs[i].setFont(font);
			nomJoueurs[i].setForeground(Color.white);
			nomJoueurs[i].setText(donnees.getJoueurs()[i].getNom() + " : ");
			nomJoueurs[i].setBounds(largeurNom, hauteurNom, this.getWidth(), 25);
			this.add(nomJoueurs[i]);
			nbPingPlaces = donnees.getJoueurs()[i].getNbPingouins();
			nbPingMax = donnees.getJoueurs()[i].getMaxPingouins();
			largeurImage = espaceEntreLabel / 2;
			
			for(int j = 0 ; j < nbPingMax ; j++)
			{
				// Recuperation du nom de l'avatar du joueur i
				String nomJoueur = liaison.getAvatars()[i].getAvatarJoueur().toString();
				Image imageBouton = liaison.getAvatars()[i].getImageAvatarJoueur();
				//ajout des bouton
				boutonPion[i][j] = new BoutonImage(nomJoueur, new Point(largeurImage, hauteurImage), largeurBouton, hauteurBouton, imageBouton);
//				boutonPion[i][j].setBounds(largeurImage, hauteurImage, largeurBouton, hauteurBouton);
				boutonPion[i][j].addActionListener(this);
				if (j < nbPingPlaces)
					boutonPion[i][j].setVisible(false);
				this.add(boutonPion[i][j]);
				largeurImage = largeurImage + 50;
			}
			hauteurNom = hauteurNom + (espaceEntreLabel * 3 + 40);
			
			
		}//fin ouverture des images
	}
	/**
	 * Charge toutes les images des pions
	 */
	public void initImages()
	{
		for(int i = 0 ; i < liaison.getDonneesAff().getNbJoueurs() ; i++){
			String nomJoueur = liaison.getAvatars()[i].getAvatarJoueur().toString();
			for(int j = 0 ; j < nbPingouins ; j++){
				//ouverture des images
				try{
					imagePion[i][j] = ImageIO.read(getClass().getResource("/Data/Images/Pions/" + nomJoueur + ".png"));
					surbrillance = ImageIO.read(getClass().getResource("/Data/Images/Plateau/Surbrillance_bouton.png"));
				}catch(IOException e){
					System.out.println("[PanelPion.initImages()] Erreur lors de l'ouverture de l'image de Tux T_T");
				}
			}
		}//fin ouverture des images
	}
	
	/**
	 * Applique les parametres de la fenetre
	 */
	public void configurationFenetre()
	{
		this.setOpaque(false); //on permet de voir le fond du panneau contexte
		this.setLayout(null);
		this.setSize(210, nbJoueurs * 60 + hauteurDepart + espaceEntreLabel * nbJoueurs);
		this.setVisible(true);
		
	}
	
	//methode de dessin des pions
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int nbJoueurs = liaison.getDonneesAff().getNbJoueurs();
		int maxPingouins = liaison.getDonneesAff().getMaxPingouins();
		Joueur joueurs[] = liaison.getDonneesAff().getJoueurs();
		boolean isAffiche;
		for (int i = 0; i < nbJoueurs; i++) 
		{
			for (int j = 0; j < maxPingouins; j++) 
			{
				if (boutonPion[i][j].isSelected())
				{
//					System.out.println("bouton ("+i+","+j+") est selectionne ? "+ boutonPion[i][j].isSelected());
					Rectangle r = boutonPion[i][j].getBounds();
					g.drawImage(surbrillance, r.x, r.y, r.width, r.height, null);
				}
				isAffiche = j >= joueurs[i].getNbPingouins();
				boutonPion[i][j].setVisible(isAffiche);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int numJoueurCourant = liaison.getDonneesAff().getJoueurCourant();
		Joueur joueurCourant = liaison.getDonneesAff().getJoueurs()[numJoueurCourant];
		for (int i = 0; i < this.nbJoueurs; i++) {
			for (int j = 0; j < this.nbPingouins; j++) {
				if (e.getSource() == boutonPion[i][j] && !joueurCourant.estOrdi() &&
					!isSelected && numJoueurCourant == i)
				{
					if (liaison.getDonneesAff().isSpeakerActif())
						BanqueDeSon.SELECT.jouerSon(false);
					this.isSelected = true;
					this.iPingSelect = i;
					this.jPingSelect = j;
					this.boutonPion[i][j].setSelected(true);
					this.repaint();
//					this.boutonPion[i][j].setVisible(false);
				}
			}
		}
		
	}
	
	public void resetSelected ()
	{
		int nbJoueurs = liaison.getDonneesAff().getNbJoueurs();
		int maxPingouins = liaison.getDonneesAff().getMaxPingouins();
		
		for (int i = 0; i < nbJoueurs; i++) 
		{
			for (int j = 0; j < maxPingouins; j++) 
			{
//				System.out.println("bouton ("+i+","+j+") reset");
				boutonPion[i][j].setSelected(false);
//				System.out.println("bouton ("+i+","+j+") est selectionne ? " + boutonPion[i][j].isSelected());
			}
		}this.repaint();
	}
	
	public Image[][] getImagePion() {
		return imagePion;
	}

	public void setImagePion(Image[][] imagePion) {
		this.imagePion = imagePion;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public BoutonImage[][] getBoutonPion() {
		return boutonPion;
	}

	public void setBoutonPion(BoutonImage[][] boutonPion) {
		this.boutonPion = boutonPion;
	}
	public void setNomJoueurs(String nomJoueur, int numeroJoueur){
		nomJoueurs[numeroJoueur].setText(nomJoueur + " : ");
	}
	
}//fin classe
