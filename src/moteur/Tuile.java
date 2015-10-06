package moteur;


/********************
*  Nawaoui Swane et Giaccone Marc
*  classe Case
*  definition d'une tuile
*
*/

@SuppressWarnings("serial")
public class Tuile implements java.io.Serializable{
	private int x, y;
	private int nbPoissons;
	private Pingouin pingouin; // référence vers le pingouin sur la tuile (null si pas de pingouin)
	private boolean estAccessible; //Vrai s'il s'agit d'une tuile accessible par un joueur selectionne

	Tuile(int i, int j, int n, boolean estAcc){
		x = i;
		y = j;
		nbPoissons = n;
		estAccessible = estAcc;
	}

	public Tuile copie(){
		Tuile t = new Tuile(x, y, nbPoissons, estAccessible);

		if(pingouin != null){
			t.setPingouin(pingouin.copie());
		}
		else{
			t.setPingouin(null);
		}

		return t;
	}

	public int getLigne(){
		return x;
	}

	public int getColonne(){
		return y;
	}

	// la tuile est occupee si un pingouin est dessus
	public boolean estOccupee(){
		return (pingouin != null);
	}

	public boolean estAccessible(){
		return estAccessible;
	}

	public void setAccessible(){
		estAccessible = true;
	}

	public void resetAccessible(){
		estAccessible = false;
	}

	public int getNbPoissons(){
		return nbPoissons;
	}

	public Pingouin getPingouin(){
		return pingouin;
	}

	public void setPingouin(Pingouin p){
		pingouin = p;
	}

	public int getNumeroJoueur()
	{
		if(pingouin == null)
		{
			return -1;
		}
		else
			return pingouin.getJoueur().getNumero();
	}

	public int getNumeroPingouin()
	{
		return pingouin.getNumero();
	}

	public String toString(){
		return ("("+x+" , "+y+"):"+nbPoissons+":"+estOccupee());
	}

}