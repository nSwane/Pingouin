package liaison;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class Coordonnees implements java.io.Serializable
{
	private int i;
	private int j;
	int cote = 40;
	
	public Coordonnees(int i, int j)
	{
		this.i = i;
		this.j = j;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}
	
	public String toString()
	{
		return ("("+i+","+j+")");
	}
	
	public Coordonnees copie()
	{
		Coordonnees c;
		if (this != null)
			c = new Coordonnees(i, j);
		else
			c = null;
		return c;
	}
	
	public Point getPoint(){
		int x;
		int y = 70*i;
		
		if(i%2 == 0){ // i pair
			x = 78*((j-1)/2)+38;
		}else{
			x = 78*(j/2);
		}
		
		return new Point(x,  y);
	}
	
	public Point getPoint2(){
		int x, y;
		Polygon p2= getPolygon(0, 0, cote); // Cree un hexagone
		Rectangle r = p2.getBounds();

		x = (j-1)/2*r.width + (i%2+1)*(r.width/2) + (j-1)/2*10;
		y = (int)(i*cote*1.5+0.5 + i * 10);
		
		return new Point(x,  y);
	}
	
	public static Polygon getPolygon(int x,int y,int cote)
	{
		// Forme le polygone
		int haut=cote/2;
		int larg=(int)(cote*(Math.sqrt(3)/2));
		Polygon p=new Polygon();
		p.addPoint(x,y+haut);					// / superieur gauche
		p.addPoint(x+larg,y); 					// \ superieur droit   
		p.addPoint(x+2*larg,y+haut);			// | milieu droit     
		p.addPoint(x+2*larg,y+(int)(1.5*cote)); // / inferieur droit
		p.addPoint(x+larg,y+2*cote);			// \ inferieur gauche
		p.addPoint(x,y+(int)(1.5*cote));		// | milieu gauche
		return p;
	}
}
