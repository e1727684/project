package de.tu_darmstadt.gdi1.gorillas.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 */
public class Options {
	private boolean musicEnabled;
	private boolean windEnabled;
	private boolean spottEnabled;
	private boolean sfxEnabled;
	private float g = 10F;
	private static final String OPTION_FILE = "settings.ini";
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;

	/**
	 * Constructor initializes options
	 */
	public Options() {
		musicEnabled = true;
		windEnabled = true;
		spottEnabled = true;
		g = 10;
		load();
	}
	
	/**
	 * Save option-data
	 */
	public void save() {
		updateOptionsFile();
	}

	/**
	 * Load option-data
	 */
	public void load() {
		loadOptionFile();
	}
	
	/**
	 * Tests if music is enabled/disabled
	 * 
	 * @return
	 * 			true if enabled/false if disabled
	 */
	public boolean isMusicEnabled() {
		return musicEnabled;
	}

	/**
	 * Enable/Disable music in game
	 * 
	 * @param musicEnabled 
	 * 			enable/disable music
	 */
	public void setMusicEnabled(boolean musicEnabled) {
		if (!musicEnabled) 
			Gorillas.data.musicIsPlaying = false;
		this.musicEnabled = musicEnabled;
		save();
	}

	/**
	 * Tests if wind is enabled/disabled
	 * 
	 * @return
	 * 			true if enabled/false if disabled
	 */
	public boolean isWindEnabled() {
		return windEnabled;
	}

	/**
	 * Enable/Disable wind in game
	 * 
	 * @param windEnabled 
	 * 			enable/disable wind
	 */
	public void setWindEnabled(boolean windEnabled) {
		this.windEnabled = windEnabled;
		save();
	}
	
	/**
	 * Tests if mockery is enabled/disabled
	 * 
	 * @return
	 * 			true if enabled/false if disabled
	 */
	public boolean isSpottEnabled() {
		return spottEnabled;
	}

	/**
	 * Enable/Disable mockery in game
	 * 
	 * @param spottEnabled 
	 * 			enable/disable mockery
	 */
	public void setSpottEnabled(boolean spottEnabled) {
		this.spottEnabled = spottEnabled;
		save();
	}
	
	/**
	 * Load data from the <code>HIGHSCORE_FILE</code>
	 */
	public void loadOptionFile() {
		try {
			inputStream = new ObjectInputStream(new FileInputStream(
					OPTION_FILE));
			String[] temp = (String[]) inputStream.readObject();
			musicEnabled = temp[0].equals("true");
			windEnabled = temp[1].equals("true");
			spottEnabled = temp[2].equals("true");
			sfxEnabled = temp[3].equals("true");
			g = Float.parseFloat(temp[4]);
		} catch (FileNotFoundException e) {
			System.out.println("[Laad] FNF Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("[Laad] IO Error: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("[Laad] CNF Error: " + e.getMessage());
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Laad] IO Error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Updates data from the <code>HIGHSCORE_FILE</code>
	 */
	public void updateOptionsFile() {
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(
					OPTION_FILE));
			String[] temp = new String[10];
			temp[0] = musicEnabled?"true":"false";
			temp[1] = windEnabled?"true":"false";
			temp[2] = spottEnabled?"true":"false";
			temp[3] = sfxEnabled?"true":"false";
			temp[4] = ""+g;
			outputStream.writeObject(temp);
		} catch (FileNotFoundException e) {
			System.out.println("[Update] FNF Error: " + e.getMessage()
					+ ",the program will try and make a new file");
		} catch (IOException e) {
			System.out.println("[Update] IO Error: " + e.getMessage());
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Update] Error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Tests if SFX is enabled or not
	 * 
	 * @return
	 * 			true if enabled/false if disabled
	 */
	public boolean isSFXEnabled() {
		return sfxEnabled;
	}

	/**
	 * Enable/Disable SFX in game
	 * 
	 * @param sfxEnabled 
	 * 			enable/disable SFX
	 */
	public void setSFXEnabled(boolean sfxEnabled) {
		this.sfxEnabled = sfxEnabled;
	}

	/**
	 * Get value of gravity
	 * 
	 * @return
	 * 			value of gravity
	 */
	public float getG() {
		return g;
	}

	/**
	 * Set value of gravity
	 * 
	 * @param g 
	 * 			value of gravity
	 */
	public void setG(float g) {
		this.g = g;
	}
}
