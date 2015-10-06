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

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FenDialogConnexionInterrompue extends JDialog implements ActionListener
{
	private JFrame mainFen;
	private JLabel labelMessage;
	
  public FenDialogConnexionInterrompue(JFrame parent, String title, boolean modal)
  {
	  super(parent, title, modal);
	  this.mainFen = parent;
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
		// Panel de control
	    this.add(panFenReseau);
	    this.setVisible(true);
	    
  	}

  	
  	@Override
	public void actionPerformed(ActionEvent e) {  	
  		this.dispose();
	}
  	
  	private void affichageTexte(JPanel panFenReseau){
  		labelMessage = new JLabel();
		Font font = new Font("Buxton Sketch",Font.BOLD,20);
		labelMessage.setFont(font);
		labelMessage.setForeground(Color.white);
		affichageTexte();
		labelMessage.setLocation(this.getWidth() / 6 , 10);
		labelMessage.setSize(this.getWidth(), 40);	
		panFenReseau.add(labelMessage);
  	}
  	
  	private void affichageTexte(){
  		labelMessage.setText("La connexion a ete interrompue...");
  	}
}
