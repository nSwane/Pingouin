/**
 * Auteur : Jeremy
 * Description : Contient toutes les donnees partagees entre les threads graphique et moteur
 * Date : 20/05/13
 * Version : 1.0
 */

package liaison;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import moteur.Joueur;
import moteur.Pingouin;
import moteur.Terrain;
import moteur.Tuile;

@SuppressWarnings("serial")
public class DonneesPartagees implements java.io.Serializable{

	// enumeration des actions possibles du moteur
	public enum Action {		// variables liees :
		ListerFichiers,
		Charger,				// fichier
		Sauvegarder,
		AnnulerCaseAccessibles, // coordPingouinAcc
		CaseAccessibles,		// coordPingouinAcc
		JouerCoup,				// coordPingouinDep, coordPingouinArr
		Annuler,				
		Refaire,				
		Conseil,				// coordConseil
		Options,				// pas une action du moteur
		PlacerPingouin,	
		SupprimerSauvegarde,
		ChargerApercu,		
		Quitter;
	}
    
	// action demandee au moteur par la partie graphique
	private Action action;
	/* Exemple d'utilisation :
		action = Action.Charger;
		if (action == Action.Charger)
			moteur.charger();
	*/

	// terrain de jeu
	private Terrain terrain;

	// tableau des joueurs
	private Joueur[] joueurs;

	// nom du fichier pour sauvegarder et charger si action = sauvegarder ou action = charger
	private String fichier;

	//Liste des fichiers contenu dans le dossier de sauvegarde
	private String [] listeFichier;

	// coordonnees du point selectionne pour cases accessibles
	private Coordonnees coordPingouinAcc;

	// coordonnees du pingouin a placer pour le conseil si action = conseil
	private Coordonnees coordConseilPingouinInitial;
		
	// coordonnees du pingouin a jouer pour le conseil si action = conseil
	private Coordonnees coordConseilPingouinDep;
	
	// coordonnees du pingouin a jouer pour le conseil si action = conseil
	private Coordonnees coordConseilPingouinArr;

	// coordonnees de la case conseillee si action = conseil
	private Coordonnees coordConseilCase;

	// coordonnees du pingouin a deplacer si action = jouerCoup
	private Coordonnees coordPingouinDep;

	// coordonnees de la nouvelle position du pingouin a deplacer si action = jouerCoup
	private Coordonnees coordPingouinArr;

	// coordonnees du pingouin pour le placement initial
	private Coordonnees coordPingouinInitial;

	// indique si le bouton annuler est disponible
	private boolean annulerPossible;

	// indique si le bouton refaire est disponible
	private boolean refairePossible;

	// indique si le jeu est fini
	private boolean jeuFini;

	// si jeu fini est vrai, indique si le jeu s'est fini sur un match nul
	private boolean matchNul;

	// contient le gagnant si le jeu est fini et qu'il n'y a pas eu match nul
	private Joueur gagnant;

	// indique si l'option des cases accessibles est selectionnee
	private boolean optionCasesAccessibles;

	// indique si l'option des cases accessibles est selectionnee
	private boolean optionAffichageConfirmation;

	// vitesse d'animation pour l'affichage
	private int vitesseAnim;

	// nombre de joueurs
	private int nbJoueurs;

	// indique si la phase de placement des pingouins est termine
	private boolean placementPingouinTermine;

	// police d'ecriture pour le graphique
	private Font font;
	
	// contient le numero du joueur courant
	private int joueurCourant;
	
	// contient le numero du joueur precedent
	private int numJoueurPrecedent;
	
	// contient la difficulte de l'ordinateur
	private int difficulte;

	// indique si le fichier existe deja
	private boolean fichierExiste;
	
	// indique si les sons d'actions sont muets
	private boolean speakerActif;

	// indique si la musique d'ambiance est muet
	private boolean musicActive;
	
	//indique si il s'agit d'une partie perso
	private boolean isPartiePerso;
	
	private boolean animationEnCours; // savoir si une animation est en cours
	private boolean animationPingouinPlace; // savoir si le pingouin anime a fini le placement
	private Point coordonneesAnimation; // coordonnees du pingouin lors de l'animation
	private Image imageAnimation;
	
	//indique si on veux l'affichage des bulles pour savoir jouer
	private boolean optionCommentJouer;
	
	// indique au moteur de vider la pile
	private boolean viderPile;

	/**
     *  Constructeur avec donnees par defaut
	 */
	public DonneesPartagees(boolean isPartiePerso)
	{
		this.terrain = new Terrain();
		this.jeuFini = false;
		this.matchNul = false;
		this.placementPingouinTermine = false;
		this.annulerPossible = false;
		this.refairePossible = false;
		this.speakerActif= true; 
		this.musicActive = true;
		this.isPartiePerso = isPartiePerso;
		lireDonneesPartagees();
		
		//this.gagnant = joueurs[0];
		
	}

	
	public DonneesPartagees(int nbClients, String [] lesNoms, int [] lesAvatars, boolean [] estOrdi){
		this.terrain = new Terrain();
		this.jeuFini = false;
		this.matchNul = false;
		this.placementPingouinTermine = false;
		this.annulerPossible = false;
		this.refairePossible = false;
		this.nbJoueurs = nbClients;
		this.joueurs = new Joueur [this.nbJoueurs];
		int maxPingouins = (nbJoueurs==2) ? 4 : ((nbJoueurs==3) ? 3 : ((nbJoueurs==4) ? 2 : 0));
		
		for(int i=0; i<nbJoueurs; i++)
		{
			String nom = lesNoms[i];
			int avatar = lesAvatars[i];
			this.joueurs[i] = new Joueur(i, nom, avatar, maxPingouins, estOrdi[i], 0, terrain, 0, 0);
		}
		
		// Cases accessibles
		this.optionCasesAccessibles = true;
		
		// vitesse animation
		this.vitesseAnim = 50;
	}
	
	public void lireDonneesPartagees(){
		FileInputStream f;
		try {
			//Ouverture du fichier
			if(!isPartiePerso){
				f = new FileInputStream("options/preferences");
			}else{
				f = new FileInputStream("options/preferencesPartiePerso");
			}
			
			Scanner s = new Scanner(f);
			
			// nombre de pingouins
			this.nbJoueurs = s.nextInt();
			
			this.joueurs = new Joueur [this.nbJoueurs];
			int maxPingouins = (nbJoueurs==2) ? 4 : ((nbJoueurs==3) ? 3 : ((nbJoueurs==4) ? 2 : 0));
			 
			// noms joueurs
			for(int i=0; i<nbJoueurs; i++)
			{
				int avatarsJoueurs = Integer.parseInt(s.next());
				String nom = s.next();
//				String suivant = s.next();
//				boolean ordi = Boolean.valueOf(suivant);
				int difficulte = Integer.parseInt(s.next());
				
				this.joueurs[i] = new Joueur(i, nom, avatarsJoueurs, maxPingouins, true, difficulte, terrain, 0, 0);
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
			if(isPartiePerso())
				fw = new FileWriter("options/preferencesPartiePerso");
			else
				fw = new FileWriter("options/preferences");
			fw.write(String.valueOf(nbJoueurs));
			fw.write("\n");
			for(int i=0; i<nbJoueurs; i++){
				fw.write(String.valueOf(joueurs[i].getNumAvatar()) + " ");
				fw.write(joueurs[i].getNom()+ " ");
//				fw.write(String.valueOf(joueurs[i].estOrdi()) + " ");
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
	
	public boolean isPlacementPingouinTermine() {
		return placementPingouinTermine;
	}

	public void setPlacementPingouinTermine(boolean placementPingouinTermine) {
		this.placementPingouinTermine = placementPingouinTermine;
	}

	/**
	 * Methode permettant de gerer la police pour le jeu entier
	 * @param taillePolice taille de la police voulu
	 * @return la font du jeu avec la taille demandé
	 */
	public Font getFont(int taillePolice) throws FileNotFoundException {
		File fichierFont;
		fichierFont = new File("options/BUXTONSKETCH.TTF");
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fichierFont);
			font = font.deriveFont(Font.BOLD);
			font = font.deriveFont((float)taillePolice);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return font;
	}

	public int getNbJoueurs() {
		return nbJoueurs;
	}

	public void setNbJoueurs(int nbJoueurs) {
		this.nbJoueurs = nbJoueurs;
	}

	
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getVitesseAnim() {
		return vitesseAnim;
	}

	public void setVitesseAnim(int vitesseAnim) {
		this.vitesseAnim = vitesseAnim;
	}

	public Coordonnees getCoordPingouinInitial() {
		return coordPingouinInitial;
	}

	public void setCoordPingouinInitial(Coordonnees coordPingouinInitial) {
		this.coordPingouinInitial = coordPingouinInitial;
	}

	public boolean isMatchNul() {
		return matchNul;
	}

	public void setMatchNul(boolean matchNul) {
		this.matchNul = matchNul;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public Joueur[] getJoueurs() {
		return joueurs;
	}

	public void setJoueurs(Joueur[] joueurs) {
		this.joueurs = joueurs;
	}

	public Coordonnees getCoordPingouinAcc() {
		return coordPingouinAcc;
	}

	public void setCoordPingouinAcc(Coordonnees coordPingouinAcc) {
		this.coordPingouinAcc = coordPingouinAcc;
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	public boolean isOptionCasesAccessibles() {
		return optionCasesAccessibles;
	}

	public void setOptionCasesAccessibles(boolean optionCasesAccessibles) {
		this.optionCasesAccessibles = optionCasesAccessibles;
	}

	public Coordonnees getCoordConseilCase() {
		return coordConseilCase;
	}

	public void setCoordConseilCase(Coordonnees coordConseilCase) {
		this.coordConseilCase = coordConseilCase;
	}

	public Coordonnees getCoordPingouinDep() {
		return coordPingouinDep;
	}

	public void setCoordPingouinDep(Coordonnees coordPingouinDep) {
		this.coordPingouinDep = coordPingouinDep;
	}

	public Coordonnees getCoordPingouinArr() {
		return coordPingouinArr;
	}

	public void setCoordPingouinArr(Coordonnees coordPingouinArr) {
		this.coordPingouinArr = coordPingouinArr;
	}

	public boolean isAnnulerPossible() {
		return annulerPossible;
	}

	public void setAnnulerPossible(boolean annulerPossible) {
		this.annulerPossible = annulerPossible;
	}

	public boolean isRefairePossible() {
		return refairePossible;
	}

	public void setRefairePossible(boolean refairePossible) {
		this.refairePossible = refairePossible;
	}

	public boolean isJeuFini() {
		int n = getNbJoueurs();
		while(getJoueurs()[getJoueurCourant()].getPingouinsRestants() <= 0 && n > 0 )
		{
			if(getJoueurCourant() == getNbJoueurs() - 1){
				setJoueurCourant(0);
			}else{
				setJoueurCourant(getJoueurCourant()+1);
			}
			n--;
		}
		return n == 0;
	}

	public void setJeuFini(boolean jeuFini) {
		this.jeuFini = jeuFini;
	}

	public Joueur getGagnant() {
		return gagnant;
	}

	public void setGagnant(Joueur gagnant) {
		this.gagnant = gagnant;
	}

	public void setJoueurCourant(int i) {
		joueurCourant = i;
	}

	public int getJoueurCourant() {
		return joueurCourant;
	}

	public String [] getListeFichier() {
		return listeFichier;
	}

	public void setListeFichier(String [] listeFichier) {
		this.listeFichier = listeFichier;
	}
	public void setNomJoueur(String nomJoueur, int numeroJoueur){
		joueurs[numeroJoueur].setNom(nomJoueur);
	}
	
	public int getDifficulte() {
		return difficulte;
	}

	public void setDifficulte(int difficulte) {
		this.difficulte = difficulte;
	}
	
	/**
	 * @return the isPartiePerso
	 */
	public boolean isPartiePerso() {
		return isPartiePerso;
	}

	/**
	 * @param isPartiePerso the isPartiePerso to set
	 */
	public void setPartiePerso(boolean isPartiePerso) {
		this.isPartiePerso = isPartiePerso;
	}

	/**
	 * Retourne une copie des donnees
	 * @return
	 */
	public DonneesPartagees copie()
	{
		// Pour les elements ne disposant pas de methode copie,
		// utilisation de leurs valeurs ajouter a l'element neutre pour ne pas donner la reference
		DonneesPartagees dp = new DonneesPartagees(this.isPartiePerso);
		dp.setAction(action);
		dp.setAnnulerPossible(annulerPossible);
		if (coordConseilCase != null)
			dp.setCoordConseilCase(coordConseilCase.copie());
		if (coordConseilPingouinDep != null)
			dp.setCoordConseilPingouinDep(coordConseilPingouinDep.copie());
		if (coordConseilPingouinArr != null)
			dp.setCoordConseilPingouinArr(coordConseilPingouinArr.copie());
		if (coordConseilPingouinInitial != null)
			dp.setCoordConseilPingouinInitial(coordConseilPingouinInitial.copie());
		if (coordPingouinAcc != null)
			dp.setCoordPingouinAcc(coordPingouinAcc.copie());
		if (coordPingouinDep != null)
			dp.setCoordPingouinDep(coordPingouinDep.copie());
		if (coordPingouinArr != null)
			dp.setCoordPingouinArr(coordPingouinArr.copie());
		if (coordPingouinInitial != null)
			dp.setCoordPingouinInitial(coordPingouinInitial.copie());
		dp.setFichier("" + fichier);
		dp.setFont(font);
		if (gagnant != null)
			dp.setGagnant(gagnant.copie());
		dp.setJeuFini(jeuFini);
		dp.setJoueurCourant(joueurCourant);
		dp.setNumJoueurPrecedent(numJoueurPrecedent);
		dp.setImageAnimation(imageAnimation);
		dp.setAnimationEnCours(animationEnCours);
		dp.setAnimationPingouinPlace(animationPingouinPlace);
		dp.setCoordonneesAnimation(coordonneesAnimation);
		dp.setPartiePerso(isPartiePerso);
		
		dp.setSpeakerActif(speakerActif);
		dp.setMusicActive(musicActive);
		
		// Copie du tableau des joueurs
		Joueur[] copyTabJ = new Joueur[nbJoueurs];
		for (int i = 0; i < copyTabJ.length; i++) {
			if(joueurs[i] != null)
				copyTabJ[i] = this.joueurs[i].copie();
		}
		
		// Copie de la liste des fichiers
		if (listeFichier != null)
		{
			String[] copyListFich = new String[listeFichier.length];
			for (int i = 0; i < copyListFich.length; i++) {
				if (listeFichier[i] != null)
					copyListFich[i] = this.listeFichier[i];
			}
			dp.setListeFichier(copyListFich);
		}
		
		dp.setMatchNul(matchNul);
		dp.setNbJoueurs(nbJoueurs);
		dp.setOptionCasesAccessibles(optionCasesAccessibles);
		dp.setPlacementPingouinTermine(placementPingouinTermine);
		dp.setRefairePossible(refairePossible);
		Terrain copieT = terrain.copie();
		dp.setTerrain(copieT);
		
		for(int i = 0; i < dp.getNbJoueurs(); i++){
			copyTabJ[i].setTerrain(copieT);
		}
		
		for(int j = 0; j < nbJoueurs; j++){
			Pingouin [] p = copyTabJ[j].getMesPingouins();
			for(int k = 0; k < copyTabJ[j].getMaxPingouins(); k++){
				if(p[k]!=null)
				{
					Coordonnees c = p[k].getCoordonnees();
					Tuile t = copieT.consulter(c.getI(), c.getJ());
					if(t != null)
						t.setPingouin(p[k]);				
				}
			}
		}
		dp.setJoueurs(copyTabJ);
		dp.setVitesseAnim(this.vitesseAnim);
		
		return dp;
	}
	
	/**
	 * Renvoie la reference au joueur courant
	 * @return
	 */
	public Joueur joueurCourant()
	{
		return joueurs[joueurCourant];
	}
	
	/**
	 * Renvoie le numero du joueur precedent
	 * @return
	 */
	public int getNumJoueurPrecedent()
	{
		return numJoueurPrecedent;
	}
	
	/**
	 * Renvoie le numero du joueur precedent
	 * @return
	 */
	public void setNumJoueurPrecedent(int num){
		numJoueurPrecedent = num;
	}
	
	/**
	 * Renvoie la reference au joueur precedent
	 * @return
	 */
	public Joueur joueurPrecedent()
	{
		return joueurs[getNumJoueurPrecedent()];
	}
	
	/**
	 * retourne vrai si les pengouins de chaque joueurs sont places
	 * sinon faux
	 * @return
	 */
	public boolean isPlacementTermine()
	{
		boolean tousPlaces = true;
		
		for(int i = 0; i < nbJoueurs; i++){
			tousPlaces &= joueurs[i].getPengouinsPlaces();
		}
//		System.out.println("[donnees] placement termine : " + tousPlaces);
		return tousPlaces;
	}
	
	/**
	 * retourne vrai si tous les joueurs sont des ordis
	 * @return
	 */
	public boolean tousOrdis()
	{
		boolean tousOrdi = true;
		for (int i = 0; i < joueurs.length; i++) 
		{
			tousOrdi = tousOrdi && joueurs[i].estOrdi();
		}
		return tousOrdi;
	}
	
	public int getMaxPingouins()
	{
		return (nbJoueurs==2) ? 4 : ((nbJoueurs==3) ? 3 : ((nbJoueurs==4) ? 2 : 0));
	}
	
	/**
	 * renvoie le joueur courant selon le nombre de pingouiins qu'il reste a placer pour chaque joueur
	 * @return
	 */
	public int etablirJoueurCourant()
	{
		int maxPing, numJMax = 0;
		maxPing = joueurs[numJMax].getNbPingouins();
		for (int i = 0; i < nbJoueurs; i++) 
		{
			if (joueurs[i].getNbPingouins() > maxPing)
			{
				maxPing = joueurs[i].getNbPingouins();
				numJMax = i;
			}
		}
		return numJMax;
	}

	public boolean isOptionAffichageConfirmation() {
		return optionAffichageConfirmation;
	}

	public void setOptionAffichageConfirmation(boolean optionAffichageConfirmation) {
		this.optionAffichageConfirmation = optionAffichageConfirmation;
	}

	public boolean isFichierExiste() {
		return fichierExiste;
	}

	public void setFichierExiste(boolean fichierExiste) {
		this.fichierExiste = fichierExiste;
	}

	public boolean isSpeakerActif() {
		return speakerActif;
	}

	public void setSpeakerActif(boolean speakerActif) {
		this.speakerActif = speakerActif;
	}

	public boolean isMusicActive() {
		return musicActive;
	}

	public void setMusicActive(boolean musicActive) {
		this.musicActive = musicActive;
	}

	public Coordonnees getCoordConseilPingouinInitial() {
		return coordConseilPingouinInitial;
	}

	public void setCoordConseilPingouinInitial(
			Coordonnees coordConseilPingouinInitial) {
		this.coordConseilPingouinInitial = coordConseilPingouinInitial;
	}

	public Coordonnees getCoordConseilPingouinDep() {
		return coordConseilPingouinDep;
	}

	public void setCoordConseilPingouinDep(Coordonnees coordConseilPingouinDep) {
		this.coordConseilPingouinDep = coordConseilPingouinDep;
	}

	public Coordonnees getCoordConseilPingouinArr() {
		return coordConseilPingouinArr;
	}

	public void setCoordConseilPingouinArr(Coordonnees coordConseilPingouinArr) {
		this.coordConseilPingouinArr = coordConseilPingouinArr;
	}


	public boolean isAnimationEnCours() {
		return animationEnCours;
	}


	public void setAnimationEnCours(boolean animationEnCours) {
		this.animationEnCours = animationEnCours;
	}


	public boolean isAnimationPingouinPlace() {
		return animationPingouinPlace;
	}


	public void setAnimationPingouinPlace(boolean animationPingouinPlace) {
		this.animationPingouinPlace = animationPingouinPlace;
	}


	public Point getCoordonneesAnimation() {
		return coordonneesAnimation;
	}


	public void setCoordonneesAnimation(Point coordonneesAnimation) {
		this.coordonneesAnimation = coordonneesAnimation;
	}


	public Image getImageAnimation() {
		return imageAnimation;
	}


	public void setImageAnimation(Image imageAnimation) {
		this.imageAnimation = imageAnimation;
	}
	
	/**
	 * @return the optionCommentJouer
	 */
	public boolean isOptionCommentJouer() {
		return optionCommentJouer;
	}

	/**
	 * @param optionCommentJouer the optionCommentJouer to set
	 */
	public void setOptionCommentJouer(boolean optionCommentJouer) {
		this.optionCommentJouer = optionCommentJouer;
	}


	public boolean isViderPile() {
		return viderPile;
	}


	public void setViderPile(boolean viderPile) {
		this.viderPile = viderPile;
	}


}
