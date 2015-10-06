/**
 * Auteur : Jeremy
 * Date : 20/05/13
 * Version : 1.0
 */

package moteur;

import java.util.Observable;
import java.util.Observer;


public class ComportementAffichage implements Observer 
{
	public ComportementAffichage()
	{
	}
	
	// appelee lors de l'appel a notifyObservers
	public void update(Observable arg0, Object arg1) 
	{
//			System.out.println("Mise à jour !");
	}

}
