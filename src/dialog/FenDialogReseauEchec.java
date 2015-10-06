/****************************************************************
*   Auteur : Michael                           					*
*   Nom de la classe :	FenDialogReseauRetry      				*
*   Version : 1                           						*
*****************************************************************
*   Description : ouvre la boite de connexion depassé  			*
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

import ressourcesIHM.BoutonImage;

import liaison.Liaison;

@SuppressWarnings("serial")
public class FenDialogReseauEchec extends JDialog implements ActionListener
{
	private JFrame mainFen;
	private JLabel labelMessage;
	private Liaison liaison;
	private JButton boutonAnnuler;
	
  public FenDialogReseauEchec(JFrame parent, String title, boolean modal)
  {
	  super(parent, title, modal);
	  this.mainFen = parent;
	  this.liaison = new Liaison(true,false);
	  this.setResizable(false);
	  this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
		affichageBouton(panFenReseau);
		// Panel de control
	    this.add(panFenReseau);
	    this.setVisible(true);
  	}

  	
  @Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == boutonAnnuler){
					this.dispose();
		}
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
		affichageTexte();
		labelMessage.setLocation(this.getWidth() / 6 , 10);
		labelMessage.setSize(this.getWidth(), 40);	
		panFenReseau.add(labelMessage);
  	}
  	
  	private void affichageTexte(){
  		labelMessage.setText("La connexion a echouee...");
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
}
