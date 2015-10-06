package maquettes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import liaison.Liaison;
import liaison.ObserveurMoteur;
import ressourcesIHM.BoutonImage;

@SuppressWarnings("serial")
public class FenCharger extends JFrame implements ActionListener, ListSelectionListener{
	
	//variables
	private JPanel panCharger, panApercu;
	private JLabel titreFen, titreApercu;
	private JPanel scoreJoueur;
	@SuppressWarnings("rawtypes")
	private JList listeSauvegarde;
	private BoutonImage boutonCharger, boutonSupprimer, boutonRetour;
	private int largeurBouton, hauteurElement = 40, espace = 5;
	private int hauteur, largeur;
	private FenPengouin fenPengouin;
	private JFrame mainFen;
	private Liaison liaison, liaisonApercuRapide;
	private PanContexte panCtxt;
	private String nomSauvegarde;
	private JLabel[] labelJoueur = new JLabel[4];
	private JLabel[] pointPoisson = new JLabel[4];
	private JLabel[] pointTuiles = new JLabel[4];
	private Image imagePoisson, imageTuile, surbrillanceScore;
	private int largeurDepart = 5, hauteurDepart = 25, espaceEntreLabel = 10;
	private Liaison liaisonApercuPerso;
	//contructeurs
	public FenCharger(JFrame mainFen, FenPengouin fenPengouin, Liaison liaison, ObserveurMoteur obM)
	{
		this.fenPengouin = fenPengouin;
		this.mainFen = mainFen;
		this.liaison = liaison;
		this.initComponent();
	}//fin constructeur
	
	//ajout de tous les elemens de la fenCharger
	public void initComponent(){
		//options de la fenetre en elle meme
		configurationFenetre();				
		//construction du Panel pour charger
		constructionPanelCharger();
		//ajout a la fenetre des panels
		getRootPane().setDefaultButton(boutonCharger); 
		getRootPane().requestFocus();
		this.add(panCharger);
	}
	//fin initComponent
	
	//methode pour la configuration de notre fenetre
	private void configurationFenetre(){
		this.setLayout(null);
		hauteur = this.mainFen.getHeight() - this.mainFen.getHeight()/3;
		largeur = this.mainFen.getWidth()* 2 / 3;
		this.setSize(largeur, hauteur);							//Taille de la fenetre
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );	// option lors de la fermeture de la fenetre avec la croix
		this.setResizable(false);								//on empeche le redimensionnement de la fenetre
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(this.fenPengouin);
		this.setLocation(0, 100);
		this.setBackground( new Color(60, 120, 192));
		//fenetre visible
		this.setVisible(true);
	}//fin de la configuration de notre fenetre
	//configuration du panelCharger
	private void constructionPanelCharger(){
		
		this.panCharger = new JPanel();
		panCharger.setLayout(null);
		panCharger.setBackground( new Color(60, 120, 192));
		panCharger.setBounds(0, 0, this.getWidth(), hauteur);//(this.getWidth()/15) *9, hauteur);
		int largeurElementListe = panCharger.getWidth()-100;
		
		titreFen = new JLabel();
		
		try {
			titreFen.setFont(liaison.getDonneesAff().getFont(22));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		titreFen.setForeground(Color.white);
		titreFen.setText("Selectionnez la partie a charger : ");
		titreFen.setBounds(((panCharger.getWidth() - largeurElementListe) /2), espace, this.getWidth() - espace * 2, hauteurElement);
		panCharger.add(titreFen);
		
		//configuration du conteneur des noms des fichiers de sauvegarde
		chargementListeSauvegarde();
		listeSauvegarde.setBounds(((panCharger.getWidth() - largeurElementListe) /2), ((panCharger.getWidth() - largeurElementListe) /2), largeurElementListe, (hauteur * 2) / 3);
		listeSauvegarde.setBackground(new Color(153,185,224));
		listeSauvegarde.setForeground(Color.white);
		listeSauvegarde.setFont( new Font("Arial",Font.BOLD,14));
		listeSauvegarde.setLayoutOrientation(JList.VERTICAL); //on affiche de maniere verticale les elements de la liste
		listeSauvegarde.setAutoscrolls(true); //on autorise a afficher une barre pour descendre dans la liste
		listeSauvegarde.setVisibleRowCount(10);	//on affiche pas plus de 10 element en meme temps dans le composant
		listeSauvegarde.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //on gere uniquement une seule selection de sauvegarde a la fois
		listeSauvegarde.setBorder(BorderFactory.createLoweredBevelBorder());
		listeSauvegarde.addListSelectionListener(this);
		@SuppressWarnings("unused")
		JScrollPane scrollPane = new JScrollPane(listeSauvegarde);
		panCharger.add(listeSauvegarde);

		//ajout des bouton
		boutonCharger = new BoutonImage("ChargerFenCharger");
		largeurBouton = ((panCharger.getWidth() / 3) / 4) * 3;
		boutonCharger.setBounds(((panCharger.getWidth() / 3)) /4 + espace, ((hauteur * 2) / 3) + (espace * 4) + hauteurElement + espace * 2, largeurBouton, hauteurElement);
		boutonCharger.addActionListener(this);
		boutonCharger.setMnemonic(KeyEvent.VK_ENTER);
		boutonCharger.setFocusable(true);
		panCharger.add(boutonCharger);
		
		boutonSupprimer = new BoutonImage("Supprimer");
		boutonSupprimer.setBounds(((panCharger.getWidth() / 3)) /4 + largeurBouton + espace * 5,  ((hauteur * 2) / 3) + (espace * 4) + hauteurElement + espace * 2, largeurBouton, hauteurElement);
		boutonSupprimer.addActionListener(this);
		boutonSupprimer.setMnemonic(KeyEvent.VK_DELETE);
		boutonSupprimer.setFocusable(true);
		panCharger.add(boutonSupprimer);
		
		boutonRetour = new BoutonImage("Retour");
		boutonRetour.setBounds(((panCharger.getWidth() / 3)) /4 + largeurBouton * 2 + espace* 9,  ((hauteur * 2) / 3) + (espace * 4) + hauteurElement + espace *2, largeurBouton, hauteurElement);
		boutonRetour.addActionListener(this);
		boutonSupprimer.setFocusable(true);
		panCharger.add(boutonRetour);
		
	}//fin construction Panel Charger
	
	//configuration du panelApercu
	public void constructionPanelApercu(JPanel panApercu){

		this.panApercu = panApercu;
		panApercu.setOpaque(true);
		panApercu.setLayout(null);
		panApercu.setBackground( new Color(60, 120, 192));
		panApercu.setBounds(panCharger.getWidth(), 0, this.getWidth() - panCharger.getWidth(), hauteur);
		titreApercu = new JLabel();
		try {
			titreApercu.setFont(liaison.getDonneesAff().getFont(18));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		titreApercu.setForeground(Color.white);
		titreApercu.setText("Aperçu de la partie : ");
		titreApercu.setBounds(espace,espace ,(this.getWidth() - panCharger.getWidth()) - 4 *espace, hauteurElement);
		panApercu.add(titreApercu);
		//ajout du panel contenant les scores
		affichageScore();
	}//fin de la construction du panel Apercu

	//Action performed
	@SuppressWarnings("rawtypes")
	public synchronized void  actionPerformed(ActionEvent e) {
		//action du bouton retour
		if(e.getSource() == this.boutonRetour){
			this.dispose(); //on ferme la fenetre et on ne charge rien.
			if(liaisonApercuRapide != null)
				liaisonApercuRapide.quitter(); //on ferme la liaison que l'on a creer.
		}//fin action pour le bouton retour
		//action du bouton supprimer
		if(e.getSource() == this.boutonSupprimer){
			if(listeSauvegarde.getModel().getSize() > 0){
				if(liaisonApercuRapide != null)
					liaisonApercuRapide.quitter();
				int i= this.listeSauvegarde.getSelectedIndex();
				System.out.println("Suppression de la sauvegarde");
				liaison.supprimerSauvegarde(this.listeSauvegarde.getModel().getElementAt(i).toString());	//on supprime le fichier
				((DefaultListModel)this.listeSauvegarde.getModel()).removeElementAt(i);	//on le supprime de la liste
				System.out.println("suppression dans la liste");
				listeSauvegarde.repaint();
				//panApercu.remove(scoreJoueur);
				this.getContentPane().removeAll();
				constructionPanelCharger();
				this.add(panCharger);
				this.repaint();
			}
		}//fin action pour le bouton supprimer
		//action du bouton charger
		if(e.getSource() == this.boutonCharger){

//			liaisonApercu.quitter();
//			liaison.deleteObservers();
//			liaisonApercuRapide.quitter();
			liaison.charger(nomSauvegarde);
			
//			this.panCtxt = new PanContexte(fenPengouin, liaison);
//			liaison.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaison));
//			// Changement de panel vers PanContexte
			mainFen.getContentPane().removeAll();
			fenPengouin.getContentPane().removeAll();
			this.dispose();
		}//fin action pour le bouton charger
		
		
	}//fin action Performed
	
	public void affichageScore(){
		scoreJoueur = new JPanel();	//a modifier avec le fichier de lien suivant le nombre de joueur
		scoreJoueur.setForeground(Color.white);
		scoreJoueur.setLocation(0, panApercu.getHeight() - 195);
		scoreJoueur.setSize(panApercu.getWidth() - espace *2, 160);
		scoreJoueur.setBackground(new Color(60, 120, 192));
		try {
			scoreJoueur.setBorder(BorderFactory.createTitledBorder(null,"Score : ", 1, 1, liaisonApercuRapide.getDonneesAff().getFont(18), Color.white));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}	//creation de la bordure autour du score des joueurs
		ouvertureImage();
		
		for(int i = 0 ; i < 2 ; i++)
		{
			Font font = null;
			try {
				font = liaison.getDonneesAff().getFont(18);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//ajout des label Joueur
			labelJoueur[i] = new JLabel();
			labelJoueur[i].setText(liaisonApercuRapide.getDonneesAff().getJoueurs()[i].getNom() + " : ");
			labelJoueur[i].setBounds(largeurDepart,hauteurDepart - 8, scoreJoueur.getWidth() - largeurDepart + espaceEntreLabel, 30);
			labelJoueur[i].setForeground(Color.white);
			labelJoueur[i].setFont(font);
			scoreJoueur.add(labelJoueur[i]);
			//ajout des labels des points Poisson
			pointPoisson[i] = new JLabel();
			pointPoisson[i].setText("" + liaisonApercuRapide.getDonneesAff().getJoueurs()[i].getNombrePoissons());
			pointPoisson[i].setBounds(largeurDepart +5, hauteurDepart + 25,25,30);
			pointPoisson[i].setForeground(Color.white);
			pointPoisson[i].setFont(font);
			scoreJoueur.add(pointPoisson[i]);
			//ajout des labels des points Tuile
			pointTuiles[i] = new JLabel();
			pointTuiles[i].setText("" + liaisonApercuRapide.getDonneesAff().getJoueurs()[i].getNombreTuiles());
			pointTuiles[i].setBounds(largeurDepart + 75, hauteurDepart + 25,25,30);
			pointTuiles[i].setForeground(Color.white);
			pointTuiles[i].setFont(font);
			scoreJoueur.add(pointTuiles[i]);
			largeurDepart = 210;
			
		
		}
		if(liaison.getDonneesAff().getNbJoueurs() >  2){
			largeurDepart = 5;
			hauteurDepart = hauteurDepart + 65;
			for(int i = 2 ; i < liaisonApercuRapide.getDonneesAff().getNbJoueurs() ; i++)
			{
				Font font = null;
				try {
					font = liaison.getDonneesAff().getFont(18);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				//ajout des label Joueur
				labelJoueur[i] = new JLabel();
				labelJoueur[i].setText(liaisonApercuRapide.getDonneesAff().getJoueurs()[i].getNom() + " : ");
				labelJoueur[i].setBounds(largeurDepart,hauteurDepart, this.getWidth() - largeurDepart + espaceEntreLabel, 30);
				labelJoueur[i].setForeground(Color.white);
				labelJoueur[i].setFont(font);
				scoreJoueur.add(labelJoueur[i]);
				//ajout des labels des points Poisson
				pointPoisson[i] = new JLabel();
				pointPoisson[i].setText("" + liaisonApercuRapide.getDonneesAff().getJoueurs()[i].getNombrePoissons());
				pointPoisson[i].setBounds(largeurDepart + 5, hauteurDepart + 25,25,30);
				pointPoisson[i].setForeground(Color.white);
				pointPoisson[i].setFont(font);
				scoreJoueur.add(pointPoisson[i]);
				//ajout des labels des points Tuile
				pointTuiles[i] = new JLabel();
				pointTuiles[i].setText("" + liaisonApercuRapide.getDonneesAff().getJoueurs()[i].getNombreTuiles());
				pointTuiles[i].setBounds(largeurDepart + 75, hauteurDepart + 25,25,30);
				pointTuiles[i].setForeground(Color.white);
				pointTuiles[i].setFont(font);
				scoreJoueur.add(pointTuiles[i]);
				largeurDepart = 210;
			
			}
		}
		panApercu.add(scoreJoueur,0);
	}//fin affichageScore

	private void ouvertureImage(){
		try{
			 imagePoisson = ImageIO.read(getClass().getResource("/Data/Images/Utile/poisson.png"));
			 imageTuile = ImageIO.read(getClass().getResource("/Data/Images/Utile/tuile1.png"));
			 surbrillanceScore = ImageIO.read(getClass().getResource("/Data/Images/Plateau/Surbrillance_score.png"));
			}catch(IOException e){
		}
	}
	
	public void paintComponent(Graphics g){
	//	System.out.println("[panel score] paint component");
		super.paintComponents(g);
		g.drawImage(surbrillanceScore, 3, 25 + 55 * liaison.getDonnees().getJoueurCourant(), null);
		hauteurDepart = (int) (scoreJoueur.getLocation().getX() + 25);
		largeurDepart = (int) (scoreJoueur.getLocation().getY() + 5);
		for(int i = 0; i < liaison.getDonneesAff().getNbJoueurs(); i++){
			Image imageBouton = liaison.getAvatars()[i].getImageAvatarJoueur();
			g.drawImage(imageBouton,largeurDepart + 145, hauteurDepart, 50, 50, null);
			g.drawImage(imagePoisson,largeurDepart + 40, hauteurDepart + 15, 30, 34, null);
			g.drawImage(imageTuile, largeurDepart + 105, hauteurDepart + 15, 30, 35, null);
			
			largeurDepart = (int)(scoreJoueur.getLocation().getX() + 210);
		}
	}
	
	//fonction permettant de remplir la structure de sauvegarde
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void chargementListeSauvegarde(){
		File f1 = new File("./Sauvegarde/");
		String [] s = f1.list();
		//if(liaison.getDonnees().getListeFichier().length != 0){
			DefaultListModel listModel = new DefaultListModel();
			this.listeSauvegarde = new JList(listModel);
			for(int i = 0; i < s.length; i++){
				((DefaultListModel) this.listeSauvegarde.getModel()).addElement(s[i]);
			}
			
		//}
	}//fin recuperation de la liste des fichiers de sauvegarde

	@Override
	public void valueChanged(ListSelectionEvent e) {
				
	if(!this.listeSauvegarde.isSelectionEmpty())
		{
			nomSauvegarde = this.listeSauvegarde.getModel().getElementAt(this.listeSauvegarde.getSelectedIndex()).toString();
//			liaisonApercuRapide = new Liaison(false,false);
//			liaisonApercuRapide.nouvellePartie(new ObserveurMoteur(fenPengouin, panCtxt, liaisonApercuRapide));
//			liaisonApercuRapide.chargerApercu(nomSauvegarde); //on charge les infos pour l'apercu
////			System.out.println("[FenCharger] apres charger");
//			this.repaint();
//			this.panApercu.repaint();
//			this.scoreJoueur.repaint();
//		}//else {
			
//			liaisonApercu = new Liaison(false,false);
//			liaisonApercu.nouvellePartie(new ObserveurMoteur(panCtxt, liaisonApercu));
//			System.out.println("[FenCharger] avant charger");
//			liaisonApercu.charger(this.listeSauvegarde.getModel().getElementAt(0).toString());
//			System.out.println("[FenCharger] apres charger");
//			nomSauvegarde = this.listeSauvegarde.getModel().getElementAt(0).toString();
//			liaisonApercu.chargerApercu(nomSauvegarde);//on affiche l'apercu.
//			panApercu.setVisible(true);
//			this.repaint();
//			this.panApercu.repaint();
//			this.scoreJoueur.repaint();
		}
	}
}//fin classe
