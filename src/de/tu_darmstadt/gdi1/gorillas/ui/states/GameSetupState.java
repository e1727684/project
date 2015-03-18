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
        private StateBasedGame sb;      //für State-Wechsel
        GameContainer gc;                       //für State-Wechsel

        private Label round_Label;
        EditField round_Input;
        private Label player1_Label;
        EditField player1_Input;
        private Label player2_Label;
        EditField player2_Input;
        Button startGameButton;
       
        boolean isEqual;                        //sind Namen gleich?
        boolean oneIsEmpty;                     //ist das erste EditField leer
        boolean twoIsEmpty;                     //ist das zweite EditField leer
        int errorMesPos;                        //Position der Fehlermeldung

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
               
                //wird zwischengespeichert für StateChange über Rootpane-Button
                sb = game;
                gc = container;

        		Entity background = new Entity("aboutBack");
        		background.setPosition(new Vector2f(400,300));
            	if (!Gorillas.data.guiDisabled) { // really.... 
                	background.addComponent(new ImageRenderComponent(new Image("assets/gorillas/backgrounds/backgroundDaWoManNamenEingibt.png")));
            	}
                entityManager.addEntity(this.stateID, background);
 
        }
       
 
        @Override
        public void render(GameContainer container, StateBasedGame game, Graphics g)
                        throws SlickException {
 
                entityManager.renderEntities(container, game, g);
               
                //Fehlermeldungen bei falscher Eingabe (wenn leer, dann bei passendem EditField)
                
                if(oneIsEmpty)
                        g.drawString("Please enter your name!", player1_Input.getX(), 380);
                if(twoIsEmpty)
                        g.drawString("Please enter your name!", player2_Input.getX(), 380);
                if(isEqual)
                        g.drawString("Playernames must not be the same!", 230, 380);
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
       
 
        /**
         * Hinzufügen der Bedienungselemente mit Hilfe von RootPane
         */
        @Override
        protected RootPane createRootPane() {
 
                // erstelle die RootPane
                RootPane rp = super.createRootPane();
               
                //Label & EditField & Überwachung von Editfield für Player1
                round_Label = new Label("positive number for score \n\nnegative number for rounds!");
                round_Input = new EditField();
                round_Input.setText(""+1);
                round_Input.addCallback(new Callback() {
                        public void callback(int key) {
                                handleEditFieldInput(key, round_Input, this, 15, round_Input.getText());
                        }
                });
                
                player1_Label = new Label("Spieler1:");
                player1_Input = new EditField();
                player1_Input.setText(Gorillas.data.getPlayer1());
                player1_Input.addCallback(new Callback() {
                        public void callback(int key) {
                                handleEditFieldInput(key, player1_Input, this, 15, player2_Input.getText());
                        }
                });
 
                //Label & EditField & Überwachung von Editfield(siehe handleEditFieldInput()) für Player2
                player2_Label = new Label("Spieler2:");
                player2_Input = new EditField();
                player2_Input.setText(Gorillas.data.getPlayer2());
                player2_Input.addCallback(new Callback() {
                        public void callback(int key) {
                                handleEditFieldInput(key, player2_Input, this, 15, player1_Input.getText());
                        }
                });
               
                //startGameButton wechselt nur den State, wenn beide Namen eingegeben wurden und nicht gleich sind
                startGameButton = new Button("Start Game!");
                
                startGameButton.addCallback(new Runnable() {
                        @Override
                        public void run() {
                               
                        		//ließt Namen aus Editfield und "trimmt" Leerzeichen
                                String playerName1 = player1_Input.getText().trim();
                                String playerName2 = player2_Input.getText().trim();
                               
                                //wenn Editfield 1 leer ist
                                if (playerName1.isEmpty())
                                        oneIsEmpty = true;
                                else
                                        oneIsEmpty = false;
                                //wenn Editfield 2 leer ist
                                if (playerName2.isEmpty())
                                        twoIsEmpty = true;                                     
                                else
                                        twoIsEmpty = false;
                                //wenn Namen gleich sind(Groß-/Kleinschreibung und Leerzeichen an Anfang oder Ende machen keinen unterschied)                                      
                                if (playerName1.equalsIgnoreCase(playerName2) && !playerName1.isEmpty())
                                        isEqual = true;
                                else
                                        isEqual = false;
                               
                                //ist keine der oberen Bedingungen gegeben
                                if(!oneIsEmpty && !twoIsEmpty && !isEqual){
                                       
                                		if (!round_Input.getText().equals(""))
                                			if (Integer.parseInt(round_Input.getText()) < 0) {
                                				Gorillas.data.setPlayTillScore(0);
                                				Gorillas.data.setRemainingRounds(-Integer.parseInt(round_Input.getText()));
                                			} else {
                                				Gorillas.data.setPlayTillScore(Integer.parseInt(round_Input.getText()));
                                				Gorillas.data.setRemainingRounds(0);
                                			}
                                			
                                        //Namen werden gespeichert
                                        Gorillas.data.setPlayer1(playerName1);
                                        Gorillas.data.setPlayer2(playerName2);
                                       
                                        Gorillas.data.setCurrentScore(0, 0);
                                        
                                        //State wird gewechselt
                                        sb.enterState(Gorillas.GAMEPLAYSTATE);
                                   
                                        if (gc.isPaused())
                                      gc.resume();
                                }                      
                       
                        }
                });
 
                //Elemente werden zur RootPane hinzugefügt
                rp.add(round_Label);
                rp.add(round_Input);
                //rp.add(player1_Label);
                rp.add(player1_Input);
                //rp.add(player2_Label);
                rp.add(player2_Input);
                rp.add(startGameButton);
               
                return rp;
        }
 
        /**
         * RootPane-Layout
         */
        @Override
        protected void layoutRootPane() {
 
                int xOffset = 120;
                int yOffset = 300;

                round_Input.setSize(40, 20);
                player1_Input.setSize(250, 40);
                player2_Input.setSize(250, 40);
 
                //Position von Label & Editfield Player1
                round_Label.setPosition(50, 450);
                round_Input.setPosition(110, 440);
                player1_Label.setPosition(xOffset, yOffset);
                player1_Input.setPosition(xOffset, yOffset + 30);
               
                //Position von Label & Editfield Player2
                player2_Label.setPosition(xOffset + 300, yOffset);
                player2_Input.setPosition(xOffset + 300, yOffset + 30);
               
                //Position von startGameButton
                //startGameButton.setSize(50, 25);                              //entweder feste Größe für Button...
                startGameButton.adjustSize();                                   //..., oder angepasst.
                startGameButton.setPosition(xOffset + 230 , yOffset + 135);
 
        }
 
       
        /**
         * Sichert das nur Buchstaben eingegeben werden können und der Name höchstens 15 Zeichen lang ist
         * @param key
         * @param editField
         * @param callback
         * @param maxLength
         */
        void handleEditFieldInput(int key, EditField editField, Callback callback,
                        int maxLength, String secName) {
 
                if (key == de.matthiasmann.twl.Event.KEY_NONE) {
                        String inputText = editField.getText();
                       
                        // Name darf höchsten 10 Zeichen lang sein
                        if (inputText.length() > maxLength) {
                                // a call of setText on an EditField triggers the callback, so
                                // remove callback before and add it again after the call
                                editField.removeCallback(callback);
                                editField
                                                .setText(inputText.substring(0, inputText.length() - 1));
                                editField.addCallback(callback);
                        }
                }
        }
}