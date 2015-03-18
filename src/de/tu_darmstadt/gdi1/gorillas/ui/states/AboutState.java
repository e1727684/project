package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

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
public class AboutState extends BasicTWLGameState {
	
	private int stateID;
	private StateBasedEntityManager entityManager;
	
	public AboutState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
        Action buttonPressed = new Action() {@Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {MusicPlayer.playButton();}};
		
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Entity escListener = new Entity("ESC_Listener");
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
		escListener.addComponent(escPressed);
		entityManager.addEntity(stateID, escListener);
		
		/* Spiel zur�ck-Entitaet */
		//-------------------------------------------------------
    	Entity zur�ck_Entity = new Entity("Back");
    	
    	// Setze Position und Bildkomponente
    	zur�ck_Entity.setPosition(new Vector2f(400, 450));
    	zur�ck_Entity.setScale(0.18f);
		Entity background = new Entity("aboutBack");
		background.setPosition(new Vector2f(400,300));
    	if (!Gorillas.data.guiDisabled) { // really.... 
        	background.addComponent(new ImageRenderComponent(new Image("assets/gorillas/backgrounds/backgroundAbout.png")));
        	zur�ck_Entity.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	}
        entityManager.addEntity(this.stateID, background);
    	// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents_z = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action zur�ck_Action = new ChangeStateInitAction(Gorillas.MAINMENUSTATE);
    	mainEvents_z.addAction(zur�ck_Action);
    	mainEvents_z.addAction(buttonPressed);
    	zur�ck_Entity.addComponent(mainEvents_z);
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(this.stateID, zur�ck_Entity);
				
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);
		
		g.drawString("Zur�ck", 370, 445); 
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
		//about_Label = new Label("13. Januar 2015.\nDie Projektgruppe trifft sich zum allerersten Mal.\nDas Thema? Unbekannt.\nDas Ziel? Unbekannt.\nDie Java-Kentnisse der Gruppenmitglieder? Unbekannt.\n\n\n04. Februar 2015.\nDas Thema wird bekanntgegeben!\nDas Abenteuer kann beginnen. Es ist... \nEine Hommage auf das Spiel Gorillas von 1991.\n\nEntwickler:\n ~ Deniz Tobias Buruncuk\n ~ Dennis Hasenstab\n ~ Marcel Dieter\n ~ Philip Stauder");
    	//rp.add(about_Label);
    	
    	return rp;
	}
	
	@Override
	protected void layoutRootPane() {
		//about_Label.setPosition(50, 220);
	}

}
