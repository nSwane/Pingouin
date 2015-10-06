package ordinateur;

import java.util.ArrayList;

public class ArbreMinMax 
{
	static final int nbTuilesInitial = 60;
	private MoteurOrdi moteur;
	private DonneesOrdi donneesInitiales;
	private Coup meilleur;
	private int profondeurMax = 2;
	int nbNoeud = 0;
	
	public ArbreMinMax(DonneesOrdi donnees, MoteurOrdi moteur)
	{
		int joueurPrec;
		this.donneesInitiales = donnees.copie();
		this.moteur = moteur;
		this.meilleur = new Coup(null, null);
		moteur.setDonnees(donnees.copie());
		int n = utils.nbTuilesAccessibles(donnees);
		int nbJoueurs = donnees.getNbJoueurs();
		if(donnees.getJoueurs()[donnees.getJoueurCourant()].getDifficulte() == 3)
		{
			if(nbJoueurs == 2)
			{
				profondeurMax = (int) (logOfBase(n, 50000) + 1);
				if(profondeurMax < 0)
					profondeurMax = Integer.MAX_VALUE;
			}
			else
			{
				profondeurMax = (int) (logOfBase(n, 100000));
				if(profondeurMax < 0)
					profondeurMax = Integer.MAX_VALUE;
			}
		}
		else
		{
			if(nbJoueurs > 2 && n < 30)
				profondeurMax = 3;
			else
				profondeurMax = 2;
		}
			
		if (moteur.getDonnees().getJoueurCourant() == 0)
		{
			joueurPrec = donnees.getNbJoueurs() - 1;
		}
		else
		{
			joueurPrec = (moteur.getDonnees().getJoueurCourant() - 1) % moteur.getDonnees().getNbJoueurs();
		}
		moteur.getDonnees().setJoueurCourant(joueurPrec); 
		calculerNoeud(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public double logOfBase( int base, int num)
	{
		return Math.log(num) / Math.log(base);
	}
	public Coup getMeilleur() {
		return meilleur;
	}
	
	public int calculerNoeud(int profondeurActu, boolean estFeuille, int alpha, int beta)
	{
		nbNoeud++;
		Configuration config;
		Graphe g;
		int retour;
		// FEUILLE
		if(estFeuille)
		{
			// on replace le joueur courant pour evaluer la configuration
			moteur.getDonnees().setJoueurCourant(donneesInitiales.getJoueurCourant());
			// creation du graphe et suppression des ilots
			g = new Graphe(moteur.getDonnees());
			moteur.getDonnees().suppressionComposantes(g.getComposantesInutiles());
			
			// si il ne reste pas que des ilots, heuristique sur les zones
			if(!g.isModeRecup())
			{
				config = new Configuration(moteur.getDonnees());
				retour = config.evaluer(g.getIlots(), g.getIlotsAdverses());
			}
			// sinon, on comptabilise les ilots
			else
			{
				retour = g.evaluerIlots();
			}
		}
		// NOEUD
		else
		{
			int meilleureValeur = Integer.MIN_VALUE;
			int evalActu = 0;
			
			// mise a jour du joueur
			moteur.miseAJourJoueur();
			boolean estNoeudJoueur = (moteur.getDonnees().getJoueurCourant() == donneesInitiales.getJoueurCourant());
			if(estNoeudJoueur)
				meilleureValeur = Integer.MIN_VALUE;
			else
				meilleureValeur = Integer.MAX_VALUE;
			
			// calcul des coups possibles
			ArrayList<Coup> coupsPossibles = utils.getCoupsPossibles(moteur.getDonnees());
			
			// si il y a des coups possibles
			if(coupsPossibles.size() > 0)
			{
				for (int c = 0; c < coupsPossibles.size() ; c++) 
				{
					Coup actuel = coupsPossibles.get(c);
					
					// on joue le coup actuel
					moteur.jouerCoup(actuel.getDepart(), actuel.getArrivee());
					
					// si le jeu est fini on evalue selon le gagnant
					if(moteur.getDonnees().isJeuFini())
					{
						// on verifie si le joueur courant est gagnant
						int joueurCourant = donneesInitiales.getJoueurCourant();
						if(moteur.getDonnees().getGagnant() == donneesInitiales.getJoueurs()[joueurCourant])
						{
							retour = Integer.MAX_VALUE;
						}
						else
						{
							retour = Integer.MIN_VALUE;
						}
					}
					else
					{
						// si le jeu n'est pas fini, on calcule les coups suivants
						evalActu = calculerNoeud(profondeurActu+1, (profondeurActu+1 == profondeurMax), alpha, beta);
						if( (estNoeudJoueur && evalActu > meilleureValeur) || (!estNoeudJoueur && evalActu < meilleureValeur))
						{
							meilleureValeur = evalActu;
							if(profondeurActu == 0)
							{
								meilleur.setDepart(actuel.getDepart());
								meilleur.setArrivee(actuel.getArrivee());
							}
						}
						if(estNoeudJoueur)
						{
							if(meilleureValeur >= beta)
							{
								moteur.annuler();
								return meilleureValeur;
							}
							alpha = Math.max(alpha,  meilleureValeur);
						}
						else
						{
							if(meilleureValeur <= alpha)
							{
								moteur.annuler();
								return meilleureValeur;
							}
							beta = Math.min(beta, meilleureValeur);
						}
					}
					// retour aux donnees precedentes
					moteur.annuler();
				}
				retour = meilleureValeur;
			}
			else
			{
				// si il n'y a pas de coups possibles, il ne reste que des ilots, on calcule la configuration
				retour = calculerNoeud(profondeurActu+1, true, alpha, beta);
			}
		}
		return retour;
	}
	
}
