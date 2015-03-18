package de.tu_darmstadt.gdi1.gorillas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameData {
	private String player1;
	private String player2;
	private boolean isPaused; // de-/aktiviert ESC-Taste im MainMenu
	private String playerWon;
	public boolean guiDisabled;
	private String[][] highscore;
	private int highscoreLength = 1000;
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;
	private static final String HIGHSCORE_FILE = "highscores.hsc";
	private int remainingRounds;
	private int playTillScore;
	private int[] currentScore;
	public boolean musicIsPlaying;
	public boolean sunAstonished;

	public GameData() {// ONE call in Gorillas, then getter&setters!
		init();
	}

	private void init() {
		player1 = "";
		player2 = "";
		isPaused = false;
		playerWon = "";
		guiDisabled = true;
		highscore = new String[highscoreLength][4];
		setRemainingRounds(1);
		setPlayTillScore(0);
		currentScore = new int[2];
		musicIsPlaying = false;
		sunAstonished = false;
		load();
		// more?
	}

	public void save() {
		deleteScoreFile();
		updateScoreFile();
	}

	public void load() {
		loadScoreFile();
		deleteScoreFile();
	}
	
	public void resetScore() {
		deleteScoreFile();
		highscore = new String[highscoreLength][4];
	}

	public void deleteScoreFile() {
		try {
    		File file = new File(HIGHSCORE_FILE);
    		file.delete();
		} catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	public void loadScoreFile() {
		try {
			inputStream = new ObjectInputStream(new FileInputStream(
					HIGHSCORE_FILE));
			int i = 0;
			String[][] temp = (String[][]) inputStream.readObject();
			while (temp[i][0] != null
					&& addHighscore(temp[i][0], Integer.parseInt(temp[i][1]),
							Integer.parseInt(temp[i][2]),
							Integer.parseInt(temp[i][3]))) {
				i++;
			}
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

	public void updateScoreFile() {
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(
					HIGHSCORE_FILE));
			outputStream.writeObject(highscore);
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

	public boolean addHighscore(String name, int numberOfRounds, int roundsWon,
			int bananasThrown) {
		if (highscore == null)
			load();
		int i = 0;
		boolean nameExist = false;
		while (i < getHighscoreCount()) {
			if (name.matches(highscore[i][0].toString())) {
				nameExist = true;
				break;
			}
			i++;
		}
		if (getHighscoreCount() == 0) {
			highscore[0][0] = name;
			highscore[0][1] = Integer.toString(numberOfRounds);
			highscore[0][2] = Integer.toString(roundsWon);
			highscore[0][3] = Integer.toString(bananasThrown);
			return true;
		} else if (!nameExist) {
			i = 0;
			while (i < getHighscoreCount() // falls schlechter als alle andern, hänge hinten an
					&& (((float)roundsWon/numberOfRounds) < ((float)getRoundsWonAtHighscorePosition(i)/getRoundsPlayedAtHighscorePosition(i)))) 
				{i++;}
			while (i < getHighscoreCount()
					&& getMeanAccuracyAtHighscorePosition(i) < Math.round((double) bananasThrown / roundsWon))
				{i++;}
			for (int j = getHighscoreCount(); j >= i; j--) { // have to move all from end to index i
				highscore[j + 1][0] = highscore[j][0];
				highscore[j + 1][1] = highscore[j][1];
				highscore[j + 1][2] = highscore[j][2];
				highscore[j + 1][3] = highscore[j][3];
			}
			highscore[i][0] = name;
			highscore[i][1] = Integer.toString(numberOfRounds);
			highscore[i][2] = Integer.toString(roundsWon);
			highscore[i][3] = Integer.toString(bananasThrown);
			return true;
		} else {
			highscore[i][0] = name;
			highscore[i][1] = Integer.toString(Integer.parseInt(highscore[i][1]) + numberOfRounds);
			highscore[i][2] = Integer.toString(Integer.parseInt(highscore[i][2]) + roundsWon);
			highscore[i][3] = Integer.toString(Integer.parseInt(highscore[i][3]) + bananasThrown);
			return sortHighscore();
		}
	}

	private boolean sortHighscore() {
		String[][] temp = highscore;
		highscore = new String[highscoreLength][4];
		int i = 0;
		while (temp[i][0] != null
				&& addHighscore(temp[i][0], Integer.parseInt(temp[i][1]),
						Integer.parseInt(temp[i][2]),
						Integer.parseInt(temp[i][3]))) {
			i++;
		}
		return false;
	}

	public int getHighscoreCount() {
		int i = 0;
		while (highscore[i][0] != null && i < highscoreLength) {
			i++;
		}
		return i;
	}

	public void printHighscoreInConsole() {
		for (int i = 0; i < getHighscoreCount(); i++) {
			System.out.println(i + ". Player: " + highscore[i][0]
					+ " Rounds played: " + highscore[i][1] + ". Rounds won: "
					+ highscore[i][2] + ". Bananas thrown: " + highscore[i][3]
					+ ". ");
		}
	}

	public String giveHighscoreAsString(int c) {
		String hsc = "";
		for (int i = 0; i < getHighscoreCount() && i < 10; i++) { // i < 10 da nur 10 anzuzeigen sind.....
			switch (c) {
				case 0:
					hsc += (i==9?"":"0") + (i + 1) + " \n";
					break;
				case 1:
					hsc += highscore[i][0] + " \n";
					break;
				case 2:
					hsc += highscore[i][1] + " \n";
					break;
				case 3:
					hsc += highscore[i][2] + " \n";
					break;
				case 4:
					hsc += " ("+getPercentageWonAtHighscorePosition(i)+"%)" + " \n";
					break;
				case 5:
					hsc += getMeanAccuracyAtHighscorePosition(i) + " bananas" + " \n";
					break;
					
			}
		}
		return hsc;
	}

	public String getNameAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return highscore[position][0];
		else
			return null;
	}

	public int getRoundsPlayedAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return Integer.parseInt(highscore[position][1]);
		else
			return -1;
	}

	public int getRoundsWonAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return Integer.parseInt(highscore[position][2]);
		else
			return -1;
	}

	public int getBananasAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return Integer.parseInt(highscore[position][3]);
		else
			return -1;
	}

	public int getPercentageWonAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return (int) Math.round(getRoundsWonAtHighscorePosition(position)
					* 100
					/ (double) getRoundsPlayedAtHighscorePosition(position));
		else
			return -1;
	}

	public double getMeanAccuracyAtHighscorePosition(int position) {
		if (getRoundsWonAtHighscorePosition(position) == 0)
			return 0;
		if (position >= 0 && highscore[position][0] != null)
			return (int) Math
					.round((double) getBananasAtHighscorePosition(position)
							/ getRoundsWonAtHighscorePosition(position));
		else
			return -1;
	}

	public String getPlayer1() {// get playername :: player 1
		return player1;
	}

	public void setPlayer1(String player1) {// set playername :: player 1
		this.player1 = player1;
	}

	public String getPlayer2() {// get playername :: player 2
		return player2;
	}

	public void setPlayer2(String player2) {// set playername :: player 2
		this.player2 = player2;
	}

	public boolean getPaused() {
		return isPaused;
	}

	public void setPaused(boolean paused) {
		isPaused = paused;
	}

	public String getPlayerWon() {
		return playerWon;
	}

	public void setPlayerWon(String playerWon) {
		this.playerWon = playerWon;
	}

	public int getRemainingRounds() {
		return remainingRounds;
	}

	public void setRemainingRounds(int remainingRounds) {
		this.remainingRounds = remainingRounds;
	}

	public int[] getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int[] currentScore) {
		this.currentScore = currentScore;
	}

	public int getPlayTillScore() {
		return playTillScore;
	}

	public void setPlayTillScore(int playTillScore) {
		this.playTillScore = playTillScore;
	}
}