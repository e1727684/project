package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

public class GameSetupState extends BasicTWLGameState  {
	
	private int stateID;
	private StateBasedEntityManager entityManager;
	
	private Label player1_Label;
	EditField player1_Input;
	private Label player2_Label;
	EditField player2_Input;

	public GameSetupState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		//Entität für Hintergrund
		Entity background = new Entity("gamesetup");
		background.setPosition(new Vector2f(400,300));														//Startposition des Hintergrunds
		background.addComponent(new ImageRenderComponent(new Image("/assets/dropofwater/background.png")));		//Bild zur Entität hinzufügen
		entityManager.addEntity(this.stateID,  background);	
		
		/* Spiel starten-Entitaet */
		//-------------------------------------------------------
    	Entity startGame_Entity = new Entity("Spiel starten");
    	
    	// Setze Position und Bildkomponente
    	startGame_Entity.setPosition(new Vector2f(400, 450));
    	startGame_Entity.setScale(0.28f);
    	startGame_Entity.addComponent(new ImageRenderComponent(new Image("assets/dropofwater/entry.png")));
    	
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_s = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action startGame_Action = new ChangeStateInitAction(Gorillas.GAMEPLAYSTATE);
    	mainEvents_s.addAction(startGame_Action);
    	startGame_Entity.addComponent(mainEvents_s);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, startGame_Entity);
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
	
	
	@Override
	protected RootPane createRootPane() {

		// erstelle die RootPane
		RootPane rp = super.createRootPane();
		
		player1_Label = new Label("Spieler1:");
		player1_Input = new EditField();
		
		/*player1_Input.addCallback(new Callback() {
			public void callback(int key) {
				handleEditFieldInput(key, player1_Input, this, 1000);
			}
		});*/
		
		player2_Label = new Label("Spieler2:");
		player2_Input = new EditField();
		
		/*player2_Input.addCallback(new Callback() {
			public void callback(int key) {
				handleEditFieldInput(key, player1_Input, this, 1000);
			}
		});*/
		
		rp.add(player1_Label);
		rp.add(player1_Input);
		rp.add(player2_Label);
		rp.add(player2_Input);
		
		return rp;
	}
	
	@Override
	protected void layoutRootPane() {

		int xOffset = 120;
		int yOffset = 300;
		
		// Ansonsten wird die Grï¿½ï¿½e manuell mit setSize() gesetzt
		player1_Input.setSize(250, 40);
		player2_Input.setSize(250, 40);

		// Nachdem alle Grï¿½ï¿½en adjustiert wurden, muss allen GUI-Elementen eine
		// Position (linke obere Ecke) zugewiesen werden
		player1_Label.setPosition(xOffset, yOffset);
		player1_Input.setPosition(xOffset, yOffset + 30);

		player2_Label.setPosition(xOffset + 300, yOffset);
		player2_Input.setPosition(xOffset + 300, yOffset + 30);

	}
	
	void handleEditFieldInput(int key, EditField editField,
			Callback callback, int maxValue) {

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
				editField.setText(inputText.substring(0, inputText.length() - 1));
				editField.addCallback(callback);
			}
		}
	}

}