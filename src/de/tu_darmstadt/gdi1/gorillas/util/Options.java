package de.tu_darmstadt.gdi1.gorillas.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

public class Options {
	private boolean musicEnabled;
	private boolean windEnabled;
	private boolean spottEnabled;
	private boolean sfxEnabled;
	private float g = 10F;
	private static final String OPTION_FILE = "settings.ini";
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;

	public Options() {
		musicEnabled = true;
		windEnabled = true;
		spottEnabled = true;
		g = 10;
		load();
	}
	
	public void save() {
		updateOptionsFile();
	}

	public void load() {
		loadOptionFile();
	}

	public boolean isMusicEnabled() {
		return musicEnabled;
	}

	public void setMusicEnabled(boolean musicEnabled) {
		if (!musicEnabled) 
			Gorillas.data.musicIsPlaying = false;
		this.musicEnabled = musicEnabled;
		save();
	}

	public boolean isWindEnabled() {
		return windEnabled;
	}

	public void setWindEnabled(boolean windEnabled) {
		this.windEnabled = windEnabled;
		save();
	}

	public boolean isSpottEnabled() {
		return spottEnabled;
	}

	public void setSpottEnabled(boolean spottEnabled) {
		this.spottEnabled = spottEnabled;
		save();
	}
	
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

	public boolean isSFXEnabled() {
		return sfxEnabled;
	}

	public void setSFXEnabled(boolean sfxEnabled) {
		this.sfxEnabled = sfxEnabled;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}
}
