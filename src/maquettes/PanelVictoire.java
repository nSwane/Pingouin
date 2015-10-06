/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 27/05/2013	*
* 									*Date de modification :			*
* Nom de la classe : PanelVictoire									*
* *******************************************************************
* Description : 													*
* Panel pour dire le gagnan											*
*********************************************************************/
package maquettes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import liaison.Liaison;
import liaison.ObserveurMoteur;
import ressourcesIHM.BoutonImage;

@SuppressWarnings("serial")
public class PanelVictoire extends JPanel implements ActionListener{
	
	
		//variable
		private JFrame mainFen;
		private Liaison liaison;
		private JLabel labelMessage1, labelMessage2;
		private JButton boutonRejouer , boutonQuitter, boutonCroix;
		private Image bulleMessage;
		private FenPengouin fenPengouin;
		//constructeur
		public PanelVictoire(JFrame mainFen,Liaison liaison, FenPengouin fenPengouin){
			this.mainFen = mainFen;
			this.liaison = liaison;
			this.fenPengouin = fenPengouin;
			initComponent();
		}//fin constructeur
	
		//ouverture image
		public void ouvertureImages(){
			//ouverture des images des boutons
			try{
				bulleMessage = ImageIO.read(getClass().getResource("/Data/Images/Utile/bulleMessage.png"));
			}catch(IOException e){
			}
		}//fin ouverture Image
		//debut initComponent
		private void initComponent()
		{
	  
			this.setSize(420, 170);
			this.setLocation(150,520);
			this.setOpaque(false);
			//Panel Victoire
		    ouvertureImages();
			this.setLayout(null);
			
			if(!liaison.getDonneesAff().tousOrdis() && liaison.getDonneesAff().getGagnant().estOrdi()){
				VousAvezPerdu();
			}else{
				VousAvezGagne();
			}
			
			ajoutBoutons();
		    this.setVisible(true);
	  	}//fin initComponent
	  
	  //paint component
  		public void paintComponent(Graphics g){
  			super.paintComponent(g);
  			g.drawImage(bulleMessage, 0, 0, null);
  			if(liaison.getDonnees().getGagnant() != null)
  				g.drawImage(liaison.getAvatars()[liaison.getDonnees().getGagnant().getNumero()].getImageAvatarJoueur(), 85, 30, null);
  			
  		}
  	
	  	@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == boutonRejouer)
			{
				// Initialisation de liaison
				liaison = new Liaison(false,liaison.getDonneesAff().isPartiePerso());
				
				// Changement de panel vers PanContexte
				mainFen.getContentPane().removeAll();
				PanContexte panCtxt= new PanContexte(fenPengouin, liaison); 
				mainFen.add(panCtxt);
				mainFen.setVisible(true);
				
				// On lance un nouveau moteur
				liaison.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
			}
			else if(e.getSource() == boutonQuitter){
				if(!liaison.isEnLigne()){
					System.exit(0);	//on quitte le jeu
				}else{
					liaison.quitter();
					mainFen.getContentPane().removeAll();
					PanelDepart panDepart= new PanelDepart(mainFen,fenPengouin); 
					mainFen.add(panDepart);
					mainFen.setVisible(true);
				}
			}else if(e.getSource() == boutonCroix){
				this.setVisible(false);
				this.repaint();
			}
		}//fin action performed
	  	
	  	//methode suivant les gagnants humain.
	  	private void VousAvezGagne(){
	  		labelMessage1 = new JLabel();
			Font font = null;
			try {
				font = liaison.getDonneesAff().getFont(24);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			labelMessage1.setForeground(new Color(60, 120, 192));
			labelMessage1.setFont(font);
			labelMessage1.setText("Bravo " + liaison.getDonneesAff().getGagnant().getNom());
			labelMessage1.setLocation(this.getWidth() / 2,30);
			labelMessage1.setSize(this.getWidth() / 2, 30);
			this.add(labelMessage1);
			labelMessage2 = new JLabel();
			labelMessage2.setForeground(new Color(60, 120, 192));
			labelMessage2.setFont(font);
			labelMessage2.setText( "a gagné !");
			labelMessage2.setLocation((this.getWidth() * 3 / 4) - 50,45);
			labelMessage2.setSize(this.getWidth() * 1 / 4, 60);
			this.add(labelMessage2);
	  	}//fin vous avez gagne
	  	//le joueur joue contre l'ordi est a perdu
	  	private void VousAvezPerdu(){
	  		labelMessage1 = new JLabel();
			Font font = null;
			try {
				font = liaison.getDonneesAff().getFont(24);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			labelMessage1.setForeground(new Color(60, 120, 192));
			labelMessage1.setFont(font);
			labelMessage1.setText("Désolé vous avez");
			labelMessage1.setLocation(this.getWidth() / 2, 20);
			labelMessage1.setSize(this.getWidth() / 2, 30);
			this.add(labelMessage1);
			labelMessage2 = new JLabel();
			labelMessage2.setForeground(new Color(60, 120, 192));
			labelMessage2.setFont(font);
			labelMessage2.setText( "perdu!");
			labelMessage2.setLocation(this.getWidth() * 3 / 4 - 50,35);
			labelMessage2.setSize(this.getWidth() * 1 / 4 , 60);
			this.add(labelMessage2);
	  	}//fin vous avez perdu
	  	//ajouts des boutons
	  	private void ajoutBoutons(){
	  		//bouton
	  		if(!liaison.isEnLigne()){
			    boutonRejouer = new BoutonImage("Rejouer");
			    boutonRejouer.setLocation(20,120);
			    boutonRejouer.setSize(200, 40);
			    boutonRejouer.addActionListener(this);
			    this.add(boutonRejouer);
	  		}
		    boutonQuitter = new BoutonImage("Quitter");
		    boutonQuitter.setLocation(220,120);
		    boutonQuitter.setSize(200,40);
		    boutonQuitter.addActionListener(this);
		    boutonCroix = new BoutonImage("Croix");
		    boutonCroix.addActionListener(this);
		    boutonCroix.setSize(31,31);
		    boutonCroix.setLocation(380,5);
		    this.add(boutonQuitter);
		    this.add(boutonCroix);
	  	}//fin ajout bouton
}//fin classe
