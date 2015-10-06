package ordinateur;

import liaison.Coordonnees;

/**
 * Classe coup
 * permet de stocker les positions qui determinent un coup joue
 * (coordonnees de depart et d'arrivee
 * auteurs : Jeremy Wambecke et Carole Plasson
 * date : 24/05
 */

public class Coup 
{
	private Coordonnees depart;
	private Coordonnees arrivee;
	
	public Coup(Coordonnees depart, Coordonnees arrivee)
	{
		this.depart = depart;
		this.arrivee = arrivee;
	}

	public Coordonnees getDepart() {
		return depart;
	}

	public void setDepart(Coordonnees depart) {
		this.depart = depart;
	}

	public Coordonnees getArrivee() {
		return arrivee;
	}

	public void setArrivee(Coordonnees arrivee) {
		this.arrivee = arrivee;
	}
	
	public String toString()
	{
		return "{ " + depart + ", " + arrivee + " }"; 
	}
	
}
