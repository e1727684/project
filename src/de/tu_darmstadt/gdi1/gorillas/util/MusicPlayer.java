package de.tu_darmstadt.gdi1.gorillas.util;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

/**
 * AboutState
 * 
 * @author Dennis Hasenstab
 * @version 1.0
 */
public class MusicPlayer {
	public static Clip clip;
	public static Music bg;
	
	@Deprecated
	public static void playBg() {
		if (!Gorillas.data.musicIsPlaying && Gorillas.options.isMusicEnabled()) {
		  try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/bg.wav").getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			Gorillas.data.musicIsPlaying = true;
    	  } catch(Exception ex) {
    		System.out.println("Error with playing sound.");
    		ex.printStackTrace();
    	  }
		}
	}
	
	public static void playBG() {
			try {
			    bg = new Music("sounds/bg.xm");
			    bg.loop();
				Gorillas.data.musicIsPlaying = true;
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@Deprecated
	public static void stopBg() {
		clip.stop();
	}
	
	public static void stopBG() {
		bg.stop();
	}
	
	public static void playButton() {
    	if (!Gorillas.data.guiDisabled && Gorillas.options.isSFXEnabled()) { // really.... 
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/button.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
    	} catch(Exception ex) {
    		System.out.println("Error with playing sound.");
    		ex.printStackTrace();
    	}
    	}
	}
	
	public static void playApplause() {
    	if (!Gorillas.data.guiDisabled && Gorillas.options.isSFXEnabled()) { // really.... 
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/applause.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
    	} catch(Exception ex) {
    		System.out.println("Error with playing sound.");
    		ex.printStackTrace();
    	}
    	}
	}
}
