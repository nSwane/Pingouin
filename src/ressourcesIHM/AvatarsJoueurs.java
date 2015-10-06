/********************************************************************
* Nom du codeur : Michael OMER		*Date de création : 30/05/2013	*
* 									*Date de modification :			*
* Nom de la classe :AvatarsJoueurs									*
* *******************************************************************
* Description : 													*
* Classe contenant les choix d'avatar possible pour les joueurs		*
*********************************************************************/
package ressourcesIHM;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import liaison.Liaison;

public class AvatarsJoueurs{

	// enumeration des choix possibles en avatar du joueur
		public enum Avatar {		// variables liees :
			Anime_Albator,
			Anime_BlancheNeige,
			Anime_Buzz,
			Anime_Chevalier,
			Anime_Dbz_Vegeta,
			Anime_Samourai,
			Anime_Savant,
			DC_Batman,
			DC_Superman,				
			Film_DarkVador,				
			Film_Dred,				
			Film_Indiana,			
			Film_PereNoel,
			Film_Rambo,
			Game_Lara,
			Game_Mario,
			Game_Pikachu,
			Game_StreetFighter,
			Histoire_Gaulois,
			Histoire_Mort,
			Histoire_Templier,
			Histoire_Zombie,
			Marvel_Captain,
			Marvel_IronMan,
			Sport_Foot,
			Sport_FootUS,
			Sport_Hockey;
		}//fin enum choix
		
		//variable
		private BufferedImage imageAvatarJoueur;
		private Avatar avatarJoueur;
		private int numAvatar, numJoueur;
		private Liaison liaison;
		
		public AvatarsJoueurs(int numJoueur, int numAvatar, Liaison liaison){
			this.numAvatar = numAvatar;
			this.numJoueur = numJoueur;
			this.liaison = liaison;
			avatarJoueur = Avatar.values()[numAvatar];
			this.initImage();
		}//fin constructeurs
		
//		public AvatarsJoueurs(int i){
//			numAvatar = i;
//			numeroJoueur = i;
//			avatarJoueur = Avatar.values()[i];
//			this.initImage(avatarJoueur);
//		}//fin constructeurs
		
		private void initImage(){
			try {
				imageAvatarJoueur = ImageIO.read(getClass().getResource("/Data/Images/Pions/"+avatarJoueur.toString()+".png"));
			} catch (IOException e) {
				System.out.println("erreur d'ouverture de l'avatar");
			}
			
		}
		
		public Avatar next(){
			
			boolean avatarDejaPris;
			int numAvatarOther;
			do{
				avatarDejaPris = false;
				// On incremente le numero d'avatar du joueur concerne
				numAvatar++;
				// si on arrive a la fin, on retourne a 0
				if(numAvatar == Avatar.values().length-1)
				{
					numAvatar = 0;
				}
				// On affecte le nouvel avatar
				avatarJoueur = Avatar.values()[numAvatar];
				
				// On regarde si l'un des autres joueurs a deja cet avatar
				for(int i = 0; i < liaison.getDonneesAff().getNbJoueurs(); i++)
				{
					if(numJoueur != i)
					{
						numAvatarOther = liaison.getAvatars()[i].getAvatarJoueur().ordinal();
						avatarDejaPris = (avatarDejaPris || numAvatarOther == avatarJoueur.ordinal());
					}
				}
			}while(avatarDejaPris);
			this.initImage();
			return avatarJoueur ;
		}
		
		public Image getImageAvatarJoueur(){
			return this.imageAvatarJoueur;			
		}
		public Avatar getAvatarJoueur(){
			return this.avatarJoueur;
		}

		public void setAvatarJoueur(Avatar avatarJoueur) {
			this.avatarJoueur = avatarJoueur;
		}
		public int getNumAvatar(){
			return this.numAvatar;
		}

		public int getNumeroJoueur() {
			return numJoueur;
		}

		public void setNumeroJoueur(int numeroJoueur) {
			this.numJoueur = numeroJoueur;
		}

		/**
		 * Change le numero d'avatar et tout ce qui en decoule (la reference a avatar et l'image)
		 * @param numAvatar
		 */
		public void setNumAvatar(int numAvatar) {
			this.numAvatar = numAvatar;
			avatarJoueur = Avatar.values()[numAvatar];
			this.initImage();
		}
}//fin classe
