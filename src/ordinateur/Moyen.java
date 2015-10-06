package ordinateur;

/******************\
 * Carole PLASSON
 * Jérémy WAMBECKE
 * classe Moyen
 * version 1
 ******************
 * Calcul du prochain coup 
 * en utilisant une heuristique basee sur les zones defendues par les pingouins
 */

import liaison.Coordonnees;
import liaison.DonneesPartagees;
import moteur.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Timer;

public class Moyen implements IComportement
{
	DonneesPartagees donnees;
	Random r;
	MoteurOrdi moteur;
	Timer t;
	boolean cheminFinalTrouve;
	ArrayList<Tuile> cheminFinal;
	Pingouin pingouinFinal;
	
	public Moyen(DonneesPartagees d)
	{
		//System.out.println("++++++++++++++CREATION MOYEN+++++++++++++++++");
		donnees = d;		
		r = new Random();
		// creation d'un moteur pour le calcul des coups suivants
		this.moteur = new MoteurOrdi(new DonneesOrdi(donnees));
		//this.cheminFinal = new ArrayList<Tuile>();
		cheminFinalTrouve = false;
	}
	
	/**
	 * calcule le prochain coup, et le met dans donneesPartagees
	 */
	public void prochainCoup()
	{
		Coup meilleur;
		int maxEval = Integer.MIN_VALUE;
		int evalActu;
		Graphe g;
		
		//long debut = System.currentTimeMillis();
		//Scanner sc = new Scanner(System.in);
		Graphe grapheMoteur;
		int difficulteCourant = this.donnees.getJoueurs()[this.donnees.getJoueurCourant()].getDifficulte();
		boolean difficile = (difficulteCourant == 3);
		//System.out.println("avant graphe ");
		//sc.nextLine();
		DonneesOrdi donneesO = new DonneesOrdi(donnees);
		// creation du graphe a partir du terrain
		g = new Graphe(donneesO);

		//System.out.println("apres graphe ");
		//sc.nextLine();
		// simplification du terrain en supprimant les zones inutiles pour le joueur
		donneesO.suppressionComposantes(g.getComposantesInutiles());
		
		// tous les pingouins sont sur des ilots, on doit recuperer le maximum de tuiles
		// on joue tout seul
		if(g.isModeRecup())
		{
			System.out.println("composante utiles");
			System.out.println(g.getComposantesUtiles());
			if(!this.cheminFinalTrouve)
			{
				System.out.println("[MODE RECUPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP] ");
				//if (difficile)
				//{
					///sc.nextLine();
					//System.out.println("liste des composantes du joueur courant : ");
					//System.out.println(g.getIlots());
					//System.out.println("liste des composantes du joueur adverse : ");
					//System.out.println(g.getIlotsAdverses());
				//}
				// calcul du chemin
				//ArrayList<Tuile> chemin =  g.getIlots().get(0).getCheminSol(donneesO.getTerrain());
				/*******Modif***************/
				this.cheminFinal =  g.getIlots().get(0).getCheminSol(donneesO.getTerrain());
				this.pingouinFinal = g.getIlots().get(0).getPingouins().get(0);

				System.out.println("chemin trouve : ");
				System.out.println(cheminFinal);
				// recuperation des coordonnees du coup calcule
				Coordonnees depart = new Coordonnees(cheminFinal.get(0).getLigne(), cheminFinal.get(0).getColonne());
				Coordonnees arrivee = new Coordonnees(cheminFinal.get(1).getLigne(), cheminFinal.get(1).getColonne());
				meilleur = new Coup(depart, arrivee);
				//Scanner sc = new Scanner(System.in);
				//sc.nextLine();
				//System.out.println("meilleur :  " + meilleur);
				cheminFinal.remove(cheminFinal.get(0));
				cheminFinalTrouve = true;
				if (cheminFinal.size() == 1)
				{
					//System.out.println("le chemin est de taille 1, on met le boolean a false");
					cheminFinalTrouve = false;
				}
				
			}
			else// if (cheminFinal.size() > 1)
			{
				
				System.out.println("RECUP du chemin  deja trouve !!!! ");
				System.out.println(cheminFinal);
				Coordonnees depart = new Coordonnees(cheminFinal.get(0).getLigne(), cheminFinal.get(0).getColonne());
				Coordonnees arrivee = new Coordonnees(cheminFinal.get(1).getLigne(), cheminFinal.get(1).getColonne());
				meilleur = new Coup(depart, arrivee);
				cheminFinal.remove(cheminFinal.get(0));
				if (cheminFinal.size() == 1)
				{
					//System.out.println("le chemin est de taille 1, on met le boolean a false");
					cheminFinalTrouve = false;
				}
				
			}
		}
		// on joue contre les autres joueurs
		else
		{
			ArbreMinMax arbre = new ArbreMinMax(donneesO, moteur);
			meilleur = arbre.getMeilleur();
		}
		
		//long fin = System.currentTimeMillis();
		
		//System.out.println("temps ecoule : " + (fin - debut));
		
		donnees.setCoordPingouinDep(meilleur.getDepart());
		donnees.setCoordPingouinArr(meilleur.getArrivee());
	}
	
	/**
	 * renvoie les coordonnees du pingouin a placer
	 */
	public void placerPingouin() 
	{
		//System.out.println("placer pingouin");
		if(donnees.getJoueurs()[donnees.getJoueurCourant()].getNbPingouins() == 0)
		{
			donnees.setCoordPingouinInitial(utils.caseMaxVoisins(donnees.getTerrain()));
		}
		else if(donnees.getJoueurs()[donnees.getJoueurCourant()].getNbPingouins() == 1)
		{
			donnees.setCoordPingouinInitial(this.meilleureZone());
		}
		else if(donnees.getJoueurs()[donnees.getJoueurCourant()].getNbPingouins() == 2)
		{
			donnees.setCoordPingouinInitial(this.bloquage());
		}
		else
		{
			donnees.setCoordPingouinInitial(this.meilleureConfig());
		}
	}
	

	/*
	 * renvoie les coordonnees du pingouin a placer selon la meilleure zone a defendre
	 */
	public Coordonnees meilleureZone()
	{
		Coordonnees placement = new Coordonnees(0, 0);
		int maxEval = Integer.MIN_VALUE;
		int evalActu;
		// mise a jour des donnees du moteur
		moteur.setDonnees(new DonneesOrdi(donnees));
		// recuperation des placements possibles
		ArrayList<Coordonnees> positionPossibles = utils.casesLibresPlacement(donnees.getTerrain());
		// recuperation de la valeur de la premiere position possible
		placement = positionPossibles.get(0);
		moteur.placerPingouin(positionPossibles.get(0));
		
		Configuration config = new Configuration(moteur.getDonnees());
		maxEval = config.evaluer();
		
		for(int i = 1 ; i < positionPossibles.size() ; i++)
		{
			// mise a jour des donnees du moteur
			moteur.setDonnees(new DonneesOrdi(donnees));
			// calcul de la prochaine configuration
			moteur.placerPingouin(positionPossibles.get(i));
			config = new Configuration(moteur.getDonnees());
			evalActu = config.evaluer();
		//	System.out.println("config : " + evalActu);
			// la configuration actuelle est meilleure que la maximale
			if(evalActu > maxEval)
			{
				placement = positionPossibles.get(i);
				maxEval = evalActu;
			}
		}
		return placement;
	}
	
	/*
	 * determine la meilleure position pour bloquer le joueur adverse
	 * le plus avantage
	 */
	public Coordonnees bloquage()
	{
		Coordonnees placement = new Coordonnees(0, 0);
		int maxEval = Integer.MIN_VALUE;
		int minEval = Integer.MAX_VALUE;
		int evalActu = 0;
		int jMax = 0;
		DonneesOrdi donneesO = new DonneesOrdi(donnees);
		ArrayList<Coordonnees> positionPossibles = utils.casesLibresPlacement(donnees.getTerrain());
		Configuration config;
		
		// recherche du joueur le plus avantage
		for(int i = 0 ; i < donnees.getNbJoueurs() ; i++)
		{
			if(i != donnees.getJoueurCourant())
			{
				config = new Configuration(donneesO);
				evalActu = config.evaluer();
				if(evalActu > maxEval)
				{
					jMax = i;
					maxEval = evalActu;
				}
			}
		}
		
		// recherche de la position la plus defavorable pour le meilleur adversaire
		for(int i = 0 ; i < positionPossibles.size() ; i++)
		{
			// mise a jour des donnees du moteur
			donneesO = new DonneesOrdi(donnees);
			moteur.setDonnees(donneesO);
			// calcul de la prochaine configuration
			moteur.placerPingouin(positionPossibles.get(i));
			donneesO.setJoueurCourant(jMax);
			config = new Configuration(moteur.getDonnees());
			evalActu = config.evaluer();
			// la configuration actuelle est meilleure que la maximale
			if(evalActu < minEval)
			{
				placement = positionPossibles.get(i);
				minEval = evalActu;
			}
		}
		
		return placement;
	}
	
	/*
	 * determine la meilleure position pour obtenir la meilleur configuration
	 */
	public Coordonnees meilleureConfig()
	{
		Coordonnees placement = new Coordonnees(0, 0);
		int maxEval = Integer.MIN_VALUE;
		int evalActu = 0;
		ArrayList<Coordonnees> positionPossibles = utils.casesLibresPlacement(donnees.getTerrain());
		Configuration config;
		
		// recherche de la position la plus defavorable pour le meilleur adversaire
		for(int i = 0 ; i < positionPossibles.size() ; i++)
		{
			// mise a jour des donnees du moteur
			moteur.setDonnees(new DonneesOrdi(donnees));
			// calcul de la prochaine configuration
			moteur.placerPingouin(positionPossibles.get(i));
			config = new Configuration(moteur.getDonnees());
			evalActu = config.evaluer();
			// la configuration actuelle est meilleure que la maximale
			if(evalActu > maxEval)
			{
				placement = positionPossibles.get(i);
				maxEval = evalActu;
			}
		}
		
		return placement;
	}
	
	
	
}
