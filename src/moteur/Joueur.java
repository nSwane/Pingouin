/********************
*  Nawaoui Swane et Giaccone Marc
*  classe Joueur
*  definition d'un joueur
*
*/

package moteur;

import java.util.ArrayList;

import liaison.Coordonnees;

@SuppressWarnings("serial")
public class Joueur implements java.io.Serializable{
	

	private int numero;
	private String nom;
    private int numAvatar;
    
    private int nbPingouins;
    private int maxPingouins;
	
	private Pingouin [] mesPingouins;
	
	private boolean pengouinsPlaces;
	private int pingouinsRestants; // nombre de pingouins qui ne sont pas encore bloqu�

	private int nbPoissons;
	private int nbTuiles;
	
	private boolean estOrdi;
	private int difficulte;	//difficulte de l'ordinateur
	private Terrain terrain;
	
	
	public Joueur(int numero, String nom, int numAvatar, int maxPingouins, boolean b,int difficulte, Terrain t, int nbPoissons, int nbTuiles){
		this.numero = numero;
		this.nom = nom;
		this.numAvatar = numAvatar;
		this.difficulte = difficulte;
		nbPingouins = 0;
		pingouinsRestants = this.maxPingouins = maxPingouins;
		mesPingouins = new Pingouin[maxPingouins];
		setPengouinsPlaces(false);
		
		this.nbPoissons = nbPoissons;
		this.nbTuiles = nbTuiles;
		estOrdi = b;
		terrain = t;
	}

	public int getNbPingouins(){
		return nbPingouins;
	}

	public void setNbPingouins(int nbPingouins) {
		this.nbPingouins = nbPingouins;
	}
 
	// copie d'un joueur
	public Joueur copie(){
		Joueur cp = new Joueur(numero, nom, numAvatar, maxPingouins, estOrdi,difficulte, terrain, nbPoissons, nbTuiles);
		cp.setPingouinsRestants(pingouinsRestants);
		cp.setPengouinsPlaces(pengouinsPlaces);
		cp.setNbPingouins(nbPingouins);
		cp.setNombrePoissons(nbPoissons);
		cp.setNombreTuiles(nbTuiles);
		
		Pingouin [] p = cp.getMesPingouins();
		for(int i = 0; i < maxPingouins; i++){
			if(mesPingouins[i] != null){ //Copie des pingouins
				int numero = mesPingouins[i].getNumero();
				Coordonnees c = mesPingouins[i].getCoordonnees().copie();
				
				p[i] = new Pingouin(numero, cp, c);
				p[i].setBloque(mesPingouins[i].isBloque());
				
			}
		}
		return cp;
	}
	
	public String getNom(){
		return nom;
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	//Ce joueur ajoute un pengouin sur la tuile terrain[i][j]
	//Retourne -1 si l'ajout est impossible, 0 sinon
	public int ajouterPengouin(Coordonnees c){
		int i = c.getI();
		int j = c.getJ();
		if(terrain.estTuileValide(i, j) && terrain.consulter(i, j).getNbPoissons() == 1 && nbPingouins < maxPingouins){
			if (nbPingouins == maxPingouins-1)
				this.pengouinsPlaces = true;
			//Ajout du pengouins
			mesPingouins[nbPingouins] = new Pingouin(nbPingouins, this, c);
			
			//On place le pingouin sur la tuile
			terrain.consulter(i, j).setPingouin(mesPingouins[nbPingouins]);
			
			nbPingouins++;
			return 0;
		}else{
				System.out.println("[Joueur] " +numero+" : Placement pengouin impossible");
				return -1;
		}
	}
	
	// déplace un pingouin des coordonnees depart vers les coordonnees arrivee
	// enleve le marquage (estAccessible) des tuiles successeur
	// retourne 1 si le déplacement a fonctionné, -1 sinon
	public int deplacerPingouin(Coordonnees depart, Coordonnees arrivee)
	{
		
		int ligneDepart = depart.getI();
		int colonneDepart = depart.getJ();
		
		int ligneArrivee = arrivee.getI();
		int colonneArrivee = arrivee.getJ();
		
		int res = -1;
		boolean trouve = false;
		
		//Verifier que la tuile soit valide
		if(!terrain.estTuileValide(ligneArrivee,  colonneArrivee)){
			System.out.println("[Joueur] " +numero+" : Deplacement pengouin impossible");
			return -1;
		}
		
		//Verifier que la tuile est un successeur de la tuile courante
		Tuile source = terrain.consulter(ligneDepart, colonneDepart);
		Tuile dest = terrain.consulter(ligneArrivee, colonneArrivee);
		Pingouin p = source.getPingouin();
		
		if(p == null){
			System.out.println("[Joueur] " +"Aucun pingouin n'est present sur cette tuile");
			return -1;
		}
		
		if(terrain.suivantsNE(ligneDepart, colonneDepart, false, dest)){
			trouve = true;
		}
		if(terrain.suivantsE(ligneDepart, colonneDepart, false, dest)){
			trouve = true;
		}
		if(terrain.suivantsSE(ligneDepart, colonneDepart, false, dest)){
			trouve = true;
		}
		if(terrain.suivantsSO(ligneDepart, colonneDepart, false, dest)){
			trouve = true;
		}
		if(terrain.suivantsNO(ligneDepart, colonneDepart, false, dest)){
			trouve = true;
		}
		if(terrain.suivantsO(ligneDepart, colonneDepart, false, dest)){
			trouve = true;
		}
		
		if(trouve){
			//Mise a jour du score du joueur
			nbTuiles++;
			nbPoissons += source.getNbPoissons();
			
			//On place le pingouin sur la nouvelle tuile
			terrain.consulter(ligneArrivee, colonneArrivee).setPingouin(source.getPingouin());
			p.setCoordonnees(arrivee);
			
			//Suppression de la tuile
			terrain.supprimerTuile(source.getLigne(), source.getColonne());
			
			// on v�rifie si le pingouin est � pr�sent bloqu�
			if(terrain.estPositionIsole(arrivee)){
				dest.getPingouin().setBloque(); 
				dest.getPingouin().getJoueur().pingouinEnMoins();
//				System.out.println("pingouin du joueur "+dest.getPingouin().getJoueur().getNom()+" bloqu�");
			}
			
			// on v�rifie si le d�placement a bloqu� un pingouin voisin
			Tuile voisin;
			
			// voisin nord-est
			voisin = terrain.consulter(dest.getLigne()-1, dest.getColonne()+1);
			majPingouinBloquer(voisin);
			
			// voisin est
			voisin = terrain.consulter(dest.getLigne(), dest.getColonne()+2);
			majPingouinBloquer(voisin);
			
			// voisin sud-est
			voisin = terrain.consulter(dest.getLigne()+1, dest.getColonne()+1);
			majPingouinBloquer(voisin);
			
			// voisin sud-ouest
			voisin = terrain.consulter(dest.getLigne()+1, dest.getColonne()-1);
			majPingouinBloquer(voisin);
			
			// voisin ouest
			voisin = terrain.consulter(dest.getLigne(), dest.getColonne()-2);
			majPingouinBloquer(voisin);
			
			// voisin nord-ouest
			voisin = terrain.consulter(dest.getLigne()-1, dest.getColonne()-1);
			majPingouinBloquer(voisin);
			
			res = 1;
		}
		
		return res;
	}
	
	// v�rifie si une tuile contient un pingouin et si celui-ci est bloqu�
	// se charge de mettre � jour l'�tat (bloqu� ou non) du pingouin ainsi que le nombre de pingouin non bloqu� d'un joueur
	public void majPingouinBloquer(Tuile t){
		if(t != null && t.estOccupee()){
			if(terrain.estPositionIsole(t.getPingouin().getCoordonnees())){
				t.getPingouin().setBloque(); 
				t.getPingouin().getJoueur().pingouinEnMoins();
//				System.out.println("pingouin du joueur "+t.getPingouin().getJoueur().getNom()+" bloqu�");
			}
		}
	}
	
	//Retourne la liste des pengouins de ce joueur
	public ArrayList<Pingouin> getPengouins(){
		ArrayList<Pingouin> lp = new ArrayList<Pingouin>();
		for(int i = 0; i < nbPingouins; i++){
			lp.add(mesPingouins[i]);
		}
		return lp;
	}
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public void ajouterPoissons(int n){
		nbPoissons += n;
	}

	public int getNombrePoissons(){
		return nbPoissons;
	}
		
	public void setNombrePoissons(int n){
		nbPoissons = n;
	}
	
	public void ajouterTuiles(int n){
		nbTuiles += n;
	}
	
	public int getNombreTuiles(){
		return nbTuiles;
	}

	public void setNombreTuiles(int n){
		nbTuiles = n;
	}
	
	/**
	 * Teste si la difficulte est differente de 0 (0 = humain, tout le reste = ordi)
	 * @return
	 */
	public boolean estOrdi(){
		return difficulte != 0;
	}
	
	public boolean getPengouinsPlaces() {
		return pengouinsPlaces;
	}

	public void setPengouinsPlaces(boolean pengouinsPlaces) {
		this.pengouinsPlaces = pengouinsPlaces;
	}
	
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	
	public void setPingouinsRestants(int n){
		pingouinsRestants = n;
	}
	
	public int getPingouinsRestants(){
		return pingouinsRestants;
	}
	
	public void pingouinEnMoins(){
		pingouinsRestants--;
	}
	
	public void setNom(String nom){
		this.nom = nom;
	}
	
	public Pingouin[] getMesPingouins() {
		return mesPingouins;
	}

	public void setMesPingouins(Pingouin[] mesPingouins) {
		this.mesPingouins = mesPingouins;
	}
	
	public int getMaxPingouins() {
		return maxPingouins;
	}

	public void setMaxPingouins(int maxPingouins) {
		this.maxPingouins = maxPingouins;
	}
	
	public String toString(){
		return (numero+" ("+nbPoissons+","+nbTuiles+") ");
	}

	/**
	 * @return the difficulte
	 */
	public int getDifficulte() {
		return difficulte;
	}

	/**
	 * @param difficulte the difficulte to set
	 */
	public void setDifficulte(int difficulte) {
		this.difficulte = difficulte;
		if(difficulte == 0){
			this.estOrdi = false;
		}else{
			this.estOrdi = true;
		}
	}

	public int getNumAvatar() {
		return numAvatar;
	}

	public void setNumAvatar(int numAvatar) {
		this.numAvatar = numAvatar;
	}
	
}