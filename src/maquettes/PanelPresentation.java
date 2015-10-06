/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 05/06/2013	*
* 									*Date de modification :			*
* Nom de la classe : PanelVictoire									*
* *******************************************************************
* Description : 													*
* Panel pour dire le gagnant										*
*********************************************************************/
package maquettes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import ressourcesIHM.BoutonImage;

@SuppressWarnings("serial")
public class PanelPresentation extends JPanel implements ActionListener{
	
	
		//variable
		private JButton boutonCroix;
		private Image bulleBienvenue, bulleDeplacement;
		private boolean isBulleBienvenue;
		private Tux tux;
		//constructeur
		public PanelPresentation(Tux tux, boolean isBulleBienvenue){
			this.tux = tux;
			this.isBulleBienvenue = isBulleBienvenue;
			initComponent();
		}//fin constructeur
	
		//ouverture image
		public void ouvertureImages(){
			//ouverture des images des boutons
			try{
				bulleBienvenue = ImageIO.read(getClass().getResource("/Data/Images/CommentJouer/bulleImageBienvenue.png"));
				bulleDeplacement = ImageIO.read(getClass().getResource("/Data/Images/CommentJouer/bulleImageDeplacement.png"));
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
			ajoutBoutons();
		    this.setVisible(true);
	  	}//fin initComponent
	  
	  //paint component
  		public void paintComponent(Graphics g){
  			super.paintComponent(g);
  			if(tux.isAlive()){
	  			if(isBulleBienvenue){
	  				g.drawImage(bulleBienvenue, 0, -15, null);
	  				tux.setBienvenue(false);
	  			}else{
	  				g.drawImage(bulleDeplacement, 0, -15, null);
	  				tux.setBienvenue(true);
	  			}
  			}else{
  				boutonCroix.setVisible(false);
  			}
  		}
  	
	  	@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == boutonCroix){
				if(!isBulleBienvenue){
					tux.setAlive(false);
					tux.setCommentJouer(false);
				}
				this.setVisible(false);
				this.repaint();
			}
		}//fin action performed

	  	//ajouts des boutons
	  	private void ajoutBoutons(){
	  		//bouton
		    boutonCroix = new BoutonImage("Croix");
		    boutonCroix.addActionListener(this);
		    boutonCroix.setSize(31,31);
		    boutonCroix.setLocation(380,5);
		    this.add(boutonCroix);
	  	}//fin ajout bouton

		/**
		 * @return the isBulleBienvenue
		 */
		public boolean isBulleBienvenue() {
			return isBulleBienvenue;
		}

		/**
		 * @param isBulleBienvenue the isBulleBienvenue to set
		 */
		public void setBulleBienvenue(boolean isBulleBienvenue) {
			this.isBulleBienvenue = isBulleBienvenue;
		}
	  	
}//fin classe
