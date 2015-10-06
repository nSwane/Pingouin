/**
 * Auteur : Jeremy
 * Description : Implemente les methodes de liaison entre thread graphique et thread moteur
 * (la notion de thread devient transparente, rien a gerer au niveau moteur/graphique)
 * Date : 20/05/13
 * Version : 1.0
 */

package liaison;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.Observer;

import liaison.DonneesPartagees.Action;
import moteur.Moteur;
import moteur.MoteurReseau;
import moteur.Profile;
import ressourcesIHM.AvatarsJoueurs;
import ressourcesIHM.AvatarsJoueurs.Avatar;

public class Liaison extends Observable
{
	Thread threadMoteur;
	public Moteur moteur;
	boolean verbose;
	// Donnees modifiees par le moteur
	DonneesPartagees donnees;
	// Donnees utilisees par le graphique (remplacee par donnees quand le moteur a fini une action)
	DonneesPartagees donneesAff;
	String debug;
	
	// Tableau des AvatarsJoueurs
	private AvatarsJoueurs[] avatars;
	
	// contient l'action precedente
	Action actionPrec;
	
	Observer observer;
	private boolean moteurActif;
	private boolean ordiActif;
	private boolean fenetreOuverte;
	private boolean chargementFini;

	
	/******************************************************/
	/**                     SERVER                       **/
	/******************************************************/
	
	//indique s'il s'agit d'une partie en ligne ou non
		@SuppressWarnings("unused")
		private boolean enLigne;

		//Indique l'etape de connexion est passee ou non
		private boolean finConnexion;

		//Indique si la connexion a ete etablie ou non
		private boolean connexionEtablie;

		//Indique si la connexion a ete annulee ou non
		private boolean annuler;
		
		//Indique si un message a ete recu ou non
		private boolean msgRecu;
		
		//Indique si un message a ete envoye ou non
		private boolean msgEnvoye;
		
		//Canal de communication entre client et server
		private Socket client;

		//Flux de donnees :	//Server -> client
		private ObjectInputStream in;	

		//Flux de donnees :	//Client -> Server
		private ObjectOutputStream out;
		

		/**
		 * Constructeur
		 * @param enLigne : indique s'il s'agit d'un partie en ligne ou pas
		 */
		public Liaison(boolean enLigne, boolean isPartiePerso)
		{
			this.enLigne = enLigne;
			this.finConnexion = false;
			this.connexionEtablie = false;
			this.annuler = false;
			this.ordiActif = true;
			this.chargementFini = false;
			
			if(!enLigne)
				this.fenetreOuverte = false;
			if(!isPartiePerso){
				
				donnees = new DonneesPartagees(isPartiePerso);
				
			}else{
				donnees = new DonneesPartagees(isPartiePerso);
			}
			verbose = false;
			donneesAff = donnees.copie();
			this.avatars = new AvatarsJoueurs[donnees.getNbJoueurs()];
			for (int i = 0; i < donnees.getNbJoueurs(); i++)
			{
				this.avatars[i] = new AvatarsJoueurs(i, donnees.getJoueurs()[i].getNumAvatar(), this);
			}
			
		}

		//Tentative de connexion au server distant
		//Retourne -1 si la connexion n'a pas pu etre etablie ou
		//si l'initialisation de la partie a echouee
		//Retourne 0 en cas de succes
		//Retourne 1 si le server est plein
		public int connect(){

			//Flux de donnees
			InputStream inFromServer;		
			OutputStream outToServer;

			//Info server
//					String serverName = "imablade207.e.ujf-grenoble.fr";
//			String serverName = "Pingouins.galdween.fr";
			byte [] addr = {(byte)192, (byte)168, 22, 10};
			
//			String serverName = "192.168.22.10";
//			String serverName = "localhost";
			int port = 1076;

			try{
				//Connexion au serveur defini par serverName et port
				InetAddress ia = InetAddress.getByAddress(addr);
				client = new Socket(ia,port);
//				client = new Socket(serverName, port);
				System.out.println("Client : connexion en cours");

				//Arret au bout de 60s d'inactivite
				try {
					client.setSoTimeout(30000);
				} catch (SocketException e) {
					System.out.println("Client : Armement du timeout echouee");
				}

				//Recuperation des flux de donnees
				//Client -> Server
				outToServer = client.getOutputStream();
				out = new ObjectOutputStream(outToServer);

				//Server -> Client
				inFromServer = client.getInputStream();
				in = new ObjectInputStream(inFromServer);

				//Informer le server que je suis pret a emettre	
				out.writeObject(new Profile());
				out.flush();
				out.reset();

				//Attendre que le server initialise la partie			
				donnees = (DonneesPartagees) in.readObject();
				if(donnees == null){
					//Le server n'a pas reussi a me trouver une partie
					//--> Server plein!
					System.out.println("Connexion interrompu par le server");
					client.close();

					return 1;
				}

				//Mise a jour des donnees
				donneesAff = donnees.copie();
				this.avatars = new AvatarsJoueurs[donnees.getNbJoueurs()];
				for(int i = 0; i < donnees.getNbJoueurs(); i++){
					this.avatars[i] = new AvatarsJoueurs(i, donnees.getJoueurs()[i].getNumAvatar(), this);
				}

				System.out.println("Client: La partie peut commencer");

				return 0;
			}
			catch(SocketTimeoutException e){
				System.out.println("Le server ne m'a pas trouve de partie a temps");
				try {
					client.close();
				} catch (IOException e1) {
					System.out.println(e1);
				}

				return -1;

			}
			catch(IOException e){
				System.out.println(e);
				System.out.println("Client : La connexion a echouee");

				return -1;
			}
			catch (ClassNotFoundException e) {
				System.out.println(e);
				System.out.println("Client : L'initialisation de la partie a echouee");
				//Fermeture de la socket cote client
				try {
					client.close();
				} catch (IOException e1) {
					System.out.println("Echec fermeture socket client");
				}

				return -1;
			}
		}

		public boolean isAnnuler() {
			return annuler;
		}

		public void setAnnuler(boolean annuler) {
			this.annuler = annuler;
		}

		public boolean isFinConnexion() {
			return finConnexion;
		}

		public void setFinConnexion(boolean finConnexion) {
			this.finConnexion = finConnexion;
		}

		public boolean isConnexionEtablie() {
			return connexionEtablie;
		}

		public void setConnexionEtablie(boolean connexionEtablie) {
			this.connexionEtablie = connexionEtablie;
		}

		public synchronized void attendreFinConnexion(){
			while(!finConnexion){
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("Client : attendreFinConnexion : "+e);
				}
			}
		}

		public synchronized void notifyConnexion(){
			finConnexion = true;
			notifyAll();
		}

		public Socket getClient() {
			return client;
		}

		public void setClient(Socket client) {
			this.client = client;
		}

		public ObjectInputStream getIn() {
			return in;
		}

		public void setIn(ObjectInputStream in) {
			this.in = in;
		}

		public ObjectOutputStream getOut() {
			return out;
		}

		public void setOut(ObjectOutputStream out) {
			this.out = out;
		}
		
		
	/******************************************************/
	/**                   GRAPHIQUE                      **/
	/******************************************************/
	
	/**
	 *  cree le thread implementant le comportement du moteur
	 *  prend en parametre une reference a la classe observant les donnees
	 *  la methode update de cette classe sera appelee lors d'un appel a notifierMiseAJour
	 *  (voir plus bas)
	 *  @param observer : objet observant les donnees partagees
	 *  
	 */
	public synchronized void nouvellePartie(Observer observer)
	{
		debug = verbose?"[Liaison] nouvellePartie()\n":""; System.out.print(debug);
		Moteur moteur = new Moteur(this);
		threadMoteur = new Thread(moteur);
		threadMoteur.start();
		this.addObserver(observer);
	}

	public synchronized void nouvellePartieReseau(Observer observer)
	{
		debug = verbose?"[Liaison] nouvellePartie()\n":""; System.out.print(debug);
		MoteurReseau moteur = new MoteurReseau(this);
		threadMoteur = new Thread(moteur);
		threadMoteur.start();
		this.addObserver(observer);
	}
	
	/**
	 *  Lister les fichiers contenus dans le dossier de sauvegarde
	 * @param fichier : aucun
	 */
	public synchronized void listerFichiers()
	{
		if (!moteurActif && !fenetreOuverte)
		{
			debug = verbose?"[Liaison] listerFichiers()\n":""; System.out.print(debug);
			donnees.setAction(Action.ListerFichiers);
			notify();
		}
	}
	/**
	 * Suppprime la sauvegarde de nom nomFichier dans le dossier Sauvegarde
	 * @param nomFichier
	 */
	public synchronized void supprimerSauvegarde(String nomFichier)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			debug = verbose?"[Liaison] supprimer()\n":""; System.out.print(debug);
			donnees.setFichier("./Sauvegarde/" + nomFichier);
			donnees.setAction(Action.SupprimerSauvegarde);
			notify();
		}
	}
	
	/**
	 *  charge une partie en mettant a jour les donnees
	 * @param fichier : nom du fichier a charger
	 */
	public synchronized void charger(String nomFichier)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			System.out.println("[Liaison] Charger() --> perso ? : " + donnees.isPartiePerso());
			donnees.setViderPile(true);
			debug = verbose?"[Liaison] charger()\n":""; System.out.print(debug);
			donnees.setFichier("./Sauvegarde/" + nomFichier);
			donnees.setAction(Action.Charger);
//			System.out.println("[Liaison] reveil du moteur charger");
			notify();
		}
	}
	
	/**
	 *  methode charger mais pour avoir uniquement les informations necessaires 
	 *  a l'affichage de l'apercu
	 * @param fichier : nom du fichier a charger
	 */
	public synchronized void chargerApercu(String nomFichier)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			debug = verbose?"[Liaison] charger()\n":""; System.out.print(debug);
			donnees.setFichier("./Sauvegarde/" + nomFichier);
			donnees.setAction(Action.ChargerApercu);
//			System.out.println("[Liaison] reveil du moteur charger");
			notify();
		}
	}
	
	/**
	 *  sauvegarde les donnees de la partie en cours sous le nom donne
	 * @param fichier : nom du fichier a sauvegarder
	 */
	public synchronized void sauvegarder(String fichier)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			donnees.setViderPile(true);
			debug = verbose?"[Liaison] sauvegarder()\n":""; System.out.print(debug);
			donnees.setFichier(fichier);
			donnees.setAction(Action.Sauvegarder);
			notify();
		}
	}
	
	/**
	 * demande au moteur de mettre a jour les cases accessibles pour le pingouin passe en parametre
	 * le moteur doit mettre le booleen estAccessible des tuiles a vrai
	 * @param pingouin : coordonnees du pingouin selectionne
	 */
	public synchronized void casesAccessibles(Coordonnees pingouin)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			donnees.setViderPile(true);
			debug = verbose?"[Liaison] casesAccessibles()\n":""; System.out.print(debug);
			donnees.setCoordPingouinAcc(pingouin);
			donnees.setAction(Action.CaseAccessibles);
			notify();
		}
	}
	
	/**
	 * demande au moteur de mettre a jour les cases accessibles pour le pingouin passe en parametre
	 * le moteur doit mettre le booleen estAccessible des tuiles a vrai
	 * @param pingouin : coordonnees du pingouin selectionne
	 */
	public synchronized void annulerCasesAccessibles(Coordonnees pingouin)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			donnees.setViderPile(true);
			debug = verbose?"[Liaison] annulerCasesAccessibles()\n":""; System.out.print(debug);
			donnees.setCoordPingouinAcc(pingouin);
			donnees.setAction(Action.AnnulerCaseAccessibles);
			notify();
		}
	}
	
	/**
	 * demande au moteur de mettre a jour la partie selon le coup joue par le joueur
	 * met les cases accessibles a faux
	 * refaire ne doit plus etre possible
	 * @param depart : coordonnees du point de depart du pingouin
	 * @param arrivee : coordonnees du point d'arrivee du pingouin
	 */
	public synchronized void jouerCoup(Coordonnees depart, Coordonnees arrivee)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			donnees.setViderPile(true);
//			System.out.println("[liaison jouer coup] depart :" + depart + " arrivee : " + arrivee);
			debug = verbose?"[Liaison] jouerCoup()\n":""; System.out.print(debug);
			if(depart != null)
				donnees.setCoordPingouinDep(depart);
			if(arrivee != null)
				donnees.setCoordPingouinArr(arrivee);
//			System.out.println("[liaison jouer coup] action :" + donnees.getAction());
			donnees.setAction(Action.JouerCoup);
			notify();
		}
	}
	
	/**
	 * demande au moteur d'annuler le dernier coup
	 * le moteur modifie les donnees pour revenir au coup precedent
	 * si l'annulation n'est plus possible, le booleen annulerPossible doit etre mis a faux
	 * refaire doit etre mis a vrai si annuler a fonctionne
	 */
	public synchronized void annuler()
	{
		if (!moteurActif && !fenetreOuverte)
		{
			debug = verbose?"[Liaison] annuler()\n":""; System.out.print(debug);
			donnees.setAction(Action.Annuler);
			notify();
		}
	}
	
	/**
	 * demande au moteur de refaire le dernier coup annule
	 * le moteur modifie les donnees pour revenir au coup suivant
	 * si refaire n'est plus possible, le booleen refairePossible doit etre mis a vrai
	 * annuler doit etre mis a vrai si refaire a fonctionne
	 */
	public synchronized void refaire()
	{
		if (!moteurActif && !fenetreOuverte)
		{
			debug = verbose?"[Liaison] refaire()\n":""; System.out.print(debug);
			donnees.setAction(Action.Refaire);
			notify();
		}
	}
	
	/**
	 * demande au moteur de conseiller un coup au joueur
	 * le moteur doit indiquer les coordonnees du pingouin a jouer dans coordConseilPingouin
	 * et les coordonnees de la case dans coordConseilCase 
	 */
	public synchronized void conseil()
	{
		if (!moteurActif && !fenetreOuverte)
		{
			debug = verbose?"[Liaison] conseil()\n":""; System.out.print(debug);
			donnees.setAction(Action.Conseil);
			notify();
		}
	}
	
	/**
	 * demande au moteur de placer un pingouin sur la carte
	 * @param position : coordonnees de la case ou le pingouin a ete place
	 */
	public synchronized void placerPingouin(Coordonnees position)
	{
		if (!moteurActif && !fenetreOuverte)
		{
			donnees.setViderPile(true);
			debug = verbose?"[Liaison] placerPingouin()\n":""; System.out.print(debug);
			donnees.setAction(Action.PlacerPingouin);
			if(position != null)
				donnees.setCoordPingouinInitial(position);
			notify();
		}
	}
	
	/**
	 * demande au moteur de mettre fin au jeu
	 */
	public synchronized void quitter()
	{
		debug = verbose?"[Liaison] placerPingouin()\n":""; System.out.print(debug);
		donnees.setAction(Action.Quitter);
		notify();
	}
	
	/**
	 * permet de donner les modifications effectuees dans les options
	 * @param optionAffichageConfirmation 
	 * @param optionCommentJouer 
	 */
	public synchronized void changerOption(String[] nomJoueurs,int[] difficulte, Avatar[] avatarJoueur, boolean optionCasesAccessibles, boolean optionAffichageConfirmation, int vitesseAnim, boolean optionCommentJouer){
		if(nomJoueurs != null && difficulte != null && avatarJoueur != null)
		{
			for(int i = 0; i < donnees.getNbJoueurs(); i++){
				// On change les noms des joueurs
				donnees.getJoueurs()[i].setNom(nomJoueurs[i]);
				// On change les avatars des joueurs
				donnees.getJoueurs()[i].setNumAvatar(avatarJoueur[i].ordinal());
				// On change la difficulte des ordinateurs
				donnees.getJoueurs()[i].setDifficulte(difficulte[i]);
			}
		}
		
		donnees.setOptionCasesAccessibles(optionCasesAccessibles);
		donnees.setOptionAffichageConfirmation(optionAffichageConfirmation);
		donnees.setVitesseAnim(vitesseAnim);
		donnees.setOptionCommentJouer(optionCommentJouer);
		donnees.setAction(Action.Options);
		// on met a jour le fichier d'option
		donnees.reecrireFichierOption();
		setDonneesAff(donnees.copie());
	}
	
	/**
	 * permet de relancer le moteur
	 */
	public synchronized void relancerMoteur()
	{
		// Apres un appel a donnees
		donnees.setAction(Action.Options);
	}
	
	public synchronized void attendreChargement()
	{
		while (!chargementFini) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public synchronized void notifierChargement()
	{
		notifyAll();
	}
	
	public synchronized void muteSound(boolean speakerActif, boolean musicActive)
	{
		System.out.print("[Liaison] oldSpeakerActif = " + donnees.isSpeakerActif());
		donnees.setSpeakerActif(speakerActif);
		donnees.setMusicActive(musicActive);
		donneesAff.setSpeakerActif(speakerActif);
		donneesAff.setMusicActive(musicActive);
//		System.out.println("speakerActif = " + donnees.isSpeakerActif() + " "+ donneesAff.isSpeakerActif());
	}

	/******************************************************/
	/**                     MOTEUR                       **/
	/******************************************************/
	
	/**
	 * indique la fin de traitement d'une demande a la partie graphique
	 * effectue un notifyObservers et appelle ainsi la methode update de l'observer de la partie graphique
	 * les donnees sont coherentes et peuvent etre traites
	 */
	public synchronized void notifierMiseAJour()
	{
		debug = verbose?"[Liaison] notifierMiseAJour()\n":""; System.out.print(debug);
		super.setChanged();
		super.notifyObservers();
	}
	
	/**
	 * permet de faire attendre le moteur jusqu'a ce que la partie graphique le reveille
	 */
	public synchronized void attendre()
	{
		debug = verbose?"[Liaison] attendre()\n":""; System.out.print(debug);
		try {
			moteurActif = false;
//			System.out.println("Moteur attend");
			wait();
			moteurActif = true;
//			System.out.println("Moteur reveille");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	public DonneesPartagees getDonneesAff() {
		return donneesAff;
	}

	public void setDonneesAff(DonneesPartagees donneesAff) {
		this.donneesAff = donneesAff;
	}

	public DonneesPartagees getDonnees() {
		return donnees;
	}

	public synchronized void setDonnees(DonneesPartagees donnees) {
		this.donnees = donnees;
	}

	public Action getActionPrec() {
		return actionPrec;
	}

	public void setActionPrec(Action actionPrec) {
		this.actionPrec = actionPrec;
	}

	public AvatarsJoueurs[] getAvatars() {
		return avatars;
	}

	public void setAvatars(AvatarsJoueurs[] avatars) {
		this.avatars = avatars;
	}

	public boolean isMoteurActif() {
		return moteurActif;
	}
	
	public void setMoteurActif(boolean moteurActif) {
		this.moteurActif= moteurActif;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}
	
	public synchronized void informerMoteur(){
		notifyAll();
	}
	
	public synchronized void attendreReponse() {
		try {
			wait();
		} catch (InterruptedException e) {
			System.out.println(e);
			System.out.println("Liaison : erreur AttendreReponse");
			System.exit(-1);
		}
	}

	public boolean isOrdiActif() {
		return ordiActif;
	}

	public void setOrdiActif(boolean ordiActif) {
		this.ordiActif = ordiActif;
	}

	public boolean isChargementFini() {
		return chargementFini;
	}

	public void setChargementFini(boolean chargementFini) {
		this.chargementFini = chargementFini;
	}

	public boolean isMsgRecu() {
		return msgRecu;
	}

	public void setMsgRecu(boolean msgRecu) {
		this.msgRecu = msgRecu;
	}

	public boolean isMsgEnvoye() {
		return msgEnvoye;
	}

	public void setMsgEnvoye(boolean msgEnvoye) {
		this.msgEnvoye = msgEnvoye;
	}

	/**
	 * @return the enLigne
	 */
	public boolean isEnLigne() {
		return enLigne;
	}

	/**
	 * @param enLigne the enLigne to set
	 */
	public void setEnLigne(boolean enLigne) {
		this.enLigne = enLigne;
	}
	
}
