package ordinateur;

/**
 * Jeremy et Carole
 * classe permettant de representer le terrain sous forme de zones defendues par les pingouins
 */

import java.util.ArrayList;
import java.util.HashSet;

import liaison.Coordonnees;
import moteur.Joueur;
import moteur.Pingouin;
import moteur.Terrain;
import moteur.Tuile;

public class TerrainZone 
{
	// nombre de cases en commun necessaires pour fusionner deux zones
	public static final int nbCasesFusion = 7;
	private Case[][] terrainZone;
	private int largeur;
	private int hauteur;
	private ArrayList<Pingouin> pingouinsZone;
	private Terrain terrain;
	private ArrayList<ArrayList<Tuile>> listeZones;
	private int nbZones;
	
	// creation du terrain de zone initial
	public TerrainZone(DonneesOrdi donnees, Joueur jActu)
	{
		this.terrain = donnees.getTerrain();
		this.largeur = terrain.getLargeur();
		this.hauteur = terrain.getHauteur();
		Joueur[] Advers = new Joueur[donnees.getNbJoueurs() -1];
		int joueurActu = 0;
		listeZones = new ArrayList<ArrayList<Tuile>>();
		
		for (int i = 0 ; i < donnees.getNbJoueurs() ; i++)
		{
			if (i != jActu.getNumero())
			{
				Advers[joueurActu] = donnees.getJoueurs()[i];
				joueurActu++;
			}
		}
		this.pingouinsZone =  jActu.getPengouins();
		// parcours des cases du terrain 
		this.terrainZone = new Case[terrain.getHauteur()][terrain.getLargeur()];
		for (int i = 0 ; i < terrain.getHauteur() ; i++)
		{
			for (int j = 0 ; j < terrain.getLargeur() ; j++)
			{
				if (terrain.consulter(i, j) != null)
				{
					terrainZone[i][j] = new Case(terrain.consulter(i, j));
					terrainZone[i][j].setNumJoueur(terrain.consulter(i, j).getNumeroJoueur());
					if (terrain.consulter(i, j).getNumeroJoueur() != -1)
						terrainZone[i][j].setNumPingouin(terrain.consulter(i, j).getPingouin().getNumero());
					else
						terrainZone[i][j].setNumPingouin(-1);
				}
				else
					terrainZone[i][j] = null;
			}
		}
		
		for (int i = 0 ; i < jActu.getNbPingouins() ; i++)
		{
			// on marque les cases des autres joueurs
			for (int j = 0 ; j < Advers.length ; j++)
			{
				marquageAdversaire(Advers[j]);
			}
			// delimite la jone du premier pingouin
			delimiteTerrainZone(pingouinsZone.get(i));
			// recuperation des coordonnees du pingouin actuel
			Coordonnees coo = pingouinsZone.get(i).getCoordonnees();
			// cases accessibles par le pingouin actuel
			ArrayList<Case> ListeCases = listeCasesAccessibles(terrainZone[coo.getI()][coo.getJ()]);
			// recuperation de la liste des zones du pingouin actuel
			ArrayList<Tuile> list = ListeZone(ListeCases);
			// ajout de la tuile du pingouin actuel
			list.add(terrainZone[coo.getI()][coo.getJ()].getTuile());
			// suppression des tuiles en double dans la liste
	        HashSet<Tuile> set = new HashSet<Tuile>() ;
	        set.addAll(list) ;
	        list = new ArrayList<Tuile>(set);
	        // on verifie si la liste cree peut etre fusionnee
	        if (listeZones.size() == 0 )
	        {
				listeZones.add(list);
	        }
	        // fusion possible
	        else
	        {
	        	// parcours des listes deja trouvees 
	        	for (int k = 0 ; k < listeZones.size() ; k++)
	        	{
	        		// si on peut les fusionner (assez de tuiles en commun)
	        		if (nbTuilesIdentiques (listeZones.get(k), list ))
	        		{
	        			// fusion
	        			list.addAll(listeZones.get(k));
	        			// suppression des tuiles en double dans la liste
	        			set = new HashSet<Tuile>() ;
	        	        set.addAll(list);
	        	        list = new ArrayList<Tuile>(set);
	        	        // suppression de la liste qu'on peut fusionner
	        	        listeZones.remove(k);
	        		}
	        	}
	        	listeZones.add(list);
	        }
		}
		// recuperation du nombre de zones du joueur actuel apres fusion
    	this.nbZones = listeZones.size();
	}
	
	// Accesseurs
	public int getHauteur()
	{
		return this.hauteur;
	}
	
	public int getLargeur()
	{
		return this.largeur;
	}
	
	public int getNbZones()
	{
		return this.nbZones;
	}
	
	public ArrayList<ArrayList<Tuile>> getListeZones() {
		return listeZones;
	}
	
	
	// Marquage des cases accessibles par l'adversaire 
	public void marquageAdversaire(Joueur jAdv)
	{
		Tuile tuile;
		for (int i = 0 ; i < jAdv.getNbPingouins(); i++)
		{
			// recuperation de la tuile des pingouins
			tuile = jAdv.getPengouins().get(i).getTuile();
			// marquage des cases accessibles par le pingouin actuel pour chaque direction 
			ajoutCasesAcc (Direction.NE, tuile);
			ajoutCasesAcc (Direction.NO, tuile);
			ajoutCasesAcc (Direction.SO, tuile);
			ajoutCasesAcc (Direction.SE, tuile);
			ajoutCasesAcc (Direction.E, tuile);
			ajoutCasesAcc (Direction.O, tuile);
		}
			
	}
	
	// Ajout des cases accessibles dans le terrainZone
	public void ajoutCasesAcc (Direction dir, Tuile t)
	{
		ArrayList<Tuile> listeTuiles;
		Tuile tuile;
		switch (dir)
		{
			case NE :
				// recuperation des successeurs NE de la tuile t 
				listeTuiles = utils.suivantsNE(t, terrain);
				// ajout de ces tuiles dans terrainZone avec la direction SO
				for (int i = 0 ; i < listeTuiles.size() ; i++ )
				{
					// modif de la case
					tuile = listeTuiles.get(i);
					terrainZone[tuile.getLigne()][tuile.getColonne()].setTuile(tuile);
					terrainZone[tuile.getLigne()][tuile.getColonne()].ajoutDirection(Direction.SO);
				}
				break;
				
			case NO : 
				// recuperation des successeurs NO de la tuile t 
				listeTuiles = utils.suivantsNO(t, terrain);
				// ajout de ces tuiles dans terrainZone avec la direction SE
				for (int i = 0 ; i < listeTuiles.size() ; i++ )
				{
					// modif de la case
					tuile = listeTuiles.get(i);
					terrainZone[tuile.getLigne()][tuile.getColonne()].setTuile(tuile);
					terrainZone[tuile.getLigne()][tuile.getColonne()].ajoutDirection(Direction.SE);
				}
				break;
			case SE : 
				// recuperation des successeurs SE de la tuile t 
				listeTuiles = utils.suivantsSE(t, terrain);
				// ajout de ces tuiles dans terrainZone avec la direction NO
				for (int i = 0 ; i < listeTuiles.size() ; i++ )
				{
					// modif de la case
					tuile = listeTuiles.get(i);
					terrainZone[tuile.getLigne()][tuile.getColonne()].setTuile(tuile);
					terrainZone[tuile.getLigne()][tuile.getColonne()].ajoutDirection(Direction.NO);
				}
				break;
				
			case SO : 
				// recuperation des successeurs SO de la tuile t 
				listeTuiles = utils.suivantsSO(t, terrain);
				// ajout de ces tuiles dans terrainZone avec la direction NE
				for (int i = 0 ; i < listeTuiles.size() ; i++ )
				{
					// modif de la case
					tuile = listeTuiles.get(i);
					terrainZone[tuile.getLigne()][tuile.getColonne()].setTuile(tuile);
					terrainZone[tuile.getLigne()][tuile.getColonne()].ajoutDirection(Direction.NE);
				}
				break;
			case E : 
				// recuperation des successeurs E de la tuile t 
				listeTuiles = utils.suivantsE(t, terrain);
				// ajout de ces tuiles dans terrainZone avec la direction O
				for (int i = 0 ; i < listeTuiles.size() ; i++ )
				{
					// modif de la case
					tuile = listeTuiles.get(i);
					terrainZone[tuile.getLigne()][tuile.getColonne()].setTuile(tuile);
					terrainZone[tuile.getLigne()][tuile.getColonne()].ajoutDirection(Direction.O);
				}
				break;
			case O : 
				// recuperation des successeurs O de la tuile t 
				listeTuiles = utils.suivantsO(t, terrain);
				// ajout de ces tuiles dans terrainZone avec la direction E
				for (int i = 0 ; i < listeTuiles.size() ; i++ )
				{
					// modif de la case
					tuile = listeTuiles.get(i);
					terrainZone[tuile.getLigne()][tuile.getColonne()].setTuile(tuile);
					terrainZone[tuile.getLigne()][tuile.getColonne()].ajoutDirection(Direction.E);
				}
				break;
				
			default : 
				break;
		}
	}
	
	// bloquage des cases qui sont successeurs de cette case
	public void bloquerAdv (Case c)
	{
		ArrayList<Tuile> ListeSucc;
		int ligne;
		int colonne;
		while(c.ListeDirections.size() != 0)
		{
			int i = 0;
			switch(c.ListeDirections.get(i))
			{
				// l'adversaire vient du NE
				case NE : 
					// efface la direction NE de cette case
					c.extraireDirection(Direction.NE);
					// reuperation des successeurs SO
					ListeSucc = utils.suivantsSO(c.getTuile(), terrain);
					// efface le marquage de ces cases
					for (int j = 0 ; j < ListeSucc.size() ; j++)
					{
						ligne = ListeSucc.get(j).getLigne();
						colonne = ListeSucc.get(j).getColonne();
						terrainZone[ligne][colonne].extraireDirection(Direction.NE);
					}
					break;
					
				// l'adversaire vient du NO
				case NO : 
					// efface la direction NO de cette case
					c.extraireDirection(Direction.NO);
					// reuperation des successeurs SE
					ListeSucc = utils.suivantsSE(c.getTuile(), terrain);
					// efface le marquage de ces cases
					for (int j = 0 ; j < ListeSucc.size() ; j++)
					{
						ligne = ListeSucc.get(j).getLigne();
						colonne = ListeSucc.get(j).getColonne();
						terrainZone[ligne][colonne].extraireDirection(Direction.NO);
					}
					break;
				
				// l'adversaire vient du O
				case O : 
					// efface la direction O de cette case
					c.extraireDirection(Direction.O);
					// reuperation des successeurs E
					ListeSucc = utils.suivantsE(c.getTuile(), terrain);
					// efface le marquage de ces cases
					for (int j = 0 ; j < ListeSucc.size() ; j++)
					{
						ligne = ListeSucc.get(j).getLigne();
						colonne = ListeSucc.get(j).getColonne();
						terrainZone[ligne][colonne].extraireDirection(Direction.O);
					}
					break;
					
				// l'adversaire vient du E
				case E : 
					// efface la direction E de cette case
					c.extraireDirection(Direction.E);
					// reuperation des successeurs O
					ListeSucc = utils.suivantsO(c.getTuile(), terrain);
					// efface le marquage de ces cases
					for (int j = 0 ; j < ListeSucc.size() ; j++)
					{
						ligne = ListeSucc.get(j).getLigne();
						colonne = ListeSucc.get(j).getColonne();
						terrainZone[ligne][colonne].extraireDirection(Direction.E);
					}
					break;
				
				// l'adversaire vient du SE
				case SE : 
					// efface la direction SE de cette case
					c.extraireDirection(Direction.SE);
					// reuperation des successeurs SO
					ListeSucc = utils.suivantsNO(c.getTuile(), terrain);
					// efface le marquage de ces cases
					for (int j = 0 ; j < ListeSucc.size() ; j++)
					{
						ligne = ListeSucc.get(j).getLigne();
						colonne = ListeSucc.get(j).getColonne();
						terrainZone[ligne][colonne].extraireDirection(Direction.SE);
					}
					break;
				
				// l'adversaire vient du SO
				case SO : 
					// efface la direction SO de cette case
					c.extraireDirection(Direction.SO);
					// reuperation des successeurs 
					ListeSucc = utils.suivantsNE(c.getTuile(), terrain);
					// efface le marquage de ces cases
					for (int j = 0 ; j < ListeSucc.size() ; j++)
					{
						ligne = ListeSucc.get(j).getLigne();
						colonne = ListeSucc.get(j).getColonne();
						terrainZone[ligne][colonne].extraireDirection(Direction.SO);
					}
					break;
			}
		}
	}
	
	// delimitation du terrain pour un pingouin selon si les cases accessibles par l'adversaire 
	// peuvent etre ou non bloque 
	public void delimiteTerrainZone(Pingouin p)
	{
		ArrayList<Tuile> ListeTuiles;
		Tuile t;
		// pour toutes les cases successeurs de ce pingouin, 
		// on verifie que les successeurs de ces cases peuvent etre bloques
		ListeTuiles = utils.suivantsE(p.getTuile(), terrain);
		for (int n = 0 ; n < ListeTuiles.size() ; n++)
		{
			t = ListeTuiles.get(n);
			bloquerAdv(terrainZone[t.getLigne()][t.getColonne()]);
		}
		
		ListeTuiles = utils.suivantsO(p.getTuile(), terrain);
		for (int n = 0 ; n < ListeTuiles.size() ; n++)
		{
			t = ListeTuiles.get(n);
			bloquerAdv(terrainZone[t.getLigne()][t.getColonne()]);
		}
		
		ListeTuiles = utils.suivantsNO(p.getTuile(), terrain);
		for (int n = 0 ; n < ListeTuiles.size() ; n++)
		{
			t = ListeTuiles.get(n);
			bloquerAdv(terrainZone[t.getLigne()][t.getColonne()]);
		}
		
		ListeTuiles = utils.suivantsNE(p.getTuile(), terrain);
		for (int n = 0 ; n < ListeTuiles.size() ; n++)
		{
			t = ListeTuiles.get(n);
			bloquerAdv(terrainZone[t.getLigne()][t.getColonne()]);
		}
		
		ListeTuiles = utils.suivantsSE(p.getTuile(), terrain);
		for (int n = 0 ; n < ListeTuiles.size() ; n++)
		{
			t = ListeTuiles.get(n);
			bloquerAdv(terrainZone[t.getLigne()][t.getColonne()]);
		}
		
		ListeTuiles = utils.suivantsSO(p.getTuile(), terrain);
		for (int n = 0 ; n < ListeTuiles.size() ; n++)
		{
			t = ListeTuiles.get(n);
			bloquerAdv(terrainZone[t.getLigne()][t.getColonne()]);
		}
	}
	
	// liste les cases accessibles depuis une case
	public ArrayList<Case> listeCasesAccessibles(Case c)
	{
		ArrayList<Case> list = new ArrayList<Case>();
		list.addAll(suivantsNE(c));
		list.addAll(suivantsNO(c));
		list.addAll(suivantsSE(c));
		list.addAll(suivantsSO(c));
		list.addAll(suivantsE(c));
		list.addAll(suivantsO(c));
		return list;
	}
	
	// renvoie vrai si le nombre de tuiles presentes dans les deux listes passees en parametre
	// est superieur a la constante nbCasesFusion
	public boolean nbTuilesIdentiques (ArrayList<Tuile> list1, ArrayList<Tuile> list2)
	{
		int i = 0;
		int nbTuiles = 0;
		while ( i < list1.size() && nbTuiles < nbCasesFusion)
		{
			if (list2.contains(list1.get(i)))
			{
				nbTuiles++;
			}
			i++;
		}
		return  (nbTuiles == nbCasesFusion);
	}
	
	//cree la liste des cases accessibles et leurs successeurs
	public ArrayList<Tuile> ListeZone(ArrayList<Case> ListeCasesAcc)
	{
		int ligne;
		int colonne;
		ArrayList<Tuile> list = new ArrayList<Tuile>();
		// parcours des cases accessibles du terrainZone
		for (int i = 0 ; i < ListeCasesAcc.size() ; i++)
		{
			ligne = ListeCasesAcc.get(i).getTuile().getLigne();
			colonne =ListeCasesAcc.get(i).getTuile().getColonne();
			list.add(ListeCasesAcc.get(i).getTuile());
			// verif de la case en haut a gauche
			if(ligne > 0 && colonne > 0 && terrain.consulter(ligne-1, colonne-1) != null && terrainZone[ligne-1][colonne-1].ListeDirections.size() == 0 
					&&  terrainZone[ligne-1][colonne-1].numJoueur == -1)
			{
				// insertion de la tuile
				list.add(terrainZone[ligne - 1][colonne -1].getTuile());
			}
			// verif de la case en haut a droite
			if(ligne > 0 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne-1, colonne+1) != null  && terrainZone[ligne-1][colonne+1].ListeDirections.size() == 0 
					&&  terrainZone[ligne-1][colonne+1].numJoueur == -1)
			{
				// insertion de la tuile
				list.add(terrainZone[ligne - 1][colonne +1].getTuile());
			}
			// verif de la case a gauche
			if(colonne > 1 && terrain.consulter(ligne, colonne-2) != null  && terrainZone[ligne][colonne-2].ListeDirections.size() == 0 
					&&  terrainZone[ligne][colonne-2].numJoueur == -1)
			{
				// insertion de la tuile
				list.add(terrainZone[ligne][colonne -2].getTuile());
			}
			// verif de la case a droite
			if(colonne < terrain.getLargeur() - 2 && terrain.consulter(ligne, colonne+2) != null && terrainZone[ligne][colonne+2].ListeDirections.size() == 0 
					&&  terrainZone[ligne][colonne+2].numJoueur == -1)
			{
				// insertion de la tuile
				list.add(terrainZone[ligne][colonne +2].getTuile());
			}
			// verif de la case en bas a gauche
			if(ligne < terrain.getHauteur() - 1 && colonne > 0 && terrain.consulter(ligne+1, colonne-1) != null  && terrainZone[ligne + 1][colonne - 1].ListeDirections.size() == 0 
					&&  terrainZone[ligne + 1][colonne - 1].numJoueur == -1)
			{
				// insertion de la tuile
				list.add(terrainZone[ligne + 1][colonne - 1].getTuile());
			}
			// verif de la case en bas a droite
			if(ligne < terrain.getHauteur() - 1 && colonne < terrain.getLargeur() - 1 && terrain.consulter(ligne+1, colonne+1) != null && terrainZone[ligne + 1][colonne + 1].ListeDirections.size() == 0 
					&&  terrainZone[ligne + 1][colonne + 1].numJoueur == -1)
			{
				// insertion de la tuile
				list.add(terrainZone[ligne + 1][colonne + 1].getTuile());
			}
		}
		return list;
	}
	// recuperation des successeurs d'une case (direction NE)
	public ArrayList<Case> suivantsNE(Case c)
	{
		ArrayList<Case> listeCases = new ArrayList<Case>();

		int ligne = c.getI()-1;
		int colonne = c.getJ()+1;
		// tant que la case n'est pas accessibles par un adversaire et qu'il n'y a pas de pingouin
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain.consulter(ligne, colonne) != null
				&& terrainZone[ligne][colonne].ListeDirections.size() == 0 &&  terrainZone[ligne][colonne].numJoueur == -1){
			listeCases.add(terrainZone[ligne][colonne]);
			ligne--;
			colonne++;
		}
		return listeCases;
	}
	
	// recuperation des successeurs d'une case (direction NE)
	public ArrayList<Case> suivantsNO(Case c)
	{
		ArrayList<Case> listeCases = new ArrayList<Case>();

		int ligne = c.getI()-1;
		int colonne = c.getJ()-1;
		// tant que la case n'est pas accessibles par un adversaire et qu'il n'y a pas de pingouin
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain.consulter(ligne, colonne) != null
				&& terrainZone[ligne][colonne].ListeDirections.size() == 0 &&  terrainZone[ligne][colonne].numJoueur == -1){
			listeCases.add(terrainZone[ligne][colonne]);
			ligne--;
			colonne--;
		}
		return listeCases;
	}
	
	// recuperation des successeurs d'une case (direction SE)
	public ArrayList<Case> suivantsSE(Case c)
	{
		ArrayList<Case> listeCases = new ArrayList<Case>();

		int ligne = c.getI()+1;
		int colonne = c.getJ()+1;
		// tant que la case n'est pas accessibles par un adversaire et qu'il n'y a pas de pingouin
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain.consulter(ligne, colonne) != null
				&& terrainZone[ligne][colonne].ListeDirections.size() == 0 &&  terrainZone[ligne][colonne].numJoueur == -1){
			listeCases.add(terrainZone[ligne][colonne]);
			ligne++;
			colonne++;
		}
		return listeCases;
	}
	
	// recuperation des successeurs d'une case (direction SO)
	public ArrayList<Case> suivantsSO(Case c)
	{
		ArrayList<Case> listeCases = new ArrayList<Case>();

		int ligne = c.getI()+1;
		int colonne = c.getJ()-1;
		// tant que la case n'est pas accessibles par un adversaire et qu'il n'y a pas de pingouin
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain.consulter(ligne, colonne) != null
				&& terrainZone[ligne][colonne].ListeDirections.size() == 0 &&  terrainZone[ligne][colonne].numJoueur == -1){
			listeCases.add(terrainZone[ligne][colonne]);
			ligne++;
			colonne--;
		}
		return listeCases;
	}
	
	// recuperation des successeurs d'une case (direction E)
	public ArrayList<Case> suivantsE(Case c)
	{
		ArrayList<Case> listeCases = new ArrayList<Case>();

		int ligne = c.getI();
		int colonne = c.getJ()+2;
		// tant que la case n'est pas accessibles par un adversaire et qu'il n'y a pas de pingouin
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain.consulter(ligne, colonne) != null
				&& terrainZone[ligne][colonne].ListeDirections.size() == 0 &&  terrainZone[ligne][colonne].numJoueur == -1){
			listeCases.add(terrainZone[ligne][colonne]);
			colonne +=2;
		}
		return listeCases;
	}
	
	// recuperation des successeurs d'une case (direction O)
	public ArrayList<Case> suivantsO (Case c)
	{
		ArrayList<Case> listeCases = new ArrayList<Case>();

		int ligne = c.getI();
		int colonne = c.getJ()-2;
		// tant que la case n'est pas accessibles par un adversaire et qu'il n'y a pas de pingouin
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain.consulter(ligne, colonne) != null
				&& terrainZone[ligne][colonne].ListeDirections.size() == 0 &&  terrainZone[ligne][colonne].numJoueur == -1){
			listeCases.add(terrainZone[ligne][colonne]);
			colonne -=2;
		}
		return listeCases;
	}

	// Affichage terrain zone 
	public void afficherTerrainZone()
	{
		for (int i = 0 ; i < this.getHauteur() ; i++)
		{
			for (int j = 0 ; j < this.getLargeur(); j++)
			{
				if (this.terrainZone[i][j] != null)
					if (terrain.consulter(i, j).getNumeroJoueur() == -1)
						System.out.print("  " + terrainZone[i][j].ListeDirections);
					else
						System.out.print("   " + terrain.consulter(i, j).getNumeroJoueur() + "P");
				else
					System.out.print("  CC");
			}
			System.out.println("");
		}
		
	}
	
}
