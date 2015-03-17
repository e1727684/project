package de.tu_darmstadt.gdi1.gorillas.util;

import com.sun.javafx.geom.Vec4f;


public class GameData {
	private String player1;
	private String player2;
	private boolean isPaused;	//de-/aktiviert ESC-Taste im MainMenu
	private String playerWon;
	public boolean guiDisabled;
	private String[][] highscore;
	private int highscoreLength = 10;
	
	public GameData() {//ONE call in Gorillas, then getter&setters!
		init();
	}
	
	private void init() {
		player1 = "";
		player2 = "";
		isPaused = false;
		playerWon = "";
		guiDisabled = true;
		highscore = new String[highscoreLength][4];
		load();
		// more?
	}

	public void save() {
		
	}
	
	public void load() {
		highscore = new String[highscoreLength][4]; //instead: load from file - TODO
	}
	
	public boolean addHighscore(String name, int numberOfRounds, int roundsWon,
			int bananasThrown) {
		if (highscore == null) load();
		int i = 0;
		while (i < getHighscoreCount() 
				&& !name.equals(highscore[i][0])) 
				{i++;}
		if (i > 0 || i == getHighscoreCount()) {
			i = 0;
			while (i < getHighscoreCount() // falls schlechter als alle andern, h�nge hinten an
					&& (float)roundsWon/numberOfRounds < (float)getRoundsWonAtHighscorePosition(i)/getRoundsPlayedAtHighscorePosition(i))
					{i++;}
			for (int j = getHighscoreCount(); j >= i; j--) { // have to move all from end to index i
				highscore[j+1][0] = highscore[j][0];
				highscore[j+1][1] = highscore[j][1];
				highscore[j+1][2] = highscore[j][2];
				highscore[j+1][3] = highscore[j][3];
			}
			highscore[i][0] = name;
			highscore[i][1] = Integer.toString(numberOfRounds);
			highscore[i][2] = Integer.toString(roundsWon);
			highscore[i][3] = Integer.toString(bananasThrown);
			return true;
		} else {
			highscore[i][1] = Integer.toString(Integer.parseInt(highscore[i][1])+numberOfRounds);
			highscore[i][2] = Integer.toString(Integer.parseInt(highscore[i][2])+roundsWon);
			highscore[i][3] = Integer.toString(Integer.parseInt(highscore[i][3])+bananasThrown);
			return sortHighscore();
		}
	}
	
	private boolean sortHighscore() {
		String[][] temp = highscore;
		highscore = new String[highscoreLength][4];
		int i = 0;
		while (temp[i][0] != null 
				&& addHighscore(temp[i][0], Integer.parseInt(temp[i][1]), Integer.parseInt(temp[i][2]), Integer.parseInt(temp[i][3]))) 
				{i++;}
		return false;
	}
	
	public int getHighscoreCount() {
		int i = 0;
		while (highscore[i][0] != null && i < highscoreLength) {i++;}
		return i;
	}
	
	public void printHighscoreInConsole() {
		for (int i = 0; i < getHighscoreCount(); i++) {
			System.out.println(i+". Player: "+highscore[i][0]+" Rounds played: "+highscore[i][1]+". Rounds won: "+highscore[i][2]+". Bananas thrown: "+highscore[i][3]+". ");
		}
	}
	
	public String giveHighscoreAsString() {
		String hsc = "";
		for (int i = 0; i < getHighscoreCount(); i++) {
			hsc += (i+1)+". Player: "+highscore[i][0]+". Rounds played: "+highscore[i][1]+". Rounds won: "+highscore[i][2]+". Bananas thrown: "+highscore[i][3]+". \n";
		}
		return hsc;
	}
	
	public String getNameAtHighscorePosition(int position) {
		if (highscore[position][0] != null)
			return highscore[position][0];
		else
			return null;
	}
	
	public int getRoundsPlayedAtHighscorePosition(int position) {
		if (highscore[position][0] != null)
			return Integer.parseInt(highscore[position][1]);
		else
			return -1;
	}
	
	public int getRoundsWonAtHighscorePosition(int position) {
		if (highscore[position][0] != null)
			return Integer.parseInt(highscore[position][2]);
		else
			return -1;
	}
	
	public int getBananasAtHighscorePosition(int position) {
		if (highscore[position][0] != null)
			return Integer.parseInt(highscore[position][3]);
		else
			return -1;
	}
	
	public int getPercentageWonAtHighscorePosition(int position) {
		if (highscore[position][0] != null)
			return (int) Math.round(getRoundsWonAtHighscorePosition(position)*100/(double)getRoundsPlayedAtHighscorePosition(position));
		else
			return -1;
	}
	
	public double getMeanAccuracyAtHighscorePosition(int position) {
		if (highscore[position][0] != null)
			return (int) Math.round(getRoundsWonAtHighscorePosition(position)*100/(double)getBananasAtHighscorePosition(position));
		else
			return -1;
	}
	
	public String getPlayer1() {//get playername :: player 1
		return player1;
	}

	public void setPlayer1(String player1) {//set playername :: player 1
		this.player1 = player1;
	}

	public String getPlayer2() {//get playername :: player 2
		return player2;
	}

	public void setPlayer2(String player2) {//set playername :: player 2
		this.player2 = player2;
	}
	
	public boolean getPaused(){
		return isPaused;
	}
	
	public void setPaused(boolean paused){
		isPaused = paused;
	}
	
	public String getPlayerWon() {
		return playerWon;
	}

	public void setPlayerWon(String playerWon) {
		this.playerWon = playerWon;
	}
}