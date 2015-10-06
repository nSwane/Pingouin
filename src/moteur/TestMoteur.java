/**
 * Auteur : Jeremy
 * Description : test pour la synchronisation entre les threads
 * Date : 20/05/13
 * Version : 1.0
 */


package moteur;
import java.util.Scanner;

import liaison.Coordonnees;
import liaison.DonneesPartagees;
import liaison.Liaison;

public class TestMoteur {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		Liaison liaison = new Liaison(false,false);
		ComportementAffichage ca = new ComportementAffichage();
		liaison.nouvellePartie(ca);
		String action;
		
		do
		{
			DonneesPartagees d = liaison.getDonnees();
			Terrain t = d.getTerrain();
			Joueur [] tabj = d.getJoueurs();
			
			t.afficher();
			for(int i = 0; i < d.getNbJoueurs(); i++){
				System.out.println(tabj[i].toString());
			}
			System.out.println("Joueur courant : "+d.getJoueurCourant());
			
			action = s.nextLine();
			if(action.equals("listerFichiers")){
				liaison.listerFichiers();
			}
			else if(action.equals("charger"))
			{
				System.out.println("entrez le nom de fichier :");
				liaison.charger(s.nextLine());
			}
			else if(action.equals("sauvegarder"))
			{
				System.out.println("entrez le nom de fichier :");
				liaison.sauvegarder(s.nextLine());
			}
			else if(action.equals("access"))
			{
				System.out.println("entrez les coordonnees de la case :");
				liaison.casesAccessibles(new Coordonnees(s.nextInt(), s.nextInt()));
			}
			else if(action.equals("deplacer"))
			{
				System.out.println("entrez les coordonnees de la case depart et de la case arrivee :");
				liaison.jouerCoup(new Coordonnees(s.nextInt(), s.nextInt()), new Coordonnees(s.nextInt(), s.nextInt()));
			}
			else if(action.equals("annuler"))
			{
				liaison.annuler();
			}
			else if(action.equals("refaire"))
			{
				liaison.refaire();
			}
			else if(action.equals("conseil"))
			{
				liaison.conseil();
			}
			else if(action.equals("placer"))
			{
				System.out.println("entrez les coordonnees de la case ou placer le pingouin :");
				liaison.placerPingouin(new Coordonnees(s.nextInt(), s.nextInt()));
			}
			else if(action.equals("quitter"))
			{
				liaison.quitter();
				break;
			}
			else
			{
				System.out.println("Commande inconnue");
			}
			
		} while(true);
	}

}
