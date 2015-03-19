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
	private boolean turn;
	private Vector2f gorilla1pos;
	private Vector2f gorilla2pos;
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
        int[] houseHeights = new int[8]; int houseWidth = 100, startPointHouses = 0, housesIndex = 0;
          // creating houses has to be done AFTER adding background and BEFORE randomizing gorilla positions
        houseHeights = randomizeHouses(houseHeights, houseWidth, startPointHouses, housesIndex, game.getContainer().getHeight());
          // gorilla positions have to be decided AFTER creating the houses and BEFORE setting their positions
        randomizeGorillaPositions(game.getContainer().getHeight(), game.getContainer().getWidth(), houseHeights, gorilla1, gorilla2);
        if (Gorillas.options != null && Gorillas.options.isWindEnabled())
        	makeWind(); // wind force has to be decided before setting the arrow.
        // --->
        
        // Setting the Entities positions!
     	// <---
        gorilla1.setPosition(gorilla1pos); 
        gorilla2.setPosition(gorilla2pos); 
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
        if (Gorillas.options != null && Gorillas.options.isWindEnabled()) // only need the arrow if we have wind
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
	 * Placing both gorillas on random houses.
	 * Valid houses are the first three and the last three.
	 * 
	 * @param height
	 * 					window height
	 * @param width
	 * 					window width
	 * @param houseHeights
	 * 					array of househeights example: from 0 to 7 for 8 houses
	 * @param gorilla1
	 * 					the entity of gorilla 1 in order to get gorilla size
	 * @param gorilla2
	 * 					the entity of gorilla 2 in order to get gorilla size
	 */
	private void randomizeGorillaPositions(int height, int width, int[] houseHeights, Entity gorilla1, Entity gorilla2) {
		// positions for gorilla 1
        float gorilla1PosX = 0;
        float gorilla1PosY = 0;
        Random rand = new Random(); // such random
        switch (rand.nextInt(3)) {  // very random
        case 0: // either
                gorilla1PosX = 50;
                gorilla1PosY = height - (houseHeights[0] + (gorilla1.getSize().y/2));
                break;
        case 1: // or
                gorilla1PosX = 150;
                gorilla1PosY = height - (houseHeights[1] + (gorilla1.getSize().y/2));
                break;
        case 2: // or
                gorilla1PosX = 250;
                gorilla1PosY = height - (houseHeights[2] + (gorilla1.getSize().y/2));
                break;
        }
        // who knows? BLACK MAGIC!
        gorilla1pos = new Vector2f(gorilla1PosX, gorilla1PosY); // set position
        // positions for gorilla 2
        float gorilla2PosX = 0;
        float gorilla2PosY = 0;
        switch (rand.nextInt(3)) { // much random
        case 0:
                gorilla2PosX = width - 50;
                gorilla2PosY = height - (houseHeights[7] + (gorilla2.getSize().y/2));
                break;
        case 1:
                gorilla2PosX = width - 150;
                gorilla2PosY = height - (houseHeights[6] + (gorilla2.getSize().y/2));
                break;
        case 2:
                gorilla2PosX = width - 250;
                gorilla2PosY = height - (houseHeights[5] + (gorilla2.getSize().y/2));
                break;
        }
        // still black magic (random = random) BUT we have our gorilla positions!
        gorilla2pos = new Vector2f(gorilla2PosX, gorilla2PosY); // set position
	}
	
	/**
	 * Creating a (not-so-)random map of houses.
	 * 
	 * @param houseHeights
	 * 						the array to be used. house heights will be stored here
	 * @param houseWidth
	 * 						house width
	 * @param startPointHouses
	 * 						need offset so we don't have 0 pixel houses
	 * @param housesIndex
	 * 						used to count through the houses
	 * @param heigth
	 * 						window heigth
	 * @return
	 * 						finished array containing the house heights
	 */
	private int[] randomizeHouses(int[] houseHeights, int houseWidth, int startPointHouses, int housesIndex, int heigth) {
        Random rand = new Random(); // such random
        for (int e = 0; e < 8; e++) {

                houseHeights[housesIndex] = rand.nextInt(380)+60;
               
                BufferedImage image = new BufferedImage(houseWidth, houseHeights[housesIndex], BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphic = image.createGraphics();
                graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                graphic.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                graphic.fillRect(0, 0, houseWidth, houseHeights[housesIndex]);

                graphic.setColor(new Color(0, 0, 0));
                for (int i = 5; i < houseHeights[housesIndex]; i = i + 20) {
                        for (int j = 5; j < houseWidth; j = j + 20) {
                                graphic.fillRect(j, i, 7, 10);
                        }
                }

                if (startPointHouses == 0)
                        startPointHouses = houseWidth / 2;
                else
                        startPointHouses = startPointHouses + houseWidth;

            	if (!Gorillas.data.guiDisabled) { // NI NI NI NI NI OPENGL NI NI NI NI NI
                DestructibleImageEntity house = new DestructibleImageEntity("obstacle", image, "gorillas/destruction.png", false);
            	
                house.setPosition(new Vector2f(startPointHouses, heigth - (houseHeights[housesIndex] / 2)));
                entityManager.addEntity(stateID, house); // add & forget
            	}
                housesIndex++;
        }
        return houseHeights;
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
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
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
		if ((Gorillas.data.getRemainingRounds() > 1 || Gorillas.data.getPlayTillScore() > 1) 
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
					int[] score = Gorillas.data.getCurrentScore();
					if (Gorillas.data.getPlayerWon().equals("player1")) {
						score[0] = score[0]+1;
					} else {
						score[1] = score[1]+1;
					}
					Gorillas.data.setCurrentScore(score[0], score[1]);
					reset = true;
				}
			}
		});
		Event timeEvent2 = new TimeEvent(100, false);
		timeEvent2.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb,
					int delta, Component event) {
				MusicPlayer.playApplause();
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
	protected RootPane createRootPane() { // TODO :: FORMAT THIS SHIT
		// erstelle die RootPane
		RootPane rp = super.createRootPane();
		nameLabel = new Label("");
		// erstelle ein Label mit der Aufschrift "x:"
		angleLabel1 = new Label("Angle:");
		angleLabel2 = new Label("Angle:");
		// erstelle ein EditField. Es dient der Eingabe von Text
		angleInput1 = new EditField();
		angleInput2 = new EditField();
		// mit der Methode addCallBack lï¿½sst sich dem EditField ein CallBack
		// hinzufï¿½gen, in dessen Methode callback(int key) bestimmt wird, was
		// geschehen soll, wenn ein Zeichen eingegeben wird
		angleInput1.addCallback(new Callback() {
			public void callback(int key) {
				// in unserem Fall wird der Input in der Methode
				// handleEditFieldInput verarbeitet (siehe weiter unten in
				// dieser Klasse, was diese tut, und was es mit ihren Parametern
				// auf sich hat)
				handleEditFieldInput(key, angleInput1, this, 360);
			}
		});
		angleInput2.addCallback(new Callback() {
			public void callback(int key) {
				// in unserem Fall wird der Input in der Methode
				// handleEditFieldInput verarbeitet (siehe weiter unten in
				// dieser Klasse, was diese tut, und was es mit ihren Parametern
				// auf sich hat)
				handleEditFieldInput(key, angleInput2, this, 360);
			}
		});

		// analog zu einer Eingabemï¿½glichkeit fï¿½r x-Werte wird auch eine
		// fï¿½r
		// y-Werte kreiert
		speedLabel1 = new Label("Speed:");
		speedInput1 = new EditField();
		speedInput1.addCallback(new Callback() {
			public void callback(int key) {
				handleEditFieldInput(key, speedInput1, this, 200);
			}
		});
		speedLabel2 = new Label("Speed:");
		speedInput2 = new EditField();
		speedInput2.addCallback(new Callback() {
			public void callback(int key) {
				handleEditFieldInput(key, speedInput2, this, 200);
			}
		});
		
		// zuletzt wird noch ein Button hinzugefï¿½gt
		dropButton = new Button("throw");
		// ï¿½hnlich wie einem EditField kann auch einem Button ein CallBack
		// hinzugefï¿½gt werden
		// Hier ist es jedoch von Typ Runnable, da keine Parameter (z. B. welche
		// Taste wurde gedrï¿½ckt) benï¿½tigt werden
		dropButton.addCallback(new Runnable() {
			@Override
			public void run() {
				// ein Klick auf den Button wird in unserem Fall in der
				// folgenden Methode verarbeitet
				try {
					inputFinished();
				} catch (NumberFormatException e) {
					System.out.println("Oy Vey! Please enter numbers!");
				}
			}
		});
        // am Schluss der Methode mï¿½ssen alle GUI-Elemente der Rootpane
		// hinzugefï¿½gt werden
		if (Gorillas.options != null && Gorillas.options.isWindEnabled()) {
			Label windLabel1 = new Label("wind");
			Label windLabel2 = new Label("strength");
			windLabel1.setPosition(530, 27);
			windLabel2.setPosition(525, 37);
			rp.add(windLabel1);
			rp.add(windLabel2);
		}
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
		// ... und die fertige Rootpane zurï¿½ckgegeben werden
		return rp;
	}

	@Override
	protected void layoutRootPane() { // TODO :: FORMAT THIS SHIT TOO

		int xOffset = 5;
		int yOffset = 80;
		int gap = 5;

		// alle GUI-Elemente mï¿½ssen eine Grï¿½ï¿½e zugewiesen bekommen. Soll
		// die
		// Grï¿½ï¿½e automatisch ï¿½ber die Beschriftung des GUI-Elements
		// bestimmt
		// werden, so muss adjustSize() aufgerufen werden.
		nameLabel.adjustSize();
		angleLabel1.adjustSize();
		angleLabel2.adjustSize();
		speedLabel1.adjustSize();
		speedLabel2.adjustSize();

		// Ansonsten wird die Grï¿½ï¿½e manuell mit setSize() gesetzt
		angleInput1.setSize(50, 25);
		angleInput2.setSize(50, 25);
		speedInput1.setSize(50, 25);
		speedInput2.setSize(50, 25);
		dropButton.setSize(50, 25);

		// Nachdem alle Grï¿½ï¿½en adjustiert wurden, muss allen GUI-Elementen
		// eine
		// Position (linke obere Ecke) zugewiesen werden
		nameLabel.setPosition(xOffset, yOffset/2);
		angleLabel1.setPosition(xOffset, yOffset + angleLabel1.getHeight() + gap);
		angleLabel2.setPosition(xOffset, yOffset + angleLabel2.getHeight() + gap);
		angleInput1.setPosition(xOffset + angleLabel1.getWidth() + gap, yOffset + angleLabel1.getHeight() + gap);
		angleInput2.setPosition(xOffset + angleLabel2.getWidth() + gap, yOffset + angleLabel2.getHeight() + gap);

		speedLabel1.setPosition(xOffset, yOffset + angleLabel1.getHeight() + gap + angleLabel1.getHeight() + gap);
		speedLabel2.setPosition(xOffset, yOffset + angleLabel2.getHeight() + gap + angleLabel2.getHeight() + gap);
		speedInput1.setPosition(xOffset + speedLabel1.getWidth() + gap, yOffset + 2*angleLabel1.getHeight() + 2*gap);
		speedInput2.setPosition(xOffset + speedLabel2.getWidth() + gap, yOffset + 2*angleLabel2.getHeight() + 2*gap);

		angleLabel1.setVisible(false);
		angleInput1.setVisible(false);
		speedLabel1.setVisible(false);
		speedInput1.setVisible(false);
		angleLabel2.setVisible(false);
		angleInput2.setVisible(false);
		speedLabel2.setVisible(false);
		speedInput2.setVisible(false);
		
		dropButton.setPosition(xOffset + speedLabel1.getWidth() + gap, yOffset + 2*angleLabel1.getHeight() + 3*gap + speedLabel1.getHeight());
	}

	/**
     * Method gets called if you type something into the input field.
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
	void handleEditFieldInput(int key, EditField editField, Callback callback,
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

	/**
	 * 
	 */
	void inputFinished() { //TODO :: FORMAT THIS SHIT ASWELL. ALSO ADD JAVADOC

		// Banane wird erzeugt
		Entity banana = new Entity("banana");
		//Entity gorilla1 = entityManager.getEntity(stateID, "gorilla1");
		//System.out.println(gorilla1.getID() + "  " + gorilla1.getPosition());
		if (turn)
			banana.setPosition(new Vector2f(gorilla1pos.getX()+15,gorilla1pos.getY()));
		else 
			banana.setPosition(new Vector2f(gorilla2pos.getX()-15,gorilla2pos.getY()));
		try {
			// Bild laden und zuweisen
			banana.addComponent(new ImageRenderComponent(new Image("assets/gorillas/banana.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find file assets/gorillas/banana.png!");
			e.printStackTrace();
		}

		// Banane will geworfen werden
		LoopEvent loop = new LoopEvent();
		// neuer wurf
		Wurf wurf = new Wurf(Integer.parseInt(turn?speedInput1.getText():speedInput2.getText()));
		// winkel wird gesetzt
		wurf.angle = turn?Integer.parseInt(angleInput1.getText()):(180-Integer.parseInt(angleInput2.getText()));
		// x0 und y0 für newtonsche gleichung..
		wurf.startPos = turn?new Vector2f(gorilla1pos.getX()+30,gorilla1pos.getY()-38):new Vector2f(gorilla2pos.getX()-30,gorilla2pos.getY()-38);
		// solange geworfen bis.... kollision // out of bounce
		wurf.wind = this.wind;
		wurf.gravity = Gorillas.options.getG();
		loop.addAction(wurf);
		// banana now rotate; infinite!
		loop.addAction(turn?new RotateRightAction(0.5F):new RotateLeftAction(0.5F));
		// adde loopzeugs zu banana
		banana.addComponent(loop);

		// out of bounce event (banane fliegt aus dem fenster)
		// <---
		Event leavingEvent = new MyLeavingScreenEvent();
		leavingEvent.addAction(new DestroyEntityAction());
		leavingEvent.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {
				daneben();
			}
		});
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

				// zerstï¿½re die Entitï¿½t (dabei wird das der Entitï¿½t
				// zugewiese Zerstï¿½rungs-Pattern benutzt)
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
}
