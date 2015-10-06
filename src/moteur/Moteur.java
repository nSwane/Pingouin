package moteur;

import java.io.File;

import liaison.Coordonnees;
import liaison.DonneesPartagees;
import liaison.Liaison;
import ordinateur.Ordinateur;

public class Moteur implements Runnable
{
	private Liaison liaison;
	private DonneesPartagees d;
	private Fonctionnalites f;
    private boolean continuer;
    private Ordinateur ordi[];
	Joueur [] tabj;
	String nomFichier;
	DonneesPartagees copieDP, precD;
    
	public Moteur(Liaison l)
	{
		this.liaison = l;
		
		this.d = l.getDonnees();	
		
        this.continuer = true;
		this.f = new Fonctionnalites(d);
		
		d.setJoueurCourant(0);
		ordi = new Ordinateur[d.getNbJoueurs()];
		
		for (int c = 0; c < d.getNbJoueurs(); c++) 
		{
			ordi[c] = new Ordinateur(d, d.getJoueurs()[c].getDifficulte());
		}
	}

	public void run() {
		
        do
        {    
        	liaison.attendre();
        	
        	d = liaison.getDonnees();
        	tabj = d.getJoueurs();
        	
//        	if(a == Action.Sauvegarder || a == Action.Charger
//        	|| a == Action.JouerCoup || a == Action.CaseAccessibles
//        	|| a == Action.AnnulerCaseAccessibles){
//        		f.viderRefaire();
//        	}
        	if(d.isViderPile())
        	{
	    		f.viderRefaire();
	    	}
        	d.setViderPile(false);
        	
    		liaison.setActionPrec(d.getAction());
    		
    		System.out.println("[Moteur] Action : " + d.getAction());
    		switch (d.getAction()) {
    		
    		case ListerFichiers: //Liste des fichiers sauvegardes
    			String [] s = f.listerFichier();
    			d.setListeFichier(s);
    			
    			break;
    			
    		case Charger:
    			System.out.println("[Moteur] Charger() --> perso ? : " + d.isPartiePerso());
    			this.charger();
    			break;
    			
    		case ChargerApercu:
    			this.chargerApercu();
    			break;
    			
    		case Sauvegarder:
    			this.sauvegarder();
    			break;
    			
    		case CaseAccessibles:    			
    			//Recuperation des coordonnees du pengouin
    			Coordonnees c = d.getCoordPingouinAcc();
    			
    			//Mise a jour des cases accessibles
    			d.getTerrain().casesAccessibles(c);
    			
    			break;
    			
    		case AnnulerCaseAccessibles:    			
    			//Recuperation des coordonnees du pengouin
    			Coordonnees cA = d.getCoordPingouinAcc();
    			
    			//Mise a jour des cases accessibles
    			d.getTerrain().annulerCasesAccessibles(cA);
    			
    			break;
    			
    		case JouerCoup:
    			this.jouerCoup();
    			break;
    			
    		case Annuler:
    			this.annuler();
    			break;
    			
    		case Refaire:
    			this.refaire();
    			break;
    			
    		case Conseil:
    			this.conseil();
    			break;
    			
    		case PlacerPingouin:
    			this.placerPingouin();
    			break;
    			
            case Quitter:
                continuer = false;
                break;
            
            case SupprimerSauvegarde :
            		this.supprimerSauvegarde();
            		break;
            
    		default:
    			break;
    		}
    		liaison.notifierMiseAJour();
        } while(continuer);
	}
	
	private void chargerApercu() {
		//Recuperation du nom de fihicer
		nomFichier = d.getFichier();

		System.out.println("[Moteur] Charger() --> perso ? : " + d.isPartiePerso());
		//Chargement et mise a jour des donnees
		DonneesPartagees dp = f.charger(nomFichier);
		liaison.setDonnees(dp);
	}

	private void charger() {
		System.out.println("[Moteur] Charger() --> perso ? : " + d.isPartiePerso());
		//Recuperation du nom de fihicer
		nomFichier = d.getFichier();
		//Chargement et mise a jour des donnees
		DonneesPartagees dp = f.charger(nomFichier);
		liaison.setDonnees(dp);
		this.d = dp;

		ordi = new Ordinateur[d.getNbJoueurs()];
		
		for (int c = 0; c < d.getNbJoueurs(); c++) 
		{
			ordi[c] = new Ordinateur(d, d.getJoueurs()[c].getDifficulte());
		}
	}

	private void sauvegarder() {
		//Recuperation du nom de fichier
		nomFichier = d.getFichier();
		System.out.println("[Moteur] Sauvegarder() --> perso ? : " + d.isPartiePerso());
//		f.existeFichier(nomFichier);
//		if (!d.isFichierExiste())
//			//Sauvegarde
			f.sauvegarder(nomFichier);		
	}

	private void jouerCoup() {
		//Recuperation du contexte courant
		copieDP = liaison.getDonnees().copie();
		
		if(d.getJoueurs()[d.getJoueurCourant()].getDifficulte() != ordi[d.getJoueurCourant()].getDifficulte())
		{
			ordi[d.getJoueurCourant()] = new Ordinateur(d, d.getJoueurs()[d.getJoueurCourant()].getDifficulte());
		}
		//Recuperation des coordonnees Depart et arrivee
		if(tabj[d.getJoueurCourant()].estOrdi())
		{
//			System.out.println("[Moteur] Coup de l'ordi");
			ordi[d.getJoueurCourant()].prochainCoup();
		}

//		System.out.println("[moteur jouer coup] coup de l'ordi : depart :" + d.getCoordPingouinDep() + " arrivee : " + d.getCoordPingouinArr());
		Coordonnees cd = d.getCoordPingouinDep();
		Coordonnees ca = d.getCoordPingouinArr();
//		System.out.println("[moteur jouer coup] coup recupere : depart :" + cd + " arrivee : " + ca);
		    			
		if(cd != null && ca != null)
		{
			//Deplacement du pengouin
			if(tabj[d.getJoueurCourant()].deplacerPingouin(cd, ca) < 0)
			{
				System.out.println(tabj[d.getJoueurCourant()].getNom()+ " : Deplacement impossible");
				return;
			}
			else
			{
				d.setAnnulerPossible(true);
			}
		}
		else
		{
			return;
		}
		
		//Sauvegarde du contexte avant deplacement pour une eventuelle recuperation
		f.empilerAnnuler(copieDP);
		
		//Mise a jour du joueur courant    			
		if(d.getJoueurCourant() == d.getNbJoueurs() - 1){
			d.setJoueurCourant(0);
		}
		else{
			d.setJoueurCourant(d.getJoueurCourant()+1);
		}
		
		// on v�rifie si le joueur suivant peut jouer, si ce n'est pas le cas il passe son tour
		// tant qu'on a pas trouv� un joueur pouvant jouer on passe au suivant
		// si tous les joueurs ne peuvent pas jouer c'est fini
		int n = d.getNbJoueurs();
		while(d.getJoueurs()[d.getJoueurCourant()].getPingouinsRestants() <= 0 && n > 0 ){
			if(d.getJoueurCourant() == d.getNbJoueurs() - 1){
				d.setJoueurCourant(0);
			}else{
				d.setJoueurCourant(d.getJoueurCourant()+1);
			}
			n--;
		}

		// on v�rifie si tous les joueurs sont bloqu�s
		// si c'est le cas on ramasse les derni�res tuiles et trouve le gagnant
		if(n == 0){
			d.getTerrain().ramasserTuilesFin(tabj);
			d.setGagnant(d.getTerrain().gagnantPartie(tabj));
			System.out.println("Le gagnant est: "+d.getGagnant().getNom()+"!!!");
			d.setJeuFini(true);
		}
		System.out.println("JC : "+tabj[d.getJoueurCourant()].getNom());
		System.out.println("PR : "+tabj[d.getJoueurCourant()].getPingouinsRestants());		
	}

	private void annuler() {
		//Recuperation du contexte precedent
		System.out.println("moteur : annuler");
		precD = f.annuler();
		if(precD != null){
			precD.setRefairePossible(true);
			//Mise a jour du contexte courant
			liaison.setDonnees(precD);
			
			for (int o = 0; o < d.getNbJoueurs(); o++) 
			{
				ordi[o] = new Ordinateur(liaison.getDonnees(), precD.getJoueurs()[o].getDifficulte());
			}
			//ordi = new Ordinateur(liaison.getDonnees());
			
			//Recuperation des coordonnees du pengouin
			Coordonnees cx = precD.getCoordPingouinAcc();
			
			//Mise a jour des cases accessibles
			if(cx != null){
				precD.getTerrain().annulerCasesAccessibles(cx);
			}
			tabj = precD.getJoueurs();
//			System.out.println("JC : "+tabj[precD.getJoueurCourant()].getNom());
//			System.out.println("PR : "+tabj[precD.getJoueurCourant()].getPingouinsRestants());
			d = precD;
		}		
	}

	private void refaire() {
		//Recuperation du contexte precedent
		precD = f.refaire();
		if(precD != null){
			precD.setAnnulerPossible(true);
			//Mise a jour du contexte courant
			liaison.setDonnees(precD);
			d = precD;
			
			if(!precD.isJeuFini())
			{
				for (int o = 0; o < d.getNbJoueurs(); o++) 
				{
					ordi[o] = new Ordinateur(liaison.getDonnees(), precD.getJoueurs()[o].getDifficulte());
				}
			}
		}		
	}

	private void conseil() {
		if (!d.joueurCourant().estOrdi())
		{
			
			if(d.isPlacementTermine()){
				ordi[d.getJoueurCourant()].prochainCoup();
				d.setCoordConseilPingouinDep(new Coordonnees(d.getCoordPingouinDep().getI(), d.getCoordPingouinDep().getJ()));
				d.setCoordConseilPingouinArr(new Coordonnees(d.getCoordPingouinArr().getI(), d.getCoordPingouinArr().getJ()));
				System.out.println("Conseil: de "+d.getCoordPingouinDep()+" vers "+d.getCoordPingouinArr());
				
			}else{
//				System.out.println("donnees du moteur : " + d);
//				d.getTerrain().afficher();
				ordi[d.getJoueurCourant()].placerPingouin();
				d.setCoordConseilPingouinInitial(new Coordonnees(d.getCoordPingouinInitial().getI(), d.getCoordPingouinInitial().getJ()));
				System.out.println("Conseil: de "+d.getCoordConseilPingouinInitial());
				System.out.println("Tuile conseil : " + d.getTerrain().consulter(d.getCoordConseilPingouinInitial().getI(), d.getCoordConseilPingouinInitial().getJ()));
				
			}
		}		
	}

	private void placerPingouin() {
		//Recuperation du contexte courant
		copieDP = liaison.getDonnees().copie();

		if(d.getJoueurs()[d.getJoueurCourant()].getDifficulte() != ordi[d.getJoueurCourant()].getDifficulte())
		{
			ordi[d.getJoueurCourant()] = new Ordinateur(d, d.getJoueurs()[d.getJoueurCourant()].getDifficulte());
		}
		
		if(tabj[d.getJoueurCourant()].estOrdi())
			ordi[d.getJoueurCourant()].placerPingouin();
		//Recuperation des coordonnees du pengouin
		Coordonnees ci = d.getCoordPingouinInitial();
		
		//Placement du pengouin
		if( tabj[d.getJoueurCourant()].ajouterPengouin(ci) < 0){
			System.out.println(tabj[d.getJoueurCourant()].getNom()+" : Placement impossible");
			return;
		}  
		else
		{
			d.setAnnulerPossible(true);
		
			//Sauvegarde du contexte avant placement pour une eventuelle recuperation
			f.empilerAnnuler(copieDP);
			
			//Si tous les placements sont termines, l'indiquer aux autres
			if(placementTermine()){
				d.setPlacementPingouinTermine(placementTermine());
				d.getTerrain().verifierBlocages(tabj);
	//			f.viderAnnuler();
			}
	//		System.out.println(placementTermine());
			
			//Mise a jour du joueur courant
			if(d.getJoueurCourant() == d.getNbJoueurs() - 1){
				d.setJoueurCourant(0);
			}
			else{
				d.setJoueurCourant(d.getJoueurCourant()+1);
			}		
		}  			
	}

	//retourne vrai sil les pengouins de chaque joueurs sont places
	//Sino faux
	public boolean placementTermine()
	{
		boolean tousPlaces = true;
		Joueur [] tabj = d.getJoueurs();
		
		for(int i = 0; i < d.getNbJoueurs(); i++)
		{
			tousPlaces &= tabj[i].getPengouinsPlaces();
		}
		return tousPlaces;
	}
	
	public void supprimerSauvegarde(){
		File f = new File(d.getFichier());
		f.delete();
	}

}
