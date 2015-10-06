package ordinateur;

/**
 * Jeremy et Carole
 * classe representant une composante d'un graphe du terrain
 */

import java.util.ArrayList;
import java.util.HashSet;
import moteur.Pingouin;
import moteur.Terrain;
import moteur.Tuile;

public class Composante 
{
	private int nbCheminParcouru = 0;
	static final int nbCheminsMax = 1000;
	
	private int nbPoissons;
	private DonneesOrdi donnees;
	// pingouins presents dans la composante
	private ArrayList<Pingouin> pingouins;
	// pingouins presents dans la composante
	private ArrayList<Pingouin> pingouinsJoueur;
	// liste des tuiles de la composante
	private ArrayList<Tuile> tuiles;
	// liste des tuiles ou sont presents les pingouins
	private ArrayList<Tuile> tuilesPingouins;
	// chemin solution pour obtenir toutes les tuiles
	ArrayList<Tuile> cheminSol;
	// nombre maximum de poissons pouvant etre recuperes
	Terrain terrainCopie;
	int maxPoissons;
	boolean cheminTrouve = false;
	boolean[][] marquage;
	ArrayList<Tuile> cheminTmp;
	Tuile debutCheminTmp;
	boolean ilotAdverse;
	boolean ilotJoueur;
	boolean presenceJoueur;

	public boolean isIlotAdverse() {
		return ilotAdverse;
	}

	public void setIlotAdverse(boolean ilotAdverse) {
		this.ilotAdverse = ilotAdverse;
	}

	public boolean isIlotJoueur() {
		return ilotJoueur;
	}

	public void setIlotJoueur(boolean ilotJoueur) {
		this.ilotJoueur = ilotJoueur;
	}

	public boolean isPresenceJoueur() {
		return presenceJoueur;
	}

	public void setPresenceJoueur(boolean presenceJoueur) {
		this.presenceJoueur = presenceJoueur;
	}
	public Composante(DonneesOrdi donnees)
	{
		this.donnees = donnees;
		this.pingouins = new ArrayList<Pingouin>();
		this.pingouinsJoueur = new ArrayList<Pingouin>();
		this.tuiles = new ArrayList<Tuile>();
		this.cheminSol = new ArrayList<Tuile>();
		this.tuilesPingouins = new ArrayList<Tuile>();
		this.maxPoissons = 0;
		this.nbPoissons = 0;
		this.marquage = new boolean[donnees.getTerrain().getHauteur()][donnees.getTerrain().getLargeur()];
		
	}
	
	/**
	 * renvoie le chemin permettant d'obtenir le maximum de poissons sur la composante
	 */
	public ArrayList<Tuile> getCheminSol(Terrain terrain) 
	{
		this.nbCheminParcouru = 0;
		this.terrainCopie = terrain.copie();
		this.solIlot(tuilesPingouins.get(0), new ArrayList<Tuile>(), terrain);
		return cheminSol;
	}

	public void addPingouinJoueur(Pingouin p)
	{
		pingouinsJoueur.add(p);
	}

	public ArrayList<Pingouin> getPingouinsJoueur() {
		return pingouinsJoueur;
	}

	/**
	 * 	 * calcule le chemin permettant de recuperer le maximum de poissons de la composante
	 * @param depart : tuile initiale
	 * @param chemin : chemin temporaire
	 * @param terrain : terrain actuel
	 * @return
	 */
	public int solIlot(Tuile depart, ArrayList<Tuile> chemin, Terrain terrain)
	{
		int nbPoissons = 0;
		// ajoute la tuile de depart au chemin 
		chemin.add(depart);
		int s = 0 ;

		// marquage de la case qu'on ajoute au chemin 
		marquage[depart.getLigne()][depart.getColonne()] = true;
		// recuperation des cases accessibles a partir du depart
		ArrayList<Tuile> succ = utils.tuilesAccessibles(depart, terrain); 
		if (!cheminTrouve && this.nbCheminParcouru < nbCheminsMax)
		{
			// si on n'a pas toutes les tuiles dans le chemin et qu'il y a des successeurs
			if (chemin.size() != this.tuiles.size() && succ.size() != 0)
			{
				terrain.supprimerTuile(depart.getLigne(), depart.getColonne());
				
				for (int i = 0 ; i < succ.size() ; i++)
				{
					Tuile tSucc = succ.get(i);
					// copie du chemin actuel
					ArrayList<Tuile> cheminCopie = new ArrayList<Tuile>();
					cheminCopie.addAll(chemin);
					// appel recursif a partir du successeur actuel 
					nbPoissons = solIlot(tSucc, chemin, terrain);
					// si on a recupere un plus grand nombre de poissons
					if(nbPoissons > maxPoissons)
					{
						// on sauvegarde ce chemin la
						this.cheminSol = new ArrayList<Tuile>();
						this.cheminSol.addAll(chemin);
						this.maxPoissons = nbPoissons;
						this.cheminTrouve = (this.cheminSol.size() == this.tuiles.size());
					}
					if (cheminTrouve || (this.debutCheminTmp != null && depart != this.debutCheminTmp))
					{
						break;
					}
					if (depart == this.debutCheminTmp)
					{
						this.debutCheminTmp = null;
						break;
					}
					chemin.clear();
					chemin.addAll(cheminCopie);
				}
				nbPoissons = maxPoissons;
				terrain.retablirTuile(depart);
				
			}
			else
			{
				// calcule du nombre de poissons que l'on peut recuperer en passant
				// par ce chemin 
				for(int i = 0 ; i < chemin.size() ; i++)
				{
					nbPoissons += chemin.get(i).getNbPoissons();
				}
				this.nbCheminParcouru++;
			}
			// demarquage de la case qu'on extrait du chemin 
			marquage[depart.getLigne()][depart.getColonne()] = false;
			return nbPoissons;
		}
		return 0;
	}

	public ArrayList<Tuile> getTuiles() {
		return tuiles;
	}

	public void setTuiles(ArrayList<Tuile> tuiles) {
		this.tuiles = tuiles;
	}

	public ArrayList<Pingouin> getPingouins() {
		return pingouins;
	}

	public void setPingouins(ArrayList<Pingouin> pingouins) {
		this.pingouins = pingouins;
	}
	
	public void addPingouin(Pingouin p)
	{
		pingouins.add(p);
	}
	
	public int getNbPoissons() {
		return nbPoissons;
	}

	public void addTuilePingouin(Tuile n)
	{
		this.tuilesPingouins.add(n);
	}
	
	public void addTuile(Tuile t)
	{
		if(!tuiles.contains(t))
		{
			this.nbPoissons += t.getNbPoissons();
			this.tuiles.add(t);
		}
	}
	
	/**
	 * permet de supprimer les tuiles en double de la composante
	 */
	public void suppDoublons()
	{
		HashSet<Tuile> set = new HashSet<Tuile>() ;
	    set.addAll(tuiles);
	    tuiles = new ArrayList<Tuile>(set);
	}
	
	public void analyseMarquage(ArrayList<Tuile> chemin)
	{
		this.cheminTmp = new ArrayList<Tuile>();
		int i = chemin.size() -1;
		boolean trouve = true;
		// parcours des elements du chemin en sens inverse 
		while (i > 0 && trouve)
		{
			int ligne = chemin.get(i).getLigne();
			int colonne = chemin.get(i).getColonne();
			// on verifie si les tuiles autour sont aussi marque
			// tuile au NO
			if(ligne > 0 && colonne > 0 && terrainCopie.consulter(ligne - 1, colonne - 1) != null && !terrainCopie.consulter(ligne - 1, colonne - 1).estOccupee())
			{
				trouve = marquage[ligne - 1][colonne -1];
			}
			// verif de la case en haut a droite
			if(trouve && ligne > 0 && colonne < terrainCopie.getLargeur() - 1 && terrainCopie.consulter(ligne - 1, colonne + 1) != null && !terrainCopie.consulter(ligne - 1, colonne + 1).estOccupee())
			{
				trouve = marquage[ligne - 1][colonne +1];
			}
			// verif de la case a gauche
			if(trouve && colonne > 1 && terrainCopie.consulter(ligne, colonne - 2) != null && !terrainCopie.consulter(ligne, colonne - 2).estOccupee())
			{
				trouve = marquage[ligne][colonne -2];
			}
			// verif de la case a droite
			if(trouve && colonne < terrainCopie.getLargeur() - 2 && terrainCopie.consulter(ligne, colonne + 2) != null && !terrainCopie.consulter(ligne, colonne + 2).estOccupee())
			{
				trouve = marquage[ligne][colonne +2];
			}
			// verif de la case en bas a gauche
			if(trouve && ligne < terrainCopie.getHauteur() - 1 && colonne > 0 && terrainCopie.consulter(ligne + 1, colonne - 1) != null && !terrainCopie.consulter(ligne + 1, colonne - 1).estOccupee())
			{
				trouve = marquage[ligne + 1][colonne -1];
			}
			// verif de la case en bas a droite
			if(trouve && ligne < terrainCopie.getHauteur() - 1 && colonne < terrainCopie.getLargeur() - 1 && terrainCopie.consulter(ligne + 1, colonne + 1) != null && !terrainCopie.consulter(ligne +  1, colonne + 1).estOccupee())
			{
				trouve = marquage[ligne + 1][colonne + 1];
			}
			if (trouve)
				i--;
		}
		if(i + 1 != chemin.size() - 1)
			debutCheminTmp = chemin.get(i+1);
			cheminTmp.addAll(chemin.subList(i+1, chemin.size()));
		
	}
	public String toString()
	{
		return tuiles.toString();
	}
	
	public void afficherMarquage()
	{
		for (int i = 0 ; i < donnees.getTerrain().getHauteur() ; i++)
		{
			System.out.print("  ");
			for (int j = 0 ; j < marquage[0].length ; j++)
			{
				if (terrainCopie.consulter(i, j) != null)
				{
					if (marquage[i][j])
						System.out.print("V  ");
					else
						System.out.print("F  ");
				}
				else
					System.out.print("   ");
			}
			System.out.print("\n");
		}
	}
}
