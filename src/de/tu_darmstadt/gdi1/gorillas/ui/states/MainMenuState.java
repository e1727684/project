package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Image;

import de.matthiasmann.twl.slick.BasicTWLGameState;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;

public class MainMenuState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;
	
	private final int distance = 100;
    private final int start_Position = 80;

	public MainMenuState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		//Entität für Hintergrund
		Entity background = new Entity("menu");
		background.setPosition(new Vector2f(400,300));														//Startposition des Hintergrunds
		background.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/background.png")));		//Bild zur Entität hinzufügen
		entityManager.addEntity(this.stateID,  background);														//Hintergrund-Entität an StateBasedEntityManager übergeben
		
		/* Neues Spiel starten-Entitaet */
		//-------------------------------------------------------
    	String new_Game = "Neues Spiel starten";
    	Entity new_Game_Entity = new Entity(new_Game);
    	
    	// Setze Position und Bildkomponente
    	new_Game_Entity.setPosition(new Vector2f(218, 90));
    	new_Game_Entity.setScale(0.28f);
    	new_Game_Entity.addComponent(new ImageRenderComponent(new Image("assets/dropofwater/entry.png")));
    	
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_n = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action new_Game_Action = new ChangeStateInitAction(Gorillas.GAMESETUPSTATE);
    	mainEvents_n.addAction(new_Game_Action);
    	new_Game_Entity.addComponent(mainEvents_n);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, new_Game_Entity);
    	
    	/* Beenden-Entitaet */
    	//-------------------------------------------------------
    	Entity instructions_Entity = new Entity("Beenden");
    	
    	// Setze Position und Bildkomponente
    	instructions_Entity.setPosition(new Vector2f(218, 190));
    	instructions_Entity.setScale(0.28f);
    	instructions_Entity.addComponent(new ImageRenderComponent(new Image("assets/dropofwater/entry.png")));
    	
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_i = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action instructions_Action = new ChangeStateAction(Gorillas.INSTRUCTIONSTATE);
    	mainEvents_i.addAction(instructions_Action);
    	instructions_Entity.addComponent(mainEvents_i);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, instructions_Entity);
    	   	
    	/* About-Entität*/
    	//-------------------------------------------------------
    	Entity about_Entity = new Entity("About");
    	
    	// Setze Position und Bildkomponente
    	about_Entity.setPosition(new Vector2f(218, 290));
    	about_Entity.setScale(0.28f);
    	about_Entity.addComponent(new ImageRenderComponent(new Image("assets/dropofwater/entry.png")));
    	
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_a = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action about_Action = new ChangeStateAction(Gorillas.ABOUTSTATE);
    	mainEvents_a.addAction(about_Action);
    	about_Entity.addComponent(mainEvents_a);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, about_Entity);
    	    	
    	/* Highscore-Entitaet */
    	//-------------------------------------------------------
    	Entity highscore_Entity = new Entity("Beenden");
    	
    	// Setze Position und Bildkomponente
    	highscore_Entity.setPosition(new Vector2f(218, 390));
    	highscore_Entity.setScale(0.28f);
    	highscore_Entity.addComponent(new ImageRenderComponent(new Image("assets/dropofwater/entry.png")));
    	
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_h = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action highscore_Action = new QuitAction();
    	mainEvents_h.addAction(highscore_Action);
    	highscore_Entity.addComponent(mainEvents_h);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, highscore_Entity);
    	
    	
    	/* Beenden-Entitaet */
    	//-------------------------------------------------------
    	Entity quit_Entity = new Entity("Beenden");
    	
    	// Setze Position und Bildkomponente
    	quit_Entity.setPosition(new Vector2f(218, 490));
    	quit_Entity.setScale(0.28f);
    	quit_Entity.addComponent(new ImageRenderComponent(new Image("assets/dropofwater/entry.png")));
    	
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_q = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action quit_Action = new QuitAction();
    	mainEvents_q.addAction(quit_Action);
    	quit_Entity.addComponent(mainEvents_q);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, quit_Entity);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		entityManager.renderEntities(container, game, g);
		
		int counter = 0;
		g.drawString("Neues Spiel", 110, start_Position+counter*distance); counter++;
		g.drawString("Instructions", 110, start_Position+counter*distance); counter++;
		g.drawString("About", 110, start_Position+counter*distance); counter++;
		g.drawString("Highscore", 110, start_Position+counter*distance); counter++;
		g.drawString("Beenden", 110, start_Position+counter*distance); counter++;
	}

	@Override
	public int getID() {
		return stateID;
	}

}

