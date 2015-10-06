package ordinateur;

import java.util.ArrayList;
import java.util.LinkedList;

import moteur.*;

public class Graphe 
{
	private DonneesOrdi donnees;
	// liste des composantes du graphe
	private ArrayList<Composante> composantes;
	// matrice indiquant les tuiles ajoutees dans une composante
	private boolean[][] tableRestantes;
	// indique si tous les pingouins du joueur actuel sont isoles
	private boolean modeRecup;
	// ilots du joueur (composantes ou le joueur est present sans adversaire)
	private ArrayList<Composante> ilots;
	// ilots du joueur (composantes ou le joueur est present sans adversaire)
	private ArrayList<Composante> ilotsAdverses;
	// composantes ou le joueur est present
	private ArrayList<Composante> composantesJoueur;
	ArrayList<Composante> composantesInutiles;
	ArrayList<Composante> composantesUtiles;
	
	public Graphe(DonneesOrdi donnees)
	{
		composantes = new ArrayList<Composante>();
		composantesJoueur = new ArrayList<Composante>();
		ilots = new ArrayList<Composante>();
		ilotsAdverses = new ArrayList<Composante>();
		composantesInutiles = new ArrayList<Composante>();
		composantesUtiles = new ArrayList<Composante>();
		this.donnees = donnees;
		// construction du graphe a partir des donnees
		construire();
	}

	public ArrayList<Composante> getComposantes() {
		return composantes;
	}

	public void setComposantes(ArrayList<Composante> composantes) {
		this.composantes = composantes;
	}
	
	/*
	 * construit le graphe a partir des donnees
	 * calcule la liste des composantes
	 */
	public void construire()
	{
		Terrain terrain = donnees.getTerrain();
		// initialisation de la matrice de marquage des tuiles
		creerTable();
		// parcours des tuiles du terrain
		// des qu'on trouve une tuile qui n'est pas dans une composante on cree une nouvelle composante
		for(int i = 0 ; i < terrain.getHauteur() ; i += 2)
		{
			for(int j = 1 ; j < terrain.getLargeur() ; j += 2)
			{
				if(tableRestantes[i][j])
					this.creerComposante(i, j);
			}
		}

		for(int i = 1 ; i < terrain.getHauteur() ; i += 2)
		{
			for(int j = 0 ; j < terrain.getLargeur() ; j += 2)
			{
				if(tableRestantes[i][j])
					this.creerComposante(i, j);
			}
		}
	}
	
	/*
	 * initialise la matrice de marquage des tuiles
	 */
	public void creerTable()
	{
		Terrain terrain = donnees.getTerrain();
		tableRestantes = new boolean[terrain.getHauteur()][terrain.getLargeur()];
		for(int i = 0 ; i < terrain.getHauteur() ; i += 2)
		{
			for(int j = 1 ; j < terrain.getLargeur() ; j += 2)
			{
				if(terrain.consulter(i, j) != null)
				{
					tableRestantes[i][j] = true;
				}
			}
		}

		for(int i = 1 ; i < terrain.getHauteur() ; i += 2)
		{
			for(int j = 0 ; j < terrain.getLargeur() ; j += 2)
			{
				if(terrain.consulter(i, j) != null)
				{
					tableRestantes[i][j] = true;
				}
			}
		}
	}
	
	/*
	 * cree une composante a partir des coordonnees d'une tuile
	 * en propageant aux cases alentour avec une file
	 */
	public void creerComposante(int ligne, int colonne)
	{
		Terrain terrain = donnees.getTerrain();
		LinkedList<Tuile> file = new LinkedList<Tuile>();
		ArrayList<Tuile> accessibles;
		Tuile tuileActu;
		int iActu;
		int jActu;
		Composante composante;
		Pingouin p;
		boolean joueur = false;
		boolean advers = false;
		
		Tuile initial = terrain.consulter(ligne, colonne);
		Tuile tmp;
		// ajout de la premiere tuile dans la file
		file.add(initial);
		// marquage de la premiere tuile
		tableRestantes[ligne][colonne] = false;
		// creation d'une composante
		composante = new Composante(donnees);
		
		// tant que la file n'est pas vide on retire une tuile
		while(!file.isEmpty())
		{
			// defilage d'une tuile
			tuileActu = file.poll();
			// on ajoute la tuile actuelle dans la composante
			composante.addTuile(tuileActu);
			// recuperation des coordonnees
			iActu = tuileActu.getLigne();
			jActu = tuileActu.getColonne();
			// marquage de la tuile actuelle
			tableRestantes[iActu][jActu] = false;
			
			// si il y a un pingouin
			if(tuileActu.estOccupee())
			{
				p = tuileActu.getPingouin();
				// si le pingouin n'est pas dans la composante on l'ajoute
				if(!composante.getPingouins().contains(p))
				{
					if(utils.tuilesAccessibles(tuileActu, donnees.getTerrain()).size() != 0)
					{
						if(p.getJoueur().getNumero() == donnees.getJoueurCourant())
						{
							composante.addPingouinJoueur(p);
							joueur = true;
						}
						else
							advers = true;
						// ajout du pingouin
						composante.addPingouin(p);
						// ajout de la tuile du pingouin
						composante.addTuilePingouin(tuileActu);
					}
				}
			}
			
			// recuperation des tuiles accessibles a partir de la tuile actuelle
			accessibles = tuilesAccessibles(tuileActu, terrain);
			for(int i = 0 ; i < accessibles.size() ; i++)
			{
				// si la tuile accessible n'a pas ete marquee on l'ajoute dans la file
				tmp = accessibles.get(i);
				if(tableRestantes[tmp.getLigne()][tmp.getColonne()])
				{
					file.offer(tmp);
				}
			}
		}
		// ajout de la composante dans la liste
		composantes.add(composante);
		if(joueur)
		{
			composantesJoueur.add(composante);
			composante.setPresenceJoueur(true);
		}
		if(joueur && !advers)
		{
			ilots.add(composante);
			composante.setIlotJoueur(true);
		}
		if(!joueur && advers)
		{
			ilotsAdverses.add(composante);
			composante.setIlotAdverse(true);
		}
		
	}
	
	
	/*
	 *  recuperation des successeurs d'une tuile (direction NE)
	 */
		public  static ArrayList<Tuile> suivantsNE(Tuile t, Terrain terrain)
		{
			ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();

			int ligne = t.getLigne()-1;
			int colonne = t.getColonne()+1;
			while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null){
				listeTuiles.add(terrain.consulter(ligne, colonne));
				ligne--;
				colonne++;
			}
			return listeTuiles;
		}

		//Retourne les tuiles accessibles depuis la tuile t direction  NO
		public static ArrayList<Tuile> suivantsNO(Tuile t, Terrain terrain){
			ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();

			int ligne = t.getLigne()-1;
			int colonne = t.getColonne()-1;
			while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null){
				listeTuiles.add(terrain.consulter(ligne, colonne));
				ligne--;
				colonne--;
			}
			return listeTuiles;
		}

		//Retourne les tuiles accessibles depuis la tuile t direction  SE
		public static ArrayList<Tuile> suivantsSE(Tuile t, Terrain terrain){
			ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();

			int ligne = t.getLigne()+1;
			int colonne = t.getColonne()+1;
			while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null){
				listeTuiles.add(terrain.consulter(ligne, colonne));
				ligne++;
				colonne++;
			}
			return listeTuiles;
		}

		//Retourne les tuiles accessibles depuis la tuile t direction  SO
		public static ArrayList<Tuile> suivantsSO(Tuile t, Terrain terrain){
			ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();

			int ligne = t.getLigne()+1;
			int colonne = t.getColonne()-1;
			while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null){
				listeTuiles.add(terrain.consulter(ligne, colonne));
				ligne++;
				colonne--;
			}
			return listeTuiles;
		}

		//Retourne les tuiles accessibles depuis la tuile t direction  NE
		public static ArrayList<Tuile> suivantsE(Tuile t, Terrain terrain){
			ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();
			int ligne = t.getLigne();
			int colonne = t.getColonne()+2;
			while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null){
				listeTuiles.add(terrain.consulter(ligne, colonne));
				colonne += 2;
			}
			return listeTuiles;
		}

		//Retourne les tuiles accessibles depuis la tuile t direction  O
		public ArrayList<Tuile> suivantsO(Tuile t, Terrain terrain ){
			ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();

			int ligne = t.getLigne();
			int colonne = t.getColonne()-2;
			while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null){
				listeTuiles.add(terrain.consulter(ligne, colonne));
				colonne -= 2;
			}
			return listeTuiles;
		}	

	    // recuperation de la liste des tuiles accessibles (toutes directions)
		public ArrayList<Tuile> tuilesAccessibles(Tuile t, Terrain terrain)
		{
			ArrayList<Tuile> list = new ArrayList<Tuile>();
			list.addAll(suivantsNE(t, terrain));
			list.addAll(suivantsNO(t, terrain));
			list.addAll(suivantsSE(t, terrain));
			list.addAll(suivantsSO(t, terrain));
			list.addAll(suivantsE(t, terrain));
			list.addAll(suivantsO(t, terrain));
			return list;
		}
		
		/*
		 * retourne la liste des composantes inutiles pour le joueur actuel
		 */
		public ArrayList<Composante> getComposantesInutiles()
		{
			this.modeRecup = (ilots.size() == composantesJoueur.size());
			for(int c = 0 ; c < composantes.size() ; c++)
			{
				if(!composantes.get(c).presenceJoueur)
					composantesInutiles.add(composantes.get(c));
				else if(composantes.get(c).ilotJoueur && !modeRecup)
					composantesInutiles.add(composantes.get(c));
				else
					composantesUtiles.add(composantes.get(c));
			}
			return composantesInutiles;
		}
		
		public ArrayList<Composante> getComposantesUtiles() {
			return composantesUtiles;
		}

		public void setComposantesUtiles(ArrayList<Composante> composantesUtiles) {
			this.composantesUtiles = composantesUtiles;
		}

		public boolean isModeRecup() {
			return modeRecup;
		}
		
		public int evaluerIlots()
		{
			int valJoueur = 0;
			int valAdverse = 0;
			for(int c = 0 ; c < ilots.size() ; c++)
			{
				valJoueur += ilots.get(c).getNbPoissons();
			}
			for(int c = 0 ; c < ilotsAdverses.size() ; c++)
			{
				valAdverse += ilotsAdverses.get(c).getNbPoissons();
			}
			Joueur courant = donnees.getJoueurs()[donnees.getJoueurCourant()];
			if(courant.getDifficulte() == 3)
			{
				valJoueur += 2*courant.getNombrePoissons();
				for(int j = 0 ; j < donnees.getNbJoueurs() ; j++)
				{
					if(j != donnees.getJoueurCourant())
					{
						valAdverse += 2*donnees.getJoueurs()[j].getNombrePoissons();
					}
				}
			}
			return valJoueur - valAdverse;
		}

		public ArrayList<Composante> getIlotsAdverses() 
		{
			return ilotsAdverses;
		}
		
		public ArrayList<Composante> getIlots() {
			return ilots;
		}

		public String toString()
		{
			String res = "";
			for(int i = 0 ; i < composantes.size() ; i++)
			{
				res += "composante numero : " + i + "\n";
				res += composantes.get(i).toString() + "\n";
				res += "liste de pingouins : \n";
				for(int p = 0 ; p < composantes.get(i).getPingouins().size() ; p++)
				{
					res += "pingouin numero " + p + " : " + composantes.get(i).getPingouins().get(p) + "\n";
				}
			}
			return res;
		}
}
