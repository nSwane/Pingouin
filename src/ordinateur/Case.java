package ordinateur;

/**
 * Carole et Jeremy
 * classe implementant une case du terrainZone, qui permet de marquer
 * les differentes zones suivant les cases accessibles
 */

import java.util.ArrayList;
import moteur.Tuile;

public class Case {

	int i;
	int j;
	// tuile de la case
	Tuile tuile;
	// liste de directions d'ou peuvent venir les pingouins adverses
	ArrayList<Direction> ListeDirections;
	int numJoueur;
	int numPingouin;
	
	// constructeur
	public Case (Tuile tuile)
	{
		this.i = tuile.getLigne();
		this.j = tuile.getColonne();
		this.tuile = tuile;
		this.ListeDirections = new ArrayList<Direction>();
		this.numJoueur = -1;
		this.numPingouin = -1;
	}
	
	public ArrayList<Direction> getListeDirections() {
		return ListeDirections;
	}

	// accesseurs
	public int getI()
	{
		return i;
	}
	
	public int getJ()
	{
		return j;
	}

	public int getNumJoueur()
	{
		return numJoueur;
	}
	
	public void setNumJoueur(int num)
	{
		numJoueur = num;
	}
	
	public void setNumPingouin(int num)
	{
		numPingouin = num;
	}
	public Tuile getTuile()
	{
		return this.tuile;
	}
	
	/**
	 *  ajout d'une direction dans la liste
	 * @param direction : direction du joueur
	 */
	public void ajoutDirection(Direction direction)
	{
		this.ListeDirections.add(direction);
	}
	
	/**
	 *  extraction d'une direction dans la liste
	 * @param direction : direction a extraire
	 */
	public void extraireDirection(Direction direction)
	{
		this.ListeDirections.remove(direction);
	}
	

	public String toString(){
		return ("("+i+" , "+j+")");
	}
	
	// modif tuile
	public void setTuile(Tuile tuile)
	{
		this.tuile = tuile;
	}
	

}
