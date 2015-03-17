package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

public class HighscoreState extends BasicTWLGameState {
	
	private int stateID;
	private StateBasedEntityManager entityManager;

	private Label highscore_Label;
	private Label scores_Label;
	private Label scores1_Label;
	private Label scores2_Label;
	private Label scores3_Label;
	private Label scores4_Label;
	private Label scores5_Label;
	private Label scores6_Label;
	
	public HighscoreState(int sid) {
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
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE)); //
		escListener.addComponent(escPressed);
		entityManager.addEntity(stateID, escListener);
		
		/* Spiel zurück-Entitaet */
		//-------------------------------------------------------
    	Entity zurück_Entity = new Entity("Zurück");
    	
    	// Setze Position und Bildkomponente
    	zurück_Entity.setPosition(new Vector2f(400, 450));
    	zurück_Entity.setScale(0.18f);
    	if (!Gorillas.data.guiDisabled) { // really.... 
    	zurück_Entity.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	}
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_z = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action zurück_Action = new ChangeStateInitAction(Gorillas.MAINMENUSTATE);
    	mainEvents_z.addAction(zurück_Action);
    	zurück_Entity.addComponent(mainEvents_z);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, zurück_Entity);

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);
		
		g.drawString("Zurück", 370, 445);
		
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
		
		/*Intruktion-Label*/
    	highscore_Label = new Label("HighScore:");
    	scores_Label = new Label("Place   Player              Rounds              Won                               Mean accuracy \n");
    	scores1_Label = new Label(Gorillas.data.giveHighscoreAsString(0));
    	scores2_Label = new Label(Gorillas.data.giveHighscoreAsString(1));
    	scores3_Label = new Label(Gorillas.data.giveHighscoreAsString(2));
    	scores4_Label = new Label(Gorillas.data.giveHighscoreAsString(3));
    	scores5_Label = new Label(Gorillas.data.giveHighscoreAsString(4));
    	scores6_Label = new Label(Gorillas.data.giveHighscoreAsString(5));
    	highscore_Label.setPosition(250, 100);
    	scores_Label.setPosition(150, 200);
    	rp.add(highscore_Label);
    	rp.add(scores_Label);
    	rp.add(scores1_Label);
    	rp.add(scores2_Label);
    	rp.add(scores3_Label);
    	rp.add(scores4_Label);
    	rp.add(scores5_Label);
    	rp.add(scores6_Label);
    	
    	return rp;
	}
	
	@Override
	protected void layoutRootPane() {
		highscore_Label.setPosition(350, 40);
		scores_Label.setPosition(150, 120);
    	scores1_Label.setPosition(150, 240);
    	scores2_Label.setPosition(200, 240);
    	scores3_Label.setPosition(320, 240);
    	scores4_Label.setPosition(410, 240);
    	scores5_Label.setPosition(420, 240);
    	scores6_Label.setPosition(560, 240);
	}

}