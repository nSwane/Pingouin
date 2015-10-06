/****************************************************************
*   Auteur : Michael                            				*
*   Nom de la classe : FenDialogReseau	        				*
*   Version : 1                           						*
*****************************************************************
*   Description : ouvre la boite de dialog lors de l'attente reseau  *
*                                								*
****************************************************************/

package dialog;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import liaison.Liaison;
import maquettes.FenPengouin;

import ressourcesIHM.BoutonImage;
 
@SuppressWarnings("serial")
public class FenDialogReseau extends JDialog implements ActionListener
{
	private FenPengouin fenPengouin;
	private JFrame mainFen;
	private JLabel labelMessage;
	private JButton boutonAnnuler;
	Liaison liaison;
	Timer timer;
 
  public FenDialogReseau(FenPengouin fenPengouin, String title, Liaison liaison, boolean modal)
  {
	  super(fenPengouin.getFenetrePrincipale(), title, modal);
	  this.mainFen = fenPengouin.getFenetrePrincipale();
	  this.setResizable(false);
	  this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	  
	  
	  this.liaison = liaison;
	  timer = new Timer(1000, this);
	  Thread t = new Thread(new Connexion(fenPengouin, timer, liaison));
	  t.start();
	   
	  this.initComponent();
	  
		
  }

  private void initComponent()
  {
		this.setSize(420, 120);
	    this.setLocationRelativeTo(mainFen);
	    this.setLocation(1024 / 2 - this.getWidth() / 2, 768 /2 - this.getHeight() / 2);
	    this.setLayout(null);
		//Panel Bot 1
		JPanel panFenReseau = new JPanel();
		panFenReseau.setBackground( new Color(60, 120, 192));
		panFenReseau.setSize(this.getWidth(), this.getHeight());
		panFenReseau.setLayout(null);
		affichageTexte(panFenReseau);
		// Panel de control
	    affichageBouton(panFenReseau);
	    this.add(panFenReseau);
	    this.setVisible(true);
	 
  	}
  
  	@Override
	public void actionPerformed(ActionEvent e) {
  		
  		if(e.getSource() == boutonAnnuler){
  			liaison.setAnnuler(true);
  			
	  		try {
	  			Socket client = liaison.getClient();
				if(client != null && !client.isClosed()){
					client.close();
				}
			} catch (IOException e1) {
				System.out.println("Fenetre dialogue reseau : "+e1);
			}
  		}
  		
		this.dispose();
	}
  	
  	private void affichageTexte(JPanel panFenReseau){
  		labelMessage = new JLabel();
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(18);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		labelMessage.setFont(font);
		labelMessage.setForeground(Color.white);
		labelMessage.setLocation(this.getWidth() / 8 , 10);
		labelMessage.setSize(this.getWidth(), 40);	
		affichageTexte();
		panFenReseau.add(labelMessage);
  	}
  	/**
  	 * affichage du bouton annuler pour retourner a la fenetre precedente et annuler la connexion
  	 * @param panFenReseau
  	 */
  	private void affichageBouton(JPanel panFenReseau){
  		boutonAnnuler = new BoutonImage("Annuler");
 	    boutonAnnuler.setLocation(this.getWidth() / 2 - 100, this.getHeight()-70);
 	    boutonAnnuler.setSize(200,40);
 	    boutonAnnuler.addActionListener(this);
 	    panFenReseau.add(boutonAnnuler);
 	    
  	}
  	/**
  	 * affichage du texte d'attente
  	 */
  	private void affichageTexte(){
  		labelMessage.setText("connexion en cours...");	
  		this.repaint();
	}

	public FenPengouin getFenPengouin() {
		return fenPengouin;
	}

	public void setFenPengouin(FenPengouin fenPengouin) {
		this.fenPengouin = fenPengouin;
	}

}
