package Son;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum BanqueDeSon {
	CRI("cri.wav", 0), // cri de chevre-pingouin
	CLIC("clic.wav", 0), // son bouton
	ANNULERCASE("cancel.wav", 0), // deselection pingouin
	DEPLACER("move.wav", 0), // deplacement pingouin
	SELECT("select.wav", 0), // selection de pingouin
	PLACER("placer.wav", 0), // placement du pingouin
//	MER("mer.wav", 0), // son de la mer
	MUSIQUE("musique.wav", -10); // musique de fond
	
	private final String nomFichier;
	private Clip clip;
	
	BanqueDeSon(String nom, int level)
	{
		nomFichier = nom;
		try {
			// Use URL to read from disk 
	        URL url = this.getClass().getClassLoader().getResource("Data/Sons/"+nomFichier);
	        // Set up an audio input stream piped from the sound file.
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
	        // Get a clip resource.
	        clip = AudioSystem.getClip();
	        // Open audio clip and load samples from the audio input stream.
	        clip.open(audioInputStream);
	        
	        // the audio level of the song is decreased
	        // level values: -15 -> 5
	        if(level != 0){
	        	FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		        volume.setValue(level);
	        }
		} catch (UnsupportedAudioFileException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (LineUnavailableException e) {
	        e.printStackTrace();
	    }
	}
	
	// return the name of the song file 
	public String getNom(){
		return nomFichier;
	}
	
	
	// play the song 
	// loop if b = true
	public void jouerSon(boolean b){
//		if(nomFichier.equals("musique.wav"))
//		{
			if (clip.isRunning()){
	            clip.stop();   // Stop the player if it is still running
			}
			clip.setFramePosition(0); // rewind to the beginning
			if(b){
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			clip.start();     // Start playing
//		}
	}
	
	// stop the song if it's running
	public void Stop(){
		if (clip.isRunning()){
            clip.stop();   // Stop the player if it is still running
		}
	}

	
}
