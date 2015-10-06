package ordinateur;
/**
 * Auteurs : Jeremy et Carole
 * Description : Moteur permettant de calculer les coups suivants de l'ordinateur
 * Copie modifiee de Moteur, sans thread 
 */

import java.util.Stack;

import liaison.Coordonnees;
import moteur.Joueur;
import moteur.Pingouin;
import moteur.Tuile;

public class MoteurOrdi
{
	public DonneesOrdi getDonnees() {
		return donnees;
	}

	public void setDonnees(DonneesOrdi donnees) {
		this.donnees = donnees;
	}

	// donnees du moteur
	private DonneesOrdi donnees;
	private DonneesOrdi copieDonnees;
	Stack<DonneesOrdi> pileAnnuler;
	Stack<DonneesOrdi> pileRefaire;
    
	public MoteurOrdi(DonneesOrdi donnees)
	{
		this.donnees = donnees;	
		donnees.setJoueurCourant(0);
		pileAnnuler = new Stack<DonneesOrdi>();
		pileRefaire = new Stack<DonneesOrdi>();
	}
	
	public void caseAccessibles(Coordonnees c)
	{
		//Mise a jour des cases accessibles
		donnees.getTerrain().casesAccessibles(c);
	}
    			
	public void annulerCaseAccessibles(Coordonnees cA)
	{
		//Mise a jour des cases accessibles
		donnees.getTerrain().annulerCasesAccessibles(cA);
	}
    			
	public void jouerCoup(Coordonnees depart, Coordonnees arrivee)
	{
    	
		copieDonnees = donnees.copie();
		
		//Deplacement du pengouin
		deplacerPingouin(depart, arrivee);		
		//Sauvegarde du contexte avant deplacement pour une eventuelle recuperation
		empilerAnnuler(copieDonnees);    
	}
	
	/**
	 * permet de passer au joueur suivant apres un coup
	 */
	public void miseAJourJoueur()
	{
		
		//Mise a jour du joueur courant
		if(donnees.getJoueurCourant() == donnees.getNbJoueurs() - 1){
			donnees.setJoueurCourant(0);
		}
		else{
			donnees.setJoueurCourant(donnees.getJoueurCourant()+1);
		}
		
	}

	// déplace un pingouin des coordonnees depart vers les coordonnees arrivee
	// enleve le marquage (estAccessible) des tuiles successeur
	// retourne 1 si le déplacement a fonctionné, -1 sinon
	public void deplacerPingouin(Coordonnees depart, Coordonnees arrivee){
		
		int ligneDepart = depart.getI();
		int colonneDepart = depart.getJ();
		
		int ligneArrivee = arrivee.getI();
		int colonneArrivee = arrivee.getJ();
		
		Joueur joueurCourant = donnees.getJoueurs()[donnees.getJoueurCourant()];
		
		Tuile source = donnees.getTerrain().consulter(ligneDepart, colonneDepart);	
		Tuile dest = donnees.getTerrain().consulter(ligneArrivee, colonneArrivee);			
		Pingouin p = source.getPingouin();
		
		//Mise a jour du score du joueur
		joueurCourant.setNombreTuiles(joueurCourant.getNombreTuiles()+1);
		joueurCourant.setNombrePoissons(source.getNbPoissons() + joueurCourant.getNombrePoissons());
		
		//On place le pingouin sur la nouvelle tuile
		donnees.getTerrain().consulter(ligneArrivee, colonneArrivee).setPingouin(source.getPingouin());
		p.setCoordonnees(arrivee);
		
		//Suppression de la tuile
		donnees.getTerrain().supprimerTuile(source.getLigne(), source.getColonne());
		
		// on v�rifie si le pingouin est � pr�sent bloqu�
		if(donnees.getTerrain().estPositionIsole(arrivee)){
			dest.getPingouin().setBloque(); 
			dest.getPingouin().getJoueur().pingouinEnMoins();
		}
		
		// on verifie si le deplacement a bloque un pingouin voisin
		Tuile voisin;
		
		// voisin nord-est
		voisin = donnees.getTerrain().consulter(dest.getLigne()-1, dest.getColonne()+1);
		joueurCourant.majPingouinBloquer(voisin);
		
		// voisin est
		voisin = donnees.getTerrain().consulter(dest.getLigne(), dest.getColonne()+2);
		joueurCourant.majPingouinBloquer(voisin);
		
		// voisin sud-est
		voisin = donnees.getTerrain().consulter(dest.getLigne()+1, dest.getColonne()+1);
		joueurCourant.majPingouinBloquer(voisin);
		
		// voisin sud-ouest
		voisin = donnees.getTerrain().consulter(dest.getLigne()+1, dest.getColonne()-1);
		joueurCourant.majPingouinBloquer(voisin);
		
		// voisin ouest
		voisin = donnees.getTerrain().consulter(dest.getLigne(), dest.getColonne()-2);
		joueurCourant.majPingouinBloquer(voisin);
		
		// voisin nord-ouest
		voisin = donnees.getTerrain().consulter(dest.getLigne()-1, dest.getColonne()-1);
		joueurCourant.majPingouinBloquer(voisin);
	}
	
	public void placerPingouin(Coordonnees ci)
	{
		
		// recuperation des joueurs
    	Joueur[] joueurs = donnees.getJoueurs();
    	
		//Placement du pengouin
  	if( joueurs[donnees.getJoueurCourant()].ajouterPengouin(ci) < 0){
			System.out.println(joueurs[donnees.getJoueurCourant()].getNom()+" : Placement impossible");
		}    			
	}
	
	//retourne vrai sil les pengouins de chaque joueurs sont places
	//Sinon faux
	public boolean placementTermine()
	{
		boolean tousPlaces = true;
		Joueur [] tabj = donnees.getJoueurs();
		
		for(int i = 0; i < donnees.getNbJoueurs(); i++){
			tousPlaces &= tabj[i].getPengouinsPlaces();
		}
		return tousPlaces;
	}
	
	//Empiler un contexte pour une eventuelle recuperation
		public void empilerAnnuler(DonneesOrdi dp){
			pileAnnuler.push(dp);
		}
		
		//Annuler un coup --> restauration de la configuration precedente
		//--> Score, joueur actuel et etat de la gaufre
		//Retourne null s'il n y a rien a annuler,
		//Retourne le contexte precedent sinon
		public void annuler(){
			try{
				//Sauvegarde du contexte courant dans la pile refaire
				//pileRefaire.push(donnees.copie());
				
				//Recuperation du contexte precedent
				donnees = pileAnnuler.pop();
				
			}catch(Exception e){
				donnees.setAnnulerPossible(false);
				System.out.println("Erreur annulation: pile vide");
			}
		}
	
		//Faire un coup precedemment annule
		//Retourne null s'il n y a rien a refaire,
		//Retourne le contexte precedent sinon
		public void refaire(){
			try{
				
				//Recuperation du contexte precedent
				donnees = pileRefaire.pop();
				
			}catch(Exception e){
				donnees.setRefairePossible(false);
				System.out.println("Erreur refaire: pile vide");
			}
		}
	

}
