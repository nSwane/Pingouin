package ressourcesIHM;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import liaison.Coordonnees;
import liaison.Liaison;
import moteur.Pingouin;
import moteur.Terrain;

public class AnimationDeplacement implements ActionListener {
	Pingouin pingouin;
	Point position;
	Terrain terrain;
	JPanel pan;
	Liaison liaison;
	Coordonnees dep;
	double xArrive, yArrive;
	int deplacement;
	
	public AnimationDeplacement(Coordonnees c, Pingouin ping, Point p, Terrain t, JPanel pan, Liaison l, int d){
		position = p;
		pingouin = ping;
		terrain = t;
		this.pan = pan;
		liaison = l;
		dep = c;
		xArrive = pingouin.getCoordonnees().getPoint().getX();
		yArrive = pingouin.getCoordonnees().getPoint().getY();
		deplacement = d; // 2 à 12
//		deplacement = 5;
	}

	public void actionPerformed(ActionEvent arg0) {
		if(xArrive == position.getX() && yArrive == position.getY()){
			// on replace le pingouin 
			terrain.consulter(pingouin.getCoordonnees().getI(), pingouin.getCoordonnees().getJ()).setPingouin(pingouin);
			
			// on signale l'arrêt de l'animation
			liaison.setMoteurActif(false);
			liaison.getDonneesAff().setAnimationPingouinPlace(true);
			//liaison.notifierMiseAJour();
			//liaison.getDonneesAff().startAnimation(false);
			
			// on arrête le timer
			Timer t = (Timer) arg0.getSource();
			t.stop();
		}
		double deplacementX = Math.abs(xArrive - position.x); 
		double deplacementY = Math.abs(yArrive - position.y);
		if(deplacementY == 0){
			deplacementY = 0;
			deplacementX = deplacement*2;
		}else if(deplacementX == 0){
			deplacementX = 0;
			deplacementY = deplacement;
		}else{
			if(deplacementX > deplacementY){
				deplacementX = (deplacement*deplacementX)/deplacementY;
				deplacementY = deplacement;
			}else{
				deplacementY = (deplacement*deplacementY)/deplacementX;
				deplacementX = deplacement;
			}
		}
		
		if(xArrive - position.getX() > 0){
			position.x += deplacementX;
			if(position.getX() > xArrive){
				position.x = (int) Math.round(xArrive);
			}
		}else if(xArrive - position.getX() < 0){
			position.x -= deplacementX;
			if(position.getX() < xArrive){
				position.x = (int) Math.round(xArrive);
			}
		}
		
		if(yArrive - position.getY() > 0){
			position.y += deplacementY;
			if(position.getY() > yArrive){
				position.y = (int) Math.round(yArrive);
			}
		}else if(yArrive - position.getY() < 0){
			position.y -= deplacementY;
			if(position.getY() < yArrive){
				position.y = (int) Math.round(yArrive);
			}
		}

		pan.repaint();
	}
}
