package ordinateur;
import java.util.ArrayList;
import java.util.Random;

import liaison.Coordonnees;
import liaison.DonneesPartagees;
import moteur.Joueur;
import moteur.Pingouin;

/******************\
 * Carole PLASSON
 * Jeremy WAMBECKE
 * classe :  Debutant
 * version : 1
 ******************
 * Calcul du prochain coup :
 * Recherche de la tuile qui a le plus de poissons
 * et ayant au moins 2 successeurs
 *
 */


public class Debutant implements IComportement
{
	DonneesPartagees donnees;
	Random r;

    // constructeur
	public Debutant(DonneesPartagees d)
	{
		donnees = d;
		r = new Random(5897);
	}

    // calcul du prochain coup
	public void prochainCoup()
	{
		// choix du pingouin (il ne doit pas etre bloque)
		Pingouin p;
		Coordonnees coo;
		Joueur j = donnees.getJoueurs()[donnees.getJoueurCourant()];
		ArrayList<Pingouin> list = utils.pingouinsNonBloques(j);
		int i = r.nextInt(list.size());
		p = list.get(i);
        // initialisation des coordonnees de depart
		donnees.setCoordPingouinDep(new Coordonnees (p.getTuile().getLigne(), p.getTuile().getColonne()));
        // calcul de la tuile suivante et mise a jour
		coo = utils.rechercheCase(p, donnees.getTerrain());
		donnees.setCoordPingouinArr(coo);

	}

    // placement des pingouins
	public void placerPingouin() {
		Random r = new Random();
		ArrayList<Coordonnees> list = utils.casesLibresPlacement(donnees.getTerrain());
		int i = r.nextInt(list.size());
        // initialisation des coordonnees initiales
		donnees.setCoordPingouinInitial(list.get(i));
	}

}
