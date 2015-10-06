package moteur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import liaison.Coordonnees;

@SuppressWarnings("serial")
public class Terrain implements java.io.Serializable{
	private int largeur, hauteur;
	//lesTuiles[i] <-> nombre de tuiles de type i+1 poissons
	private int [] lesTuiles;
	private Tuile [][] terrain;
	
	//Creation du terrain avec tirage aleatoire de tuile
	public Terrain(){
		largeur = 15;
		hauteur = 8;
		lesTuiles = new int[3];
		lesTuiles[0] = 30;
		lesTuiles[1] = 20;
		lesTuiles[2] = 10;
		
		File dossierTerrain = new File("Terrains/default");
		String listeFichiers[] = dossierTerrain.list();
		if(listeFichiers != null && listeFichiers.length != 0){
			System.out.println("Lecture d'un fichier: "+listeFichiers[0]);
			lireTerrain(listeFichiers[0], largeur, hauteur);
		}else{
			terrain = new Tuile[hauteur][largeur];
			//Initialisation du terrain
			Random r = new Random();
			boolean termine = false;
			int [] nbPoissons = new int[3]; //nbPoissons[i] <-> nombre de tuile placees de type i+1 poissons
			nbPoissons[0] = nbPoissons[1] = nbPoissons[2] = 0;
			
			int i = 0;
			while(i < hauteur && !termine){
				int j = 1-i%2;
				while(j < largeur && !termine){
					//Tirage d'un entier entre 1 et le nombre de tuile restante
					int n = r.nextInt(lesTuiles[0]+lesTuiles[1]+lesTuiles[2]);
					
					//Creation de la tuile
					if(n < lesTuiles[0]){ 
						terrain[i][j] = new Tuile(i, j, 1, false);
						lesTuiles[0]--;
					}else if(n < (lesTuiles[0]+lesTuiles[1])){ 
						terrain[i][j] = new Tuile(i, j, 2, false);
						lesTuiles[1]--;
					}else{ 
						terrain[i][j] = new Tuile(i, j, 3, false);
						lesTuiles[2]--;
					}
					
					if(lesTuiles[0]==0 && lesTuiles[1]==0 && lesTuiles[2]==0){
						termine = true;
					}
					j += 2;
				}
				i++;
			}
		}
	}
	
	//Creation du terrain a partir d'element connus
	public Terrain(Tuile [][] t){
		largeur = 15;
		hauteur = 8;
		lesTuiles = new int[3];
		lesTuiles[0] = 30;
		lesTuiles[1] = 20;
		lesTuiles[2] = 10;
		terrain = new Tuile[hauteur][largeur];
		
		//Initialisation du terrain
		for(int i = 0; i < hauteur; i++){
			for(int j = 0; j < largeur; j++){
				terrain[i][j] = t[i][j];
			}
		}
	}
	
	//Creation du terrain a partir d'un fichier
	public void lireTerrain(String fichier, int largeur, int hauteur){
		terrain = new Tuile[hauteur][largeur];
        FileInputStream f;
        
		try {
			//Ouverture du fichier
			f = new FileInputStream("Terrains/default/"+fichier);
			Scanner s = new Scanner(f);
			
			 //Lecture
			int i = 0;
			while(i < hauteur && s.hasNext()){
				int j = 1-i%2;
				while(j < largeur && s.hasNext()){
					terrain[i][j]= new Tuile(i, j, s.nextInt(), false);
					j += 2;
				}
				i++;
			}
			
			s.close();
			
	        //Fermeture du fichier
	        f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Impossible d'ouvrir le fichier. Vérifier que le fichier existe et que le chemin est correcte");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("il s'est produit une erreur lors de la fermeture du fichier.");
			System.exit(0);
		}
	}
	
	//Copier le terrain
	public Terrain copie(){	
		Tuile [][] tab = new Tuile[hauteur][largeur];
		for(int i = 0; i < hauteur; i++){
			for(int j = 0; j < largeur; j++){
				if(terrain[i][j] != null){
					tab[i][j] = terrain[i][j].copie();
				}
				else{
					tab[i][j] = null;
				}
			}
		}
		
		Terrain t = new Terrain(tab);
		return t;
	}
	
	// regarde tous les pingouins de chaque joueur afin de v�rifier ceux qui sont bloqu�
	public void verifierBlocages(Joueur tabj[]){
		ArrayList<Pingouin> listePingouin;
    	for(int i=0; i<tabj.length; i++){
    		listePingouin = tabj[i].getPengouins();
    		for(int j=0; j<listePingouin.size(); j++){
    			if(!listePingouin.get(j).isBloque() && estPositionIsole(listePingouin.get(j).getCoordonnees())){
    				System.out.println("pingouin bloqu�");
    				listePingouin.get(j).setBloque();
    				tabj[i].pingouinEnMoins();
    				if(tabj[i].getPingouinsRestants() <= 0){
    					System.out.println("le joueur "+tabj[i].getNom()+" ne peut plus jouer.");
    				}
    			}
    			
    		}
    	}
	}
	
	// r�cup�re les tuile sous les pingouins � la fin d'une partie
	public void  ramasserTuilesFin(Joueur tabj[]){
		ArrayList<Pingouin> listePingouins;
		Tuile t;
		Pingouin p;
		for(int i=0; i<tabj.length; i++){
			listePingouins = tabj[i].getPengouins();
			for(int j=0; j<listePingouins.size(); j++){
				p = listePingouins.get(j);
				t = consulter(p.getCoordonnees().getI(), p.getCoordonnees().getJ());
				tabj[i].ajouterPoissons(t.getNbPoissons());
				tabj[i].ajouterTuiles(1);
				supprimerTuile(t.getLigne(), t.getColonne());
			}
		}
	}
	
	// determine le vainqueur de la partie en fonction du score et des tuiles
	public Joueur gagnantPartie(Joueur tabj[]){
		int maxScore = 0, maxTuile = 0;
		Joueur gagnant=null;
		
		for(int i=0; i<tabj.length; i++){
			if(tabj[i].getNombrePoissons() > maxScore){
				maxScore = tabj[i].getNombrePoissons();
				maxTuile = tabj[i].getNombreTuiles();
				gagnant = tabj[i];
			}else if(tabj[i].getNombrePoissons() == maxScore){
				if(tabj[i].getNombreTuiles() > maxTuile){
					maxScore = tabj[i].getNombrePoissons();
					maxTuile = tabj[i].getNombreTuiles();
					gagnant = tabj[i];
				}
			}
		}
		
		return gagnant;
	}
	
	//Retourne la tuile terrain[ligne][console]
	public Tuile consulter(int ligne, int colonne){
		if(ligne>=0 && ligne<hauteur && colonne>=0 && colonne<largeur){
			return terrain[ligne][colonne];
		}else{
			return null;
		}
		
	}
	
	//Supprimer la tuile terrain[i][j]
	public void supprimerTuile(int i, int j){
		terrain[i][j] = null;
	}
	
	public void retablirTuile(Tuile t)
	{
		terrain[t.getLigne()][t.getColonne()] = t;
	}
	
	//Marquer les cases accessibles depuis une tuile (coordonnées)
	public void casesAccessibles(Coordonnees c){
		int i = c.getI();
		int j = c.getJ();
		
		// on parcours chaque branche (verticales et diagonales)
		// on active le marquage (estAccessible()) des cases
		suivantsNE(i, j, true, null);
		suivantsE(i, j, true, null);
		suivantsSE(i, j, true, null);
		suivantsSO(i, j, true, null);
		suivantsO(i, j, true, null);
		suivantsNO(i, j, true, null);
	}
	
	// Enlever le marquage des cases accessibles depuis une tuile (coordonn�es)
	public void annulerCasesAccessibles(Coordonnees c){
		int i = c.getI();
		int j = c.getJ();
		
		// on parcours chaque branche (verticales et diagonales)
		// on active le marquage (estAccessible()) des cases
		suivantsNE(i, j, false, null);
		suivantsE(i, j, false, null);
		suivantsSE(i, j, false, null);
		suivantsSO(i, j, false, null);
		suivantsO(i, j, false, null);
		suivantsNO(i, j, false, null);
	}
	
	//Retourne vrai si cette tuile est isolé
	//faux sinon
	public boolean estPositionIsole(Coordonnees c){
		//Une tuile est isolee si toutes ses tuiles voisines sont occupees ou vide
		boolean bloqueNE, bloqueE, bloqueSE, bloqueSO, bloqueO, bloqueNO;
		
		try{
			bloqueNE = !estTuileValide(c.getI()-1, c.getJ()+1);
			bloqueE = !estTuileValide(c.getI(), c.getJ()+2);
			bloqueSE = !estTuileValide(c.getI()+1, c.getJ()+1);
			bloqueSO = !estTuileValide(c.getI()+1, c.getJ()-1);
			bloqueO = !estTuileValide(c.getI(), c.getJ() -2);
			bloqueNO = !estTuileValide(c.getI()-1, c.getJ()-1);
		}catch(NullPointerException e){
			System.out.println("La tuile vide!");
			return false;
		}
		
		return bloqueNE && bloqueE && bloqueSE && bloqueSO && bloqueO && bloqueNO;
	}
	
	//Retourne vrai si la tuile terrain[ligne][colonne] est valide
	//Sinon false
	public boolean estTuileValide(int ligne, int colonne){
		// tuile inexistante
		if(ligne<0 || ligne>=hauteur || colonne<0 || colonne>=largeur){ 
			return false;
		}
		
		// tuile vide
		if(terrain[ligne][colonne] == null){
			return false;
		}
		// tuile occupée
		if(terrain[ligne][colonne].estOccupee()){
			return false;
		}
		
		return true;
	}
	
	// Retourne vrai si la tuile dest est accessibles depuis les coordonnées i,j en prenant la direction nord-est
	// marque (booleen estAccessible) les tuiles accessibles à partir de i,j si le booleen b est à true  
	public  boolean suivantsNE(int ligne, int colonne, boolean b, Tuile dest){
		boolean res = false;
		ligne --;
		colonne++;
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain[ligne][colonne] != null && !terrain[ligne][colonne].estOccupee()){
			if(b){
//				System.out.println("case accessible en : ("+ ligne+","+colonne+")");
				terrain[ligne][colonne].setAccessible();
			}else{
				terrain[ligne][colonne].resetAccessible();
			}
			
			if(dest != null && ligne == dest.getLigne() && colonne == dest.getColonne()){
				res = true;
			}
			ligne--;
			colonne++;
		}
		return res;
	}
	
	// Retourne vrai si la tuile dest est accessibles depuis les coordonnées i,j en prenant la direction nord-ouest
	// marque (booleen estAccessible) les tuiles accessibles à partir de i,j si le booleen b est à true 
	public boolean suivantsNO(int ligne, int colonne, boolean b, Tuile dest){
		boolean res = false;
		ligne--;
		colonne--;
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain[ligne][colonne] != null && !terrain[ligne][colonne].estOccupee()){
			if(b){
				terrain[ligne][colonne].setAccessible();
			}else{
				terrain[ligne][colonne].resetAccessible();
			}
			
			if(dest != null && ligne == dest.getLigne() && colonne == dest.getColonne()){
				res = true;
			}
			ligne--;
			colonne--;
		}
		return res;
	}
	
	// Retourne vrai si la tuile dest est accessibles depuis les coordonnées i,j en prenant la direction sud-est
	// marque (booleen estAccessible) les tuiles accessibles à partir de i,j si le booleen b est à true
	public boolean suivantsSE(int ligne, int colonne, boolean b, Tuile dest){
		boolean res = false;
		ligne++;
		colonne++;
		while(ligne >= 0 && ligne < hauteur && colonne >= 0 && colonne < largeur && terrain[ligne][colonne] != null && !terrain[ligne][colonne].estOccupee()){
			if(b){
				terrain[ligne][colonne].setAccessible();
			}else{
				terrain[ligne][colonne].resetAccessible();
			}
			
			if(dest != null && ligne == dest.getLigne() && colonne == dest.getColonne()){
				res = true;
			}
			ligne++;
			colonne++;
		}
		return res;
	}
	
	// Retourne vrai si la tuile dest est accessibles depuis les coordonnées i,j en prenant la direction sud-ouest
	// marque (booleen estAccessible) les tuiles accessibles à partir de i,j si le booleen b est à true
	public boolean suivantsSO(int ligne, int colonne, boolean b, Tuile dest){
		boolean res = false;
		ligne++;
		colonne--;
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain[ligne][colonne] != null && !terrain[ligne][colonne].estOccupee()){
			if(b){
				terrain[ligne][colonne].setAccessible();
			}else{
				terrain[ligne][colonne].resetAccessible();
			}
			
			if(dest != null && ligne == dest.getLigne() && colonne == dest.getColonne()){
				res = true;
			}
			ligne++;
			colonne--;
		}
		return res;
	}
	
	// Retourne vrai si la tuile dest est accessibles depuis les coordonnées i,j en prenant la direction est
	// marque (booleen estAccessible) les tuiles accessibles à partir de i,j si le booleen b est à true
	public boolean suivantsE(int ligne, int colonne, boolean b, Tuile dest){
		boolean res = false;
		colonne += 2;
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain[ligne][colonne] != null && !terrain[ligne][colonne].estOccupee()){
			if(b){
				terrain[ligne][colonne].setAccessible();
			}else{
				terrain[ligne][colonne].resetAccessible();
			}
			
			if(dest != null && ligne == dest.getLigne() && colonne == dest.getColonne()){
				res = true;
			}
			colonne += 2;
		}
		return res;
	}
	
	// Retourne vrai si la tuile dest est accessibles depuis les coordonnées i,j en prenant la direction ouest
	// marque (booleen estAccessible) les tuiles accessibles à partir de i,j si le booleen b est à true
	public boolean suivantsO(int ligne, int colonne, boolean b, Tuile dest){
		colonne -= 2;
		boolean res = false;
		
		while(0 <= ligne && ligne < hauteur && 0 <= colonne && colonne < largeur && terrain[ligne][colonne] != null && !terrain[ligne][colonne].estOccupee()){
			if(b){
				terrain[ligne][colonne].setAccessible();
			}else{
				terrain[ligne][colonne].resetAccessible();
			}
			
			if(dest != null && ligne == dest.getLigne() && colonne == dest.getColonne()){
				res = true;
			}
			colonne -= 2;
		}
		return res;
	}	
	
	public int getHauteur(){
		return hauteur;
	}
	
	public int getLargeur(){
		return largeur;
	}
	
	//Affichage du terrain
	public void afficher(){
		// affichage des indices y
		System.out.printf("%-2c", ' ');
		for(int j = 0; j < largeur; j++){
			System.out.printf("%-2d ", j);
		}
		System.out.println();
	
		for(int i = 0; i < hauteur; i++){
			System.out.printf("%-2d", i); // affichage des indices x
			for(int j = 0; j < largeur; j++){
				if(terrain[i][j] != null){
					if(terrain[i][j].estOccupee()){
						System.out.printf("%-2c ", 'P');
					}else if(terrain[i][j].estAccessible()){
						System.out.printf("%-2c ", 'X');
					}else{
						System.out.printf("%-2d ",terrain[i][j].getNbPoissons());
					}
				}else{
					System.out.printf("%-2c ", ' ');
				}
			}
			System.out.println();
			
		}
		System.out.println();
	}
}