/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 27/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :FenAide											*
* *******************************************************************
* Description : 													*
* Fenetre contenant les Règles du jeu								*
*********************************************************************/
package help;

import javax.swing.JFrame;

import maquettes.FenPengouin;

@SuppressWarnings("serial")
public class FenAide extends JFrame{
	//variable
	private FenPengouin fenPengouin;
	private PanelAide panAide;
	private int hauteur = 690, largeur = 820;
	//contructeur
	public FenAide(FenPengouin fenPengouin){
		this.fenPengouin = fenPengouin;
		configurationFenetre();
		initComponent();
	}//fin constructeur
	/**
	 * Parametres de configuration de la fenetre
	 */
	private void configurationFenetre()
	{
		this.setTitle("Aide");
		this.setLayout(null);
		this.setSize(largeur, hauteur);							//Taille de la fenetre
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// option lors de la fermeture de la fenetre avec la croix
		this.setResizable(false);		//on empeche le redimensionnement de la fenetre
		this.setUndecorated(false);	
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(fenPengouin);
//		this.setLocation(200, 100);
		//fenetre visible
		this.setVisible(true);	
	}//fin configuration de le fenetre	
	/**
	 * initialisation du contenu de la fenetre
	 */
	private void initComponent(){
		panAide = new PanelAide();
		this.add(panAide);
		
	}//fin initComponent
}//fin classe
