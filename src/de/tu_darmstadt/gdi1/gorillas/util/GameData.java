package de.tu_darmstadt.gdi1.gorillas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 */
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
	
	/**
	 * Constructor. Creates a new instance of GameData.
	 */
	public GameData() {// ONE call in Gorillas, then getter&setters!
		init();
	}
	
	/**
	 * Initialize Gamedata with the necessary values.
	 */
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

	/**
	 * Save data from class in the <code>HIGHSCORE_FILE</code>.
	 */
	public void save() {
		deleteScoreFile();
		updateScoreFile();
	}

	/**
	 * Load data from the <code>HIGHSCORE_FILE</code> into class.
	 */
	public void load() {
		loadScoreFile();
		deleteScoreFile();
	}
	
	/**
	 * Reset the highscore and delete the <code>HIGHSCORE_FILE</code>.
	 */
	public void resetScore() {
		deleteScoreFile();
		highscore = new String[highscoreLength][4];
	}
	
	/**
	 * Delete  the <code>HIGHSCORE_FILE</code>.
	 */
	public void deleteScoreFile() {
		try {
    		File file = new File(HIGHSCORE_FILE);
    		file.delete();
		} catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Load data from the <code>HIGHSCORE_FILE</code>.
	 */
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
	
	/**
	 * Update data in the <code>HIGHSCORE_FILE</code>.
	 */
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

	/**
	 * Adds new highscore-entry.
	 * 
	 * @param name 
	 * 				name of player
	 * @param numberOfRounds 
	 * 				amount of rounds
	 * @param roundsWon 
	 * 				amount of wins
	 * @param bananasThrown 
	 * 				thrown bananas
	 * @return
	 * 			Returns true if no sort was made - False if highscore had to be re-sorted
	 */
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
					&& getMeanAccuracyAtHighscorePosition(i) < Math.round((double) bananasThrown / roundsWon)
					&& (((float)roundsWon/numberOfRounds) == ((float)getRoundsWonAtHighscorePosition(i)/getRoundsPlayedAtHighscorePosition(i))))
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

	/**
	 * Sorting highscore with a simple sorting algorithm.
	 * Basically empties our highscore matrix and re-adds 
	 * every element single which then results in a sorted matrix. 
	 * 
	 * @return
	 * 			False since highscore was sorted
	 */
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

	/**
	 * Returns length of highscore.
	 * 
	 * @return 
	 * 			length of highscore
	 */
	public int getHighscoreCount() {
		int i = 0;
		while (highscore[i][0] != null && i < highscoreLength) {
			i++;
		}
		return i;
	}

	/**
	 * Prints highscore in console
	 */
	public void printHighscoreInConsole() {
		for (int i = 0; i < getHighscoreCount(); i++) {
			System.out.println(i + ". Player: " + highscore[i][0]
					+ " Rounds played: " + highscore[i][1] + ". Rounds won: "
					+ highscore[i][2] + ". Bananas thrown: " + highscore[i][3]
					+ ". ");
		}
	}

	/**
	 * Returns specified highscore-data-columns as string.
	 * 
	 * @param c 
	 * 			column in highscore-matrix
	 * 
	 * @return 
	 * 			highscore-data as String
	 */
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

	/**
	 * Returns the name at the specified position in our highscore-matrix.
	 * 
	 * @param position
	 * 			the row we want the name of
	 * 
	 * @return 
	 * 			position of Name
	 */
	public String getNameAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return highscore[position][0];
		else
			return null;
	}

	/**
	 * Returns the roundsPlayed at the specified position in our highscore-matrix.
	 * 
	 * @param position
	 * 			the row we want the roundsPlayed of
	 * 
	 * @return 
	 * 			amount of roundsPlayed
	 */
	public int getRoundsPlayedAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return Integer.parseInt(highscore[position][1]);
		else
			return -1;
	}

	/**
	 * Returns the roundsWon at the specified position in our highscore-matrix.
	 * 
	 * @param position 
	 * 			the row we want the roundsWon of
	 * 
	 * @return 
	 * 			amount of roundsWon
	 */
	public int getRoundsWonAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return Integer.parseInt(highscore[position][2]);
		else
			return -1;
	}

	/**
	 * Returns the bananasThrown at the specified position in our highscore-matrix.
	 * 
	 * @param position 
	 * 			the row we want the bananasThrown of
	 * 
	 * @return 
	 * 			amount of bananasThrown
	 */
	public int getBananasAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return Integer.parseInt(highscore[position][3]);
		else
			return -1;
	}

	/**
	 * Returns the percentage of won games at the specified position in our highscore-matrix.
	 * 
	 * @param position 
	 * 			the row we want the percentage won of
	 * 
	 * @return 
	 * 			exact percentage of won games
	 */
	public int getPercentageWonAtHighscorePosition(int position) {
		if (position >= 0 && highscore[position][0] != null)
			return (int) Math.round(getRoundsWonAtHighscorePosition(position)
					* 100
					/ (double) getRoundsPlayedAtHighscorePosition(position));
		else
			return -1;
	}

	/**
	 * Returns the mean accuracy at the specified position in our highscore-matrix.
	 * 
	 * @param position 
	 * 			the row we want the mean accuracy of
	 * 
	 * @return 
	 * 			exact percentage of mean accuracy
	 */
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

	/**
	 * Returns name of player 1.
	 * 
	 * @return 
	 * 			name of player 1
	 */
	public String getPlayer1() {// get playername :: player 1
		return player1;
	}
	
	/**
	 * Sets name of player 1.
	 * 
	 * @param player1 
	 * 			name of the player
	 */
	public void setPlayer1(String player1) {// set playername :: player 1
		this.player1 = player1;
	}

	/**
	 * Returns name of player 2.
	 * 
	 * @return 
	 * 			name of player 2
	 */
	public String getPlayer2() {// get playername :: player 2
		return player2;
	}

	/**
	 * Sets name of player 2.
	 * 
	 * @param player2
	 * 			name of the player
	 */
	public void setPlayer2(String player2) {// set playername :: player 2
		this.player2 = player2;
	}
	
	/**
	 * Returns true if game is in pause-mode.
	 * 
	 * @return 
	 * 			True is paused - False is not paused
	 */
	public boolean getPaused() {
		return isPaused;
	}

	/**
	 * Sets variable <code>isPaused</code> true/false.
	 * 
	 * @param paused 
	 * 			True to set the game into paused-mode
	 */
	public void setPaused(boolean paused) {
		isPaused = paused;
	}

	/**
	 * Returns the play that won.
	 * 
	 * @return 
	 * 			winning player
	 */
	public String getPlayerWon() {
		return playerWon;
	}
	
	/**
	 * Sets variable <code>playerWon</code> to the specified input.
	 * 
	 * @param playerWon
	 * 			input string for the player that won
	 */
	public void setPlayerWon(String playerWon) {
		this.playerWon = playerWon;
	}
	
	/**
	 * Returns amount of remaining rounds.
	 * 
	 * @return 
	 * 			amount of remaining rounds
	 */
	public int getRemainingRounds() {
		return remainingRounds;
	}
	
	/**
	 * Sets amount of remaining rounds.
	 * 
	 * @param remainingRounds 
	 * 			amount of remaining rounds
	 */
	public void setRemainingRounds(int remainingRounds) {
		this.remainingRounds = remainingRounds;
	}
	
	/**
	 * Returns current score.
	 * 
	 * @return 
 	 * 			current score
	 */
	public int[] getCurrentScore() {
		return currentScore;
	}
	
	/**
	 * Sets current score.
	 * 
	 * @param score1 
	 * 			score of player1
	 * @param score2 
	 * 			score of player2
	 */
	public void setCurrentScore(int score1, int score2) {
		this.currentScore[0] = score1;
		this.currentScore[1] = score2;
	}
	
	/**
	 * Returns score, which is needed to win the game.
	 * 
	 * @return 
	 * 			score which is needed to win the game
	 */
	public int getPlayTillScore() {
		return playTillScore;
	}
	
	/**
	 * Sets score, which is needed to win the game.
	 * 
	 * @param playTillScore
	 * 			score which is needed to win the game
	 */
	public void setPlayTillScore(int playTillScore) {
		this.playTillScore = playTillScore;
	}
}
