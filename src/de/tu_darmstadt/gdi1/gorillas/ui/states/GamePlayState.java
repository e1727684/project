package de.tu_darmstadt.gdi1.gorillas.ui.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.util.GameData;
import de.tu_darmstadt.gdi1.gorillas.util.MusicPlayer;
import de.tu_darmstadt.gdi1.gorillas.util.MyCollisionEvent;
import de.tu_darmstadt.gdi1.gorillas.util.MyLeavingScreenEvent;
import de.tu_darmstadt.gdi1.gorillas.util.Wurf;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.RotateLeftAction;
import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.DestructibleImageEntity;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.TimeEvent;
import eea.engine.interfaces.IDestructible;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 */
public class GamePlayState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;

	private Label angleLabel1;
	private Label angleLabel2;
	public EditField angleInput1;
	public EditField angleInput2;
	private Label speedLabel1;
	private Label speedLabel2;
	public EditField speedInput1;
	public EditField speedInput2;
	private Button dropButton;
	private Label nameLabel;
	public boolean turn;
	private AtomicInteger wurfAnzahl;
	private boolean goCongratulate;
	private boolean reset;
	private int wind = 0;
	private boolean daneben;


	/**
	 * The constructor. Creates a new state.
	 * 
	 * @param sid  
	 * 				this state's id. it can be identified by it and is unique!
	 */
	public GamePlayState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		if (Gorillas.data == null) Gorillas.data = new GameData(); // eh.... ok. we testing i guess? 
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// Initializing stuff
		// <---
		Gorillas.data.setPaused(false);
		wurfAnzahl = new AtomicInteger(1);
		goCongratulate = false;
		daneben = false;
		cont = container;
		sb = game;
        // --->

        // Creating required Entities
		// <---
		Entity sun_astonished = new Entity("sun_astonished");
        Entity escListener = new Entity("ESC_Listener");
        Entity returnListener = new Entity("return_Listener");
        Entity gorilla1 = new Entity("gorilla1");
        Entity gorilla2 = new Entity("gorilla2");
        Entity sun_smiling = new Entity("sun_smiling");
        Entity arrow_wind = new Entity("arrow_wind");
        // --->
        
        // Giving the Entities a picture.... If we aren't testing!
     	// <---
    	if (!Gorillas.data.guiDisabled) {
			sun_astonished.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/sun/sun_astonished.png")));
            sun_smiling.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/sun/sun_smiling.png")));
            arrow_wind.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/pfeil.png")));
            gorilla1.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/gorillas/gorilla.png")));
            gorilla2.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/gorillas/gorilla.png")));
    	}
        // --->
        
		// Doing stuff we have to do exactly here. Let's call it destiny.
        // <---
    	  // has to be added BEFORE doing houses
        entityManager.addEntity(this.stateID, entityManager.getEntity(0, "background")); 
        if (Gorillas.data.getMap().size() == 0)
        	Gorillas.data.makeRandomMap(800, 600, (int)gorilla1.getShape().getWidth(), (int)gorilla1.getShape().getHeight());
        drawHouses();
        // gorilla positions have to be decided AFTER creating the houses and BEFORE setting their positions
        if (Gorillas.data.getGorilla1pos() == null || Gorillas.data.getGorilla2pos() == null)
        	Gorillas.data.randomizeGorillaPositions(game.getContainer().getWidth(), game.getContainer().getHeight());
        if (Gorillas.options != null && Gorillas.options.isWindEnabled())
        	makeWind(); // wind force has to be decided before setting the arrow.
        // --->
        
        // Setting the Entities positions!
     	// <---
        gorilla1.setPosition(Gorillas.data.getGorilla1pos()); 
        gorilla2.setPosition(Gorillas.data.getGorilla2pos()); 
        sun_smiling.setPosition(new Vector2f((game.getContainer().getWidth() / 2), 30));
        sun_astonished.setPosition(new Vector2f((game.getContainer().getWidth() / 2), 30));
        arrow_wind.setPosition(new Vector2f(550, 55));
        arrow_wind.setScale(0.7F*wind/15); // (if it looks stupid but works it ain't stupid)
        // --->
        
		// Creating the Events for all buttons and keylisteners!
		// <---
        KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
        KeyPressedEvent returnPressed = new KeyPressedEvent(Input.KEY_RETURN);
    	// --->

		// Creating and adding the Actions!
		// Care: One-line-actions are >literally< summarized as one-line-actions but given a comment on what they do.
		// <--- Creating
		// ---> 
		// <--- Adding
        escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
          // Pausing
        escPressed.addAction(new Action(){ @Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {Gorillas.data.setPaused(true);}});
        escListener.addComponent(escPressed);
          // Shooting (Try-catch if player hits return before having entered numbers into angle&speed field..)
        returnPressed.addAction(new Action(){ @Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {try {inputFinished();} catch (NumberFormatException e) {}}});
        returnListener.addComponent(returnPressed);
        // --->

        // Entities dem StateBasedEntityManager übergeben
        // <---
        entityManager.addEntity(this.stateID, gorilla1);
        entityManager.addEntity(this.stateID, gorilla2);
        entityManager.addEntity(stateID, escListener);
        entityManager.addEntity(stateID, returnListener);
        entityManager.addEntity(this.stateID, sun_smiling);
        entityManager.addEntity(this.stateID, sun_astonished);
        if (Gorillas.options != null && Gorillas.options.isWindEnabled()) // only need the arrow if we have wind, check for null is only for the tests when options may not be initialized
        	entityManager.addEntity(this.stateID, arrow_wind);
        // --->
        
        // left starts the game
        turn = true;
	}
	
	/**
	 * This method sets a random int from -15 to +15 as wind.
	 */
	private void makeWind() {
        Random rand = new Random();
		this.wind = 15-rand.nextInt(30);
	}
	
	/**
	 * Drawing a the house-map.
	 * 
	 * @return
	 * 						array containing the house heights
	 */
	public int[] drawHouses() {
		int houseWidth = 100;
        Random rand = new Random(); // such random
        for (int houseIndex = 0; houseIndex < Gorillas.data.getHouseAmount(); houseIndex++) {
    		
            BufferedImage image = new BufferedImage(houseWidth, (int) Gorillas.data.getMap().get(houseIndex).y, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
            graphic.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            graphic.fillRect(0, 0, houseWidth, (int) Gorillas.data.getMap().get(houseIndex).y);
            
            graphic.setColor(new Color(0, 0, 0));
            for (int i = 5; i < (int) Gorillas.data.getMap().get(houseIndex).y; i = i + 20) {
                    for (int j = 5; j < houseWidth; j = j + 20) {
                            graphic.fillRect(j, i, 7, 10);
                    }
            }
        	if (!Gorillas.data.guiDisabled) { // NI NI NI NI NI OPENGL NI NI NI NI NI
            DestructibleImageEntity house = new DestructibleImageEntity("obstacle", image, "gorillas/destruction.png", false);
        	
            house.setPosition(new Vector2f(Gorillas.data.getMap().get(houseIndex).x+50, 600-((int) Gorillas.data.getMap().get(houseIndex).y/2)));
            entityManager.addEntity(stateID, house); // add & forget
        	}
        }
        int[] houseHeights = new int[Gorillas.data.getHouseAmount()];
        for (int i = 0; i < houseHeights.length; i++) {
        	houseHeights[i] = (int) Gorillas.data.getMap().get(i).y;
        }
        return houseHeights;
	}

	StateBasedGame sb;
	GameContainer cont;
	public void changeMap() throws SlickException {
		entityManager.clearEntitiesFromState(stateID);
		sb.enterState(Gorillas.GAMEPLAYSTATE);
		init(cont, sb);
	}

	int clk = 0;
	@Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
                    throws SlickException {

            entityManager.renderEntities(container, game, g);
            
            // Displaying names on upper corners of the window
            g.drawString(Gorillas.data.getPlayer1(), 20, 10);
            g.drawString(Gorillas.data.getPlayer2(), 730, 10);
            
            // Mocking at player for throwing outside the window. HAH
            if (daneben && Gorillas.options.isSpottEnabled()) {
    			if (clk > 100) { daneben = false; clk = 0; }
            	g.drawString(spott, 320, 150);
            	clk++;
            }
    }

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (entityManager.hasEntity(stateID, "banana") || !Gorillas.data.getPlayerWon().equals("")) // switch the input label to invisible while the banana is flying AND ROTATING
			switchInputLabel(false);
		else 
			switchInputLabel(true);

		if (Gorillas.data.sunAstonished) {
			entityManager.getEntity(stateID, "sun_astonished").setVisible(true);
			entityManager.getEntity(stateID, "sun_smiling").setVisible(false); 
		} else {
			entityManager.getEntity(stateID, "sun_astonished").setVisible(false);
			entityManager.getEntity(stateID, "sun_smiling").setVisible(true); 
		}
		
		if (reset) {
			reset = false;
			Gorillas.data.setPlayerWon("");
			Gorillas.data.flushMap();
			entityManager.clearEntitiesFromState(stateID);
			game.enterState(Gorillas.GAMEPLAYSTATE);
			init(container, game);
		}
		
		nameLabel.setText(buildTheLabel()); //updated for turns, rounds, score, ..
		
		if (goCongratulate) {
			game.enterState(Gorillas.CONGRATULATIONSTATE);
		}
		
		if (!Gorillas.data.getPlayerWon().equals("")) {
			someoneWon();
		}
		entityManager.updateEntities(container, game, delta);
	}
	
	/**
	 * Method used to build the label above the input fields.
	 * 
	 * @return
	 * 			String which contains stuff like score, rounds and which turn it is.
	 */
	private String buildTheLabel() {
		String buildNameLabel = "";
		if (Gorillas.data.getRemainingRounds() != 0 && Gorillas.data.getPlayTillScore() == 0) // display names so the players know whose turn it is!
			if (Gorillas.data.getRemainingRounds() == 1)
				buildNameLabel += "LAST ROUND!\n";
			else
				buildNameLabel += Gorillas.data.getRemainingRounds() + " rounds left!\n";
		else 
			if (Gorillas.data.getPlayTillScore() == 1)
				buildNameLabel += "NEXT HIT WINS!\n";
			else
				buildNameLabel += "Score "+Gorillas.data.getPlayTillScore() + " times to win!\n";
		if ((Gorillas.data.getRemainingRounds() > 1 || Gorillas.data.getPlayTillScore() > 1) // display score IF (we play more than 1 round or to 1 score) OR (if 1 round remains to be played AND we have played some before)
				|| ((Gorillas.data.getRemainingRounds() == 1 || Gorillas.data.getPlayTillScore() == 1) 
						&& (Gorillas.data.getCurrentScore()[0] != 0 || Gorillas.data.getCurrentScore()[1] != 0)))
			buildNameLabel += ""+Gorillas.data.getCurrentScore()[0]+">Score<"+Gorillas.data.getCurrentScore()[1]+"\n" +wurfAnzahl + ". Wurf! ";
		if (turn)
			buildNameLabel += "Turn of player: "+Gorillas.data.getPlayer1();
		else 
			buildNameLabel += "Turn of player: "+Gorillas.data.getPlayer2();
		return buildNameLabel;
	}
	
	/**
	 * Method that gets called if someone won a round. 
	 * Displays explosion, makes the winning gorilla celebrate and 
	 * decides whether to change to congratulationstate (=game over) 
	 * or continue into next round.
	 * 
	 * @throws SlickException
	 * 							throws.
	 */
	private void someoneWon() throws SlickException {
		if (!entityManager.hasEntity(stateID, "boomTimer")) {
		entityManager.getEntity(stateID, "gorilla1").setVisible(false);
		entityManager.getEntity(stateID, "gorilla2").setVisible(false);

		Entity jubel1 = new Entity("jubel1");
		Entity jubel2 = new Entity("jubel2");
		Entity boom = new Entity("boom");
		Entity boomTimer = new Entity("boomTimer");
		
		boom.setPosition(entityManager.getEntity(stateID, Gorillas.data.getPlayerWon().equals("player1")?"gorilla2":"gorilla1").getPosition());
		jubel1.setPosition(entityManager.getEntity(stateID, Gorillas.data.getPlayerWon().equals("player1")?"gorilla1":"gorilla2").getPosition());
		jubel2.setPosition(entityManager.getEntity(stateID, Gorillas.data.getPlayerWon().equals("player1")?"gorilla1":"gorilla2").getPosition());
		
		boom.addComponent(new ImageRenderComponent(new Image("assets/gorillas/explosions/explosion_1.png")));
		jubel1.addComponent(new ImageRenderComponent(new Image(Gorillas.data.getPlayerWon().equals("player1")?"assets/gorillas/gorillas/gorilla_left_up.png":"assets/gorillas/gorillas/gorilla_right_up.png")));
		jubel2.addComponent(new ImageRenderComponent(new Image(Gorillas.data.getPlayerWon().equals("player1")?"assets/gorillas/gorillas/gorilla_right_up.png":"assets/gorillas/gorillas/gorilla_left_up.png")));
		
		LoopEvent loop = new LoopEvent();
		loop.addAction(new Action() {
			int clk = 0;
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {
				if (clk % 200 == 0) {
					entityManager.getEntity(stateID, "jubel1").setVisible(true);
					entityManager.getEntity(stateID, "jubel2").setVisible(false);
				} else if (clk % 100 == 0) {
					entityManager.getEntity(stateID, "jubel1").setVisible(false);
					entityManager.getEntity(stateID, "jubel2").setVisible(true);
				}
				clk++;
			}
		});
		Event timeEvent = new TimeEvent(3000, false);
		timeEvent.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb,
					int delta, Component event) {
				if (Gorillas.data.getRemainingRounds() <= 1 && Gorillas.data.getPlayTillScore() == 0) {
					goCongratulate = true;
				} else if (Gorillas.data.getPlayTillScore() == Gorillas.data.getCurrentScore()[0]+1 || Gorillas.data.getPlayTillScore() == Gorillas.data.getCurrentScore()[1]+1) {
					goCongratulate = true;
				} else {
					Gorillas.data.setRemainingRounds(Gorillas.data.getRemainingRounds()-1);
					if (Gorillas.data.getPlayerWon().equals("player1"))
						Gorillas.data.setCurrentScore(Gorillas.data.getCurrentScore()[0]+1, Gorillas.data.getCurrentScore()[1]); // TODO
					else
						Gorillas.data.setCurrentScore(Gorillas.data.getCurrentScore()[0], Gorillas.data.getCurrentScore()[1]+1);
					reset = true;
				}
			}
		});
		Event timeEvent2 = new TimeEvent(100, false);
		timeEvent2.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb,
					int delta, Component event) {
				MusicPlayer.playApplause();  // plays applause if a gorilla is kill
			}
		});
		boomTimer.addComponent(timeEvent);
		boomTimer.addComponent(timeEvent2);
		boomTimer.addComponent(loop);
		entityManager.addEntity(stateID, boom);
		entityManager.addEntity(stateID, boomTimer);
		entityManager.addEntity(stateID, jubel1);
		entityManager.addEntity(stateID, jubel2);
		}
	}
	
	/**
	 * Used to hide the input field and the text labels
	 * 
	 * @param visible
	 * 					True/False, depending on banana flying or finished game
	 */
	private void switchInputLabel(boolean visible) {
		if (visible)
			Gorillas.data.sunAstonished = false;
		nameLabel.setVisible(visible);
		if (turn) {
			angleLabel1.setVisible(visible);
			angleInput1.setVisible(visible);
			speedLabel1.setVisible(visible);
			speedInput1.setVisible(visible);
		} else {
			angleLabel2.setVisible(visible);
			angleInput2.setVisible(visible);
			speedLabel2.setVisible(visible);
			speedInput2.setVisible(visible);
		}
		dropButton.setVisible(visible);
	}
	
	@Override
	public int getID() {
		return stateID;
	}

	@Override
	protected RootPane createRootPane() {
    	// Custom rootpane
		RootPane rp = super.createRootPane();
		
        // Creating labels ...
        // <---
		nameLabel = new Label("");
		angleLabel1 = new Label("Angle:");
		angleLabel2 = new Label("Angle:");
		speedLabel1 = new Label("Speed:");
		speedLabel2 = new Label("Speed:");
        // --->

        // Creating input fields ...
        // <---
		angleInput1 = new EditField();
		angleInput2 = new EditField();
		speedInput1 = new EditField();
		speedInput2 = new EditField();
        // --->

        // Creating a button...
        // <---
		dropButton = new Button("throw");
        // --->
		
        // Creating and assigning callbacks ...
		// Care: One-line-callbacks are >literally< summarized as one-line-callbacks but given a comment on what they do.
        // <---
		  // The callbacks handle the input into those fields. If you type something into it they get called back!
		angleInput1.addCallback(new Callback() { public void callback(int key) { handleEditFieldInput(key, angleInput1, this, 360); }});
		angleInput2.addCallback(new Callback() { public void callback(int key) { handleEditFieldInput(key, angleInput2, this, 360); }});
		speedInput1.addCallback(new Callback() { public void callback(int key) { handleEditFieldInput(key, speedInput1, this, 200); }});
		speedInput2.addCallback(new Callback() { public void callback(int key) { handleEditFieldInput(key, speedInput2, this, 200); }});
		  // This callback gets called if you push the button
		dropButton.addCallback(new Runnable() { @Override public void run() {try {inputFinished();} catch (NumberFormatException e) {}}});
        // --->

        // Adding wind label - if wind is activated!
        // <---
		if (Gorillas.options != null && Gorillas.options.isWindEnabled()) {
			Label windLabel1 = new Label("wind");
			Label windLabel2 = new Label("strength");
			windLabel1.setPosition(530, 27);
			windLabel2.setPosition(525, 37);
			rp.add(windLabel1);
			rp.add(windLabel2);
		}
        // --->

        // Finally: Adding the elements to our rootpane ...
        // <---
		rp.add(nameLabel);
		rp.add(angleLabel1);
		rp.add(angleLabel2);
		rp.add(angleInput1);
		rp.add(angleInput2);
		rp.add(speedLabel1);
		rp.add(speedLabel2);
		rp.add(speedInput1);
		rp.add(speedInput2);
		rp.add(dropButton);
        // --->
		
		return rp;
	}

	@Override
	protected void layoutRootPane() { 
        // Literally layout-ing our rootpane!
		// Initializing stuff which we need
        // <--- 
		int xOffset = 5;
		int yOffset = 80;
		int gap = 5;
        // --->

		// Auto-adjust size of labels
        // <--- 
		nameLabel.adjustSize();
		angleLabel1.adjustSize();
		angleLabel2.adjustSize();
		speedLabel1.adjustSize();
		speedLabel2.adjustSize();
        // --->

		// Manually set size of input boxes
        // <--- 
		angleInput1.setSize(50, 25);
		angleInput2.setSize(50, 25);
		speedInput1.setSize(50, 25);
		speedInput2.setSize(50, 25);
        // --->

		// Manually set size of button
        // <--- 
		dropButton.setSize(50, 25);
        // --->

		// Setting positions...
        // <--- Labels
		nameLabel.setPosition(xOffset, yOffset/2);
		angleLabel1.setPosition(xOffset, yOffset + angleLabel1.getHeight() + gap);
		angleLabel2.setPosition(xOffset, yOffset + angleLabel2.getHeight() + gap);
		speedLabel1.setPosition(xOffset, yOffset + angleLabel1.getHeight() + gap + angleLabel1.getHeight() + gap);
		speedLabel2.setPosition(xOffset, yOffset + angleLabel2.getHeight() + gap + angleLabel2.getHeight() + gap);
        // --->
        // <--- Input boxes
		angleInput1.setPosition(xOffset + angleLabel1.getWidth() + gap, yOffset + angleLabel1.getHeight() + gap);
		angleInput2.setPosition(xOffset + angleLabel2.getWidth() + gap, yOffset + angleLabel2.getHeight() + gap);
		speedInput1.setPosition(xOffset + speedLabel1.getWidth() + gap, yOffset + 2*angleLabel1.getHeight() + 2*gap);
		speedInput2.setPosition(xOffset + speedLabel2.getWidth() + gap, yOffset + 2*angleLabel2.getHeight() + 2*gap);
        // --->
        // <--- Button
		dropButton.setPosition(xOffset + speedLabel1.getWidth() + gap, yOffset + 2*angleLabel1.getHeight() + 3*gap + speedLabel1.getHeight());
        // --->

		// Setting all to invisible... gets swapped around for each turn!
        // <--- 
		angleLabel1.setVisible(false);
		angleInput1.setVisible(false);
		speedLabel1.setVisible(false);
		speedInput1.setVisible(false);
		angleLabel2.setVisible(false);
		angleInput2.setVisible(false);
		speedLabel2.setVisible(false);
		speedInput2.setVisible(false);
        // --->
	}

	/**
     * Method gets called if you type something into the input field.
	 * <code>Credits to dropofwater</code>
	 * 
     * @param key 
     * 				the pressed button
     * @param editField 
     * 				which field to edit
     * @param callback
	 *				the callback on the field
	 * @param maxValue
	 * 				biggest number that you may type into the <code>editField</code>
	 */
	public void handleEditFieldInput(int key, EditField editField, Callback callback,
			int maxValue) {

		if (key == de.matthiasmann.twl.Event.KEY_NONE) {
			String inputText = editField.getText();

			if (inputText.isEmpty()) {
				return;
			}

			char inputChar = inputText.charAt(inputText.length() - 1);
			if (!Character.isDigit(inputChar)
					|| Integer.parseInt(inputText) > maxValue) {
				// a call of setText on an EditField triggers the callback, so
				// remove callback before and add it again after the call
				editField.removeCallback(callback);
				editField
						.setText(inputText.substring(0, inputText.length() - 1));
				editField.addCallback(callback);
			}
		}
	}
	
	public void handleEditFieldInputForTests(char charac, EditField editField, int maxValue) {
		String inputText = editField.getText();
		if (!Character.isDigit(charac)) {
			return;
		}
		inputText += charac;
		if (Integer.parseInt(inputText) < maxValue) {
			editField.setText(inputText);
		}
	}

	/**
	 * Method which gets called as soon as player presses the throw button.
	 * Creates and throws the banana.
	 * 
	 */
	public void inputFinished() {
		// New banana entity
		Entity banana = new Entity("banana");
		
		// if turn-true: left gorilla, otherwise right
		if (turn)
			banana.setPosition(new Vector2f(entityManager.getEntity(stateID, "gorilla1").getPosition().x+30,entityManager.getEntity(stateID, "gorilla1").getPosition().y+38));
		else 
			banana.setPosition(new Vector2f(entityManager.getEntity(stateID, "gorilla2").getPosition().x-30,entityManager.getEntity(stateID, "gorilla2").getPosition().y+38));
		
		// give the banana a picture
		try {
        	if (!Gorillas.data.guiDisabled)
        		banana.addComponent(new ImageRenderComponent(new Image("assets/gorillas/banana.png")));
		} catch (SlickException e) {}

		// move banana until leavingevent or collisionevent
		// <---
		  LoopEvent loop = new LoopEvent();
		  	// custom made movementaction
		    // <---
		  	  Wurf wurf = new Wurf(Integer.parseInt(turn?speedInput1.getText():speedInput2.getText()));
		  	  wurf.angle = turn?Integer.parseInt(angleInput1.getText()):(180-Integer.parseInt(angleInput2.getText()));
		  	  wurf.startPos = turn?new Vector2f(entityManager.getEntity(stateID, "gorilla1").getPosition().x+30,entityManager.getEntity(stateID, "gorilla1").getPosition().y-38):new Vector2f(entityManager.getEntity(stateID, "gorilla2").getPosition().x-30,entityManager.getEntity(stateID, "gorilla2").getPosition().y-38);
		  	  wurf.wind = this.wind;
		  	  if (Gorillas.options==null) 
		  		  wurf.gravity = 10;
		  	  else
		  		  wurf.gravity = Gorillas.options.getG();
		  	// --->
		  loop.addAction(wurf);
		  loop.addAction(turn?new RotateRightAction(0.5F):new RotateLeftAction(0.5F));
		banana.addComponent(loop);
		// -->

		// out of bounce event (banana leaves screen)
		// <---
		Event leavingEvent = new MyLeavingScreenEvent(); // <- custom because we have no roof
		leavingEvent.addAction(new DestroyEntityAction());
		leavingEvent.addAction(new Action() { @Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				daneben(); // mockery!
			}});
		banana.addComponent(leavingEvent);
		// --->
		
		// collision event (banane trifft auf etwas auf)
		// <---
		MyCollisionEvent collisionEvent = new MyCollisionEvent();
		collisionEvent.setEm(entityManager); // übergeben um collision mit background zu vermeiden und auf sonne zu prüfen!
		collisionEvent.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {

				// hole die Entity, mit der kollidiert wurde
				MyCollisionEvent collider = (MyCollisionEvent) event;
				Entity entity = collider.getCollidedEntity();

				// wenn diese durch ein Pattern zerstört werden kann, dann
				// caste zu IDestructible
				// ansonsten schaue ob ein gorilla getroffen ist, dann spielende
				// sonst passiert bei der Kollision nichts
				IDestructible destructible = null;
				if (entity instanceof IDestructible) {
					destructible = (IDestructible) entity;
				} else {
					if (entity.getID() == "gorilla1") {
						Gorillas.data.setPlayerWon("player2");
						Gorillas.data.addHighscore(Gorillas.data.getPlayer1(), 1, 0, wurfAnzahl.get());
						Gorillas.data.addHighscore(Gorillas.data.getPlayer2(), 1, 1, wurfAnzahl.get());
						Gorillas.data.save();
					} else if (entity.getID() == "gorilla2") {
						Gorillas.data.setPlayerWon("player1");
						Gorillas.data.addHighscore(Gorillas.data.getPlayer1(), 1, 1, wurfAnzahl.get());
						Gorillas.data.addHighscore(Gorillas.data.getPlayer2(), 1, 0, wurfAnzahl.get()-1);
						Gorillas.data.save();
					}
					return;
				}

				// destroy the hit entity
				destructible.impactAt(event.getOwnerEntity().getPosition());
			}
		});
		collisionEvent.addAction(new DestroyEntityAction());
		banana.addComponent(collisionEvent);
		// --->
		
		// turn wechselt von spieler 1 auf 2 oder umgekehrt
		turn = !turn;
		
		// wurf anzahl +1!
		if (turn)wurfAnzahl.incrementAndGet();
		
		// banane darf endlich fliegen und rotieren!!!
		entityManager.addEntity(stateID, banana);
	}

	String spott;
	/**
	 * The method which decides with what sentence the player will get mocked at.
	 * Care: At the moment only available in german.
	 */
	protected void daneben() { // TODO :: Translate?
    	Random rand = new Random();
    	String[] spott = {"Knapp daneben ist auch vorbei.", "Einfach nein.", "Versuchs doch einfach noch mal.", "Gestern Nacht war wohl lang.",
    				"Das hast du jetzt nicht wirklich getan.", "Zielen nicht vergessen.", "Ist das dein Ernst?", "Probiers gar nicht erst.",
    				"Triffst du überhaupt?"};
		daneben = true;
		this.spott = spott[rand.nextInt(spott.length)];
	}

	public void resetLabel() {
		speedInput1.setText("");
		speedInput2.setText("");
		angleInput1.setText("");
		angleInput2.setText("");
	}
}
