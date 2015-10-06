/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 31/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :PanelBulle										*
* *******************************************************************
* Description : 													*
*Ici on gere la bulle contenant les question du profs				*
*********************************************************************/
package help;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ressourcesIHM.BoutonImage;

@SuppressWarnings("serial")
public class PanelBulle extends JPanel implements ActionListener {

	//variable
	private BufferedImage imageBulle, imageBulleDeroulement;
	private BoutonImage boutonRegle, boutonPrincipe, boutonDeroulement, boutonDeplacement, boutonPrendreTuile;
	private PanelAide panelAide;
	private boolean  isQuestionDeroulement = false, boutonDeroulementExiste= false;
	//constructeur
	public PanelBulle(PanelAide panAide){	
		this.panelAide = panAide;
		initComponent();
	}
	
	
	/**
	 *  Ajout de tous les elements de la FenOptions
	 */
	public void initComponent()
	{
		this.setBounds(0, 0, 434 , 198);
		this.setLayout(null);
		this.setOpaque(false);
		this.setVisible(true);
		ajoutBoutonIntroduction();
		ouvertureImage();
		
	}//fin initComponent
	
	/**
	 * ouverture des images de la fenetre aide.
	 */
	private void ouvertureImage(){
		try{
			imageBulle = ImageIO.read(getClass().getResource("/Data/Help/bulleMessageIntroduction.png"));	
			imageBulleDeroulement = ImageIO.read(getClass().getResource("/Data/Help/bulleMessageDeroulementPartie.png"));
		}catch(IOException e){
		}
	}//fin ouverture image
	
	public void paintComponent(Graphics g){
		//affichage de la bulle
		if(!isQuestionDeroulement){
			g.drawImage(imageBulle, 0, 0, 434 , 198,  null);
			
		}else{
			g.drawImage(imageBulleDeroulement, 0, 0, 434, 198, null);
			
		}
		this.repaint();		
	}
	/**
	 * ajout des boutons pour la fenetre d'introduction
	 */
	public void ajoutBoutonIntroduction(){
		//variable a modifier pour l'emplacement des boutons
		int emplacementLargeurBouton = 25, emplacementHauteurBouton = 90, hauteurBouton = 30, largeurBouton = 310;
		
		boutonPrincipe = new BoutonImage("Principe");
		boutonPrincipe.setLocation(emplacementLargeurBouton,emplacementHauteurBouton);
		boutonPrincipe.setSize(largeurBouton,hauteurBouton);
		boutonPrincipe.addActionListener(this);
		this.add(boutonPrincipe,0);
		
		boutonRegle = new BoutonImage("Regles");
		boutonRegle.setLocation(emplacementLargeurBouton,emplacementHauteurBouton + hauteurBouton);
		boutonRegle.setSize(largeurBouton,hauteurBouton);
		boutonRegle.addActionListener(this);
		this.add(boutonRegle,0);
		
		boutonDeroulement = new BoutonImage("DeroulementPartie");
		boutonDeroulement.setLocation(emplacementLargeurBouton,emplacementHauteurBouton + hauteurBouton *2);
		boutonDeroulement.setSize(largeurBouton,hauteurBouton);
		boutonDeroulement.addActionListener(this);
		this.add(boutonDeroulement,0);
	}
	/**
	 * methode qui enleve les boutons du menu intro de la fenetre
	 */
	public void enleverBoutonIntro(){
		this.remove(boutonPrincipe);
		this.remove(boutonRegle);
		this.remove(boutonDeroulement);
		this.repaint();
	}
	
	/**
	 * ajout des boutons pour la fenetre deroulement
	 */
	public void ajoutBoutonDeroulement(){
		boutonDeroulementExiste = true;
		//variable a modifier pour l'emplacement des boutons
		int emplacementLargeurBouton = 25, emplacementHauteurBouton = 110, hauteurBouton = 30, largeurBouton = 310;
		
		boutonDeplacement = new BoutonImage("DeplacerPingouin");
		boutonDeplacement.setLocation(emplacementLargeurBouton,emplacementHauteurBouton);
		boutonDeplacement.setSize(largeurBouton,hauteurBouton);
		boutonDeplacement.addActionListener(this);
		this.add(boutonDeplacement,0);
		
		boutonPrendreTuile = new BoutonImage("CollecterTuiles");
		boutonPrendreTuile.setLocation(emplacementLargeurBouton,emplacementHauteurBouton + hauteurBouton);
		boutonPrendreTuile.setSize(largeurBouton,hauteurBouton);
		boutonPrendreTuile.addActionListener(this);
		this.add(boutonPrendreTuile,0);
		
	}
	/**
	 * methode qui enleve les boutons du menu deroulement
	 */
	public void enleverBoutonDeroulement(){
		boutonDeroulementExiste = false;
		this.remove(boutonDeplacement);
		this.remove(boutonPrendreTuile);
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//action des boutons
		if(e.getSource() == boutonPrincipe){	//action pour le bouton principe
			setQuestionDeroulement(false);
			this.panelAide.getProfTux().afficherPrincipe();
			if(boutonDeroulementExiste){
				enleverBoutonDeroulement();
			}
			this.repaint();
		}
		else if(e.getSource() == boutonRegle){	//action pour le bouton regle
			setQuestionDeroulement(false);
			this.panelAide.getProfTux().afficherRegle();
			if(boutonDeroulementExiste){
				enleverBoutonDeroulement();
			}
			this.repaint();
		}
		else if(e.getSource() == boutonDeroulement){	//action pour le bouton deroulement
			setQuestionDeroulement(true);	//on dis que l'on demande la question deroulement et on change l'image a afficher
			if(boutonPrincipe != null){
				enleverBoutonIntro();
			}
			ajoutBoutonDeroulement();
			this.repaint();
		}
		else if(e.getSource() == boutonDeplacement){
			setQuestionDeroulement(false);
			if(boutonDeroulementExiste){
				enleverBoutonDeroulement();
			}
			ajoutBoutonIntroduction();
			this.panelAide.getProfTux().afficherDeplacement();
			this.repaint();
			
		}else if(e.getSource() == boutonPrendreTuile){
			setQuestionDeroulement(false);
			if(boutonDeroulementExiste){
				enleverBoutonDeroulement();
			}
			ajoutBoutonIntroduction();
			this.panelAide.getProfTux().afficherPrendreTuile();
			this.repaint();
			
		}
		
	}


	/**
	 * @return the imageBulle
	 */
	public BufferedImage getImageBulle() {
		return imageBulle;
	}


	/**
	 * @param imageBulle the imageBulle to set
	 */
	public void setImageBulle(BufferedImage imageBulle) {
		this.imageBulle = imageBulle;
	}
	
	
	/**
	 * @return the isQuestionDeroulement
	 */
	public boolean isQuestionDeroulement() {
		return isQuestionDeroulement;
	}
	/**
	 * @param isQuestionDeroulement the isQuestionDeroulement to set
	 */
	public void setQuestionDeroulement(boolean isQuestionDeroulement) {
		this.isQuestionDeroulement = isQuestionDeroulement;
	}
	
}
