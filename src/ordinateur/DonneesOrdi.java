/**
 * Auteur : Jeremy et Carole
 * Description : Contient les donnees pour le calcul des coups suivants lors du choix d'un coup
 * de l'ordinateur
 * (copie modifiee de donnees partagees)
 */

package ordinateur;
import java.util.ArrayList;

import liaison.Coordonnees;
import liaison.DonneesPartagees;
import moteur.Joueur;
import moteur.Pingouin;
import moteur.Terrain;
import moteur.Tuile;

public class DonneesOrdi
{
	
	public DonneesOrdi(DonneesOrdi donneesO)
	{
		this.terrain = donneesO.getTerrain().copie();
		this.nbJoueurs = donneesO.getNbJoueurs();
		this.joueurs = new Joueur[this.nbJoueurs];
		Pingouin[] pingouinsJoueur;
		ArrayList<Pingouin> listPingouins = new ArrayList<Pingouin>();
		Pingouin pingouinOriginal;
		
		// copie des joueurs
		for(int i = 0 ; i < this.nbJoueurs ; i++)
		{
			joueurs[i] = donneesO.getJoueurs()[i].copie();
			// mise a jour de la reference des joueurs vers le nouveau terrain copie
			joueurs[i].setTerrain(this.terrain);
			// copie des pingouins des joueurs
			listPingouins = donneesO.getJoueurs()[i].getPengouins();
			pingouinsJoueur = new Pingouin[donneesO.getJoueurs()[i].getMaxPingouins()];
			for(int p = 0 ; p < donneesO.getJoueurs()[i].getNbPingouins() ; p++)
			{
				pingouinOriginal = listPingouins.get(p);
				Coordonnees position = pingouinOriginal.getCoordonnees().copie();
				pingouinsJoueur[p] = new Pingouin(pingouinOriginal.getNumero(), joueurs[i], position);
				this.terrain.consulter(position.getI(), position.getJ()).setPingouin(pingouinsJoueur[p]);
			}	
			joueurs[i].setMesPingouins(pingouinsJoueur);
			joueurs[i].setNbPingouins(donneesO.getJoueurs()[i].getNbPingouins());
		}
		
		this.jeuFini = false;
		this.matchNul = false;
		this.gagnant = null;
		this.optionCasesAccessibles = true;
		this.joueurCourant = donneesO.getJoueurCourant();
	}
	
	/*
	 * 	creation des nouvelles donnees a partir des donnees partagees
	 * 	permet de mettre a jour les donnees du moteur ordi pour le calcul
	 *  des coups suivants 
	 */
	public DonneesOrdi(DonneesPartagees donnees)
	{
		this.terrain = donnees.getTerrain().copie();
		this.nbJoueurs = donnees.getNbJoueurs();
		this.joueurs = new Joueur[this.nbJoueurs];
		Pingouin[] pingouinsJoueur;
		ArrayList<Pingouin> listPingouins = new ArrayList<Pingouin>();
		Pingouin pingouinOriginal;
		
		// copie des joueurs
		for(int i = 0 ; i < this.nbJoueurs ; i++)
		{
			joueurs[i] = donnees.getJoueurs()[i].copie();
			// mise a jour de la reference des joueurs vers le nouveau terrain copie
			joueurs[i].setTerrain(this.terrain);
			// copie des pingouins des joueurs
			listPingouins = donnees.getJoueurs()[i].getPengouins();
			pingouinsJoueur = new Pingouin[donnees.getJoueurs()[i].getMaxPingouins()];
			for(int p = 0 ; p < donnees.getJoueurs()[i].getNbPingouins() ; p++)
			{
				pingouinOriginal = listPingouins.get(p);
				Coordonnees position = pingouinOriginal.getCoordonnees().copie();
				pingouinsJoueur[p] = new Pingouin(pingouinOriginal.getNumero(), joueurs[i], position);
				this.terrain.consulter(position.getI(), position.getJ()).setPingouin(pingouinsJoueur[p]);
			}	
			joueurs[i].setMesPingouins(pingouinsJoueur);
			joueurs[i].setNbPingouins(donnees.getJoueurs()[i].getNbPingouins());
		}
		
		this.jeuFini = false;
		this.matchNul = false;
		this.gagnant = null;
		this.optionCasesAccessibles = true;
		this.joueurCourant = donnees.getJoueurCourant();
	}

	// terrain de jeu
	private Terrain terrain;

	// tableau des joueurs
	private Joueur[] joueurs;

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

	// nombre de joueurs
	private int nbJoueurs;

	public int getNbJoueurs() {
		return nbJoueurs;
	}

	public void setNbJoueurs(int nbJoueurs) {
		this.nbJoueurs = nbJoueurs;
	}

	private int joueurCourant;

	public boolean isMatchNul() {
		return matchNul;
	}

	public void setMatchNul(boolean matchNul) {
		this.matchNul = matchNul;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	/*
	 * mise a jour du terrain (y compris les references des joueurs)
	 */
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
		for(int j = 0 ; j < this.nbJoueurs ; j++)
			joueurs[j].setTerrain(terrain);
	}

	public Joueur[] getJoueurs() {
		return joueurs;
	}

	public void setJoueurs(Joueur[] joueurs) {
		this.joueurs = joueurs;
	}

	public boolean isOptionCasesAccessibles() {
		return optionCasesAccessibles;
	}

	public void setOptionCasesAccessibles(boolean optionCasesAccessibles) {
		this.optionCasesAccessibles = optionCasesAccessibles;
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
		return jeuFini;
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
	
	/**
	 * suppression des tuiles presentes dans les composantes inutiles
	 * passees en parametres
	 * @param composantesInutiles : liste de composantes sans interet pour le joueur courant
	 */
	public void suppressionComposantes(ArrayList<Composante> composantesInutiles)
	{
		ArrayList<Tuile> tuiles;
		ArrayList<Pingouin> pingouins;
		// parcours des composantes
		for(int c = 0 ; c < composantesInutiles.size() ; c++)
		{
			tuiles = composantesInutiles.get(c).getTuiles();
			for(int t = 0 ; t < tuiles.size() ; t++)
			{
				// si la tuile n'est pas occupee on la supprime
				if(!tuiles.get(t).estOccupee())
					terrain.supprimerTuile(tuiles.get(t).getLigne(), tuiles.get(t).getColonne());
			}
			pingouins = composantesInutiles.get(c).getPingouins();
			for(int p = 0 ; p < pingouins.size() ; p++)
			{
				// on ne supprime pas une tuile avec un pingouin, mais on le marque comme etant bloque
				pingouins.get(p).setBloque();
				pingouins.get(p).getJoueur().pingouinEnMoins();
			}
		}
	}
	
	/**
	 * Retourne une copie des donnees
	 * NON IMPLEMENTE, OBSOLETE A REFAIRE
	 * @return
	 */
	public DonneesOrdi copie()
	{
		
		return new DonneesOrdi(this);
	}
	
	public String toString()
	{
		String res = "Donnees ordi : \n";
		res += "annuler possible : ";
		res += this.annulerPossible + "\n";
		res += "gagnant : ";
		res += this.gagnant + "\n";
		res += "jeu fini : ";
		res += this.jeuFini + "\n";
		res += "joueur courant : ";
		res += this.joueurCourant + "\n";
		res += "nbJoueurs : ";
		res += this.nbJoueurs + "\n";
		res += "joueurs : \n";
		for(int i = 0 ; i < nbJoueurs ; i++)
		{
			res += "est ordi : ";
			res += joueurs[i].estOrdi();
			res += "numero : ";
			res += joueurs[i].getNumero();
			res += "liste de pingouins : ";
			res += joueurs[i].getPengouins();
			res += "terrain du joueur \n";
			joueurs[i].getTerrain().afficher();
		}
		res += "match nul : ";
		res += this.matchNul + "\n";
		res += "refaire possible : ";
		res += this.refairePossible + "\n";
		this.terrain.afficher();
		return res;
	}
	
}
