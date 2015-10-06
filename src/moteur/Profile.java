package moteur;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SuppressWarnings("serial")
public class Profile implements java.io.Serializable{
	String nom;
	int numeroAvatar;
	boolean estOrdi;
	
	public Profile(String nom, int numeroAvatar, boolean estOrdi){
		this.nom = nom;
		this.numeroAvatar = numeroAvatar;
		this.estOrdi = estOrdi;
		
	}

	public Profile(){
		//Ouverture du fichier
		FileInputStream f = null;
		try {
			f = new FileInputStream("./profile/profile");
		} catch (FileNotFoundException e) {
			System.out.println("Client : erreur fichier profile introuvable!!");
			System.exit(-1);
		}
		
		Scanner s = new Scanner(f);
		
		this.nom = s.nextLine();
		this.numeroAvatar = s.nextInt();
		this.estOrdi = false;
		
		//Fermeture du fichier
		s.close();
	}
	
	public String getNom() {
		return nom;
	}

	public int getNumeroAvatar() {
		return numeroAvatar;
	}

	public void setNumeroAvatar(int numeroAvatar) {
		this.numeroAvatar = numeroAvatar;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isEstOrdi() {
		return estOrdi;
	}

	public void setEstOrdi(boolean estOrdi) {
		this.estOrdi = estOrdi;
	}	
	
}
