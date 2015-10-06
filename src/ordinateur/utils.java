package ordinateur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import liaison.Coordonnees;
import moteur.Joueur;
import moteur.Pingouin;
import moteur.Terrain;
import moteur.Tuile;

/******************\
 * Carole PLASSON
 * Jeremy WAMBECKE
 * classe :  utils
 * version : 1
 ******************
 * Listes de fonctions utiles pour les ordinateurs
 */
public class utils
{
	/*
	 *  cree la liste des cases libres ayant un seul poisson
	 */
	public static ArrayList<Coordonnees> casesLibresPlacement(Terrain t)
	{
		ArrayList<Coordonnees> list = new ArrayList<Coordonnees>();
		Tuile tuile;
		for(int i = 0; i < t.getHauteur(); i++)
		{
			for(int j = 0; j < t.getLargeur(); j++)
			{
				tuile = t.consulter(i, j);
				if(tuile != null)
				{
					if(!tuile.estOccupee() && tuile.getNbPoissons() == 1)
					{
						// ajout des coordonnees de la tuile dans la liste
						list.add(new Coordonnees(i, j));
					}
				}
			}
		}
		return list;
	}

	// compte le nombre de tuiles restantes dans le terrain
	public static int nbTuilesAccessibles (DonneesOrdi donnees)
	{
		int nb = 0;
		ArrayList<Tuile> liste;
		// parcours de tous les joueurs
		for (int j = 0 ; j < donnees.getNbJoueurs() ; j++)
		{
			// pour chaque pingouins du joueur, on calcul le nombre de
			// tuiles accessibles
			for (int i = 0 ; i < donnees.getJoueurs()[j].getNbPingouins() ; i++)
			{
				liste = tuilesAccessibles(donnees.getJoueurs()[j].getMesPingouins()[i], donnees.getTerrain());
				nb += liste.size();
			}
		}

		return (nb / donnees.getNbJoueurs());
	}
    // recuperation de la liste des pingouins non bloques
	public static ArrayList<Pingouin> pingouinsNonBloques(Joueur j)
	{
		ArrayList<Pingouin> list = new ArrayList<Pingouin>();
		for (int i = 0 ; i < j.getPengouins().size() ; i++)
		{
			if (! j.getPengouins().get(i).isBloque())
			{
				list.add(j.getPengouins().get(i));
			}
		}
		return list;

	}

	// recuperation des successeurs d'une tuile (direction NE)
	public  static ArrayList<Tuile> suivantsNE(Tuile t, Terrain terrain)
	{
		ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();

		int ligne = t.getLigne()-1;
		int colonne = t.getColonne()+1;
		while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null && !terrain.consulter(ligne, colonne).estOccupee()){
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
		while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null && !terrain.consulter(ligne, colonne).estOccupee()){
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
		while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null && !terrain.consulter(ligne, colonne).estOccupee()){
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
		while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null && !terrain.consulter(ligne, colonne).estOccupee()){
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
		while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null && !terrain.consulter(ligne, colonne).estOccupee()){
			listeTuiles.add(terrain.consulter(ligne, colonne));
			colonne += 2;
		}
		return listeTuiles;
	}

	//Retourne les tuiles accessibles depuis la tuile t direction  O
	public static ArrayList<Tuile> suivantsO(Tuile t, Terrain terrain ){
		ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();

		int ligne = t.getLigne();
		int colonne = t.getColonne()-2;
		while(0 <= ligne && ligne < terrain.getHauteur() && 0 <= colonne && colonne < terrain.getLargeur() && terrain.consulter(ligne, colonne) != null && !terrain.consulter(ligne, colonne).estOccupee()){
			listeTuiles.add(terrain.consulter(ligne, colonne));
			colonne -= 2;
		}
		return listeTuiles;
	}

    // recuperation de la liste des tuiles accessibles (toutes directions)
	public static ArrayList<Tuile> tuilesAccessibles(Pingouin p, Terrain terrain)
	{
		ArrayList<Tuile> list = new ArrayList<Tuile>();

		ArrayList<Tuile> listNE;
		ArrayList<Tuile> listNO;
		ArrayList<Tuile> listSE;
		ArrayList<Tuile> listSO;
		ArrayList<Tuile> listE;
		ArrayList<Tuile> listO;
		ArrayList<Integer> listSize = new ArrayList<Integer>();
		Tuile t = p.getTuile();

		listNE = suivantsNE(t, terrain);
		listSize.add(listNE.size());

		listNO = suivantsNO(t, terrain);
		listSize.add(listNO.size());

		listSE = suivantsSE(t, terrain);
		listSize.add(listSE.size());

		listSO = suivantsSO(t, terrain);
		listSize.add(listSO.size());

		listE = suivantsE(t, terrain);
		listSize.add(listE.size());

		listO = suivantsO(t, terrain);
		listSize.add(listO.size());

		int maxSize = Collections.max(listSize);
		for(int i = 0 ; i < maxSize ; i++)
		{
			if(i < listNE.size())
				list.add(listNE.get(i));

			if(i < listNO.size())
				list.add(listNO.get(i));

			if(i < listSE.size())
				list.add(listSE.get(i));

			if(i < listSO.size())
				list.add(listSO.get(i));

			if(i < listE.size())
				list.add(listE.get(i));

			if(i < listO.size())
				list.add(listO.get(i));
		}
		return list;
	}

	 // recuperation de la liste des tuiles accessibles (toutes directions)
		public static ArrayList<Tuile> tuilesAccessibles(Tuile t, Terrain terrain)
		{
			ArrayList<Tuile> list = new ArrayList<Tuile>();

			ArrayList<Tuile> listNE;
			ArrayList<Tuile> listNO;
			ArrayList<Tuile> listSE;
			ArrayList<Tuile> listSO;
			ArrayList<Tuile> listE;
			ArrayList<Tuile> listO;
			ArrayList<Integer> listSize = new ArrayList<Integer>();

			listNE = suivantsNE(t, terrain);
			listSize.add(listNE.size());

			listNO = suivantsNO(t, terrain);
			listSize.add(listNO.size());

			listSE = suivantsSE(t, terrain);
			listSize.add(listSE.size());

			listSO = suivantsSO(t, terrain);
			listSize.add(listSO.size());

			listE = suivantsE(t, terrain);
			listSize.add(listE.size());

			listO = suivantsO(t, terrain);
			listSize.add(listO.size());

			int maxSize = Collections.max(listSize);
			for(int i = 0 ; i < maxSize ; i++)
			{
				if(i < listNE.size())
					list.add(listNE.get(i));

				if(i < listNO.size())
					list.add(listNO.get(i));

				if(i < listSE.size())
					list.add(listSE.get(i));

				if(i < listSO.size())
					list.add(listSO.get(i));

				if(i < listE.size())
					list.add(listE.get(i));

				if(i < listO.size())
					list.add(listO.get(i));
			}
			return list;
		}

	// renvoie le nombre de successeurs d'une case de coordonnees coo
	public static int nbSucc(Coordonnees coo, Terrain terrain)
	{
		int nb = 0;
		int ligne = coo.getI();
		int colonne = coo.getJ();
		// verif de la case en haut a gauche
		if(ligne > 0 && colonne > 0 && terrain.consulter(ligne - 1, colonne - 1) != null && !terrain.consulter(ligne - 1, colonne - 1).estOccupee())
		{
			nb++;
		}
		// verif de la case en haut a droite
		if(ligne > 0 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne - 1, colonne + 1) != null && !terrain.consulter(ligne - 1, colonne + 1).estOccupee())
		{
			nb++;
		}
		// verif de la case a gauche
		if(colonne > 1 && terrain.consulter(ligne, colonne - 2) != null && !terrain.consulter(ligne, colonne - 2).estOccupee())
		{
			nb++;
		}
		// verif de la case a droite
		if(colonne < terrain.getLargeur() - 2 && terrain.consulter(ligne, colonne + 2) != null && !terrain.consulter(ligne, colonne + 2).estOccupee())
		{
			nb++;
		}
		// verif de la case en bas a gauche
		if(ligne < terrain.getHauteur() - 1 && colonne > 0 && terrain.consulter(ligne + 1, colonne - 1) != null && !terrain.consulter(ligne + 1, colonne - 1).estOccupee())
		{
			nb++;
		}
		// verif de la case en bas a droite
		if(ligne < terrain.getHauteur() - 1 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne + 1, colonne + 1) != null && !terrain.consulter(ligne +  1, colonne + 1).estOccupee())
		{
			nb++;
		}
		return nb;
	}

    // recherche des cases accessibles par p et ayant nb_poissons
	public static ArrayList<Tuile> rechercheCaseNbpoissons (Pingouin p, int nb_poissons, Terrain terrain)
	{
		// liste des tuiles accessibles
		ArrayList<Tuile> list = tuilesAccessibles(p, terrain);
		// creation d'une liste contenant toutes les tuiles avec nb_poissons
		ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();
		// parcours de la liste a la recherche d'une tuile ayant nb_poissons
		for (int i = 0 ; i < list.size() ; i++)
		{
			// si la tuile a nb_poissons
			if (list.get(i).getNbPoissons() == nb_poissons)
			{
				// Recuperation des coordonnees du pingouin
				Coordonnees coo = new Coordonnees(list.get(i).getLigne(), list.get(i).getColonne());
				// si la tuile a au moins 2 successeurs
				if (nbSucc(coo, terrain) > 0)
				{
					listeTuiles.add(list.get(i));
				}
			}
		}
		return listeTuiles;
	}

	// renvoie la tuile de valeur max autour du pingouin
	public static Tuile tuileMaxAutour(Pingouin p, Terrain terrain)
	{
		ArrayList<Tuile> listeTuiles;
		int nbPoissonsMax = 0;
		int ligne = p.getTuile().getLigne();
		int colonne = p.getTuile().getColonne();
		int i;
		// creation d'une liste contenant les tuiles autour du pingouin
		listeTuiles = new ArrayList<Tuile>();

		// verif de la case en haut a gauche
		if(ligne > 0 && colonne > 0 && terrain.consulter(ligne - 1, colonne - 1) != null && !terrain.consulter(ligne - 1, colonne - 1).estOccupee())
		{
			// mise a jour si besoin du nombre de pingouins max
			nbPoissonsMax = terrain.consulter(ligne - 1, colonne -1).getNbPoissons();
			// insertion de la tuile
			listeTuiles.add(terrain.consulter(ligne - 1, colonne -1));
		}
		// verif de la case en haut a droite
		if(ligne > 0 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne - 1, colonne + 1) != null && !terrain.consulter(ligne - 1, colonne + 1).estOccupee())
		{
			// insertion de la tuile
			listeTuiles.add(terrain.consulter(ligne - 1, colonne + 1));
			// mise a jour si besoin du nombre de pingouins max
			if (terrain.consulter(ligne - 1, colonne + 1).getNbPoissons() > nbPoissonsMax)
			{
				nbPoissonsMax = terrain.consulter(ligne - 1, colonne + 1).getNbPoissons();
			}
		}
		// verif de la case a gauche
		if(colonne > 1 && terrain.consulter(ligne, colonne - 2) != null && !terrain.consulter(ligne, colonne - 2).estOccupee())
		{
			// insertion de la tuile
			listeTuiles.add(terrain.consulter(ligne, colonne - 2));
			// si on a trouve un nouveau max, mise a jour de nbPoissonsMax
			if (terrain.consulter(ligne, colonne - 2).getNbPoissons() > nbPoissonsMax)
			{
				nbPoissonsMax = terrain.consulter(ligne, colonne - 2).getNbPoissons();
			}
		}
		// verif de la case a droite
		if(colonne < terrain.getLargeur() - 2 && terrain.consulter(ligne, colonne + 2) != null && !terrain.consulter(ligne, colonne + 2).estOccupee())
		{
			// insertion de la tuile
			listeTuiles.add(terrain.consulter(ligne, colonne + 2));
			// si on a trouve un nouveau max, mise a jour de nbPoissonsMax
			if (terrain.consulter(ligne, colonne + 2).getNbPoissons() > nbPoissonsMax)
			{
				nbPoissonsMax = terrain.consulter(ligne, colonne + 2).getNbPoissons();
			}
		}
		// verif de la case en bas a gauche
		if(ligne < terrain.getHauteur() - 1 && colonne > 0 && terrain.consulter(ligne + 1, colonne - 1) != null && !terrain.consulter(ligne + 1, colonne - 1).estOccupee())
		{
			// insertion de la tuile
			listeTuiles.add(terrain.consulter(ligne + 1, colonne - 1));
			// si on a trouve un nouveau max, mise a jour de nbPoissonsMax
			if (terrain.consulter(ligne + 1, colonne - 1).getNbPoissons() > nbPoissonsMax)
			{
				nbPoissonsMax = terrain.consulter(ligne + 1, colonne - 1).getNbPoissons();
			}
		}
		// verif de la case en bas a droite
		if(ligne < terrain.getHauteur() - 1 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne + 1, colonne + 1) != null && !terrain.consulter(ligne +  1, colonne + 1).estOccupee())
		{
			// insertion de la tuile
			listeTuiles.add(terrain.consulter(ligne + 1, colonne + 1));
			// si on a trouve un nouveau max, mise a jour de nbPoissonsMax
			if (terrain.consulter(ligne + 1, colonne + 1).getNbPoissons() > nbPoissonsMax)
			{
				nbPoissonsMax = terrain.consulter(ligne + 1, colonne + 1).getNbPoissons();
			}
		}
//		System.out.println("nbPoissonsMax = " + nbPoissonsMax);
		i = 0;
		while (i < listeTuiles.size() && listeTuiles.get(i).getNbPoissons() != nbPoissonsMax)
		{
			i++;
		}
//		System.out.println("la tuile max est : " + listeTuiles.get(i));
		return listeTuiles.get(i);
	}

    // Calcul des coordonnees de la prochaine case (cese ayant le plus de poissons et ayant au moins 2 successeurs)
	public static Coordonnees rechercheCase(Pingouin p, Terrain terrain)
	{
		Random r = new Random(44);
		Coordonnees coo;
		int nbPoissons = 3;
		int indiceTuile;
		Tuile tMax;
		ArrayList<Tuile> listeTuiles;
		// on verifie en premier s'il existe une case a 3 poissons ayant au moins 2 successeurs
		// s'il en existe
		if ((listeTuiles = rechercheCaseNbpoissons(p, nbPoissons, terrain)).size() !=  0)
		{
			indiceTuile = r.nextInt(listeTuiles.size());
			coo = new Coordonnees(listeTuiles.get(indiceTuile).getLigne(), listeTuiles.get(indiceTuile).getColonne());
		}
		// s'il n'y en a pas a 3 poissons on regarde s'il y en a avec 2
		else if ((listeTuiles = rechercheCaseNbpoissons(p, --nbPoissons, terrain)).size() !=  0)
		{
			indiceTuile = r.nextInt(listeTuiles.size());
			coo = new Coordonnees(listeTuiles.get(indiceTuile).getLigne(), listeTuiles.get(indiceTuile).getColonne());
		}
		// s'il n'y en a pas a 2 poissons on regarde s'il y en a avec 1
		else if  ((listeTuiles = rechercheCaseNbpoissons(p, --nbPoissons, terrain)).size() !=  0)
		{
			indiceTuile = r.nextInt(listeTuiles.size());
			coo = new Coordonnees(listeTuiles.get(indiceTuile).getLigne(), listeTuiles.get(indiceTuile).getColonne());
		}
		// sinon le pingouin est entoure de cases ayant un seul successeur
		else
		{
			tMax = tuileMaxAutour(p, terrain);
			coo = new Coordonnees(tMax.getLigne(), tMax.getColonne());
		}
		return coo;
	}

	/*
	 * trouve la case avec la somme maximum de poissons autour
	 */
	public static Coordonnees caseMaxVoisins(Terrain terrain)
	{
		Coordonnees res = null;
		Tuile actu = null;
		int max = Integer.MIN_VALUE;
		int valActu = 0;
		// liste de tuiles possibles pour placer le pingouin
		ArrayList<Tuile> tuilesPossibles = new ArrayList<Tuile>();

		for(int i = 0 ; i < terrain.getHauteur() ; i += 2)
		{
			for(int j = 1 ; j < terrain.getLargeur() ; j += 2)
			{
				actu = terrain.consulter(i, j);
				// si la case n'est pas occupee et qu'il y a un poisson
				// on l'ajoute aux cases disponibles
				if(!actu.estOccupee() && actu.getNbPoissons() == 1)
				{
					tuilesPossibles.add(actu);
				}
			}
		}

		for(int i = 1 ; i < terrain.getHauteur() ; i += 2)
		{
			for(int j = 0 ; j < terrain.getLargeur() ; j += 2)
			{
				actu = terrain.consulter(i, j);
				if(!actu.estOccupee() && actu.getNbPoissons() == 1)
				{
					tuilesPossibles.add(actu);
				}
			}
		}
		// recherche de la case avec le maximum de poissons autour
		max = Integer.MIN_VALUE;
		for(int i = 0 ; i < tuilesPossibles.size() ; i++)
		{
			actu = tuilesPossibles.get(i);
			valActu = nbPoissonsVoisins(terrain, actu);
			if(valActu > max)
			{
				max = valActu;
				res = new Coordonnees(actu.getLigne(), actu.getColonne());
			}
		}
		return res;
	}

	/*
	 * calcule le total des poissons des voisins de la tuile
	 */
	public static int nbPoissonsVoisins(Terrain terrain, Tuile tuile)
	{
		int nb = 0;
		int ligne = tuile.getLigne();
		int colonne = tuile.getColonne();
		if(ligne > 0 && colonne > 0 && terrain.consulter(ligne - 1, colonne - 1) != null && !terrain.consulter(ligne - 1, colonne - 1).estOccupee())
		{
			nb += terrain.consulter(ligne - 1, colonne - 1).getNbPoissons();
		}
		if(ligne > 0 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne - 1, colonne + 1) != null && !terrain.consulter(ligne - 1, colonne + 1).estOccupee())
		{
			nb += terrain.consulter(ligne - 1, colonne + 1).getNbPoissons();
		}
		if(colonne > 1 && terrain.consulter(ligne, colonne - 2) != null && !terrain.consulter(ligne, colonne - 2).estOccupee())
		{
			nb += terrain.consulter(ligne, colonne - 2).getNbPoissons();
		}
		if(colonne < terrain.getLargeur() - 2 && terrain.consulter(ligne, colonne + 2) != null && !terrain.consulter(ligne, colonne + 2).estOccupee())
		{
			nb += terrain.consulter(ligne, colonne + 2).getNbPoissons();
		}
		if(ligne < terrain.getHauteur() - 1 && colonne > 0 && terrain.consulter(ligne + 1, colonne - 1) != null && !terrain.consulter(ligne + 1, colonne - 1).estOccupee())
		{
			nb += terrain.consulter(ligne + 1, colonne - 1).getNbPoissons();
		}
		if(ligne < terrain.getHauteur() - 1 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne + 1, colonne + 1) != null && !terrain.consulter(ligne +  1, colonne + 1).estOccupee())
		{
			nb += terrain.consulter(ligne + 1, colonne + 1).getNbPoissons();
		}
		return nb;
	}

	public static ArrayList<Coup> getCoupsPossibles(DonneesOrdi donnees)
	{
		// liste a renvoyer
		ArrayList<Coup> coupsSuivants = new ArrayList<Coup>();
		// tuiles accessibles par le pingouin actuel
		ArrayList<Tuile> tuilesAccessibles = new ArrayList<Tuile>();
		Graphe g = new Graphe(donnees);
		g.getComposantesInutiles();
		ArrayList<Pingouin> pingouins;

		ArrayList<Composante> composantesUtiles = g.getComposantesUtiles();

		if(!g.isModeRecup())
		{
			for(int c = 0 ; c < composantesUtiles.size() ; c++)
			{
				// liste des pingouins du joueur
				pingouins = composantesUtiles.get(c).getPingouinsJoueur();
				// nombre de pingouins du joueur
				int nbPingouins = pingouins.size();

				// parcours des pingouins du joueur
				for(int i = 0 ; i < nbPingouins ; i++)
				{
					// recuperation de la liste des tuiles accessibles par le pingouin actuel
					tuilesAccessibles = tuilesAccessibles(pingouins.get(i), donnees.getTerrain());
					// parcours des tuiles accessibles du pingouin actuel
					for(int j = 0 ; j < tuilesAccessibles.size() ; j++)
					{
						// position de la tuile actuelle
						Coordonnees positionTuile = new Coordonnees(tuilesAccessibles.get(j).getLigne(), tuilesAccessibles.get(j).getColonne());
						// creation du coup actuel et ajout a la liste
						coupsSuivants.add(new Coup(pingouins.get(i).getCoordonnees(), positionTuile));
					}
				}
			}
		}

		return coupsSuivants;
	}

}
