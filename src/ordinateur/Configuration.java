package ordinateur;

/**
 * Jeremy Wambecke et Carole Plasson
 * classe permettant de calculer et d'evaluer une configuration du terrain
 * pour le joueur courant, selon les zones occupees par tous les pingouins
 */

import java.util.ArrayList;

import moteur.Joueur;
import moteur.Tuile;

public class Configuration 
{
	// acces aux donnees partagees
	DonneesOrdi donnees;
	// liste des listes des zones pour tous les joueurs
	private ArrayList<ArrayList<Zone>> tabZones;
	// nombre de zones par joueur
	private int[] nbZones;
	
	
	// CONSTRUCTEUR :
	public Configuration (DonneesOrdi d)
	{
		this.donnees = d;
		this.nbZones = new int[donnees.getNbJoueurs()];
		this.tabZones = new ArrayList<ArrayList<Zone>>();
		ArrayList<ArrayList<Tuile>> list;
		// pour tous les joueurs, on cree le terrain de zone et la liste des tuiles  
		for (int i = 0 ; i < donnees.getNbJoueurs() ; i++)
		{
			this.tabZones.add(new ArrayList<Zone>());
			// creation d'un terrain zones par joueur
			TerrainZone tz = new TerrainZone(donnees, donnees.getJoueurs()[i]);
			//recuperation d'une liste de listes de tuiles
			list = tz.getListeZones();
			// recuperation du nombre de zones par joueur
			this.nbZones[i] =list.size();
			// pour toutes les listes de tuiles => creation de la zone associee
			for (int j = 0 ; j <  nbZones[i]; j++)
			{
				// ajout de la zone dans liste des zones du joueur i
				tabZones.get(i).add(new Zone(list.get(j), donnees, i));
			}
		}

	}
	
	// ACCESSEURS :
	public ArrayList<ArrayList<Zone>> getTabZones()
	{
		return this.tabZones;
	}
	
	public int[] getNbZones()
	{
		return this.nbZones;
	}
	
	
	/**
	 *  retourne la zone ayant la meilleure valeur d'evaluation
	 */
	public Zone choisirZone(ArrayList<Zone> listZone)
	{
		Zone zmax = listZone.get(0);
		int emax = zmax.evaluer();
		int e;
		if (listZone.size() > 1)
		{
			// on evalue toutes les zones de la list
			for (int i = 1 ; i < listZone.size() ; i++)
			{
				e = listZone.get(i).evaluer();
				// si on trouve une valeur d'evaluation plus elevee
				if ( e > emax)
				{
					// modif zone max
					emax = e;
					zmax = listZone.get(i);
				}
			}
		}
		return zmax;
	}
	
	/**
	 * permet d'evaluer la configuration en lui associant un entier
	 */
	public int evaluer(ArrayList<Composante> ilotsJoueur, ArrayList<Composante> ilotsAdverses)
	{
		int valCourant = 0;
		int valAdvers = 0;
		// somme des evaluations de toutes les zones du joueur courant
		for (int l = 0 ; l < tabZones.get(donnees.getJoueurCourant()).size() ; l++)
		{    			
			valCourant += tabZones.get(donnees.getJoueurCourant()).get(l).evaluer();
		}
		// evaluation des ilots du joueur
		for(int c = 0 ; c < ilotsJoueur.size() ; c++)
		{
			valCourant += ilotsJoueur.get(c).getNbPoissons();
		}
		// evaluation des ilots adverses
		for(int c = 0 ; c < ilotsAdverses.size() ; c++)
		{
			valAdvers += ilotsAdverses.get(c).getNbPoissons();
		}
		// parcours des zones des adversaires
		for (int i = 0 ; i  < donnees.getNbJoueurs(); i++)
		{
			// si c'est un adversaire 
			if (i != donnees.getJoueurCourant())
			{
				for (int l = 0 ; l < tabZones.get(i).size() ; l++)
				{	
					valAdvers += tabZones.get(i).get(l).evaluer();
				}
			}
		}
		Joueur courant = donnees.getJoueurs()[donnees.getJoueurCourant()];
		if(courant.getDifficulte() == 3)
		{
			valCourant += 2*courant.getNombrePoissons();
			for(int j = 0 ; j < donnees.getNbJoueurs() ; j++)
			{
				if(j != donnees.getJoueurCourant())
				{
					valAdvers += 2*donnees.getJoueurs()[j].getNombrePoissons();
				}
			}
		}
		return valCourant - valAdvers;
	}
	
	/**
	 * permet d'evaluer la configuration en lui associant un entier
	 */
	public int evaluer()
	{
		int valCourant = 0;
		int valAdvers = 0;
		// somme des evaluations de toutes les zones du joueur courant
		for (int l = 0 ; l < tabZones.get(donnees.getJoueurCourant()).size() ; l++)
		{    			
			valCourant += tabZones.get(donnees.getJoueurCourant()).get(l).evaluer();
		}
		// parcours des zones des adversaires
		for (int i = 0 ; i  < donnees.getNbJoueurs(); i++)
		{
			// si c'est un adversaire 
			if (i != donnees.getJoueurCourant())
			{
				for (int l = 0 ; l < tabZones.get(i).size() ; l++)
				{	
					valAdvers += tabZones.get(i).get(l).evaluer();
				}
			}
		}
		return valCourant - valAdvers;
	}
	
}
