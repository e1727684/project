package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
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
public class OptionState extends BasicTWLGameState {
	
	private int stateID;
	private StateBasedEntityManager entityManager;
	private final int distance = 80;
    private final int start_Position = 80;
    EditField g_Input;
    Button gButton;

	/**
	 * The constructor. Creates a new state.
	 * 
	 * @param sid 
	 * 				this state's id. it can be identified by it and is unique!
	 */
	public OptionState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		float scale = 0.18f;
		int offset = 90;
		
		// Required Entities
		// <---
		Entity mbackground = new Entity("menubackground");// Entität für Hintergrund
    	Entity musicButton = new Entity("music");
    	Entity sfxButton = new Entity("sfx");
    	Entity windButton = new Entity("wind");
    	Entity spottButton = new Entity("spott");
    	Entity zurück_Entity = new Entity("Zurück");
		Entity escListener = new Entity("ESC_Listener");
    	// --->
		
		// Giving the Entities a picture.... If we aren't testing!
		// <---
    	if (!Gorillas.data.guiDisabled) {
    	mbackground.addComponent(new ImageRenderComponent(new Image("/assets/gorillas/backgrounds/backgroundMain.png")));
    	musicButton.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	sfxButton.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	windButton.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	spottButton.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	zurück_Entity.addComponent(new ImageRenderComponent(new Image("assets/gorillas/button.png")));
    	}
    	// --->

		// Setting the Entities positions!
		// <---
		mbackground.setPosition(new Vector2f(400,300));	
    	musicButton.setPosition(new Vector2f(218, offset));
    	sfxButton.setPosition(new Vector2f(218, offset+distance));
    	windButton.setPosition(new Vector2f(218, offset+distance*2));
    	spottButton.setPosition(new Vector2f(218, offset+distance*3));
    	zurück_Entity.setPosition(new Vector2f(218, offset+distance*5));
    	// --->

		// Scaling the Entities pictures!
		// <---
    	musicButton.setScale(scale);
    	sfxButton.setScale(scale);
    	windButton.setScale(scale);
    	spottButton.setScale(scale);
    	zurück_Entity.setScale(scale);
    	// --->

		// Creating the Events for all buttons and keylisteners!
		// <---
    	ANDEvent mButton = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	ANDEvent seButton = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	ANDEvent wButton = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	ANDEvent sButton = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	ANDEvent mainEvents_z = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
    	// --->

		// Creating and adding the Actions!
		// Care: One-line-actions are >literally< summarized as one-line-actions but given a comment on what they do.
		// <---
    	Action buttonPressed = new Action() {@Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {MusicPlayer.playButton();}};
    	Action zurück_Action = new ChangeStateInitAction(Gorillas.MAINMENUSTATE);
    	mButton.addAction(buttonPressed);
    	// Switches music on/off
    	mButton.addAction(new Action() {@Override 
    		public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				Gorillas.options.setMusicEnabled(!Gorillas.options.isMusicEnabled());
				if (Gorillas.options.isMusicEnabled())
					MusicPlayer.playBg();
				if (!Gorillas.options.isMusicEnabled())
					MusicPlayer.stopBg();
			}});
    	seButton.addAction(buttonPressed);
    	// Switches SFX on/off
    	seButton.addAction(new Action() { @Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) { Gorillas.options.setSFXEnabled(!Gorillas.options.isSFXEnabled()); }});
    	wButton.addAction(buttonPressed);
    	// Switches wind on/off
    	wButton.addAction(new Action() { @Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) { Gorillas.options.setWindEnabled(!Gorillas.options.isWindEnabled()); }});
    	sButton.addAction(buttonPressed);
    	// Switches sarcasm on/off
    	sButton.addAction(new Action() { @Override public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) { Gorillas.options.setSpottEnabled(!Gorillas.options.isSpottEnabled()); }});
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
    	mainEvents_z.addAction(buttonPressed);
    	mainEvents_z.addAction(zurück_Action);
    	// --->

		// Assigning the previously created Events to our Entities!
    	// Note: A game would be very boring without events..
		// <---
		escListener.addComponent(escPressed);
    	musicButton.addComponent(mButton);
    	sfxButton.addComponent(seButton);
    	windButton.addComponent(wButton);
    	spottButton.addComponent(sButton);
    	zurück_Entity.addComponent(mainEvents_z);
    	// --->

		// Finally: Adding all local created Entities into our game-wide entity manager!
		// <---
		entityManager.addEntity(this.stateID, mbackground);
    	entityManager.addEntity(stateID, musicButton);
    	entityManager.addEntity(stateID, sfxButton);
    	entityManager.addEntity(stateID, windButton);
    	entityManager.addEntity(stateID, spottButton);
		entityManager.addEntity(stateID, escListener);
    	entityManager.addEntity(this.stateID, zurück_Entity);
    	// --->
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);

		// Draw our menu and draw our menu and draw our menu and draw our menu and ...
		// <---
		g.drawString("Music: "+Gorillas.options.isMusicEnabled(), 160, start_Position);
		g.drawString("Sfx: "+Gorillas.options.isSFXEnabled(), 160, start_Position+distance);
		g.drawString("Wind: "+Gorillas.options.isWindEnabled(), 160, start_Position+2*distance);
		g.drawString("Sarcasm: "+Gorillas.options.isSpottEnabled(), 160, start_Position+3*distance); 
		g.drawString("Custom Gravity: ", 160, start_Position+4*distance - distance/4); 
		g.drawString("Back", 160, start_Position+5*distance);
		// --->
		
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
    	// Custom rootpane
        RootPane rp = super.createRootPane();
        
        // Creating input field and buttons ...
        // <---
        g_Input = new EditField();
        g_Input.setText(""+Gorillas.options.getG());
        g_Input.addCallback(new Callback() {
                public void callback(int key) {
                        handleEditFieldInput(key, g_Input, this, 15);
                }
        });
        gButton = new Button("Save gravity!");
        gButton.addCallback(new Runnable() {
                @Override
                public void run() {
                	try {
                        Gorillas.options.setG(Float.parseFloat(g_Input.getText()));
                	} catch (NumberFormatException e) {
                		//ignore. DAU put in NaN
                	}
                }
        });
        // --->
        
        // Finally: Adding the buttons to our rootpane ...
        // <---
        rp.add(gButton);
        rp.add(g_Input);
        // --->
        
        return rp;
    }
    
    @Override
    protected void layoutRootPane() {
        // Literally layout-ing our rootpane!
        // <---
        g_Input.setSize(40, 20);
        gButton.adjustSize();
        g_Input.setPosition(160, start_Position+4*distance+10);
        gButton.setPosition(200, start_Position+4*distance+10);
        // --->
    }
    
    /**
     * Method gets called if you type something into the input field.
     * 
     * @param key 
     * 				the pressed button
     * @param editField 
     * 				which field to edit
     * @param callback
	 *				the callback on the field
     * @param maxLength 
     * 				max length of digits that you may type into the <code>editField</code>
     */
    void handleEditFieldInput(int key, EditField editField, Callback callback,
                    int maxLength) {

            if (key == de.matthiasmann.twl.Event.KEY_NONE) {
                    String inputText = editField.getText();
                   
                    // Input may only be maxLength long BUT all input is allowed since it's a float.
                    if (inputText.length() > maxLength) {
                            // a call of setText on an EditField triggers the callback, so
                            // remove callback before and add it again after the call
                            editField.removeCallback(callback);
                            editField.setText(inputText.substring(0, inputText.length() - 1));
                            editField.addCallback(callback);
                    }
            }
    }
}