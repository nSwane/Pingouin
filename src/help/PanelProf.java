/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 31/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :PanelProf										*
* *******************************************************************
* Description : 													*
*Ici on gere les actions lorsque l'on clique sur le prof			*
*********************************************************************/
package help;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelProf extends JPanel implements ActionListener {

	//variables
	private BufferedImage imageProf;
	private Graphics2D dessinProf;
	private JButton boutonProf;
	private boolean afficherBulle = false;
	private PanelAide panAide;
	private PanelBulle bulleQuestionProf;
	//consructeur
	public PanelProf(PanelAide panAide, PanelBulle bulleQuestionProf){
		this.panAide = panAide;
		this.bulleQuestionProf = bulleQuestionProf;
		initComponent();
	}
	
	private void initComponent(){
		ouvertureImage();
		this.setOpaque(false);
		this.setLayout(null);
		boutonProf = new JButton("");
		boutonProf.setLocation(0,0);
		boutonProf.setSize(204,204);
		boutonProf.setContentAreaFilled(false);
		boutonProf.setBorderPainted(false);
		boutonProf.addActionListener(this);
		this.add(boutonProf);
	}
	
	/**
	 * ouverture des images de la fenetre aide.
	 */
	private void ouvertureImage(){
		try{
			imageProf = ImageIO.read(getClass().getResource("/Data/Help/Prof.png"));
			}catch(IOException e){
		}
	}//fin ouverture image
	
	public void paintComponent(Graphics g){
		this.dessinProf = (Graphics2D) g;
		this.dessinProf.drawImage(imageProf, 0, 0, 204, 204, null);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == boutonProf){	
			if(afficherBulle){
				this.afficherBulle = false;
				this.panAide.remove(bulleQuestionProf);
			}else{
				this.afficherBulle = true;
				//this.bulleQuestionProf.ajoutBoutonIntroduction();
				this.panAide.add(bulleQuestionProf,0);	
			}
			//Refresh des images
			this.bulleQuestionProf.repaint();
			this.panAide.repaint();
		}
		
		
	}//fin action performed

	/**
	 * fonction pour afficher les principes de jeu
	 */
	public void afficherPrincipe(){
		//on enleve tout ce qui correspond a la premiere fenetre
		afficherBulle = false;
		this.panAide.remove(this.bulleQuestionProf);
		//on affiche ce dont on a besoin
		this.panAide.setQuestionPrincipe(true);
		this.panAide.setQuestionRegles(false);
		this.panAide.setQuestionDeplacement(false);
		this.panAide.setQuestionPrendreTuile(false);
		this.repaint();
		this.panAide.repaint();
		this.bulleQuestionProf.repaint();
	}
	/**
	 * fonction pour afficher les Regles du jeu
	 */
	public void afficherRegle(){
		afficherBulle = false;
		this.panAide.remove(this.bulleQuestionProf);
		this.panAide.setQuestionPrincipe(false);
		this.panAide.setQuestionRegles(true);
		this.panAide.setQuestionDeplacement(false);
		this.panAide.setQuestionPrendreTuile(false);
		this.repaint();
		this.panAide.repaint();
		this.bulleQuestionProf.repaint();
	}
	
	/**
	 * fonction pour afficher les Regles du jeu
	 */
	public void afficherDeplacement(){
		afficherBulle = false;
		this.panAide.remove(this.bulleQuestionProf);
		this.panAide.setQuestionPrincipe(false);
		this.panAide.setQuestionRegles(false);
		this.panAide.setQuestionDeplacement(true);
		this.panAide.setQuestionPrendreTuile(false);
		this.repaint();
		this.panAide.repaint();
		this.bulleQuestionProf.repaint();
	}
	/**
	 * fonction pour afficher les Regles du jeu
	 */
	public void afficherPrendreTuile(){
		afficherBulle = false;
		this.panAide.remove(this.bulleQuestionProf);
		this.panAide.setQuestionPrincipe(false);
		this.panAide.setQuestionRegles(false);
		this.panAide.setQuestionDeplacement(false);
		this.panAide.setQuestionPrendreTuile(true);
		this.repaint();
		this.panAide.repaint();
		this.bulleQuestionProf.repaint();
	}

	/**
	 * @return the afficherBulle
	 */
	public boolean isAfficherBulle() {
		return afficherBulle;
	}

	/**
	 * @param afficherBulle the afficherBulle to set
	 */
	public void setAfficherBulle(boolean afficherBulle) {
		this.afficherBulle = afficherBulle;
	}
	

}//fin classe
