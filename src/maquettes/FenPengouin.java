	/********************************************************************
	* Nom du codeur : Michael OMER		*Date de création : 17/05/2013	*
	* 									*Date de modification :			*
	* Nom de la classe :FenPengouin										*
	* *******************************************************************
	* Description : 													*
	* Fenetre principale du dans lequels tous les panels s'affichent	*
	*********************************************************************/

package maquettes;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import ressourcesIHM.EcouteurActionMenu;
import dialog.FenDialogConfirmationQuitter;


@SuppressWarnings("serial") //enleve le warning concernant la serialisation
public class FenPengouin extends JFrame implements Runnable, WindowListener{

	//variable 
	private static final int hauteur = 740, largeur = 1024;
	private JFrame fenetrePrincipale;
	public JMenuBar menuPrincipal;
	public JMenuItem partieRapide,partiePerso, sauvegarder, charger, menu, quitter, regle, commentJouer, conseilCoup;
	private JMenu jeu, aide;
	private PanelDepart panDepart;
	
		
	//Constructeur de la fenetre au lancement du jeu
	public void run() {
		//initialisation de la fenetre principale
		fenetrePrincipale = new JFrame("Pingouins - La banquise fond!");	//creation fenetre avec son titre		
		this.setLayout(null);
		
		//ajout des composants de la fenetre lors du lancement du jeu
		panDepart = new PanelDepart(fenetrePrincipale, this);
		fenetrePrincipale.add(panDepart);
		fenetrePrincipale.setSize(largeur, hauteur);						//Taille de la fenetre
		fenetrePrincipale.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );	// option lors de la fermeture de la fenetre avec la croix
		fenetrePrincipale.setResizable(false);								//on empeche le redimensionnement de la fenetre
		fenetrePrincipale.addWindowListener(this);
		
		//fenetre visible
		fenetrePrincipale.setVisible(true);									//la fenetre est visible
		
	}
	
	

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new FenPengouin());						//lancement de l'application
	}
	
	
	public void ajoutMenuPrincipal(EcouteurActionMenu ecouteurMenuJeu){
		//creation de la barre de menu
		menuPrincipal = new JMenuBar();
		//creation de l'ecouteur pour le menu
		//menu jeu
		jeu = new JMenu("Jeu");
		partieRapide = new JMenuItem("Partie rapide");
		partieRapide.addActionListener(ecouteurMenuJeu);
		partiePerso = new JMenuItem("Partie personnalisée");
		partiePerso.addActionListener(ecouteurMenuJeu);
		sauvegarder = new JMenuItem("Sauvegarder");
		sauvegarder.addActionListener(ecouteurMenuJeu);
		charger = new JMenuItem("Charger");
		charger.addActionListener(ecouteurMenuJeu);
		menu = new JMenuItem("Menu Principal");
		menu.addActionListener(ecouteurMenuJeu);
		quitter = new JMenuItem("Quitter");
		quitter.addActionListener(ecouteurMenuJeu);
		//ajout au menu jeu
		jeu.add(partieRapide);
		jeu.add(partiePerso);
		jeu.add(sauvegarder);
		jeu.add(charger);
		jeu.add(menu);
		jeu.add(quitter);
		//menu Aide
		aide = new JMenu("Aide");
		regle = new JMenuItem("Règles du jeu");
		regle.addActionListener(ecouteurMenuJeu);
//		commentJouer = new JMenuItem("Comment jouer?");
//		commentJouer.addActionListener(ecouteurMenuJeu);
		conseilCoup = new JMenuItem("Demander indice");
		conseilCoup.addActionListener(ecouteurMenuJeu);
		//ajout au menu aide
		aide.add(regle);
//		aide.add(commentJouer);
		aide.add(conseilCoup);
		//ajout a la barre du menu
		menuPrincipal.add(jeu);
		menuPrincipal.add(aide);
		fenetrePrincipale.setJMenuBar(menuPrincipal);
		fenetrePrincipale.getJMenuBar().setSize(this.WIDTH, 40);
	}//fin creation du menu principal

	//fonction getteur
	public JMenuItem getPartieRapide(){
		return this.partieRapide;
	}
	public JMenuItem getQuitter(){
		return this.quitter;
	}
	
	//methode permettant de retourner l'information concernant la fenetre principale
	public JFrame getFenetrePrincipale(){
		return this.fenetrePrincipale;
	}
	public JMenuItem getRegle(){
		return this.regle;
	}
	public JMenuItem getCharger(){
		return this.charger;
	}
	public JMenuItem getSauvegarder(){
		return this.sauvegarder;
	}
	/**
	 * @return the panDepart
	 */
	public PanelDepart getPanDepart() {
		return panDepart;
	}

	/**
	 * @param panDepart the panDepart to set
	 */
	public void setPanDepart(PanelDepart panDepart) {
		this.panDepart = panDepart;
	}
	
	/**
	 * @return the partiePerso
	 */
	public JMenuItem getPartiePerso() {
		return partiePerso;
	}

	/**
	 * @param partiePerso the partiePerso to set
	 */
	public void setPartiePerso(JMenuItem partiePerso) {
		this.partiePerso = partiePerso;
	}

	public JMenuItem getMenu() {
		return menu;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	//action lorsque l'on veux fermer la fenetre
	@Override
	public void windowClosing(WindowEvent e) {
		// Recuperation du boolean d'affichage des pop up de confirmation
		FileInputStream f;
		boolean optionAffichageConfirmation = false;
		try {
			//Ouverture du fichier
			f = new FileInputStream("options/preferences");
			Scanner s = new Scanner(f);
			// nombre de pingouins
			int nbJoueurs = s.nextInt();
			// noms joueurs
			for(int i=0; i<nbJoueurs; i++)
			{
				s.next();
				s.next();
				s.next();
			}
			s.next();
			// Affichage Pop up
			optionAffichageConfirmation = Boolean.valueOf(s.next());
			s.close();
	        f.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Impossible d'ouvrir le fichier. Verifier que le fichier existe et que le chemin est correcte");
			System.exit(0);
		} catch (IOException e1) {
			System.out.println("il s'est produit une erreur lors de la fermeture du fichier.");
			System.exit(0);
		}
		if(!panDepart.isEstOuvert() && e.getSource() == fenetrePrincipale && optionAffichageConfirmation)
		{
			new FenDialogConfirmationQuitter(this,"Confirmation quitter", true);
		}else{
			System.exit(0);
		}
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}



	public JMenuItem getConseilCoup() {
		return conseilCoup;
	}



	public void setConseilCoup(JMenuItem conseilCoup) {
		this.conseilCoup = conseilCoup;
	}
}//fin classe
