package ordinateur;

/**
 * Jeremy et Carole
 * UTILISATION ULTERIEURE
 */

import java.util.ArrayList;

public class Noeud 
{
	private ArrayList<Noeud> succ;
	private int valConfig;
	private Coup coup;
	private int numeroJoueur;
	private Coup meilleur;

	public Noeud()
	{
		this.succ = new ArrayList<Noeud>();
		this.meilleur = new Coup(null, null);
	}
	
	public Noeud(int valConfig)
	{
		this.succ = new ArrayList<Noeud>();
	}

	public ArrayList<Noeud> getSucc() {
		return succ;
	}

	public void setSucc(ArrayList<Noeud> succ) {
		this.succ = succ;
	}

	public void addSucc(Noeud n)
	{
		this.succ.add(n);
	}

	public int getValConfig() {
		return valConfig;
	}

	public void setValConfig(int valConfig) {
		this.valConfig = valConfig;
	}

	public Coup getCoup() {
		return coup;
	}

	public void setCoup(Coup coup) {
		this.coup = coup;
	}

	public int getNumeroJoueur() {
		return numeroJoueur;
	}

	public void setNumeroJoueur(int numeroJoueur) {
		this.numeroJoueur = numeroJoueur;
	}
	
	public int evaluer(int joueurCourant)
	{
		int max = 0;
		int min = 0;
		int evalActu = 0;
		if(succ.size() == 0)
		{
			meilleur.setDepart(this.coup.getDepart());
			meilleur.setArrivee(this.coup.getArrivee());
			return this.valConfig;
		}
		else
		{
			if(this.getNumeroJoueur() == joueurCourant)
			{
				max = succ.get(0).evaluer(joueurCourant);
				meilleur.setDepart(succ.get(0).getCoup().getDepart());
				meilleur.setArrivee(succ.get(0).getCoup().getArrivee());
				
				for (int s = 1; s < succ.size(); s++) 
				{
					evalActu = succ.get(s).evaluer(joueurCourant);
					if(evalActu > max)
					{
						max = evalActu;
						meilleur.setDepart(succ.get(s).getCoup().getDepart());
						meilleur.setArrivee(succ.get(s).getCoup().getArrivee());
					}
				}
				return max;
			}
			else
			{
				min = succ.get(0).evaluer(joueurCourant);
				for (int s = 1; s < succ.size(); s++) 
				{
					evalActu = succ.get(s).evaluer(joueurCourant);
					if(evalActu < min)
					{
						min = evalActu;
					}
				}
				return min;
			}
		}
	}

	public Coup getMeilleur() {
		return meilleur;
	}

	public void setMeilleur(Coup meilleur) {
		this.meilleur = meilleur;
	}
	
}
