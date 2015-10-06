package ordinateur;

/**
 * Jeremy et Carole
 * classe representant une zone defendue par un ou plusieurs pingouins
 */

import java.util.ArrayList;
import java.util.HashSet;

import moteur.*;

// represente une zone defendue par au moins un Pingouin du joueur
public class Zone 
{
	DonneesOrdi donnees;
	// tuiles de la zone
	ArrayList<Tuile> tuiles;
	// tuiles accessibles par au moins un Pingouin du joueur
	ArrayList<Tuile> tuilesAcc;
	// tuiles accessibles par au moins un Pingouin d'un joueur adverse
	ArrayList<Tuile> tuilesAccEnnemi;
	// liste des Pingouins du joueur presents dans la zone
	ArrayList<Pingouin> Pingouins;
	// liste de tous les Pingouins du joueur
	ArrayList<Pingouin> PingouinsJoueur;
	// liste des Pingouins ennemis pouvant atteindre la zone directement
	ArrayList<Pingouin> PingouinsEnnemi;
	// numero du joueur courant
	int numJoueur;
	// nombre total de poissons
	int nbPoissons;
	// nombre total de poissons accessibles par le joueur
	int nbPoissonsAcc;
	// pourcentage de poissons accessibles par le joueur
	double pourcentPoissonsAcc;
	// nombre de poissons directement accessibles par un joueur adverse
	int nbPoissonsAccEnnemi;
	// pourcentage de poissons accessibles par un joueur adverse
	double pourcentPoissonsAccEnnemi;
	// nombre de tuiles dans la zone
	int nbTuiles;
	// nombre de tuiles accessibles par un Pingouin du joueur
	int nbTuilesAcc;
	// pourcentage de cases accessibles par les Pingouins du joueur presents dans la zone
	double pourcentCasesAcc;
	// nombre de tuiles accessibles par un Pingouin adverse
	int nbTuilesAccEnnemi;
	// pourcentage de tuiles accessibles par un Pingouin adverse
	double pourcentCasesAccEnnemi;
	// nombre de Pingouins adverse pouvant se rendre dans la zone en un coup
	int nbAttaquants;
	// nombre de Pingouins du joueur presents dans la zone
	int nbDefenseurs;
	
	/*
	 * cree la zone a partir de la liste de tuiles passee en parametres
	 * calcule les coefficients associes a la zone
	 */
	public Zone(ArrayList<Tuile> listeTuiles, DonneesOrdi donnees, int joueur)
	{
		this.donnees = donnees;
		this.numJoueur = joueur;
		this.tuiles = listeTuiles;
		this.PingouinsJoueur = donnees.getJoueurs()[this.numJoueur].getPengouins();
		this.estimerZone();
		// calcule toutes les statistiques sur les Pingouins ennemis
		this.estimerEnnemi();
	}

	public ArrayList<Tuile> getTuiles() {
		return tuiles;
	}

	public ArrayList<Tuile> getTuilesAcc() {
		return tuilesAcc;
	}

	public ArrayList<Tuile> getTuilesAccEnnemi() {
		return tuilesAccEnnemi;
	}

	public ArrayList<Pingouin> getPingouins() {
		return Pingouins;
	}

	public ArrayList<Pingouin> getPingouinsJoueur() {
		return PingouinsJoueur;
	}

	public ArrayList<Pingouin> getPingouinsEnnemi() {
		return PingouinsEnnemi;
	}

	public int getNumJoueur() {
		return numJoueur;
	}

	public int getNbPoissons() {
		return nbPoissons;
	}

	public int getNbPoissonsAcc() {
		return nbPoissonsAcc;
	}

	public double getPourcentPoissonsAcc() {
		return pourcentPoissonsAcc;
	}

	public int getNbPoissonsAccEnnemi() {
		return nbPoissonsAccEnnemi;
	}

	public double getPourcentPoissonsAccEnnemi() {
		return pourcentPoissonsAccEnnemi;
	}

	public int getNbTuiles() {
		return nbTuiles;
	}

	public int getNbTuilesAcc() {
		return nbTuilesAcc;
	}

	public double getPourcentCasesAcc() {
		return pourcentCasesAcc;
	}

	public int getNbTuilesAccEnnemi() {
		return nbTuilesAccEnnemi;
	}

	public double getPourcentCasesAccEnnemi() {
		return pourcentCasesAccEnnemi;
	}

	public int getNbAttaquants() {
		return nbAttaquants;
	}

	public int getNbDefenseurs() {
		return nbDefenseurs;
	}

	ArrayList<Pingouin> estimerZone()
	{
		ArrayList<Pingouin> list = new ArrayList<Pingouin>();
		for(int i = 0 ; i < tuiles.size() ; i++)
		{
			Tuile t = tuiles.get(i);
			if(t.estOccupee())
			{
				if(t.getNumeroJoueur() == numJoueur)
				{
					// il y a un Pingouin du joueur sur cette case
					list.add(PingouinsJoueur.get(t.getNumeroPingouin()));
				}
			}
			nbPoissons += t.getNbPoissons();
		}
		this.nbDefenseurs = list.size();
		return list;
	}
	
	
	//ACCESSEURS 
	/*
	 * renvoie la liste des Pingouins du joueur courant presents dans la zone
	 * calcule le nombre de defenseurs
	 */
	ArrayList<Pingouin> PingouinsZone()
	{
		ArrayList<Pingouin> list = new ArrayList<Pingouin>();
		for(int i = 0 ; i < tuiles.size() ; i++)
		{
			Tuile t = tuiles.get(i);
			if(t.estOccupee())
			{
				if(t.getNumeroJoueur() == numJoueur)
				{
					// il y a un Pingouin du joueur sur cette case
					list.add(PingouinsJoueur.get(t.getNumeroPingouin()));
				}
			}
		}
		this.nbDefenseurs = list.size();
		return list;
	}
	
	/*
	 *  calcule le nombre de poissons total de la zone
	 */
	int calculerNbPoissons()
	{
		int nb = 0;
		for(int i = 0 ; i < tuiles.size() ; i++)
		{
			nb += tuiles.get(i).getNbPoissons();
		}
		return nb;
	}
	
	/*
	 *  calcule le nombre de poissons accessibles dans la zone
	 */
	int calculerNbPoissonsAcc()
	{
		int nb = 0;
		for(int i = 0 ; i < this.tuilesAcc.size() ; i++)
		{
			nb += tuilesAcc.get(i).getNbPoissons();
		}
		return nb;
	}

	/*
	 * calcule la liste des tuiles accessibles par les Pingouins du joueur
	 */
	ArrayList<Tuile> tuilesAccessibles()
	{
		ArrayList<Tuile> list = new ArrayList<Tuile>();
		// recuperation des cases accessibles des Pingouins dans la zone
		for(int i = 0 ; i < Pingouins.size() ; i++)
		{
			list.addAll(utils.tuilesAccessibles(Pingouins.get(i), donnees.getTerrain()));
			// ajout de la case du Pingouin
			list.add(Pingouins.get(i).getTuile());
		}
		// suppression des tuiles en double dans la liste
        HashSet<Tuile> set = new HashSet<Tuile>() ;
        set.addAll(list) ;
        list = new ArrayList<Tuile>(set);
        // intersection avec la liste des cases de la zone
        list.retainAll(tuiles);
		return list;
	}
	
	/*
	 * renvoie la liste des Pingouins ennemis pouvant aller dans la zone
	 * calcule toutes les statistiques concernant les Pingouins adverses
	 */
	void estimerEnnemi()
	{
		this.PingouinsEnnemi = new ArrayList<Pingouin>();
		this.tuilesAccEnnemi = new ArrayList<Tuile>();
		ArrayList<Tuile> tuilesTmp = new ArrayList<Tuile>();
		
		// parcours des joueurs adverses
		for(int numJoueur = 0 ; numJoueur < donnees.getNbJoueurs() ; numJoueur++)
		{
			if(numJoueur != this.numJoueur)
			{
				// recuperation du joueur actuel a partir de son indice
				Joueur joueurActu = donnees.getJoueurs()[numJoueur]; 
				// liste des Pingouins du joueur actuel
				ArrayList<Pingouin> listPingouins = joueurActu.getPengouins();
				// parcours des Pingouins du joueur actuel
				for(int numPingouin = 0 ; numPingouin < listPingouins.size() ; numPingouin++)
				{
					// recuperation des cases accessibles du Pingouin ennemi
					tuilesTmp = utils.tuilesAccessibles(listPingouins.get(numPingouin), donnees.getTerrain());
					// on ne garde que les tuiles accessibles de l'ennemi dans la zone
					tuilesTmp.retainAll(tuiles);
					if(tuilesTmp.size() != 0)
					{
						// le Pingouin actuel peut aller dans la zone, on l'ajoute a la liste des Pingouins ennemis
						PingouinsEnnemi.add(listPingouins.get(numPingouin));
						// on ajoute les cases ou il peut aller a la liste des cases accessibles par l'ennemi
						tuilesAccEnnemi.addAll(tuilesTmp);
						// suppression des tuiles en double dans la liste
				        HashSet<Tuile> set = new HashSet<Tuile>() ;
				        set.addAll(tuilesAccEnnemi) ;
				        tuilesAccEnnemi = new ArrayList<Tuile>(set) ;
					}
				}
			}
		}
		
		this.nbTuilesAccEnnemi = tuilesAccEnnemi.size();
		this.nbAttaquants = PingouinsEnnemi.size();

		this.nbPoissonsAccEnnemi = 0;
		for(int i = 0 ; i < tuilesAccEnnemi.size() ; i++)
		{
			// on ajoute les poissons de la case au total
			nbPoissonsAccEnnemi += tuilesAccEnnemi.get(i).getNbPoissons();
		}
	}
	
	public int evaluer ()
	{
		return this.nbPoissons - this.nbPoissonsAccEnnemi;
	}
	
	public String toString()
	{
		String res = "";
		res += "zone : \n";
		res += "nombre de tuiles : " + nbTuiles + "\n";
		res += "liste de tuiles : \n";
		res += tuiles + "\n";
		res += "nombre de defenseurs : " + this.nbDefenseurs + "\n";
		res += "liste de defenseurs : \n";
		res += Pingouins + "\n";
		res += "nombre de tuiles accessibles : " + nbTuilesAcc + "\n";
		res += "tuiles accessibles : \n";
		res += tuilesAcc + "\n";
		res += "pourcentage de tuiles accessibles : " + pourcentCasesAcc + "\n";
		res += "nombre total de poissons : " + nbPoissons + "\n";
		res += "nombre de poissons accessibles : " + nbPoissonsAcc + "\n";
		res += "pourcentage de poissons accessibles : " + pourcentPoissonsAcc + "\n";
		res += "nombre d'attaquants : " + this.nbAttaquants + "\n";
		res += "liste de Pingouins attaquants : \n";
		res += PingouinsEnnemi + "\n";
		res += "nombre de cases accessibles par l'ennemi : " + this.nbTuilesAccEnnemi + "\n";
		res += "pourcentage de tuiles accessibles par l'ennemi : " + this.pourcentCasesAccEnnemi + "\n";
		res += "nombre de poissons accessibles par l'ennemi : " + this.nbPoissonsAccEnnemi + "\n";
		res += "pourcentage de poissons accessibles par l'ennemi : " + this.pourcentPoissonsAccEnnemi;
		return res;
	}
	
}
