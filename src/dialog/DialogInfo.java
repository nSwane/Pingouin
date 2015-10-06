/****************************************************************
*   Auteur : Gregory                            				*
*   Nom de la classe : EcouteurDeClavier        				*
*   Version : 1                           						*
*****************************************************************
*   Description : Stocke les informations changees dans les		*
*   boites de dialogues											*
*                                								*
****************************************************************/

package dialog;
import java.awt.Color;


public class DialogInfo 
{
	private String nomJ1, nomJ2, lvlBot1, lvlBot2;
	private Color colJ1, colJ2;
		 
	public DialogInfo(){}
	public DialogInfo(String nomJ1, String nomJ2, String lvlBot1, String lvlBot2 , Color colJ1, Color colJ2)
	{
		this.nomJ1 = nomJ1;
		this.nomJ2 = nomJ2;
		this.lvlBot1 = lvlBot1;
		this.lvlBot2 = lvlBot2;
		this.colJ1 = colJ1;
		this.colJ2 = colJ2;
	}
	  
	public String getNomJ1() {
		return nomJ1;
	}
	public void setNomJ1(String nomJ1) {
		this.nomJ1 = nomJ1;
	}
	public String getNomJ2() {
		return nomJ2;
	}
	public void setNomJ2(String nomJ2) {
		this.nomJ2 = nomJ2;
	}
	public String getLvlBot1() {
		return lvlBot1;
	}
	public void setLvlBot1(String lvlBot1) {
		this.lvlBot1 = lvlBot1;
	}
	public String getLvlBot2() {
		return lvlBot2;
	}
	public void setLvlBot2(String lvlBot2) {
		this.lvlBot2 = lvlBot2;
	}
	public Color getColJ1() {
		return colJ1;
	}
	public void setColJ1(Color colJ1) {
		this.colJ1 = colJ1;
	}
	public Color getColJ2() {
		return colJ2;
	}
	public void setColJ2(Color colJ2) {
		this.colJ2 = colJ2;
	}
}
