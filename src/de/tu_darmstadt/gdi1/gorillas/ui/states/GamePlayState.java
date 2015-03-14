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
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.DestructibleImageEntity;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.interfaces.IDestructible;

public class GamePlayState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;

	private Label xLabel;
	EditField xInput;
	private Label yLabel;
	EditField yInput;
	private Button dropButton;

	public GamePlayState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		// Entität für Hintergrund
        Entity background = new Entity("gamesetup");
        background.setPosition(new Vector2f(400, 300)); // Startposition des
                                                                                                        // Hintergrunds
        background.addComponent(new ImageRenderComponent(new Image(
                        "/assets/gorillas/background.png"))); // Bild zur Entität
                                                                                                                // hinzufügen
        entityManager.addEntity(this.stateID, background);

        // Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
        Entity escListener = new Entity("ESC_Listener");
        KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
        escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));//TODO: PauseState
        escListener.addComponent(escPressed);
        entityManager.addEntity(stateID, escListener);

        // Hochhäuser
        // -------------------------------------------------------------------------------
        Random rand = new Random();
        int houseWidth = 100;
        int startPointHouses = 0; // Anfangspunkt Häuser
        int[] houseHeights = new int[8];
        int housesIndex = 0;

        for (int e = 0; e < 8; e++) {

                houseHeights[housesIndex] = rand.nextInt(380) + 120;
               
                // Häuser
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
                                "obstacle", image, "dropofwater/destruction.png", false);
                house.setPosition(new Vector2f(startPointHouses, game
                                .getContainer().getHeight()
                                - (houseHeights[housesIndex] / 2)));
                entityManager.addEntity(stateID, house);
               
                housesIndex++;
        }

        // add Gorillas
        // -------------------------------------------------------------------------------
        float gorillaPosX = 0;
        float gorillaPosY = 0;

        Entity gorilla1 = new Entity("gorilla1");
        gorilla1.addComponent(new ImageRenderComponent(new Image(
                        "/assets/gorillas/gorillas/gorilla.png")));
       
        switch (rand.nextInt(3)) {
        case 0:
                gorillaPosX = 50;
                gorillaPosY = game.getContainer().getHeight() - (houseHeights[0] + (gorilla1.getSize().y/2));
                break;
        case 1:
                gorillaPosX = 150;
                gorillaPosY = game.getContainer().getHeight() - (houseHeights[1] + (gorilla1.getSize().y/2));
                break;
        case 2:
                gorillaPosX = 250;
                gorillaPosY = game.getContainer().getHeight() - (houseHeights[2] + (gorilla1.getSize().y/2));
                break;
        }

        gorilla1.setPosition(new Vector2f(gorillaPosX, gorillaPosY)); // Startposition
        entityManager.addEntity(this.stateID, gorilla1);

        //Gorilla2
        Entity gorilla2 = new Entity("gorilla2");
        gorilla2.addComponent(new ImageRenderComponent(new Image(
                        "/assets/gorillas/gorillas/gorilla.png")));
       
        switch (rand.nextInt(3)) {
        case 0:
                gorillaPosX = game.getContainer().getWidth() - 50;
                gorillaPosY = game.getContainer().getHeight() - (houseHeights[7] + (gorilla2.getSize().y/2));
                break;
        case 1:
                gorillaPosX = game.getContainer().getWidth() - 150;
                gorillaPosY = game.getContainer().getHeight() - (houseHeights[6] + (gorilla2.getSize().y/2));
                break;
        case 2:
                gorillaPosX = game.getContainer().getWidth() - 250;
                gorillaPosY = game.getContainer().getHeight() - (houseHeights[5] + (gorilla2.getSize().y/2));
                break;
        }

        gorilla2.setPosition(new Vector2f(gorillaPosX, gorillaPosY)); // Startposition
        entityManager.addEntity(this.stateID, gorilla2);

       
        // Entität für Sonne
        // -------------------------------------------------------------------------------
        Entity sun_smiling = new Entity("sun_smiling");
        sun_smiling.setPosition(new Vector2f(
                        (game.getContainer().getWidth() / 2), 30)); // Startposition des
        // Hintergrunds
        sun_smiling.addComponent(new ImageRenderComponent(new Image(
                        "/assets/gorillas/sun/sun_smiling.png"))); // Bild zur Entität
                                                                                                                // hinzufügen
        entityManager.addEntity(this.stateID, sun_smiling);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		entityManager.renderEntities(container, game, g);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public int getID() {
		return stateID;
	}

	/**
	 * In dieser Methode werden in einem BasicTWLGameSate alle GUI-Elemente dem
	 * GameState mit Hilfe einer RootPane hinzugefï¿½gt
	 */
	@Override
	protected RootPane createRootPane() {

		// erstelle die RootPane
		RootPane rp = super.createRootPane();

		// erstelle ein Label mit der Aufschrift "x:"
		xLabel = new Label("angle:");
		// erstelle ein EditField. Es dient der Eingabe von Text
		xInput = new EditField();
		// mit der Methode addCallBack lï¿½sst sich dem EditField ein CallBack
		// hinzufï¿½gen, in dessen Methode callback(int key) bestimmt wird, was
		// geschehen soll, wenn ein Zeichen eingegeben wird
		xInput.addCallback(new Callback() {
			public void callback(int key) {
				// in unserem Fall wird der Input in der Methode
				// handleEditFieldInput verarbeitet (siehe weiter unten in
				// dieser Klasse, was diese tut, und was es mit ihren Parametern
				// auf sich hat)
				handleEditFieldInput(key, xInput, this, 360);
			}
		});

		// analog zu einer Eingabemï¿½glichkeit fï¿½r x-Werte wird auch eine
		// fï¿½r
		// y-Werte kreiert
		yLabel = new Label("speed:");
		yInput = new EditField();
		yInput.addCallback(new Callback() {
			public void callback(int key) {
				handleEditFieldInput(key, yInput, this, 200);
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
		rp.add(xLabel);
		rp.add(xInput);

		rp.add(yLabel);
		rp.add(yInput);

		rp.add(dropButton);

		// ... und die fertige Rootpane zurï¿½ckgegeben werden
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

		// alle GUI-Elemente mï¿½ssen eine Grï¿½ï¿½e zugewiesen bekommen. Soll
		// die
		// Grï¿½ï¿½e automatisch ï¿½ber die Beschriftung des GUI-Elements
		// bestimmt
		// werden, so muss adjustSize() aufgerufen werden.
		xLabel.adjustSize();
		yLabel.adjustSize();

		// Ansonsten wird die Grï¿½ï¿½e manuell mit setSize() gesetzt
		xInput.setSize(50, 25);
		yInput.setSize(50, 25);
		dropButton.setSize(50, 25);

		// Nachdem alle Grï¿½ï¿½en adjustiert wurden, muss allen GUI-Elementen
		// eine
		// Position (linke obere Ecke) zugewiesen werden
		xLabel.setPosition(xOffset, yOffset);
		xInput.setPosition(xOffset + xLabel.getWidth() + gap, yOffset);

		yLabel.setPosition(xOffset, yOffset + xLabel.getHeight() + gap);
		yInput.setPosition(xOffset + yLabel.getWidth() + gap,
				yOffset + xLabel.getHeight() + gap);

		dropButton.setPosition(xOffset + yLabel.getWidth() + gap, yOffset
				+ xLabel.getHeight() + gap + yLabel.getHeight() + gap);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn ein Zeichen in ein EditField
	 * eingegeben wurde.
	 * 
	 * @param key
	 *            die gedrï¿½ckte Taste
	 * @param editField
	 *            das EditField, in das ein Zeichen eingefï¿½gt wurde
	 * @param callback
	 *            der CallBack, der dem EditField hinzugefï¿½gt wurde
	 * @param maxValue
	 *            die grï¿½ï¿½te Zahl, die in das <code>editField</code>
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
	 * diese Methode wird bei Klick auf den Button ausgefï¿½hrt
	 */
	void inputFinished() {

		// Wassertropfen wird erzeugt
		Entity drop = new Entity("drop of water");
		drop.setPosition(new Vector2f(Integer.parseInt(xInput.getText()),
				Integer.parseInt(yInput.getText())));

		try {
			// Bild laden und zuweisen
			drop.addComponent(new ImageRenderComponent(new Image(
					"assets/dropofwater/drop.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find file assets/dropofwater/drop.png!");
			e.printStackTrace();
		}

		// Wassertropfen faellt nach unten
		LoopEvent loop = new LoopEvent();
		loop.addAction(new MoveDownAction(0.5f));
		drop.addComponent(loop);

		Event collisionEvent = new CollisionEvent();
		collisionEvent.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {

				// hole die Entity, mit der kollidiert wurde
				CollisionEvent collider = (CollisionEvent) event;
				Entity entity = collider.getCollidedEntity();

				// wenn diese durch ein Pattern zerstï¿½rt werden kann, dann
				// caste
				// zu IDestructible
				// ansonsten passiert bei der Kollision nichts
				IDestructible destructible = null;
				if (entity instanceof IDestructible) {
					destructible = (IDestructible) entity;
				} else {
					return;
				}

				// zerstï¿½re die Entitï¿½t (dabei wird das der Entitï¿½t
				// zugewiese Zerstï¿½rungs-Pattern benutzt)
				destructible.impactAt(event.getOwnerEntity().getPosition());
			}
		});
		collisionEvent.addAction(new DestroyEntityAction());
		drop.addComponent(collisionEvent);

		entityManager.addEntity(stateID, drop);
	}

	// Häuser zeichnen
	public void drawHouses(StateBasedGame g) {
		// Hochhäuser
		Random rand = new Random();
		int a,b;
		int c = 0;		//Anfangspunkt Häuser
	
		// int n = rand.nextInt(50) + 1;
		for (int e = 0; e < 9; e++) {
			
			a = rand.nextInt(50) + 80;
			b = rand.nextInt(200) + 100;
			
			// Hochhäuser
			BufferedImage image = new BufferedImage(a, b,
					BufferedImage.TYPE_INT_ARGB);
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