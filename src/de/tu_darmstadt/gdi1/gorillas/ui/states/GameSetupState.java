package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 */
public class GameSetupState extends BasicTWLGameState {
 
        private int stateID;
        private StateBasedEntityManager entityManager;
        private StateBasedGame sb;      		
        GameContainer gc;                      

        private Label round_Label;
        EditField round_Input;
        EditField player1_Input;
        EditField player2_Input;
        Button startGameButton;
       
        boolean isEqual;                      
        boolean oneIsEmpty;                  
        boolean twoIsEmpty;                  
        int errorMesPos;              

    	/**
    	 * The constructor. Creates a new state.
    	 * 
    	 * @param sid  
    	 * 				this state's id. it can be identified by it and is unique!
    	 */
        public GameSetupState(int sid) {
                stateID = sid;
                entityManager = StateBasedEntityManager.getInstance();
        }
       
 
        @Override
        public void init(GameContainer container, StateBasedGame game)
                        throws SlickException {
                // Being saved temporarily for statechange via rootpane-button
                sb = game;
                gc = container;

        		// Creating required Entities
        		// <---
        		Entity background = new Entity("aboutBack");
        		// --->
        		
        		// Giving the Entities a picture.... If we aren't testing!
        		// <---
            	if (!Gorillas.data.guiDisabled) { // really.... Best name..
                	background.addComponent(new ImageRenderComponent(new Image("assets/gorillas/backgrounds/backgroundDaWoManNamenEingibt.png")));
            	}
            	// --->

        		// Setting the Entities positions!
        		// <---
        		background.setPosition(new Vector2f(400,300));
            	// --->

        		// Scaling the Entities pictures!
        		// <---
            	// --->

        		// Creating the Events for all buttons and keylisteners!
        		// <---
            	// --->

        		// Creating and adding the Actions!
        		// Care: One-line-actions are >literally< summarized as one-line-actions but given a comment on what they do.
        		// <--- Creating
        		// --->
            	// <--- Adding
        		// --->

        		// Assigning the previously created Events to our Entities!
            	// Note: A game would be very boring without events..
        		// <---
        		// --->

        		// Finally: Adding all local created Entities into our game-wide entity manager!
        		// <---
                entityManager.addEntity(this.stateID, background);
        		// --->
                
                // There is not much happening in this init() but I pasted all those comments in here to make that even more visible!
                // IS IT CLEAR YET?
        }
       
 
        @Override
        public void render(GameContainer container, StateBasedGame game, Graphics g)
                        throws SlickException {
 
                entityManager.renderEntities(container, game, g);
               

        		// Draw our m- Actually we draw error messages but only when they occur... No menu here!
        		// <---
                if(oneIsEmpty)
                        g.drawString("Please enter your name!", player1_Input.getX(), 380);
                if(twoIsEmpty)
                        g.drawString("Please enter your name!", player2_Input.getX(), 380);
                if(isEqual)
                        g.drawString("Playernames must not be the same!", 230, 380);
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
		// Custom rootpane; The real action in this class happens here!
		RootPane rp = super.createRootPane();

        // Creating stuff ...
        // <---
		  // <--- input fields
			player1_Input = new EditField();
			player2_Input = new EditField();
			round_Input = new EditField();
		  // --->
		  // <--- a label
			round_Label = new Label("positive number for score \n\nnegative number for rounds!");
		  // --->
		  // <--- and a button
			startGameButton = new Button("Start Game!");
		  // --->
		// --->
			
		// Initiating the stuff we created
		// <---
		round_Input.setText("" + 1);
		player1_Input.setText(Gorillas.data.getPlayer1());
		player2_Input.setText(Gorillas.data.getPlayer2());
		// --->
		
		// Adding callbacks to all input fields
		// Care: One-line-callbacks are >literally< summarized as one-line-callbacks but given a comment on what they do.
		// <---
		  // <--- Fun-fact: Max length of round input is 3 digits. That means you can play a up-to-999-score-game but only 99 rounds...
			round_Input.addCallback(new Callback() { public void callback(int key) {handleEditFieldInput(key, round_Input, this, 3, round_Input.getText());}});
		  // --->
		  // <---	
			player1_Input.addCallback(new Callback() { public void callback(int key) {handleEditFieldInput(key, player1_Input, this, 15, player2_Input.getText());}});
		  // --->
		  // <---
			player2_Input.addCallback(new Callback() { public void callback(int key) {handleEditFieldInput(key, player2_Input, this, 15, player1_Input.getText());}});
		  // --->
		  // <---
			startGameButton.addCallback(new Runnable() {
			  @Override
			  public void run() {
				// catches the names from input field and eliminates spaces
				String playerName1 = player1_Input.getText().trim();
				String playerName2 = player2_Input.getText().trim();

				// check if the namefield 1 is empty
				if (playerName1.isEmpty()) oneIsEmpty = true; else oneIsEmpty = false;
				
				// check if the namefield 2 is empty
				if (playerName2.isEmpty()) twoIsEmpty = true; else twoIsEmpty = false;
				
				// check if both namefields are filled with same name (ignore case + start-/ending space)
				if (playerName1.equalsIgnoreCase(playerName2) && !playerName1.isEmpty()) isEqual = true; else isEqual = false;

				// if none of the above we can switch to the gameplaystate
				if (!oneIsEmpty && !twoIsEmpty && !isEqual) {
					if (!round_Input.getText().equals("")) // check if player wants to play rounds or score or just 1 quick game and adjust values accordingly
						if (Integer.parseInt(round_Input.getText()) < 0) {
							Gorillas.data.setPlayTillScore(0);
							Gorillas.data.setRemainingRounds(-Integer.parseInt(round_Input.getText()));
						} else {
							Gorillas.data.setPlayTillScore(Integer.parseInt(round_Input.getText()));
							Gorillas.data.setRemainingRounds(0);
						}

					// saving names
					Gorillas.data.setPlayer1(playerName1);
					Gorillas.data.setPlayer2(playerName2);
					
					// resetting score
					Gorillas.data.setCurrentScore(0, 0);

					// finally! state change into play mode
					sb.enterState(Gorillas.GAMEPLAYSTATE);
					
					// shouldnt happen, could happen(?), whatever. handled. 
					if (gc.isPaused())
						gc.resume();
				}}});
		  // --->
		// --->

	    // Finally: Adding everything to our rootpane ...
	    // <---
		rp.add(round_Label);
		rp.add(round_Input);
		rp.add(player1_Input);
		rp.add(player2_Input);
		rp.add(startGameButton);
		// --->
		return rp;
	}

	@Override
	protected void layoutRootPane() {
        // Literally layout-ing our rootpane!
        // <---
          // <--- Setting offset
			int xOffset = 120;
			int yOffset = 300;
          // --->

          // <--- Resizinng accordingly
			round_Input.setSize(40, 20);
			player1_Input.setSize(250, 40);
			player2_Input.setSize(250, 40);
			startGameButton.adjustSize();
          // --->

	      // <--- Moving stuff around till it looks pretty
			round_Label.setPosition(50, 450);
			round_Input.setPosition(110, 440);
			player1_Input.setPosition(xOffset, yOffset + 30);
			player2_Input.setPosition(xOffset + 300, yOffset + 30);
			startGameButton.setPosition(xOffset + 230, yOffset + 135);
          // --->
        // --->
	}

    /**
     * Method gets called if you type something into the input field. 
     * Makes sure that you can only type up to maxLength digits into the field.
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
			int maxLength, String secName) {
		if (key == de.matthiasmann.twl.Event.KEY_NONE) {
			String inputText = editField.getText();
			// Length may only be maxLength
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