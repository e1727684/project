package de.tu_darmstadt.gdi1.gorillas.util;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

public class MusicPlayer {
	public static void playBg() {
		if (!Gorillas.data.musicIsPlaying) {
		  try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/bg.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			Gorillas.data.musicIsPlaying = true;
    	  } catch(Exception ex) {
    		System.out.println("Error with playing sound.");
    		ex.printStackTrace();
    	  }
		}
	}
	
	public static void playButton() {
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
