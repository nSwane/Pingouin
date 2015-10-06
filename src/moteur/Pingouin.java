package moteur;

import java.io.Serializable;

import liaison.Coordonnees;

@SuppressWarnings("serial")
public class Pingouin implements Serializable
{
	private int numero;
	private Joueur joueur;
	private boolean bloque;
	private Coordonnees coordonnees;
	
	public Pingouin(int n, Joueur j, Coordonnees c){
		numero = n;
		coordonnees = new Coordonnees(c.getI(), c.getJ());
		joueur = j;
		bloque = false;
	}
	
	public Pingouin copie(){
		Pingouin p = new Pingouin(numero, joueur.copie(), coordonnees.copie());
		p.setBloque(bloque);
		
		return p;
	}
	
	public boolean isBloque() {
		return bloque;
	}

	public void setBloque(boolean bloque) {
		this.bloque = bloque;
	}

	public int getNumero(){
		return numero;
	}
	
	public Joueur getJoueur(){
		return joueur;
	}
	
	public Tuile getTuile()
	{
		return joueur.getTerrain().consulter(coordonnees.getI(), coordonnees.getJ());
	}
	
	public Coordonnees getCoordonnees(){
		return coordonnees;
	}
	
	public void setCoordonnees(Coordonnees c){
		coordonnees = new Coordonnees(c.getI(), c.getJ());
	}
	
	public void setBloque(){
		bloque = true;
	}
	
	public void resetBloque(){
		bloque = false;
	}
	
	public String toString(){
		return (numero+":"+joueur.getNom()+":"+coordonnees);
	}
}