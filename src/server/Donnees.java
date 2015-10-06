package server;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import ressourcesIHM.AvatarsJoueurs.Avatar;

import liaison.DonneesPartagees;


public class Donnees {
	Socket [] servers;
	int nbClients, maxClients;
	int clientsConnectes;
	boolean poolStart, initStart;
	DonneesPartagees donneesP;
	String [] lesNoms;
	int [] lesAvatars;
	int maxAvatars;
	boolean [] avatarPris;
	int [] JoueurToClient; //Convertir numeroClient vers indice joueur
	int [] ClientToJoueur; //Convertir indice joueur vers numeroClient
	ObjectOutputStream [] outFromServer;
	boolean [] estOrdi;
	boolean partieTerminee;
	boolean reset;
	int nbAccept;
	ServerSocket socketChat;
	
	//estPret[i] vaut vrai si le thread joueur i est pret a recevoir
	boolean [] estPret;
	
	public Donnees(int maxClients){
		servers = new Socket[maxClients];
		lesNoms = new String[maxClients];
		lesAvatars = new int[maxClients];
		JoueurToClient = new int[maxClients];
		ClientToJoueur = new int[maxClients];
		estOrdi = new boolean[maxClients];
		estPret = new boolean[maxClients];
		reset = false;
		nbAccept = 0;		
		outFromServer = new ObjectOutputStream[maxClients];
		
		for(int i = 0; i < maxClients; i++){
			servers[i] = new Socket();
			JoueurToClient[i] = -1;
			ClientToJoueur[i] = -1;
			lesAvatars[i] = -1;
			estOrdi[i] = false;
			estPret[i] = false;
		}
		
		this.maxAvatars = Avatar.values().length;
		avatarPris = new boolean[maxAvatars];
		for(int i = 0; i < maxAvatars; i++){
			avatarPris[i] = false;
		}
		
		this.maxClients = maxClients;
		this.nbClients = 0;
		clientsConnectes = 0;
		poolStart = initStart = false;
		donneesP = null;
		partieTerminee = true;
	}
	
	public void reset(){
		lesNoms = new String[maxClients];
		reset = false;
		nbAccept = 0;		
		outFromServer = new ObjectOutputStream[maxClients];
		
		for(int i = 0; i < maxClients; i++){
			servers[i] = new Socket();
			JoueurToClient[i] = -1;
			ClientToJoueur[i] = -1;
			lesAvatars[i] = -1;
			estOrdi[i] = false;
			estPret[i] = false;
		}
		
		this.maxAvatars = Avatar.values().length;
		avatarPris = new boolean[maxAvatars];
		for(int i = 0; i < maxAvatars; i++){
			avatarPris[i] = false;
		}
		
		this.nbClients = 0;
		clientsConnectes = 0;
		poolStart = initStart = false;
		donneesP = null;
		partieTerminee = true;
	}
	
	//La partie est prete si tous les threads joueurs sont prets a recevoir
	public boolean partiePrete(){
		boolean pret = true;
		int i = 0;
		while(i < nbClients && pret){
			pret &= estPret[i];
		}
		
		return pret;
	}
	
	public synchronized void attendreThreadsJoueurs(){
		while(!partiePrete()){
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Donnees : attendreThreadsJoueurs "+e);
			}
		}
	}
	
	public synchronized void notifyThreadsJoueurs(){
		notifyAll();
	}
	
	public boolean[] getEstPret() {
		return estPret;
	}

	public boolean getEstPret(int i){
		if(0 <= i && i < nbClients){
			return estPret[i];
		}
		return false;
	}
	
	public void setEstPret(boolean[] estPret) {
		this.estPret = estPret;
	}

	public void setEstPret(int i, boolean pret){
		if(0 <= i && i < nbClients){
			estPret[i] = pret;
		}
	}
	
	public boolean isPartieTerminee() {
		return partieTerminee;
	}


	public void setPartieTerminee(boolean partieTerminee) {
		this.partieTerminee = partieTerminee;
	}

	public boolean isInitStart() {
		return initStart;
	}

	public void setInitStart(boolean initStart) {
		this.initStart = initStart;
	}

	
	public int getNbAccept() {
		return nbAccept;
	}

	public void setNbAccept(int nbAccept) {
		this.nbAccept = nbAccept;
	}

	public void addAccept(){
		nbAccept++;
	}
	
	public void removeAccept(){
		if(nbAccept > 0)
			nbAccept--;
	}
	
	public void addClient(){
		if(nbClients < maxClients){
			nbClients++;
		}
	}
	
	public void removeClient(){
		if(nbClients > 0){
			nbClients--;
		}
		
	}
	
	public int[] getJoueurToClient() {
		return JoueurToClient;
	}

	
	public int[] getClientToJoueur() {
		return ClientToJoueur;
	}

	public int getClientToJoueur(int client){
		if(0 <= client && client < maxClients){
			return ClientToJoueur[client];
		}
		return -1;
	}
	
	public void setClientToJoueur(int[] clientToJoueur) {
		ClientToJoueur = clientToJoueur;
	}

	public void setClientToJoueur(int client, int joueur){
		if(0 <= client && client < maxClients){
			ClientToJoueur[client] = joueur;
		}
	}
	
	public int getJoueurToClient(int i){
		if (0 <= i && i < maxClients){
			return JoueurToClient[i];
		}
		return -1;
	}
	
	public void setJoueurToClient(int[] joueurToClient) {
		JoueurToClient = joueurToClient;
	}

	public void setJoueurToClient(int joueur, int client){
		if(0 <= joueur && joueur < maxClients){
			JoueurToClient[joueur] = client;
		}
	}
	
	public int getMaxClients() {
		return maxClients;
	}

	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}

	public ObjectOutputStream[] getOutFromServer() {
		return outFromServer;
	}

	public ObjectOutputStream getOutFromServer(int i){
		if(0 <= i && i < maxClients){
			return outFromServer[i];
		}
		else{
			return null;
		}
	}
	
	public void setOutFromServer(ObjectOutputStream[] outFromServer) {
		this.outFromServer = outFromServer;
	}

	public void setOutFromServer(int i, ObjectOutputStream out){
		if(0 <= i && i < maxClients ){
			outFromServer[i] = out;
		}
	}
	
	public DonneesPartagees getDonneesP() {
		return donneesP;
	}

	public void setDonneesP(DonneesPartagees donneesP) {
		this.donneesP = donneesP;
	}

	public void setServers(int i, Socket server){
		if(0 <= i && i < maxClients){
			servers[i] = server;
			clientsConnectes++;
		}
	}
	
	public Socket[] getServers() {
		return servers;
	}

	public Socket getServers(int i){
		if(0 <= i && i < maxClients){
			return servers[i];
		}
		return null;
	}
	
	public void setServers(Socket[] servers) {
		this.servers = servers;
	}
	
	public int getNbClients() {
		return nbClients;
	}

	public void setNbClients(int nbClients) {
		this.nbClients = nbClients;
	}
	
	public int getClientsConnectes() {
		return clientsConnectes;
	}

	public void setClientsConnectes(int clientsConnectes) {
		this.clientsConnectes = clientsConnectes;
	}
	
	public boolean isPoolStart() {
		return poolStart;
	}

	public void setPoolStart(boolean poolStart) {
		this.poolStart = poolStart;
	}
	
	public synchronized void notifyInit(){
		notifyAll();
	}
	
	public synchronized void waitInit(){
		while(!poolStart){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void attendreConnexion(){
		
		//Attente de connexion de tous les clients
		while(!initStart){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void notifyPool(){
		notifyAll();
	}
	
	public String[] getLesNoms() {
		return lesNoms;
	}

	public void setLesNoms(String[] lesNoms) {
		this.lesNoms = lesNoms;
	}

	public String getNom(int i){
		if(0 <= i && i < maxClients){
			return lesNoms[i];
		}
		return null;
	}
	
	public void setNom(int i, String nom){
		if(0 <= i && i < maxClients){
			lesNoms[i] = nom;
		}
	}
	
	public int getMaxAvatars() {
		return maxAvatars;
	}

	public void setMaxAvatars(int maxAvatars) {
		this.maxAvatars = maxAvatars;
	}
	
	public int[] getLesAvatars() {
		return lesAvatars;
	}

	public int getAvatars(int i){
		if(0 <= i && i < maxClients){
			return lesAvatars[i];
		}
		return 0;
	}
	
	public void setLesAvatars(int[] lesAvatars) {
		this.lesAvatars = lesAvatars;
	}
	
	public void setAvatars(int i, int numeroAvatar){
		//Ne pas attribuer un avatar si celui ci est deja pris
		if(0 <= i && i < maxClients){
			if(!avatarPris[numeroAvatar]){
				lesAvatars[i] = numeroAvatar;
				avatarPris[numeroAvatar] = true;
			}
			else{
				lesAvatars[i] = -1;
			}
		}
	}
	
	public boolean[] getAvatarPris() {
		return avatarPris;
	}

	public void setAvatarPris(boolean[] avatarPris) {
		this.avatarPris = avatarPris;
	}

	public boolean[] getEstOrdi() {
		return estOrdi;
	}

	public boolean getEstOrdi(int i){
		if(0 <= i && i < maxClients){
			return estOrdi[i];
		}
		return false;
	}
	
	public void setEstOrdi(boolean[] estOrdi) {
		this.estOrdi = estOrdi;
	}
	
	public void setEstOrdi(int i, boolean b){
		if(0 <= i && i < maxClients){
			estOrdi[i] = b;
		}
	}
	
	
	public boolean isReset() {
		return reset;
	}


	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public synchronized void attendreReset(){
		while(!reset){
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("attendreReset : Wait failed!!");
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void notifyReset(){
		notifyAll();
	}
	
	public void setSocketChat(ServerSocket s){
		socketChat = s;
	}
	
	public ServerSocket getSocketChat(){
		return socketChat;
	}
	
}
