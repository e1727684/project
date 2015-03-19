package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.util.GameData;
import de.tu_darmstadt.gdi1.gorillas.util.MusicPlayer;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import eea.engine.event.basicevents.TimeEvent;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 */
public class MainMenuState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;
	
	private final int distance = 80;
    private final int start_Position = 80;

	/**
	 * The constructor. Creates a new state.
	 * 
	 * @param sid  
	 * 				this state's id. it can be identified by it and is unique!
	 */
	public MainMenuState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		if (Gorillas.data == null) Gorillas.data = new GameData(); // eh.... ok. we testing i guess? 
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// Initializing stuff
		// <---
		float scale = 0.18f;
		int offset = 90;
		if (!Gorillas.data.getPaused())
			Gorillas.data.setPlayerWon("");
		// --->
		
		// Creating required Entities
		// <---
		Entity background = new Entity("background"); // @deprecated
		Entity mbackground = new Entity("menubackground");
		Entity new_Game_Entity = new Entity("Neues Spiel starten");
		Entity instructions_Entity = new Entity("Instructions");
		Entity highscore_Entity = new Entity("Highscore");
		Entity options_Entity = new Entity("Options");
		Entity about_Entity = new Entity("About");
		Entity quit_Entity = new Entity("Beenden");
		Entity music = new Entity("Music");
		Entity escListener = new Entity("ESC_Listener");
		Entity nListener = new Entity("n_Listener");
		// --->

		// Giving the Entities a picture.... If we aren't testing!
		// <---
		if (!Gorillas.data.guiDisabled) { // really....
			background.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/backgrounds/background.png")));
			mbackground.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/backgrounds/backgroundMain.png")));
			new_Game_Entity.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/button.png")));
			instructions_Entity.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/button.png")));
			highscore_Entity.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/button.png")));
			options_Entity.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/button.png")));
			about_Entity.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/button.png")));
			quit_Entity.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/button.png")));

			// Sneaking this in here so we get no sound event while testing...
			// <---
			Event sound = new TimeEvent(1, false);
			sound.addAction(new Action() { @Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) { MusicPlayer.playBG(); }});
			music.addComponent(sound);
			// --->
		}
		// --->

		// Setting the Entities positions!
		// <---
		background.setPosition(new Vector2f(400, 300));
		mbackground.setPosition(new Vector2f(400, 300));
		new_Game_Entity.setPosition(new Vector2f(218, offset));
		instructions_Entity.setPosition(new Vector2f(218, offset + distance));
		highscore_Entity.setPosition(new Vector2f(218, offset + distance * 2));
		options_Entity.setPosition(new Vector2f(218, offset + distance * 3));
		about_Entity.setPosition(new Vector2f(218, offset + distance * 4));
		quit_Entity.setPosition(new Vector2f(218, offset + distance * 5));
		// --->

		// Scaling the Entities pictures!
		// <---
		new_Game_Entity.setScale(scale);
		instructions_Entity.setScale(scale);
		highscore_Entity.setScale(scale);
		options_Entity.setScale(scale);
		about_Entity.setScale(scale);
		quit_Entity.setScale(scale);
		// --->

		// Creating the Events for all buttons and keylisteners!
		// <---
		ANDEvent mainEvents_n = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		ANDEvent mainEvents_i = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		ANDEvent mainEvents_h = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		ANDEvent mainEvents_o = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		ANDEvent mainEvents_a = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		ANDEvent mainEvents_q = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		KeyPressedEvent nPressed = new KeyPressedEvent(Input.KEY_N);
    	// --->

		// Creating and adding the Actions!
		// Care: One-line-actions are >literally< summarized as one-line-actions but given a comment on what they do.
		// <--- Creating
		Action new_Game_Action = new ChangeStateInitAction(Gorillas.GAMESETUPSTATE);
		Action instructions_Action = new ChangeStateAction(Gorillas.INSTRUCTIONSTATE);
		Action highscore_Action = new ChangeStateAction(Gorillas.HIGHSCORESTATE);
		Action options_Action = new ChangeStateAction(Gorillas.OPTIONSTATE);
		Action about_Action = new ChangeStateAction(Gorillas.ABOUTSTATE);
		Action quit_Action = new QuitAction();
			// Sound-action when a button is pressed :: SFX
		Action buttonPressed = new Action() { @Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) { MusicPlayer.playButton(); }};
		// ---> 
		// <--- Adding
		mainEvents_n.addAction(new_Game_Action);
		mainEvents_i.addAction(instructions_Action);
		mainEvents_h.addAction(highscore_Action);
		mainEvents_o.addAction(options_Action);
		mainEvents_a.addAction(about_Action);
		mainEvents_q.addAction(quit_Action);
		mainEvents_n.addAction(buttonPressed);
		mainEvents_i.addAction(buttonPressed);
		mainEvents_h.addAction(buttonPressed);
		mainEvents_o.addAction(buttonPressed);
		mainEvents_a.addAction(buttonPressed);
		mainEvents_q.addAction(buttonPressed);
		escPressed.addAction(new Action() { @Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {
				if (Gorillas.data.getPaused()) {
					sb.enterState(Gorillas.GAMEPLAYSTATE);
					if (gc.isPaused())
						gc.resume();
					Gorillas.data.setPaused(false);
					MusicPlayer.playButton();
				}}});
		nPressed.addAction(new ChangeStateAction(Gorillas.GAMESETUPSTATE)); //
		nPressed.addAction(buttonPressed);
		// --->

		// Assigning the previously created Events to our Entities!
    	// Note: A game would be very boring without events..
		// <---
		new_Game_Entity.addComponent(mainEvents_n);
		instructions_Entity.addComponent(mainEvents_i);
		highscore_Entity.addComponent(mainEvents_h);
		options_Entity.addComponent(mainEvents_o);
		about_Entity.addComponent(mainEvents_a);
		quit_Entity.addComponent(mainEvents_q);
		escListener.addComponent(escPressed);
		nListener.addComponent(nPressed);
    	// --->

		// Finally: Adding all local created Entities into our game-wide entity manager!
		// <---
		entityManager.addEntity(this.stateID, background);
		entityManager.addEntity(this.stateID, mbackground);
		entityManager.addEntity(this.stateID, new_Game_Entity);
		entityManager.addEntity(this.stateID, instructions_Entity);
		entityManager.addEntity(this.stateID, highscore_Entity);
		entityManager.addEntity(this.stateID, options_Entity);
		entityManager.addEntity(this.stateID, about_Entity);
		entityManager.addEntity(this.stateID, quit_Entity);
		entityManager.addEntity(this.stateID, music);
		entityManager.addEntity(stateID, escListener);
		entityManager.addEntity(stateID, nListener);
    	// --->
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (!Gorillas.data.musicIsPlaying) MusicPlayer.playBG();
		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		entityManager.renderEntities(container, game, g);

		// Draw our menu and draw our menu and draw our menu and draw our menu and ...
		// <---
        g.setColor(new Color(0, 0, 0)); // font color
		int counter = 0;
		g.drawString("Start New Game", 160, start_Position+counter*distance); counter++;
		g.drawString("Instructions", 160, start_Position+counter*distance); counter++;
		g.drawString("Highscore", 160, start_Position+counter*distance); counter++;
		g.drawString("Options", 160, start_Position+counter*distance); counter++;
		g.drawString("About", 160, start_Position+counter*distance); counter++;
		g.drawString("Quit", 160, start_Position+counter*distance); counter++;
		// --->
		
		// Blinking GamePaused-Message
		if (Gorillas.data.getPaused()) {
			gamePausedMessage(g);
		}
	}

	boolean draw = true; // Actually needed non-local so they don't reset... o.O
	int clk = 0; // Fun-fact: The game will give us some funny error as soon as Integer.MAX_VALUE is reached here. Will only take a few years in pause state.
	
	/**
	 * A method to display a blinking pause message in the main menu.
	 * 
	 * @param g
	 * 				the Graphics we draw on
	 */
	private void gamePausedMessage(Graphics g) {
		if (clk % 75 == 0) { draw = !draw; } clk++; // "Clock"
		if (draw)
			g.drawString("- Game paused (press ESC) -", 500, 120);
	}

	@Override
	public int getID() {
		return stateID;
	}
}

