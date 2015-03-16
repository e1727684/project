package de.tu_darmstadt.gdi1.gorillas.ui.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
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
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.interfaces.IDestructible;

public class GamePlayState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;

	private Label angleLabel;
	EditField angleInput;
	private Label speedLabel;
	EditField speedInput;
	private Button dropButton;
	private Label nameLabel;
	private boolean turn;
	private Vector2f gorilla1pos;
	private Vector2f gorilla2pos;

	public GamePlayState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

        entityManager.addEntity(this.stateID, entityManager.getEntity(0, "background"));

        // Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
        Entity escListener = new Entity("ESC_Listener");
        KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
        escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
        escListener.addComponent(escPressed);
        entityManager.addEntity(stateID, escListener);

        // Hochh�user
        // -------------------------------------------------------------------------------
        turn = true;
        Random rand = new Random();
        int houseWidth = 100;
        int startPointHouses = 0; // Anfangspunkt H�user
        int[] houseHeights = new int[8];
        int housesIndex = 0;

        for (int e = 0; e < 8; e++) {

                houseHeights[housesIndex] = rand.nextInt(380) + 120;
               
                // H�user
                BufferedImage image = new BufferedImage(houseWidth,
                                houseHeights[housesIndex], BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphic = image.createGraphics();
                graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                graphic.setColor(new Color(rand.nextInt(255), rand.nextInt(255),
                                rand.nextInt(255)));
                graphic.fillRect(0, 0, houseWidth, houseHeights[housesIndex]);

                // Fenster
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

                DestructibleImageEntity house = new DestructibleImageEntity(
                                "obstacle", image, "gorillas/destruction.png", false);
                house.setPosition(new Vector2f(startPointHouses, game
                                .getContainer().getHeight()
                                - (houseHeights[housesIndex] / 2)));
                entityManager.addEntity(stateID, house);
               
                housesIndex++;
        }

        // add Gorillas
        // -------------------------------------------------------------------------------
        // two players := two gorillas
        Entity gorilla1 = new Entity("gorilla1");
        Entity gorilla2 = new Entity("gorilla2");
        // two gorillas := two components := two images
        gorilla1.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/gorillas/gorilla.png")));
        gorilla2.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/gorillas/gorilla.png")));
        // creates the random positions for BOTH gorillas! therefore only has to be called ONCE
        randomizeGorillas(game.getContainer().getHeight(), game.getContainer().getWidth(), houseHeights, gorilla1, gorilla2); 
        // give the gorillas their positions
        gorilla1.setPosition(gorilla1pos); 
        gorilla2.setPosition(gorilla2pos); 
        // and add them to the entity manager!
        entityManager.addEntity(this.stateID, gorilla1);
        entityManager.addEntity(this.stateID, gorilla2);
       
        // Entit�t f�r Sonne
        // -------------------------------------------------------------------------------
        Entity sun_smiling = new Entity("sun_smiling");
        sun_smiling.setPosition(new Vector2f(
                        (game.getContainer().getWidth() / 2), 30)); // Startposition des
        // Hintergrunds
        sun_smiling.addComponent(new ImageRenderComponent(new Image(
                        "/assets/gorillas/sun/sun_smiling.png"))); // Bild zur Entit�t
                                                                                                                // hinzuf�gen
        entityManager.addEntity(this.stateID, sun_smiling);
	}
	
	private void randomizeGorillas(int height, int width, int[] houseHeights, Entity gorilla1, Entity gorilla2) {
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
        gorilla1pos = new Vector2f(gorilla1PosX, gorilla1PosY); // Startposition
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
        // still black magic BUT we have our gorilla positions!
        gorilla2pos = new Vector2f(gorilla2PosX, gorilla2PosY); // Startposition
	}
	
	@Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
                    throws SlickException {

            entityManager.renderEntities(container, game, g);
           
            //Name werden am oberen Rand angezeigt
            g.drawString(Gorillas.data.getPlayer1(), 20, 10);
            g.drawString(Gorillas.data.getPlayer2(), 730, 10);
    }

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (entityManager.hasEntity(stateID, "banana")) { // switch the input label to invisible while the banana is flying AND ROTATING
			nameLabel.setVisible(false);
			angleLabel.setVisible(false);
			angleInput.setVisible(false);
			speedLabel.setVisible(false);
			speedInput.setVisible(false);
			dropButton.setVisible(false);
		} else {
			nameLabel.setVisible(true);
			angleLabel.setVisible(true);
			angleInput.setVisible(true);
			speedLabel.setVisible(true);
			speedInput.setVisible(true);
			dropButton.setVisible(true);
		}
		if (turn) // display names so the players know whose turn it is!
			nameLabel.setText("Player 1: "+Gorillas.data.getPlayer1());
		else 
			nameLabel.setText("Player 2: "+Gorillas.data.getPlayer2());
		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public int getID() {
		return stateID;
	}

	/**
	 * In dieser Methode werden in einem BasicTWLGameSate alle GUI-Elemente dem
	 * GameState mit Hilfe einer RootPane hinzugef�gt
	 */
	@Override
	protected RootPane createRootPane() {
		// erstelle die RootPane
		RootPane rp = super.createRootPane();
		nameLabel = new Label("");
		// erstelle ein Label mit der Aufschrift "x:"
		angleLabel = new Label("angle:");
		// erstelle ein EditField. Es dient der Eingabe von Text
		angleInput = new EditField();
		// mit der Methode addCallBack l�sst sich dem EditField ein CallBack
		// hinzuf�gen, in dessen Methode callback(int key) bestimmt wird, was
		// geschehen soll, wenn ein Zeichen eingegeben wird
		angleInput.addCallback(new Callback() {
			public void callback(int key) {
				// in unserem Fall wird der Input in der Methode
				// handleEditFieldInput verarbeitet (siehe weiter unten in
				// dieser Klasse, was diese tut, und was es mit ihren Parametern
				// auf sich hat)
				handleEditFieldInput(key, angleInput, this, 360);
			}
		});

		// analog zu einer Eingabem�glichkeit f�r x-Werte wird auch eine
		// f�r
		// y-Werte kreiert
		speedLabel = new Label("speed:");
		speedInput = new EditField();
		speedInput.addCallback(new Callback() {
			public void callback(int key) {
				handleEditFieldInput(key, speedInput, this, 200);
			}
		});

		// zuletzt wird noch ein Button hinzugef�gt
		dropButton = new Button("throw");
		// �hnlich wie einem EditField kann auch einem Button ein CallBack
		// hinzugef�gt werden
		// Hier ist es jedoch von Typ Runnable, da keine Parameter (z. B. welche
		// Taste wurde gedr�ckt) ben�tigt werden
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
		// am Schluss der Methode m�ssen alle GUI-Elemente der Rootpane
		// hinzugef�gt werden
		rp.add(nameLabel);
		rp.add(angleLabel);
		rp.add(angleInput);

		rp.add(speedLabel);
		rp.add(speedInput);

		rp.add(dropButton);
		// ... und die fertige Rootpane zur�ckgegeben werden
		return rp;
	}

	/**
	 * in dieser Methode des BasicTWLGameState werden die erstellten
	 * GUI-Elemente platziert
	 */
	@Override
	protected void layoutRootPane() {

		int xOffset = 50;
		int yOffset = 50;
		int gap = 5;

		// alle GUI-Elemente m�ssen eine Gr��e zugewiesen bekommen. Soll
		// die
		// Gr��e automatisch �ber die Beschriftung des GUI-Elements
		// bestimmt
		// werden, so muss adjustSize() aufgerufen werden.
		nameLabel.adjustSize();
		angleLabel.adjustSize();
		speedLabel.adjustSize();

		// Ansonsten wird die Gr��e manuell mit setSize() gesetzt
		angleInput.setSize(50, 25);
		speedInput.setSize(50, 25);
		dropButton.setSize(50, 25);

		// Nachdem alle Gr��en adjustiert wurden, muss allen GUI-Elementen
		// eine
		// Position (linke obere Ecke) zugewiesen werden
		nameLabel.setPosition(xOffset, yOffset);
		angleLabel.setPosition(xOffset, yOffset + angleLabel.getHeight() + gap);
		angleInput.setPosition(xOffset + angleLabel.getWidth() + gap, yOffset + angleLabel.getHeight() + gap);

		speedLabel.setPosition(xOffset, yOffset + angleLabel.getHeight() + gap + angleLabel.getHeight() + gap);
		speedInput.setPosition(xOffset + speedLabel.getWidth() + gap,
				yOffset + 2*angleLabel.getHeight() + 2*gap);

		dropButton.setPosition(xOffset + speedLabel.getWidth() + gap, yOffset
				+ 2*angleLabel.getHeight() + 3*gap + speedLabel.getHeight());
	}

	/**
	 * Diese Methode wird aufgerufen, wenn ein Zeichen in ein EditField
	 * eingegeben wurde.
	 * 
	 * @param key
	 *            die gedr�ckte Taste
	 * @param editField
	 *            das EditField, in das ein Zeichen eingef�gt wurde
	 * @param callback
	 *            der CallBack, der dem EditField hinzugef�gt wurde
	 * @param maxValue
	 *            die gr��te Zahl, die in das <code>editField</code>
	 *            eingegeben werden kann
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
	 * diese Methode wird bei Klick auf den Button ausgef�hrt
	 */
	void inputFinished() {

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
			banana.addComponent(new ImageRenderComponent(new Image(
					"assets/gorillas/banana.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find file assets/gorillas/banana.png!");
			e.printStackTrace();
		}

		// Banane wird verworfen
		LoopEvent loop = new LoopEvent();
		// neuer wurf
		Wurf wurf = new Wurf(Integer.parseInt(speedInput.getText()));
		// winkel wird gesetzt
		wurf.angle = turn?Integer.parseInt(angleInput.getText()):(180-Integer.parseInt(angleInput.getText()));
		// x0 und y0 f�r newtonsche gleichung..
		wurf.startPos = turn?new Vector2f(gorilla1pos.getX()+45,gorilla1pos.getY()-25):new Vector2f(gorilla2pos.getX()-45,gorilla2pos.getY()-25);
		// solange geworfen bis.... kollision // out of bounce
		loop.addAction(wurf);
		// banana now rotate; infinite!
		loop.addAction(turn?new RotateRightAction(0.5F):new RotateLeftAction(0.5F));
		// adde loopzeugs zu banana
		banana.addComponent(loop);
		
		// out of bounce event (banane fliegt aus dem fenster)
		// <---
		Event leavingEvent = new LeavingScreenEvent();
		leavingEvent.addAction(new DestroyEntityAction());
		banana.addComponent(leavingEvent);
		// --->
		
		// collision event (banane trifft auf etwas auf)
		// <---
		Event collisionEvent = new CollisionEvent();
		collisionEvent.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {

				// hole die Entity, mit der kollidiert wurde
				CollisionEvent collider = (CollisionEvent) event;
				Entity entity = collider.getCollidedEntity();

				// wenn diese durch ein Pattern zerst�rt werden kann, dann
				// caste
				// zu IDestructible
				// ansonsten passiert bei der Kollision nichts
				IDestructible destructible = null;
				if (entity instanceof IDestructible) {
					destructible = (IDestructible) entity;
				} else {
					return;
				}

				// zerst�re die Entit�t (dabei wird das der Entit�t
				// zugewiese Zerst�rungs-Pattern benutzt)
				destructible.impactAt(event.getOwnerEntity().getPosition());
			}
		});
		collisionEvent.addAction(new DestroyEntityAction());
		banana.addComponent(collisionEvent);
		// --->
		
		// turn wechselt von spieler 1 auf 2 oder umgekehrt
		turn = !turn;
		
		// banane darf endlich fliegen und rotieren!!!
		entityManager.addEntity(stateID, banana);
	}

	// H�user zeichnen
	public void drawHouses(StateBasedGame g) {
		// Hochh�user
		Random rand = new Random();
		int a,b;
		int c = 0;		//Anfangspunkt H�user
	
		// int n = rand.nextInt(50) + 1;
		for (int e = 0; e < 9; e++) {
			
			a = rand.nextInt(50) + 80;
			b = rand.nextInt(200) + 100;
			
			// Hochh�user
			BufferedImage image = new BufferedImage(a, b, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
			graphic.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
			graphic.fillRect(0, 0, a, b);

			// Fenster
			graphic.setColor(new Color(0, 0, 0));
			for (int i = 5; i < b; i = i + 20) {
				for (int j = 5; j < a; j = j + 20) {
					graphic.fillRect(j, i, 7, 10);
				}
			}

			DestructibleImageEntity obstacle1 = new DestructibleImageEntity(
					"obstacle", image, "dropofwater/destruction.png", false);
			obstacle1.setPosition(new Vector2f(c, 
					g.getContainer().getHeight() - (b/2)));
			entityManager.addEntity(stateID, obstacle1);
			
			c = c + a;
		}
	}

}
