/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 20/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :PanelOutils										*
* *******************************************************************
* Description : 													*
* Panneau contenant les boutons d'outils pour le joueur				*
*********************************************************************/
package maquettes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import liaison.Liaison;
import moteur.Profile;
import chat.ConnexionChat;
import chat.Reception;

@SuppressWarnings("serial")
public class PanelChat extends JPanel implements ActionListener, KeyListener {
	
	//variable
	private FenPengouin fenPengouin;
	private Liaison liaison;
	private JFrame mainFen;
	private PanContexte panCtxt;
	
	
	JTextArea texte;
	JTextField champMessage;
	boolean estConnecte;
	String nomJoueur;
	PrintWriter out;
	
	//constructeur
	public PanelChat(PanContexte panCtxt, JFrame mainFen,FenPengouin fenPengouin, Liaison liaison){
		// Recuperation des parametres
		this.panCtxt = panCtxt;
		this.fenPengouin = fenPengouin;
		this.liaison = liaison;
		this.mainFen = mainFen;
		
		// Options de la fenetre
		this.setOpaque(false); // on permet de voir le fond du panneau contexte
		
		// Ajout de tous les elements du panelOutils
		this.initComponent();
		
		// connexion au serveur de chat
		this.connexion();
		
	}//fin constructeur
	
	/**
	 *  Ajout de tous les elements du panelChat
	 */
	public void initComponent(){
		texte = new JTextArea(15, 18);
		texte.setEditable(false);
		texte.setLineWrap(true);
		texte.setWrapStyleWord(true);
		texte.setBackground(new Color(153, 185, 224));
		//on ajoute la scrollbar
		JScrollPane panelTexte = new JScrollPane(texte, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		JPanel panUtilisateur = new JPanel();
		panUtilisateur.setOpaque(false);
		
		JButton envoyer = new JButton("Ok");
		envoyer.addActionListener(this);
		
		champMessage = new JTextField();
		champMessage.setBackground(new Color(153, 185, 224));
		champMessage.setPreferredSize(new Dimension(148, 25));
		champMessage.addKeyListener(this);
		
		panUtilisateur.add(champMessage, BorderLayout.CENTER);
		panUtilisateur.add(envoyer, BorderLayout.EAST);
		
		this.add(panelTexte, BorderLayout.CENTER);
		this.add(panUtilisateur, BorderLayout.SOUTH);
	}
	
	public void connexion(){
		ConnexionChat connexionChat = new ConnexionChat(1234, new Profile().getNom());
		connexionChat.Connexion();
		
		// on récupère la sortie du socket d'echange
		out = new PrintWriter(connexionChat.getOutput());
		
		// on lance le thread qui gerent la reception des messages
		Thread threadReception = new Thread(new Reception(connexionChat.getInput(), texte));
		threadReception.start();
	}
	
	public void traiterAction(){
		// on récupère le message écrit par l'utilisateur
		String ligne = champMessage.getText();
		
		// on efface le textarea
		champMessage.setText(null);
		
		// on envoie le message au serveur
		out.println(ligne);
		out.flush();
	}
	
	public void actionPerformed(ActionEvent e){
		traiterAction();	
	}

	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyChar() == '\n'){
			traiterAction();
		}
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

}//fin classe
