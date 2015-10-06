package ressourcesIHM;

import java.awt.Component;
import java.awt.Cursor;


public class SourisWait {

	
	//constructeur
	public SourisWait(Component parent, boolean isWaiting){
		
		if(isWaiting){
			parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}else{
			parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
