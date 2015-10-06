/****************************************************************
*   Auteur : Gregory                            				*
*   Nom de la classe : EcouteurDeClavier        				*
*   Version : 1                           						*
*****************************************************************
*   Description : Ouvre les boites de dialogues des options en	*
* jeu ou celle de la difficulte des IA dans le cas de IA vs IA  *
*                                								*
****************************************************************/

package dialog;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import liaison.Liaison;
import liaison.ObserveurMoteur;
import maquettes.FenPengouin;
import maquettes.PanContexte;
import ressourcesIHM.BoutonImage;
 
@SuppressWarnings("serial")
public class FenDialogConfirmationRejouer extends JDialog implements ActionListener
{
	private JFrame mainFen;
	private Liaison liaison;
	private JLabel labelMessage, labelMessage2;
	private JButton boutonOui, boutonNon, boutonAnnuler;
	private PanContexte panCtxt;
	private FenPengouin fenPengouin;
	
	public FenDialogConfirmationRejouer(JFrame parent,FenPengouin fenPengouin, Liaison liaison, String title, boolean modal){
	  super(parent, title, modal);
	  this.mainFen = parent;
	  this.fenPengouin = fenPengouin;
	  this.liaison = liaison;
	  this.setResizable(false);
	  this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	  this.initComponent();
	}

  private void initComponent()
  {
		this.setSize(420, 150);
	    this.setLocationRelativeTo(mainFen);
	    this.setLocation(1024 / 2 - this.getWidth() / 2, 768 /2 - this.getHeight() / 2);
	    this.setLayout(null);
		//Panel Bot 1
		JPanel panFenConfQuitter = new JPanel();
		panFenConfQuitter.setBackground( new Color(60, 120, 192));
		panFenConfQuitter.setSize(this.getWidth(), this.getHeight());
		panFenConfQuitter.setLayout(null);
		affichageTexte(panFenConfQuitter);
		// Panel de control
	    affichageBouton(panFenConfQuitter);
	    getRootPane().setDefaultButton(boutonNon); 
		getRootPane().requestFocus();
	    this.add(panFenConfQuitter);
	    this.setVisible(true);
  	}

  	
  	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == boutonOui)	//on quitte en sauvegardant
		{
			new FenDialogSave(this.mainFen, liaison, "Sauvegarder", true);
			// on relance une partie
			rejouer();
			this.dispose();
		}
		else if(e.getSource() == boutonNon){// on quitte sans sauvegarder
			rejouer();
			this.dispose();
  		}
  		else if(e.getSource() == boutonAnnuler){//on retourne au jeu
  			this.dispose();
  		}
	}
  	
  	private void affichageTexte(JPanel panFenConfQuitter){
  		labelMessage = new JLabel();
  		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(16);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		labelMessage.setFont(font);
		labelMessage.setForeground(Color.white);
		labelMessage.setText("Vous êtes sur le point de perdre votre partie en cours,");
		labelMessage.setLocation(20,5);
		labelMessage.setSize(this.getWidth(), 30);	
		panFenConfQuitter.add(labelMessage);
		labelMessage2 = new JLabel();
		labelMessage2.setFont(font);
		labelMessage2.setForeground(Color.white);
		labelMessage2.setText("souhaitez-vous sauvegarder avant de rejouer?");
		labelMessage2.setLocation(40,40);
		labelMessage2.setSize(this.getWidth(), 30);
		panFenConfQuitter.add(labelMessage2);
  	}
  	
  	private void affichageBouton(JPanel panFenConfQuitter){
  		boutonOui = new BoutonImage("Oui");
 	    boutonOui.setLocation(5,70);
 	    boutonOui.setSize(100, 40);
 	    boutonNon = new BoutonImage("Non");
 	    boutonNon.setLocation(110,70);
 	    boutonNon.setSize(100,40);
 	    boutonNon.setMnemonic(KeyEvent.VK_ENTER);
 	    boutonAnnuler = new BoutonImage("Annuler");
 	    boutonAnnuler.setLocation(215,70);
 	    boutonAnnuler.setSize(200,40);
 	    boutonOui.addActionListener(this);
 	    boutonNon.addActionListener(this);
 	    boutonAnnuler.addActionListener(this);
 	    panFenConfQuitter.add(boutonOui);
 	    panFenConfQuitter.add(boutonNon);
 	    panFenConfQuitter.add(boutonAnnuler);
 	    
  	}
  	private void rejouer(){
  		
	  	// Initialisation de liaison
		Liaison liaisonTempo = new Liaison(false,liaison.getDonneesAff().isPartiePerso());
		liaison.quitter();
		// Changement de panel vers PanContexte
		mainFen.getContentPane().removeAll();
		this.panCtxt = new PanContexte(fenPengouin, liaisonTempo);
		mainFen.add(this.panCtxt);//a modifier
		mainFen.setVisible(true);
		// Declaration de l'observer
		ObserveurMoteur ca = new ObserveurMoteur(fenPengouin, panCtxt, liaisonTempo);
		liaisonTempo.nouvellePartie(ca);
  	}
}
