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
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import liaison.Liaison;
import ressourcesIHM.BoutonImage;
 
@SuppressWarnings("serial")
public class FenDialogSave extends JDialog implements ActionListener, KeyListener
{
	private JFrame mainFen;
	private Liaison liaison;
	private JLabel nomLabel;
	private JButton okBouton , cancelBouton;
	private JTextField nomSave;
	private String dateString;
 
  public FenDialogSave(JFrame parent, Liaison liaison, String title, boolean modal){
	  super(parent, title, modal);
	  this.mainFen = parent;
	  this.liaison = liaison;
	  this.setResizable(false);
	  this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	  this.initComponent();
  }

  private void initComponent()
  {
		this.setSize(420, 150);
	    this.setLocationRelativeTo(mainFen);
	    this.setLayout(null);
		//Panel Bot 1
		JPanel panSave = new JPanel();
		panSave.setBackground( new Color(60, 120, 192));
		panSave.setSize(this.getWidth(), this.getHeight());
		panSave.setLayout(null);
		nomLabel = new JLabel();
		Font font = null;
		try {
			font = liaison.getDonneesAff().getFont(18);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		nomLabel.setFont(font);
		nomLabel.setText("Sauvegarder la partie");
		nomLabel.setLocation(150,5);
		nomLabel.setSize(this.getWidth() / 2, 10);
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm");
		dateString = dateFormat.format(date);
		nomSave = new JTextField("Sauvegarde-" + dateString);
		nomSave.addKeyListener(this);
		nomSave.setLocation(100,30);
		nomSave.setSize(this.getWidth()/2, 30);
		
		panSave.add(nomLabel);
		panSave.add(nomSave);

		// Panel de control
	    okBouton = new BoutonImage("Valider");
	    okBouton.setMnemonic(KeyEvent.VK_ENTER); //ajout d'un Mnemonic de clavier pour que lorsque l'on tape entree on effectue valider
	    okBouton.setLocation(5,70);
	    okBouton.setSize(200, 40);
	    cancelBouton = new BoutonImage("Annuler");
	    cancelBouton.setLocation(210,70);
	    cancelBouton.setSize(200,40);
	    okBouton.addActionListener(this);
	    cancelBouton.addActionListener(this);
	    panSave.add(okBouton);
	    panSave.add(cancelBouton);
	    getRootPane().setDefaultButton(okBouton); 
		getRootPane().requestFocus(); 
	    
	    this.add(panSave);
	    this.setVisible(true);
  	}

  	
  	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okBouton)
		{
			// le nom dela sauvegarde est : le nom 
			liaison.sauvegarder(nomSave.getText().toString().equals("")?("Sauvegarde-" + dateString):nomSave.getText().toString());
			if (liaison.getDonneesAff().isFichierExiste())
				// Cree une nouvelle fenetre d'erreur
				System.out.println("Une sauvegarde avec ce nom la existe deja");
			else
			{
				this.dispose();
			}
		}
		else if(e.getSource() == cancelBouton)
			this.dispose();
	}
  	
  	public String difficulteItoS(int lvl)
  	{
  		switch (lvl) {
		case 0:
			return "Facile";
		case 1:
			return "Moyenne";
		case 2:
			return "Difficile";
		default:
			return "Moyenne";
		}
  	}
  	
  	public static int difficulteStoI(String lvl)
  	{
  		if(lvl.equals("Facile"))
  			return 0;
  		else if(lvl.equals("Moyenne"))
  			return 1;
  		else if(lvl.equals("Difficile"))
  			return 2;
  		else
  			return 1;
  	}
  
	public static Color nextColor(Color couleur)
	{
		if (couleur == Color.BLUE)
		{
			return Color.RED;
		}
		else if (couleur == Color.RED)
		{
			return Color.GREEN;
		}
		else if (couleur == Color.GREEN)
		{
			return Color.YELLOW;
		}
		else if (couleur == Color.YELLOW)
		{
			return Color.ORANGE;
		}
		else if (couleur == Color.ORANGE)
		{
			return Color.CYAN;
		}
		else if (couleur == Color.CYAN)
		{
			return Color.PINK;
		}
		else
		{
			return Color.BLUE;
		}
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent k) {
		switch (k.getKeyChar()) {
		case ' ':
		case '/':
		case '\\':
		case '*':
		case ':':
		case ';':
		case '\'':
		case '\"':
		case '<':
		case '>':
		case '?':
		case '|':
			k.consume();
			break;
		default:
			break;
		}
			
	}
}
