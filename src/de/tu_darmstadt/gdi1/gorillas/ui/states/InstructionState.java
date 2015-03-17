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

public class InstructionState extends BasicTWLGameState {
	
	private int stateID;
	private StateBasedEntityManager entityManager;
	
	private Label instruction_Label;
	
	public InstructionState(int sid) {
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
		
		/* Spiel zur�ck-Entitaet */
		//-------------------------------------------------------
    	Entity zur�ck_Entity = new Entity("Zur�ck");
    	
    	// Setze Position und Bildkomponente
    	zur�ck_Entity.setPosition(new Vector2f(400, 470));
    	zur�ck_Entity.setScale(0.18f);
    	if (!Gorillas.data.guiDisabled) { // really.... 
    	zur�ck_Entity.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	}
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_z = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action zur�ck_Action = new ChangeStateInitAction(Gorillas.MAINMENUSTATE);
    	mainEvents_z.addAction(zur�ck_Action);
    	zur�ck_Entity.addComponent(mainEvents_z);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, zur�ck_Entity);

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);
		
		g.drawString("Zur�ck", 370, 465);
		
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
    	instruction_Label = new Label("Erinnerst Du Dich noch an das QBasic-Spiel Gorilla? \n\nEs war das klassische Spiel, in dem hoch oben auf Wolkenkratzern zwei Gorillas gegen den Tod k�mpften. \n\nBeschie�e Deinen Gegner mit hochexplosiven Bananen. \n\nDu musst jedoch den richtigen Schusswinkel sowie die Geschwindigkeit herausfinden, \num ihn zu treffen und zu verletzen. \nAuch Wind spielt eine Rolle und muss beachtet werden. \n\nDas Spiel kann nur zu zweit gespielt werden. \n\nWenn man im ''Neues Spiel Starten''-Fenster eine positive Zahl eingibt,\ndann spielt man bis einer die eingegebene Punktzahl erreicht. \nGibt man eine negative Zahl ein, spielt man eine feste Anzahl an Runden. \n\nDuelliere dich gegen Deinen Freund oder Kollegen und sei der bessere Gorilla.");
    	rp.add(instruction_Label);
    	
    	return rp;
	}
	
	@Override
	protected void layoutRootPane() {
		instruction_Label.setPosition(50, 220);
	}

}