package ordinateur;
import liaison.DonneesPartagees;

/******************\
 * Carole PLASSON
 * Jeremy WAMBECKE
 * classe Ordinateur
 * version 1
 ******************
 *  Classe implementant le comportement
 *  de l'ordinateur selon le niveau 
 *  de difficulte
 */

public class Ordinateur implements IComportement
{
	int difficulte;
	IComportement comportement;

	// Constructeur 
	public Ordinateur(DonneesPartagees d, int difficulte)
	{
		this.difficulte = difficulte;
		switch(difficulte)
		{
			case 0:
				comportement = new Moyen(d);
				break;
            case 1:
                comportement = new Debutant(d);
                break;
            case 2:
                comportement = new Moyen(d);
                break;
            case 3:
                comportement = new Moyen(d);
                break;
            default:
                throw new RuntimeException("Difficulte incoherente");
        }
	}

	public int getDifficulte() {
		return difficulte;
	}

	// Calcul du pochain coup 
	public void prochainCoup() 
	{
		comportement.prochainCoup();
	}

	// placement d'un pingouin
	public void placerPingouin() {
		comportement.placerPingouin();
	}

}
