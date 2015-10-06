/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 30/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :PanePartiePerso									*
* *******************************************************************
* Description : 													*
* Panel pour choisir sa partie personnalise							*
*********************************************************************/
package maquettes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import liaison.Liaison;
import liaison.ObserveurMoteur;
import moteur.Joueur;
import moteur.Terrain;
import ressourcesIHM.BoutonImage;

@SuppressWarnings("serial")
public class PanelPartiePerso extends JPanel implements ActionListener{
	//variables
	private JFrame mainFen;
	private FenPengouin fenPengouin;
	private Liaison liaison;
	private JPanel pan, panJoueur;
	private JLabel titreFenetre;
	private BoutonImage boutonValider, boutonAnnuler;
	private BoutonImage[] boutonAjouterJoueur;
	int indexCreation; //correspond a la fenetre qui ouvre ce panel si 0 c'est panel depart sinon c'est lorsque l'on joue
	private int hauteur = 740, largeur = 1024,  largeurTitre = largeur / 2, espace = 10, hauteurElement = 80;
	private final static int hauteurPan = 80, largeurPan = 270;
	private int nbJoueurs = 2,vitesseAnim;
	private PanSelectionJoueur[] panSelectionJoueur;
	private PanContexte panCtxt;
	private ObserveurMoteur obM;
	// tableau des joueurs
	private Joueur[] joueurs;
	private Boolean optionCasesAccessibles, optionAffichageConfirmation,optionCommentJouer;
	//constructeur
		public PanelPartiePerso(JFrame mainFen, FenPengouin fenPengouin,int indexCreation, Liaison liaison){
			this.mainFen = mainFen;
			this.fenPengouin = fenPengouin;
			this.liaison = liaison;
			this.indexCreation = indexCreation;
			panJoueur = new JPanel();
			//initialisation des fenetres
			initFenetre();
			//initialisation des composants
			initComponent();
			
		}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == boutonValider){
			// Changement de panel vers PanContexte
			reecrireFichierOption();
			liaison.quitter();
			mainFen.getContentPane().removeAll();
			liaison = new Liaison(false,true);
			liaison.getDonnees().lireDonneesPartagees();
			PanContexte panCtxt = new PanContexte(fenPengouin, liaison);
			this.fenPengouin.getPanDepart().setEstOuvert(false);
			mainFen.add(panCtxt);//a modifier
			mainFen.setVisible(true);
			// Declaration de l'observer
			liaison.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
		}
		else if(e.getSource() == boutonAnnuler){
//				if(indexCreation == 0){
					mainFen.getContentPane().removeAll();
					this.mainFen.add(new PanelDepart(this.mainFen, this.fenPengouin));
					this.mainFen.repaint();
//				}else{				
//					boolean isPartiePerso = liaison.getDonneesAff().isPartiePerso();
//					liaison.quitter();
//					liaison.deleteObservers();
//					liaison = new Liaison(false,isPartiePerso);
//					this.panCtxt = new PanContexte(fenPengouin, liaison);
//					liaison.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
//					// Changement de panel vers PanContexte
//					mainFen.getContentPane().removeAll();
//					mainFen.add(this.panCtxt);
//					mainFen.setVisible(true);
//					liaison.charger("sauvegardeTemporaire");
//				}
			
		}
		//action pour enlever les boutons d'ajout et de rajouter un panel de jeu
		else if(e.getSource() == boutonAjouterJoueur[0]){
			panJoueur.add(panSelectionJoueur[2]);
			panSelectionJoueur[2].setVisible(true);
			panJoueur.remove(boutonAjouterJoueur[0]);
			System.out.println(boutonAjouterJoueur[1].isVisible());
			if(boutonAjouterJoueur[1].isVisible()){
				panJoueur.add(boutonAjouterJoueur[1]);
				boutonAjouterJoueur[1].setVisible(true);
			}
			if(!boutonAjouterJoueur[1].isVisible() && !panSelectionJoueur[3].isVisible()){
				panJoueur.add(boutonAjouterJoueur[1]);
				boutonAjouterJoueur[1].setVisible(true);
			}
			nbJoueurs++;
			repaint();
		}else if(e.getSource() == boutonAjouterJoueur[1]){
			panJoueur.add(panSelectionJoueur[3]);
			panSelectionJoueur[3].setVisible(true);
			panJoueur.remove(boutonAjouterJoueur[1]);
			boutonAjouterJoueur[1].setVisible(false);
			nbJoueurs++;
			repaint();
		}
	}
	
	private void initFenetre(){
		this.setLayout(null);
		this.setSize(largeur, hauteur);							//Taille de la fenetre
		this.setLocation(0, 0);
		//fenetre visible
		this.setVisible(true);
	}

	private void initComponent(){
		this.pan = new JPanel();
		this.pan.setBounds(0, 0,largeur, hauteur);
		this.pan.setBackground( new Color(60, 120, 192));
		this.pan.setLayout(null);
		//ajout des composants dans la fenetre options
		titreFenetre = new JLabel();
		try {
			titreFenetre.setFont(liaison.getDonneesAff().getFont(24));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		titreFenetre.setForeground(Color.white);
		titreFenetre.setText("Sélectionnez le nombre de joueurs :");
		titreFenetre.setLocation(((largeur / 2 - largeurTitre / 3) ), 0);
		titreFenetre.setSize(largeurTitre, hauteurElement/2);
		pan.add(titreFenetre);
		//on ouvre le fichier d'options tel qu'il est
		ouvertureFichier();
		//on quitte la liaison actuelle
		liaison.quitter();
		//on reecrit le fichier d'options avec les modif de creation pour la page et eviter une erreur car il manque des joueurs
		reecrireFichierOption();
		//on recree une nouvelle liaison
		liaison = new Liaison(false,true);
		affichageBoutons();
		affichageJoueurs();	
		this.add(pan);
	}
	
	/**
	 * affichage des joueurs suivant la decision des joueurs
	 */
	private void affichageJoueurs(){
		panJoueur.setLayout(null);
		panJoueur.setLocation(largeur / 6, hauteur / 6);
		panJoueur.setSize(largeurPan + espace * 2, (hauteurPan + espace) * 4 + 25);
		panJoueur.setBackground(new Color(60, 120, 192));
		boutonAjouterJoueur = new BoutonImage[2];
		try {
			panJoueur.setBorder(BorderFactory.createTitledBorder(null, "Configuration Joueurs : ", 1, 1, liaison.getDonneesAff().getFont(18), Color.white));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	//creation de la bordure autour du score des joueurs
		panSelectionJoueur = new PanSelectionJoueur[4];
		int hauteurPlacement = 25;
		
		for(int i = 0; i <= nbJoueurs; i++){
			panSelectionJoueur[i] = new PanSelectionJoueur(liaison, i, false, this);
			panSelectionJoueur[i].setLocation(espace, hauteurPlacement);
			panSelectionJoueur[i].setSize(largeurPan, hauteurPan);
			panSelectionJoueur[i].setOpaque(false);
			//chargement des informations dans les panSelectionJoueur
			panSelectionJoueur[i].setNomJoueur(joueurs[i].getNom());
			panSelectionJoueur[i].setImageBoutonAvatarJoueur(liaison.getAvatars()[i].getImageAvatarJoueur());
			panSelectionJoueur[i].setTypeJoueur(joueurs[i].getDifficulte());
			//rajouter pour afficher la difficulte choisi
			panJoueur.add(panSelectionJoueur[i]);
			
			//on place les boutons enlever joueurs
			if(i == 2){
				boutonAjouterJoueur[0] = new BoutonImage("Ajouter");
				boutonAjouterJoueur[0].setLocation(espace, hauteurPlacement);
				boutonAjouterJoueur[0].setSize(270, 80);
				boutonAjouterJoueur[0].addActionListener(this);
				panJoueur.add(boutonAjouterJoueur[0]);
				panJoueur.remove(panSelectionJoueur[2]);
				nbJoueurs--;
			}
			if(i == 3){
				boutonAjouterJoueur[1] = new BoutonImage("Ajouter");
				boutonAjouterJoueur[1].setLocation(espace, hauteurPlacement);
				boutonAjouterJoueur[1].setSize(270, 80);
				boutonAjouterJoueur[1].addActionListener(this);
//				panJoueur.add(boutonAjouterJoueur[1]);
				panJoueur.remove(panSelectionJoueur[3]);
				nbJoueurs--;
			}
//			System.out.println(nbJoueurs);
			hauteurPlacement = hauteurPlacement + ((espace + hauteurElement));
		}
		
		//on cache pour l'esthetique et surtout pour eviter que l'on supprime les deux premiers joueurs qui doivent toujours rester en place
		panSelectionJoueur[0].isBoutonSupprimerVisible(false);
		panSelectionJoueur[1].isBoutonSupprimerVisible(false);
		
		this.add(panJoueur);
		this.repaint();
		panJoueur.repaint();
	}
	
	/**
	 * affichage des boutons de la fenetre
	 */
	private void affichageBoutons(){
		boutonValider = new BoutonImage("Valider");
		boutonValider.setBounds(largeur - (200 + espace *4), (hauteur / 2) - (hauteurElement + espace/2), 200, hauteurElement / 2 );
		boutonValider.addActionListener(this);
		boutonValider.setMnemonic(KeyEvent.VK_ENTER); //ajout d'un Mnemonic de clavier pour que lorsque l'on tape entree on effectue valider
		this.pan.add(boutonValider);
		boutonAnnuler = new BoutonImage("Annuler");
		boutonAnnuler.setBounds(largeur - (200 + espace * 4), (hauteur / 2) + espace / 2, 200, hauteurElement / 2 );
		boutonAnnuler.addActionListener(this);
		this.pan.add(boutonAnnuler);
	}

	/**
	 * @return the nbJoueurs
	 */
	public int getNbJoueurs() {
		return nbJoueurs;
	}
	/**
	 * @param nbJoueurs the nbJoueurs to set
	 */
	public void setNbJoueurs(int nbJoueurs) {
		this.nbJoueurs = nbJoueurs;
		this.repaint();
	}
	
	public void ouvertureFichier(){
		FileInputStream f;
		try {
			//Ouverture du fichier
			f = new FileInputStream("options/preferencesPartiePerso");
			Scanner s = new Scanner(f);
			// nombre de pingouins
			this.nbJoueurs = s.nextInt();
			this.joueurs = new Joueur [4];
			int maxPingouins = (nbJoueurs==2) ? 4 : ((nbJoueurs==3) ? 3 : ((nbJoueurs==4) ? 2 : 0));
			// noms joueurs
			for(int i=0; i<nbJoueurs; i++)
			{
				int avatarsJoueurs = Integer.parseInt(s.next());
				String nom = s.next();
				int difficulte = Integer.parseInt(s.next());
				
				this.joueurs[i] = new Joueur(i, nom, avatarsJoueurs, maxPingouins, true, difficulte, new Terrain(), 0, 0);
			}
			
			if(nbJoueurs < 3){
				nbJoueurs++;
				this.joueurs[2] = new Joueur(2, "Ordi3", 8, 3, true, 2, new Terrain(), 0, 0);
			}
			if(nbJoueurs < 4){
				nbJoueurs++;
				this.joueurs[3] = new Joueur(3, "Ordi4",14, 2, true, 2, new Terrain(), 0, 0);
			}

			// Cases accessibles
			this.optionCasesAccessibles = Boolean.valueOf(s.next());
			// Affichage Pop up
			this.optionAffichageConfirmation = Boolean.valueOf(s.next());
			// affichage des bulles d'aide
			this.optionCommentJouer = Boolean.valueOf(s.next());
			// vitesse animation
			this.vitesseAnim = s.nextInt();
			s.close();
	        f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Impossible d'ouvrir le fichier. Verifier que le fichier existe et que le chemin est correcte");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("il s'est produit une erreur lors de la fermeture du fichier.");
			System.exit(0);
		}
	}
	
	public void reecrireFichierOption(){
		FileWriter fw;
		try {
			fw = new FileWriter("options/preferencesPartiePerso");
			fw.write(String.valueOf(nbJoueurs));
			fw.write("\n");
			for(int i=0; i<nbJoueurs; i++){
				fw.write(String.valueOf(joueurs[i].getNumAvatar()) + " ");
				fw.write(joueurs[i].getNom()+ " ");
				fw.write(String.valueOf(joueurs[i].getDifficulte()) + " ");
				fw.write("\n");
			}
			
			fw.write(String.valueOf(optionCasesAccessibles));
			fw.write("\n");
			fw.write(String.valueOf(optionAffichageConfirmation));
			fw.write("\n");
			fw.write(String.valueOf(optionCommentJouer));
			fw.write("\n");
			
			fw.write(String.valueOf(vitesseAnim));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param boutonAjouterJoueur the boutonAjouterJoueur to set
	 */
	public void setBoutonAjouterJoueur(int numeroJoueur) {
		if(numeroJoueur < 3){
			if(!boutonAjouterJoueur[1].isVisible() && !panSelectionJoueur[2].isVisible()){
				panJoueur.add(boutonAjouterJoueur[1]);
				boutonAjouterJoueur[1].setVisible(true);
			}
			if(panSelectionJoueur[3].isVisible() && !panSelectionJoueur[2].isVisible()){
				panJoueur.add(boutonAjouterJoueur[0]);
				boutonAjouterJoueur[0].setVisible(true);
				panJoueur.remove(boutonAjouterJoueur[1]);
				boutonAjouterJoueur[1].setVisible(false);
			}
			else{
				panJoueur.remove(boutonAjouterJoueur[1]);
				boutonAjouterJoueur[1].setVisible(false);
				panJoueur.add(boutonAjouterJoueur[0]);
				boutonAjouterJoueur[0].setVisible(true);
			}
			
		}else if(numeroJoueur == 3){
			if(panSelectionJoueur[2].isVisible() && !panSelectionJoueur[3].isVisible()){
				panJoueur.remove(boutonAjouterJoueur[0]);
				boutonAjouterJoueur[0].setVisible(false);
				panJoueur.add(boutonAjouterJoueur[1]);
				boutonAjouterJoueur[1].setVisible(true);
			}else if(panSelectionJoueur[3].isVisible() && !panSelectionJoueur[2].isVisible()){
				panJoueur.add(boutonAjouterJoueur[0]);
				boutonAjouterJoueur[0].setVisible(true);
				panJoueur.remove(boutonAjouterJoueur[1]);
				boutonAjouterJoueur[1].setVisible(false);
			}
		}else{
			if(!panSelectionJoueur[2].isVisible()){
				panJoueur.add(boutonAjouterJoueur[0]);
				boutonAjouterJoueur[0].setVisible(true);
			}
		}
		this.repaint();
	}
	/**
	 * @return the panJoueur
	 */
	public JPanel getPanJoueur() {
		return panJoueur;
	}
	/**
	 * @param panJoueur the panJoueur to set
	 */
	public void setPanJoueur(JPanel panJoueur) {
		this.panJoueur = panJoueur;
	}
	
}
