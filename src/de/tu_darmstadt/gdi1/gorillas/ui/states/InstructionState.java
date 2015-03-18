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
import de.tu_darmstadt.gdi1.gorillas.util.MusicPlayer;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 */
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


		Entity background = new Entity("aboutBack");
		background.setPosition(new Vector2f(400,300));
    	if (!Gorillas.data.guiDisabled) { // really.... 
        	background.addComponent(new ImageRenderComponent(new Image("assets/gorillas/backgrounds/backgroundInstruction.png")));
    	}
        entityManager.addEntity(this.stateID, background);
        Action buttonPressed = new Action() {@Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {MusicPlayer.playButton();}};
		
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
    	mainEvents_z.addAction(buttonPressed);
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
    	instruction_Label = new Label("Erinnerst Du Dich noch an das QBasic-Spiel Gorilla? \n\nEs war das klassische Spiel, in dem hoch oben auf Wolkenkratzern zwei Gorillas gegen den Tod kämpften. \n\nBeschieße Deinen Gegner mit hochexplosiven Bananen. \n\nDu musst jedoch den richtigen Schusswinkel sowie die Geschwindigkeit herausfinden, \num ihn zu treffen und zu verletzen. \nAuch Wind spielt eine Rolle und muss beachtet werden. \n\nDas Spiel kann nur zu zweit gespielt werden. \n\nWenn man im ''Neues Spiel Starten''-Fenster eine positive Zahl eingibt,\ndann spielt man bis einer die eingegebene Punktzahl erreicht. \nGibt man eine negative Zahl ein, spielt man eine feste Anzahl an Runden. \n\nDuelliere dich gegen Deinen Freund oder Kollegen und sei der bessere Gorilla.");
    	rp.add(instruction_Label);
    	
    	return rp;
	}
	
	@Override
	protected void layoutRootPane() {
		instruction_Label.setPosition(50, 200);
	}

}