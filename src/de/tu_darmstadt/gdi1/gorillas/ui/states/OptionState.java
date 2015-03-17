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
import de.tu_darmstadt.gdi1.gorillas.ui.widgets.valueadjuster.AdvancedValueAdjusterInt;
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

public class OptionState extends BasicTWLGameState {
	
	private int stateID;
	private StateBasedEntityManager entityManager;
	private final int distance = 80;
    private final int start_Position = 80;
	
	public OptionState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		float scale = 0.18f;
		int offset = 90;
		Entity mbackground = new Entity("menubackground");// Entität für Hintergrund
    	Entity musicButton = new Entity("music");
    	Entity windButton = new Entity("wind");
    	Entity spottButton = new Entity("spott");
		mbackground.setPosition(new Vector2f(400,300));		
        Action buttonPressed = new Action() {@Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {MusicPlayer.playButton();}};
		Entity escListener = new Entity("ESC_Listener");
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE)); //
		escListener.addComponent(escPressed);
    	Entity zurück_Entity = new Entity("Zurück");
    	if (!Gorillas.data.guiDisabled) { // really.... 
    	mbackground.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/backgroundMain.png")));
    	musicButton.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	windButton.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	spottButton.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	zurück_Entity.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	}
		entityManager.addEntity(this.stateID, mbackground);
    	ANDEvent mainEvents_z = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action zurück_Action = new ChangeStateInitAction(Gorillas.MAINMENUSTATE);
    	mainEvents_z.addAction(buttonPressed);
    	mainEvents_z.addAction(zurück_Action);
    	zurück_Entity.addComponent(mainEvents_z);
    	
    	musicButton.setPosition(new Vector2f(218, offset));
    	windButton.setPosition(new Vector2f(218, offset+distance));
    	spottButton.setPosition(new Vector2f(218, offset+distance*2));
    	zurück_Entity.setPosition(new Vector2f(218, offset+distance*5));
    	musicButton.setScale(scale);
    	windButton.setScale(scale);
    	spottButton.setScale(scale);
    	zurück_Entity.setScale(scale);
    	ANDEvent mButton = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	ANDEvent wButton = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	ANDEvent sButton = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	mButton.addAction(buttonPressed);
    	mButton.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {
				Gorillas.options.setMusicEnabled(!Gorillas.options.isMusicEnabled());
				if (Gorillas.options.isMusicEnabled())
					MusicPlayer.playBg();
				if (!Gorillas.options.isMusicEnabled())
					MusicPlayer.stopBg();
			}});
    	wButton.addAction(buttonPressed);
    	wButton.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {
				Gorillas.options.setWindEnabled(!Gorillas.options.isWindEnabled());
			}});
    	sButton.addAction(buttonPressed);
    	sButton.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {
				Gorillas.options.setSpottEnabled(!Gorillas.options.isSpottEnabled());
			}});
    	musicButton.addComponent(mButton);
    	windButton.addComponent(wButton);
    	spottButton.addComponent(sButton);
    	entityManager.addEntity(stateID, musicButton);
    	entityManager.addEntity(stateID, windButton);
    	entityManager.addEntity(stateID, spottButton);
		entityManager.addEntity(stateID, escListener);
    	entityManager.addEntity(this.stateID, zurück_Entity);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);

		g.drawString("Music: "+Gorillas.options.isMusicEnabled(), 160, start_Position);
		g.drawString("Wind: "+Gorillas.options.isWindEnabled(), 160, start_Position+1*distance);
		g.drawString("Spott: "+Gorillas.options.isSpottEnabled(), 160, start_Position+2*distance); 
		g.drawString("Zurück", 160, start_Position+5*distance);
		
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
}