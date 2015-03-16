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
				float scale = 0.28f;
				int offsety = 100;
				int starty = 90;
				ImageRenderComponent buttonImage = new ImageRenderComponent(new Image("assets/dropofwater/entry.png"));
				
				Entity background = new Entity("background");// Entit�t f�r Hintergrund
		    	Entity instructions_Entity = new Entity("Einstellungen");// Beenden-Entitaet
		    	Entity new_Game_Entity = new Entity("Neues Spiel starten");// Neues Spiel starten-Entitaet
		    	Entity about_Entity = new Entity("About");// About-Entit�t
		    	Entity highscore_Entity = new Entity("Highscore");// Highscore-Entitaet
		    	Entity quit_Entity = new Entity("Beenden");// Beenden-Entitaet
				
		    	// Setze Positionen
				// <---
				background.setPosition(new Vector2f(400,300));		
		    	new_Game_Entity.setPosition(new Vector2f(218, starty));
		    	instructions_Entity.setPosition(new Vector2f(218, starty+offsety));
		    	about_Entity.setPosition(new Vector2f(218, starty+offsety*2));
		    	highscore_Entity.setPosition(new Vector2f(218, starty+offsety*3));
		    	quit_Entity.setPosition(new Vector2f(218, starty+offsety*4));
				// --->
				
		    	// Setze Skalierungen
				// <---
		    	new_Game_Entity.setScale(scale);
		    	instructions_Entity.setScale(scale);
		    	about_Entity.setScale(scale);
		    	highscore_Entity.setScale(scale);
		    	quit_Entity.setScale(scale);
				// --->
				
		    	// F�ge Bilder hinzu
				// <---
				background.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/background.png")));
		    	new_Game_Entity.addComponent(buttonImage);
		    	instructions_Entity.addComponent(buttonImage);
		    	about_Entity.addComponent(buttonImage);
		    	highscore_Entity.addComponent(buttonImage);
		    	quit_Entity.addComponent(buttonImage);
				// --->
		    	
		    	// Erstelle die Events und f�ge die zugeh�rigen Aktionen hinzu
				// <---
		    	ANDEvent mainEvents_n = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		    	ANDEvent mainEvents_i = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		    	ANDEvent mainEvents_a = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		    	ANDEvent mainEvents_h = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		    	ANDEvent mainEvents_q = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		    	Action new_Game_Action = new ChangeStateInitAction(Gorillas.GAMESETUPSTATE);
		    	Action instructions_Action = new ChangeStateAction(Gorillas.INSTRUCTIONSTATE);
		    	Action about_Action = new ChangeStateAction(Gorillas.ABOUTSTATE);
		    	Action highscore_Action = new ChangeStateAction(Gorillas.HIGHSCORESTATE);
		    	Action quit_Action = new QuitAction();
		    	mainEvents_n.addAction(new_Game_Action);
		    	mainEvents_i.addAction(instructions_Action);
		    	mainEvents_a.addAction(about_Action);
		    	mainEvents_h.addAction(highscore_Action);
		    	mainEvents_q.addAction(quit_Action);
		    	new_Game_Entity.addComponent(mainEvents_n);
		    	instructions_Entity.addComponent(mainEvents_i);
		    	about_Entity.addComponent(mainEvents_a);
		    	highscore_Entity.addComponent(mainEvents_h);
		    	quit_Entity.addComponent(mainEvents_q);
				// --->
		    	
		    	// Fuege die Entities zum StateBasedEntityManager hinzu
		    	entityManager.addEntity(this.stateID, quit_Entity);
		    	entityManager.addEntity(this.stateID, highscore_Entity);
		    	entityManager.addEntity(this.stateID, about_Entity);
		    	entityManager.addEntity(this.stateID, instructions_Entity);
		    	entityManager.addEntity(this.stateID, new_Game_Entity);
				entityManager.addEntity(this.stateID, background);
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

