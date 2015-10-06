	/********************************************************************
	* Nom du codeur : Greg				*Date de création : 20/05/2013	*
	* 									*Date de modification :			*
	* Nom de la classe :PanelJeu										*
	* *******************************************************************
	* Description : 													*
	* Fenetre contenant la grille des hexagones 						*
	*********************************************************************/

package maquettes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import liaison.Coordonnees;
import liaison.DonneesPartagees;
import liaison.Liaison;
import moteur.Joueur;
import moteur.Tuile;
import Son.BanqueDeSon;

@SuppressWarnings("serial")
public class PanelJeu extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	// Definit la taille du côte d'un polygone
	int cote; 
	// Retient le n° du polygone sur lequel est la souris
	int numPoly=0;
	int i, j;
	private Polygon pol;
	private BufferedImage hex1, hex2, hex3, acc, conseil; 	 // Image tuiles, tuile accessible et conseil
	private BufferedImage halo, haloNonAcc, haloAcc; // Image pour les halos
	private int xPoly, yPoly; // Conserve la position du curseur au survol
	private int xHalo, yHalo; // Conserve la position ou afficher le halo
	private Liaison liaison;
	private PanContexte panContxt;
	private int iPingSelect, jPingSelect; // Contient les indices du pingouin selectionne
	private boolean isSelected;    // Vrai si un pingouin a ete selectionne (phase de deplacement)
	private boolean curseurValide; // Vrai si le curseur est sur un hexagone actuellement
	private boolean conseilActif;
	private Timer t;
	private boolean isInPanel;
	private DonneesPartagees donnees;
	private Rectangle r;
	
	public PanelJeu (Liaison liaison, PanContexte panContxt)
	{
		// Recuperation des parametres et initialisation des constantes
		this.liaison = liaison;
		this.cote = 40;
		Polygon p2=getPolygon(0, 0, cote); // Cree un hexagone
		this.r = p2.getBounds(); // Recupere le plus petit rectangle aux bord de la fenêtre dans lequel l'hexagone peut s'inscrire
		
		this.setConseilActif(false);
		this.panContxt = panContxt;
		this.isSelected = false;
		this.t = new Timer(500, this);
		
		// Initialisation de pol pour que le curseur soit pris en compte ou qu'il soit au lancement de la partie
		this.pol = getPolygon(0, 0, cote);
		
		// Affichage du terrain
		liaison.getDonneesAff().getTerrain().afficher();
		
		// Ajout des Listener de souris
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		//Chargement des images
		this.initImages();
		// Si le joueur 1 est un ordi, on lance la partie
//		if (liaison.getDonneesAff().joueurCourant().estOrdi())
//		{
//			this.lancerPartie();
//		}
	}
	
	/**
	 * Initialise les images
	 */
	public void initImages()
	{
		try {
			hex1 = ImageIO.read(getClass().getResource("/Data/Images/Plateau/tuile1.png")); // Image de la tuile a 1 poisson
			hex2 = ImageIO.read(getClass().getResource("/Data/Images/Plateau/tuile2.png")); // Image de la tuile a 2 poissons
			hex3 = ImageIO.read(getClass().getResource("/Data/Images/Plateau/tuile3.png")); // Image de la tuile a 3 poissons
			acc = ImageIO.read(getClass().getResource("/Data/Images/Plateau/tuile_verte.png")); // Image de la tuile accessible
			conseil = ImageIO.read(getClass().getResource("/Data/Images/Plateau/tuile_conseil.png")); // Image de la tuile conseil
			halo = ImageIO.read(getClass().getResource("/Data/Images/Plateau/Surbrillance.png")); // Image du halo de surbrillance
			haloNonAcc = ImageIO.read(getClass().getResource("/Data/Images/Plateau/Surbrillance_non_acc.png")); // Image du halo de surbrillance non accessible
			haloAcc = ImageIO.read(getClass().getResource("/Data/Images/Plateau/Surbrillance_acc.png")); // Image du halo de surbrillance accessible
		} 
		catch (IOException e)
		{
			System.out.println("[PanelJeu.initImages()] Erreur au chargement des images des tuiles");
		}
	}
	
	/**
	 * Lance la partie si le joueur 1 est un ordinateur
	 */
	public void lancerPartie()
	{
		t.start();
	}
	
	/**
	 * Affiche la grille d'hexagones en fonction du Terrain
	 */
	public void paintComponent(Graphics arg0) {
		this.curseurValide = false;
		Point survol=null;
		arg0.setColor(Color.black);
		super.paintComponent(arg0);
		Graphics2D g2d;
		g2d=(Graphics2D) arg0;
		BasicStroke bsHexa=new BasicStroke(1);// Epaisseur du trait pour les hexagones
		g2d.setStroke(bsHexa);
		int espace = 10;
		Point curseur;
		DonneesPartagees d = liaison.getDonneesAff();
		
		/** Affichage des lignes paires (lignes de 7) */
		for(int l=0;l<8;l=l+2)
		{
			for(int c=1;c<14;c+=2)
			{
				// Si la tuile est toujours presente sur le plateau
				if (liaison.getDonneesAff().getTerrain().consulter(l, c) != null)
				{
					// Calcul de la position en pixels de l'hexagone courant
					xPoly = c/2*r.width+r.width/2 + c/2 * espace + 4;
					yPoly = (int)(l*cote*1.5+0.5 + l * espace);
					
					// On recupere la position du curseur
					curseur=getMousePosition();
					// et l'hexagone courant
					Polygon poly = getPolygon(xPoly, yPoly,cote);
//					System.out.println("[PanelJeu] LIGNE DE 7 (xPoly, yPoly) : ("+xPoly+","+yPoly+")");
					arg0.setColor(Color.black);
					// Si le curseur est dans le panel et dans l'hexagone
					if(curseur!=null && poly.contains(curseur)) // Si le curseur est dans l'hexagone concerne
					{
						// On cree le point pour afficher la surbrillance
						survol=new Point(xPoly, yPoly);
						xHalo = xPoly; yHalo = yPoly;
						i = l; j = c;
						// calcul du numero de l'hexa : pour chaque ligne paire, il y a l/2 ligne de 8 et l/2 ligne de 7
						numPoly=l/2*8 + (l-l/2)*7 + c/2;
						pol=poly;
					}
					
					// On trace le contour de l'hexagone autour de l'image --> pour le MouseListener
					g2d.setColor(Color.black);
					g2d.drawPolygon(poly);
					// Selon le nombre de poissons, on affiche la tuile 1, 2 ou 3
					// Si on a demande un conseil, on affiche la tuile bleue au bon endroit
					if(!conseilActif)
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), false);
					else if (!d.isPlacementTermine() && l == d.getCoordConseilPingouinInitial().getI() && c == d.getCoordConseilPingouinInitial().getJ())
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), true);
					else if (d.isPlacementTermine() && l == d.getCoordConseilPingouinDep().getI() && c == d.getCoordConseilPingouinDep().getJ())
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), true);
					else if (d.isPlacementTermine() && l == d.getCoordConseilPingouinArr().getI() && c == d.getCoordConseilPingouinArr().getJ())
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), true);
					else
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), false);
				}
			}
		}
		
		/** Affichage des lignes impaires (lignes de 8) */
		for(int l=1;l<8;l=l+2){
			for(int c=0;c<16;c+=2){
				if (liaison.getDonneesAff().getTerrain().consulter(l, c) != null)
				{
					// Calcul de la position en pixels de l'hexagone courant
					xPoly = c/2*r.width+ c/2 * espace;
					yPoly = (int)(l*cote*1.5+0.5 + l * espace);
					
					// On recupere la position du curseur
					curseur=getMousePosition();
					// et l'hexagone courant
					Polygon poly = getPolygon(xPoly, yPoly,cote);
//					System.out.println("[PanelJeu] LIGNE DE 8 (xPoly, yPoly) : ("+xPoly+","+yPoly+")");
					arg0.setColor(Color.black);
					// Si le curseur est dans le panel et dans l'hexagone
					if(curseur!=null && poly.contains(curseur)) // Si le curseur est dans l'hexagone concerne
					{
						// On cree le point pour afficher la surbrillance
						survol=new Point(xPoly, yPoly);
						xHalo = xPoly; yHalo = yPoly;
						this.i = l; this.j = c;
						// calcul du numero de l'hexa : pour chaque ligne impaire, il y a l/2 ligne de 8 et l-l/2 ligne de 7
						numPoly=l/2*8 + (l-l/2)*7 + c/2;
						pol=poly;
					}
					
					// On trace le contour de l'hexagone autour de l'image --> pour le MouseListener
					g2d.setColor(Color.black);
					g2d.drawPolygon(poly);
					
					// Selon le nombre de poissons, on affiche la tuile 1, 2 ou 3
					// Si on a demande un conseil, on affiche la tuile bleue au bon endroit
					if(!conseilActif)
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), false);
					else if (!d.isPlacementTermine() && l == d.getCoordConseilPingouinInitial().getI() && c == d.getCoordConseilPingouinInitial().getJ())
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), true);
					else if (d.isPlacementTermine() && l == d.getCoordConseilPingouinDep().getI() && c == d.getCoordConseilPingouinDep().getJ())
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), true);
					else if (d.isPlacementTermine() && l == d.getCoordConseilPingouinArr().getI() && c == d.getCoordConseilPingouinArr().getJ())
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), true);
					else
						this.afficherTuile(g2d, liaison.getDonneesAff().getTerrain().consulter(l, c), false);
					
				}
			}
		}
		/** Affichage au survol d'un hexagone :
		 * On affiche un halo vert autour d'un hexagone si :
		 * 	- pendant la phase de placement : 
		 * 		- un pingouin est selectionne, la case ciblee est une case a 1 poisson et qu'elle est libre
		 * 	- pendant la phase de deplacement :
		 * 		- aucun pingouin selectionne et le pingouin cible est celui du joueur courant
		 * 		- un pingouin est selectionne et la case ciblee est accessible
		 */
		if(survol!=null && isInPanel)
		{
			Tuile tuile = d.getTerrain().consulter(i, j);
			if(tuile != null)
			{
				Joueur joueurCourant = d.getJoueurs()[d.getJoueurCourant()]; // reference du joueur courant
				boolean optionCaseAccessible = d.isOptionCasesAccessibles(); // l'option case accessible est activee
				boolean tuileLibre = !tuile.estOccupee(); // la tuile est libre
				boolean tuileAccessible = tuile.estAccessible(); // la tuile est accessible
				boolean placementTermine = d.isPlacementTermine(); // si le placement des pingouins est termine
				boolean unPoisson = tuile.getNbPoissons() == 1; // la tuile contient qu'un seul poisson
				boolean pingSelected = panContxt.getPionJoueur().isSelected(); // un pingouin est selectionne
				boolean pingBelongCurrentPlayer; // le pingouin appartient au joueur courant
				boolean currentPlayerIsBot = joueurCourant.estOrdi(); // le joueur courant est un ordi
				boolean pingBloque; // le pingouin cible est bloque

				// Si l'option case accessible est activee
				// si la tuile est libre ET
				// que (la tuile est accessible OU
				// si on est encore dans le placement ET
				// qu'on a choisi un pingouin a place ET
				// que la case choisie contient 1 seule poisson)
				if (optionCaseAccessible && tuileLibre && (tuileAccessible || !placementTermine && pingSelected && unPoisson && !currentPlayerIsBot))
					// on affiche un halo vert
					g2d.drawImage(haloAcc, xHalo-3, yHalo-3, null);
				// Sinon si l'option est cochee et que la tuile n'est pas nulle
				else if (optionCaseAccessible && tuile != null)
				{
					// Si la tuile est occupee (pingouin dessus)
					if(!tuileLibre)
					{
						// On regarde si le pingouin appartient au joueur courant
						pingBelongCurrentPlayer = tuile.getPingouin().getJoueur().getNumero() == d.getJoueurCourant();
						pingBloque = tuile.getPingouin().isBloque();
						// Si le pingouin appartient au joueur courant et que le placement est termine et que le joueur courant est humain
						if (pingBelongCurrentPlayer && placementTermine && !currentPlayerIsBot && !pingBloque)
							// On affiche un halo vert
							g2d.drawImage(haloAcc, xHalo-3, yHalo-3, null);
						else
							// Sinon rouge
							g2d.drawImage(haloNonAcc, xHalo-3, yHalo-3, null);
					}
					else
						g2d.drawImage(haloNonAcc, xHalo-3, yHalo-3, null);
				}
				// Sinon on affiche un halo rouge si l'option est activee
				else if (optionCaseAccessible)
					g2d.drawImage(haloNonAcc, xHalo-3, yHalo-3, null);
				// Sinon si l'option est desactivee, on affiche un halo bleu
				else
				{
					g2d.drawImage(halo, xHalo-3, yHalo-3, null);
				}
				this.curseurValide = true;
			}
		}
		
		// tant que l'animation est en cours et que le pingouin n'est pas placé on affiche une image
		if(liaison.getDonneesAff().isAnimationEnCours() && !liaison.getDonneesAff().isAnimationPingouinPlace()){
			g2d.drawImage(d.getImageAnimation(), liaison.getDonneesAff().getCoordonneesAnimation().x, liaison.getDonneesAff().getCoordonneesAnimation().y, null);
		}
	}
	
	/**
	 * Affiche - la tuile 1, 2 ou 3 selon le nombre de poissons
	 * 		   - la tuile verte si la case est accessible
	 * 		   - un pingouin si la tuile est occupee (selon le n° de joueur ET de pingouin)
	 * 		   - un halo dont la couleur varie selon la situation et l'option case accessible
	 * @param g2d
	 * @param tuile : tuile cliquee
	 */
	public void afficherTuile(Graphics2D g2d, Tuile tuile, boolean isConseil)
	{
		if(tuile != null)
		{
			
			switch (tuile.getNbPoissons()) 
			{
				case 1:
					// On affiche la tuile de base
					g2d.drawImage(hex1, xPoly, yPoly, null);
					break;
				case 2:	
					// On affiche la tuile de base
					g2d.drawImage(hex2, xPoly, yPoly, null);
					break;
				case 3:	
					// On affiche la tuile de base
					g2d.drawImage(hex3, xPoly, yPoly, null);
					break;
					
				default: 
					break;
			}
			// Affiche une tuile transparente verte par dessus si la case est accessible
			if (liaison.getDonneesAff().isOptionCasesAccessibles() && tuile.estAccessible())
				g2d.drawImage(acc, xPoly, yPoly, null);
			// Si la tuile est occupee, on rajoute un pingouin sur la tuile 
			if (tuile.estOccupee())
			{
				g2d.drawImage(liaison.getAvatars()[tuile.getPingouin().getJoueur().getNumero()].getImageAvatarJoueur(), xPoly, yPoly, null);
			}
			if(isConseil)
			{ // On affiche la tuile conseil
//				g2d.drawImage(conseil, xPoly-3, yPoly-3, null);
				g2d.drawImage(conseil, xPoly-1, yPoly-1, null);
//				g2d.drawImage(conseil, xPoly, yPoly, null);
					
			}
		}
	}
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getNumero() {
		return numPoly;
	}
	public void setNumero(int numero) {
		this.numPoly = numero;
	}
	public Polygon getPolHover(){
		return pol;
	}
	
	/**
	 * Retourne un hexagone inclus dans le rectangle dont le coin superieur gauche est (#x,#y) de #cote de long
	 * @param x : abscisse du coin superieur gauche
	 * @param y : ordonnee du coin superieur gauche
	 * @param cote : longueur ducote de l'hexagone
	 * @return
	 */
	public static Polygon getPolygon(int x,int y,int cote)
	{
		// Forme le polygone
		int haut=cote/2;
		int larg=(int)(cote*(Math.sqrt(3)/2));
		Polygon p=new Polygon();
		p.addPoint(x,y+haut);					// / superieur gauche
		p.addPoint(x+larg,y); 					// \ superieur droit   
		p.addPoint(x+2*larg,y+haut);			// | milieu droit     
		p.addPoint(x+2*larg,y+(int)(1.5*cote)); // / inferieur droit
		p.addPoint(x+larg,y+2*cote);			// \ inferieur gauche
		p.addPoint(x,y+(int)(1.5*cote));		// | milieu gauche
		return p;
	}
	
	public Polygon getPol() {
		return pol;
	}
	public void setPol(Polygon pol) {
		this.pol = pol;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// quand on entre dans le panel, on active l'affichage du halo
		isInPanel = true;
		this.repaint();
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
		// quand on entre dans le panel, on desactive l'affichage du halo
		isInPanel = false;
		this.repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	/**
	 * Quand le clic est relache, on fait les actions suivantes :
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		DonneesPartagees donnees = liaison.getDonneesAff();
		//i et j sont les indices dans le terrain de l'hexagone clique
		Tuile tuile = donnees.getTerrain().consulter(i, j);
		
		// PHASE DE PLACEMENT
		if (!donnees.isPlacementTermine())
		{
			/**              PLACEMENT DU PINGOUIN           **/
			// Si la tuile cliquee n'est pas nulle, qu'un pingouin a ete selectionne dans le panel Pion,
			// que la tuile contient qu'un seul poisson et que la tuile n'est pas deja occupee par un pingouin
			if (tuile != null && panContxt.getPionJoueur().isSelected() && tuile.getNbPoissons() == 1 && !tuile.estOccupee() && !donnees.joueurCourant().estOrdi())
			{ // --> On place le pingouin
				if (liaison.getDonneesAff().isSpeakerActif())
					BanqueDeSon.DEPLACER.jouerSon(false);
				
				
				
				if(!liaison.isEnLigne()){
					// Si le joueur deplace son pingouin, on reactive l'ordinateur
					panContxt.getOutilsJoueur().getBoutonControle().setActif(true);
					liaison.setOrdiActif(true);
					panContxt.getOutilsJoueur().getBoutonAnnuler().setEnabled(false);
				}
				// On place notre pingouin dans l'hexagone clique
				liaison.placerPingouin(new Coordonnees(i, j));
				
				// On informe le panelPion que le pingouin a ete place et qu'il peut en selectionner un nouveau
//				panContxt.getPionJoueur().setSelected(false);
				// On reaffiche le terrain pour afficher le pingouin dans l'hexagone
				this.repaint();
				this.panContxt.getPionJoueur().repaint();
//				System.out.println("Hexagone n°" + numero + " : " + "(" + i + "," + j + ")");
			}
			
		}
		// PHASE DE DEPLACEMENT
		else
		{
			System.out.println("[PanelJeu] Phase de deplacement");
			int  numJCourant = liaison.getDonneesAff().getJoueurCourant();
			
			/**       SELECTION DU PINGOUIN POUR LE DEPLACEMENT          */
			/* Si la tuile cliquee  : - n'est pas nulle
									  - qu'elle contient un pingouin
									  - qu'un pingouin n'est pas deja selectionne
									  - que le pingouin appartient au joueur
									  - que ce n'est pas un ordi
									  - que le pingouin n'est pas bloque
									  - et que le curseur est sur un hexagone a cet instant 
			   --> on definit que le pingouin qu'elle contient est selectionne */
			if(tuile != null && tuile.estOccupee() && !isSelected &&
				tuile.getPingouin().getJoueur().getNumero() == numJCourant &&
			   !tuile.getPingouin().getJoueur().estOrdi() && !tuile.getPingouin().isBloque() && curseurValide)
			{
				System.out.println("[PanelJeu] Phase de deplacement : placement du pingouin en ("+i+","+j+")");
				if (liaison.getDonneesAff().isSpeakerActif())
					BanqueDeSon.SELECT.jouerSon(false);
				if (isSelected)
					liaison.annulerCasesAccessibles(new Coordonnees(iPingSelect, jPingSelect));
				// On stocke la position du pingouin selectionne
				this.iPingSelect = tuile.getLigne();
				this.jPingSelect = tuile.getColonne();
				// Sert a definir qu'un pingouin est selectionne
				this.isSelected = true;
				// Appel moteur pour recuperer les cases accessibles
				liaison.casesAccessibles(new Coordonnees(iPingSelect, jPingSelect));
			}
			
			/**         DEPLACEMENT DU PINGOUIN               **/
			// Si la tuile cliquee est accessible, qu'on a selectionné un pingouin et que le curseur est sur un hexagone,
			else if(isSelected && tuile.estAccessible() && curseurValide) 
			{
				System.out.println("[PanelJeu - " + Thread.currentThread().getId()+"] spearkeactif : "+ liaison.getDonneesAff().isSpeakerActif());
				if (liaison.getDonneesAff().isSpeakerActif())
					BanqueDeSon.DEPLACER.jouerSon(false);
				
				// Si le joueur deplace son pingouin, on reactive l'ordinateur
				if(!liaison.isEnLigne()){
					panContxt.getOutilsJoueur().getBoutonControle().setActif(true);
					liaison.setOrdiActif(true);
				}
				// On deselectionne le pingouin courant
				this.isSelected = false;
				Coordonnees coordPingouin, coordDestination;
				coordPingouin = new Coordonnees(this.iPingSelect, this.jPingSelect);
				coordDestination = new Coordonnees(tuile.getLigne(), tuile.getColonne());
				System.out.println("[PanelJeu] jouerCoup humain");
				// on deplace le pingouin en appelant le thread moteur, il va de coordPingouin a coordDestination
				liaison.jouerCoup(coordPingouin, coordDestination);
			}
			
			/**          DESELECTION DU PINGOUIN              **/
			// Sinon si un pingouin est selectionne et (la tuile cliquee non accessible ou la tuile cliquee est null)
			else if(isSelected && (!tuile.estAccessible() || !curseurValide))
			{
				if (liaison.getDonneesAff().isSpeakerActif())
					BanqueDeSon.ANNULERCASE.jouerSon(false);
				// On annule la selection du pingouin
				this.isSelected = false;
				// Appel moteur pour enlever les cases accessibles
				liaison.annulerCasesAccessibles(new Coordonnees(iPingSelect, jPingSelect));
				this.repaint();
			}
		}
	}
	
	//Methode concernant le drag and drop a voir plus tard
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// pol est calcule dans paintComponent
		if(pol != null)
		{
			this.repaint();
		}
	}
	
	/**
	 * Action du timer (pour les bots)
	 */
	public void actionPerformed(ActionEvent arg0) 
	{
		donnees = liaison.getDonneesAff();
		if (!donnees.isPlacementTermine())
		{
			int nbPingouins = donnees.joueurCourant().getNbPingouins();
			liaison.placerPingouin(donnees.getCoordPingouinInitial());
			
			// Actualisation boutons pions
			int numJoueurCourant = liaison.getDonneesAff().getJoueurCourant();
			this.panContxt.getPionJoueur().getBoutonPion()[numJoueurCourant][nbPingouins].setVisible(false);
		}
		else
		{
//			System.out.println("[PanelJeu] jouerCoup ordi");
			liaison.jouerCoup(donnees.getCoordPingouinDep(), donnees.getCoordPingouinArr());
		}
		this.panContxt.repaint();
		t.stop();
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isConseilActif() {
		return conseilActif;
	}

	public void setConseilActif(boolean conseilActif) {
		this.conseilActif = conseilActif;
	}
}