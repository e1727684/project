package de.tu_darmstadt.gdi1.gorillas.util;

public class GameData {
	private String player1;
	private String player2;
	
	public GameData() {//ONE call in Gorillas, then getter&setters!
		init();
	}
	
	private void init() {
		player1 = "";
		player2 = "";
		// more?
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
}