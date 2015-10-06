package chat;

import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JTextArea;


public class Reception implements Runnable {
	BufferedReader in;
	JTextArea discussion; // r�ference vers le textarea pour ecrire lesmessages re�us
	
	public Reception(BufferedReader br, JTextArea j){
		in = br;
		discussion = j;
	}
	
	public void run() {
		while(true){
			try{
				// on ajoute le texte re�u en provenance du serveur dans le texte area
				discussion.append(in.readLine()+"\n");
			}catch (IOException e){
				System.out.println(e);
				break;
			}
		}
	}

}
