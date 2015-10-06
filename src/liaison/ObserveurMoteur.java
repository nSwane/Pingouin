/**
 * Auteur : Jeremy
 * Date : 20/05/13
 * Version : 1.0
 */

package liaison;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.Timer;

import liaison.DonneesPartagees.Action;
import maquettes.FenCharger;
import maquettes.FenPengouin;
import maquettes.PanContexte;
import moteur.Joueur;
import moteur.Pingouin;
import ressourcesIHM.AnimationDeplacement;


public class ObserveurMoteur implements Observer, ActionListener
{
	private Liaison liaison;
	private PanContexte panCtxt;
	private Timer t;
	private int numJoueurCourant;
	private DonneesPartagees donnees;
	private FenCharger fenCharger;
	private FenPengouin fenPengouin;
	private boolean panelPionActif;
	private JFrame mainFen;
	
	public ObserveurMoteur(FenPengouin fenPengouin, PanContexte panCtxt, Liaison liaison)
	{
		this.fenPengouin = fenPengouin;
		this.mainFen = this.fenPengouin.getFenetrePrincipale();
		this.panCtxt = panCtxt;
		this.liaison = liaison;
		this.t = new Timer(800, this);
		this.panelPionActif = !liaison.getDonneesAff().isPlacementTermine();
	}
	
	// appelee lors de l'appel a notifyObservers
	public void update(Observable arg0, Object arg1)
	{
//		System.out.println("Update !");

		// Si une partie a ete lancee, on met a jour le conseil
		if(panCtxt != null)
			this.panCtxt.getPlateauDeJeu().setConseilActif(false);
		// Copie des donnees modifiees dans les donnes de l'affichage graphique
		liaison.setDonneesAff(liaison.getDonnees().copie());
		donnees = liaison.getDonneesAff();
		System.out.println("[Observer] action precedente : " + liaison.getActionPrec());
    	System.out.println("[Observer] action courante   : " + donnees.getAction());
		if(liaison.getActionPrec() == Action.Annuler)
		{
			this.annuler();
		}
		else if(liaison.getActionPrec() == Action.Refaire)
		{
			this.refaire();
		}
		else if(liaison.getActionPrec() == Action.Charger)
		{
			this.charger();
		}
		else
		{
			switch (donnees.getAction()) 
			{
				case Charger:
					this.charger(); // ne fait rien
					break;
				case ChargerApercu:
					this.chargerApercu();
					break;
				case Sauvegarder:
					this.sauvegarder(); // ne fait rien
					break;
				case PlacerPingouin:
					this.placerPingouin();
					break;
				case CaseAccessibles:
					this.caseAccessibles();
					break;
				case Conseil:
					this.conseil();
					break;
				case JouerCoup:
					this.jouerCoup();
					break;
				case Options:
					this.options();
					break;
				case Quitter: // le cas ou on quitte dans le reseau
					this.quitter();
					break;
				default:
					// panCtxt est null ssi on fait Charger dans liaisonApercu
					if(panCtxt != null)
						this.panCtxt.repaint();
					break;
			}
		}
	}

	
	private void chargerApercu() {
		
	}

	/**
	 * Affiche le conseil donne par l'ordi si le joueur courant est un humain
	 */
	private void conseil()
	{
		this.panCtxt.getPlateauDeJeu().setConseilActif(true);
		this.panCtxt.repaint();
	}

	/**
	 * retour au menu principal, a revoir. On appelle souvent quitter mais c'est pas forcement dans les bons contextes
	 */
	private void quitter()
	{
//		JFrame mainFen = panCtxt.getFenPengouin().getFenetrePrincipale();
//		mainFen.add(new PanelDepart(mainFen, panCtxt.getFenPengouin())); //ouverture de la fenetre pour choisir ces paramètres
//		this.panCtxt.getFenPengouin().getPanDepart().setEstOuvert(true);
//		mainFen.setVisible(true);
	}

	private void options() {
		System.out.println("[Observer] options()");
		if (!donnees.isJeuFini())
		{
			if (donnees.isPlacementTermine())
			{
				donnees.setAction(Action.JouerCoup);
				this.jouerCoup();
			}
			else
			{
				donnees.setAction(Action.PlacerPingouin);
				this.placerPingouin();
			}
		}
	}

	public void charger()
	{
		mainFen.add(this.panCtxt);
		mainFen.setVisible(true);
		// Si tous les pingouins ont ete places, on remplace le panneau Pion par le panneau Score
		if(donnees.isPlacementTermine())
		{
//			System.out.println("[Observer] Changement panel Pion en Score");
			if (panelPionActif == true)
			{
				this.panelPionActif = false;
				// Si tous les pingouins ont ete places, on remplace le panel Pion par Score
				this.panCtxt.switchPionScore();
			}
			// Si le 1er joueur est un ordi, a la fin du placement il faut declencher la phase de deplacement pour cet ordi
			if(donnees.joueurCourant().estOrdi() && liaison.isOrdiActif())
			{
//				System.out.println("[Observer] Appel a liaison.jouerCoup()");
				donnees.setAction(Action.JouerCoup);
				t.start();
			}
		}
		if(panCtxt != null)
			this.panCtxt.repaint();
		
	}
	
	public void sauvegarder()
	{
//		System.out.println("[Observer] Sauvegarder");
	}
	
	public void placerPingouin()
	{
		panCtxt.getPionJoueur().resetSelected();
		System.out.println("[Observer] PlacerPingouin()");
		if(!liaison.isEnLigne()){
			panCtxt.getOutilsJoueur().getBoutonAnnuler().setEnabled(donnees.isAnnulerPossible());
			panCtxt.getOutilsJoueur().getBoutonRefaire().setEnabled(donnees.isRefairePossible());
		}
		// Si tous les pingouins ont ete places, on remplace le panneau Pion par le panneau Score
		if(donnees.isPlacementTermine())
		{
//			System.out.println("[Observer] Changement panel Pion en Score");
			this.panelPionActif = false;
			// Si tous les pingouins ont ete places, on remplace le panel Pion par Score
			this.panCtxt.switchPionScore();
			// Si le 1er joueur est un ordi, a la fin du placement il faut declencher la phase de deplacement pour cet ordi
			if(donnees.joueurCourant().estOrdi() && liaison.isOrdiActif())
			{
				System.out.println("[Observer] Appel a liaison.jouerCoup()");
				donnees.setAction(Action.JouerCoup);
				t.start();
			}
		}

		this.panCtxt.repaint();
		
		numJoueurCourant = donnees.getJoueurCourant();
		// Si le joueur courant est un ordi, on declenche le timer pour son tour
		if (donnees.getJoueurs()[numJoueurCourant].estOrdi() && !donnees.isPlacementTermine() && liaison.isOrdiActif())
		{
//			System.out.println("[Observer] t.start()");
			t.start();
		}
	}
	
	public void caseAccessibles()
	{
//		System.out.println("[Observer] CaseAccessibles");
		this.panCtxt.repaint();
	}
	
	public void jouerCoup()
	{
		System.out.println("[Observer] JouerCoup()");
		if(!liaison.isEnLigne()){
			panCtxt.getOutilsJoueur().getBoutonAnnuler().setEnabled(donnees.isAnnulerPossible());
			panCtxt.getOutilsJoueur().getBoutonRefaire().setEnabled(donnees.isRefairePossible());
		}
		// Actualisation du panel des Scores
		for (int i = 0; i < donnees.getNbJoueurs(); i++) 
		{
			this.panCtxt.getScoreJoueur().setScore(i, liaison.getDonneesAff().getJoueurs()[i]);
//			System.out.println("[PanelJeu] Score joueur "+(i+1)+" : " + liaison.getDonneesAff().getJoueurs()[i].getNombrePoissons());
		}
		
		
		/******* ANIMATION *******/
		DonneesPartagees d = liaison.getDonneesAff();
		if(d.isAnimationEnCours()){
			liaison.getDonneesAff().setAnimationEnCours(false);
		}else{
			d.setAnimationPingouinPlace(false);
			
			if(d.getCoordPingouinDep()!=null && d.getCoordPingouinArr()!=null && d.getTerrain().consulter(d.getCoordPingouinArr().getI(), d.getCoordPingouinArr().getJ())!=null){
//				System.out.println("[observateur moteur] animation");
				// On recupere le pingouin sur la tuile d'arrivee
				Pingouin p = d.getTerrain().consulter(d.getCoordPingouinArr().getI(), d.getCoordPingouinArr().getJ()).getPingouin();
				d.setImageAnimation(liaison.getAvatars()[p.getJoueur().getNumero()].getImageAvatarJoueur());
				// On supprime le pingouin de la tuile d'arrivee
				d.getTerrain().consulter(p.getCoordonnees().getI(), p.getCoordonnees().getJ()).setPingouin(null);
				
				// Recuperation de l'image du pion du joueur precedent
				Point point = d.getCoordPingouinDep().getPoint();
				
				// on signale qu'une animation demarre
				d.setCoordonneesAnimation(point);
				d.setAnimationEnCours(true);
				liaison.setMoteurActif(true);
//				System.out.println("[Observer] vitesse anim : " + d.getVitesseAnim() + " calcul : " +  (d.getVitesseAnim()/10 + 3));
				// on lance le timer d'animation
				int vitesse = d.getVitesseAnim()/10 + 3;
				Timer t = new Timer(10, new AnimationDeplacement(d.getCoordPingouinDep(), p, point, d.getTerrain(), this.panCtxt, liaison, vitesse));
				t.start();
			}
			this.panCtxt.repaint();
		}
			/***************************/
		
		// Si le jeu n'est pas termine
		if (!donnees.isJeuFini())
		{
			numJoueurCourant = liaison.getDonneesAff().getJoueurCourant();
			// Si le joueur courant est un ordi, on dï¿½clenche le timer pour son tour
			if (liaison.getDonneesAff().getJoueurs()[numJoueurCourant].estOrdi() && liaison.isOrdiActif())
			{
				t.start();
			}
			
			if(!liaison.isEnLigne()){
				panCtxt.getOutilsJoueur().getBoutonAnnuler().setEnabled(true);
			}
		}
		// Sinon quand le jeu est termine
		else
		{
			// Si c'est un match nul (meme nombre de poisson et de tuiles)
			if(donnees.isMatchNul())
				System.out.println("Match nul !");
			// Sinon s'il y a un gagnant
			else
			{
				/** Version avec Tux qui change :  3/5 trucs a changer
				this.panCtxt.getAnimateurJeu().setImage(liaison.getAvatars()[liaison.getDonneesAff().getGagnant().getNumero()]);*/
				this.panCtxt.getAnimateurJeu().parler(this.panCtxt, this.liaison);	// on fait parler notre Tuxy
				this.panCtxt.repaint();
				System.out.println("Le gagnant est : " + donnees.getGagnant().getNom() + "!!!!!!!");
			}
		}
	}
	
	public void annuler()
	{
		System.out.println("[Observer] Annuler()");
		if(!liaison.isEnLigne()){
			panCtxt.getOutilsJoueur().getBoutonAnnuler().setEnabled(donnees.isAnnulerPossible());
			panCtxt.getOutilsJoueur().getBoutonRefaire().setEnabled(donnees.isRefairePossible());
		}
		panCtxt.repaint();
		panCtxt.getAnimateurJeu().parler(panCtxt, liaison);
		// Si l'action annulee est JouerCoup (le placement des pingouins est termine)
		if(donnees.isPlacementTermine())
		{
			// Si le joueur est un ordi, il rejoue
//			if (donnees.joueurCourant().estOrdi())
//				this.jouerCoup();
		}
		// Sinon si l'action est PlacerPingouin
		else
		{
			// Si l'annulation fait passer de la phase de deplacement a celle de placement, 
			// on remplace le panneau des score pas celui de pions
			if(!donnees.isPlacementTermine() && !panelPionActif)
			{		
				panelPionActif = true;
				this.panCtxt.switchScorePion();
			}
			
//			if (donnees.joueurCourant().estOrdi())
//				this.placerPingouin();
		}
		this.panCtxt.repaint();
	}

	private void refaire()
	{
		System.out.println("[Observer] Refaire()");
		if(!liaison.isEnLigne()){
			panCtxt.getOutilsJoueur().getBoutonAnnuler().setEnabled(donnees.isAnnulerPossible());
			panCtxt.getOutilsJoueur().getBoutonRefaire().setEnabled(donnees.isRefairePossible());
		}
			panCtxt.repaint();
		// Si l'action refaite est JouerCoup (le placement des pingouins est termine)
		// ou que c'est le dernier pingouin a etre place (donc on repasse dans la phase de deplacement)
		if(donnees.isPlacementTermine())
		{
//			System.out.println("	[Observer] Refaire() : placement termine");
			// Si le placement est termine et que le panel Pion est toujours actif, on switch de panel
			if(panelPionActif)
			{		
				panelPionActif = false;
				this.panCtxt.switchPionScore();
			}
			// Si le joueur est un ordi, il rejoue
			if (donnees.joueurCourant().estOrdi())
				this.jouerCoup();
		}
		// Sinon si l'action est PlacerPingouin
		else
		{
//			System.out.println("	[Observer] Refaire() : placement non termine + JC ordi ? : " + donnees.joueurCourant().estOrdi());
			if (donnees.joueurCourant().estOrdi())
			{
				this.placerPingouin();
			}
		}
		this.panCtxt.repaint();
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		if(liaison.getActionPrec() != Action.Annuler && liaison.getActionPrec() != Action.Refaire && liaison.getActionPrec() != Action.Charger)
		{
			numJoueurCourant = donnees.getJoueurCourant();
			donnees = liaison.getDonneesAff();
			Joueur jCourant = donnees.getJoueurs()[numJoueurCourant];
			// Si l'action est JouerCoup
			if (donnees.isPlacementTermine())
			{
//				System.out.println("[Observer] JouerCoup de l'ordi");
				liaison.jouerCoup(donnees.getCoordPingouinDep(), donnees.getCoordPingouinArr());
				this.panCtxt.repaint();
			}
			if (!donnees.isPlacementTermine() && donnees.joueurCourant().estOrdi())
			{
				liaison.placerPingouin(donnees.getCoordPingouinInitial());
				if(jCourant.getNbPingouins() < donnees.joueurCourant().getMaxPingouins())
					this.panCtxt.getPionJoueur().getBoutonPion()[numJoueurCourant][jCourant.getNbPingouins()].setVisible(false);
				this.panCtxt.repaint();
			}
		}
		t.stop();
	}

	public FenCharger getFenCharger() {
		return fenCharger;
	}

	public void setFenCharger(FenCharger fenCharger) {
		this.fenCharger = fenCharger;
	}

}
