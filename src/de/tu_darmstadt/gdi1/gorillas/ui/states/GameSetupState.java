package de.tu_darmstadt.gdi1.gorillas.ui.states;
 
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.entity.StateBasedEntityManager;
 
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
                entityManager.addEntity(this.stateID, entityManager.getEntity(0, "background"));
 
        }
       
 
        @Override
        public void render(GameContainer container, StateBasedGame game, Graphics g)
                        throws SlickException {
 
                entityManager.renderEntities(container, game, g);
               
                //Fehlermeldungen bei falscher Eingabe (wenn leer, dann bei passendem EditField)
                if(oneIsEmpty)
                        g.drawString("Bitte Name eingeben!", player1_Input.getX(), 380);
                if(twoIsEmpty)
                        g.drawString("Bitte Name eingeben!", player2_Input.getX(), 380);
                if(isEqual)
                        g.drawString("Spielernamen dürfen nicht gleich sein!", 230, 380);
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
                round_Label = new Label("Rounds:");
                round_Input = new EditField();
 
                round_Input.addCallback(new Callback() {
                        public void callback(int key) {
                                handleEditFieldInput(key, round_Input, this, 15, round_Input.getText());
                        }
                });
                
                player1_Label = new Label("Spieler1:");
                player1_Input = new EditField();
 
                player1_Input.addCallback(new Callback() {
                        public void callback(int key) {
                                handleEditFieldInput(key, player1_Input, this, 15, player2_Input.getText());
                        }
                });
 
                //Label & EditField & Überwachung von Editfield(siehe handleEditFieldInput()) für Player2
                player2_Label = new Label("Spieler2:");
                player2_Input = new EditField();
 
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
                               
                                String playerName1 = player1_Input.getText();
                                String playerName2 = player2_Input.getText();
                               
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
                                //wenn Namen gleich sind                                       
                                if (playerName1.equals(playerName2) && !playerName1.isEmpty())
                                        isEqual = true;
                                else
                                        isEqual = false;
                               
                                //ist keine der oberen Bedingungen gegeben
                                if(!oneIsEmpty && !twoIsEmpty && !isEqual){
                                       
                                		if (!round_Input.getText().equals(""))
                                			Gorillas.data.setRemainingRounds(Integer.parseInt(round_Input.getText()));
                                	
                                        //Namen werden gespeichert
                                        Gorillas.data.setPlayer1(playerName1);
                                        Gorillas.data.setPlayer2(playerName2);
                                       
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
                rp.add(player1_Label);
                rp.add(player1_Input);
                rp.add(player2_Label);
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

                round_Input.setSize(80, 40);
                player1_Input.setSize(250, 40);
                player2_Input.setSize(250, 40);
 
                //Position von Label & Editfield Player1
                round_Label.setPosition(250, 120);
                round_Input.setPosition(250, 150);
                player1_Label.setPosition(xOffset, yOffset);
                player1_Input.setPosition(xOffset, yOffset + 30);
               
                //Position von Label & Editfield Player2
                player2_Label.setPosition(xOffset + 300, yOffset);
                player2_Input.setPosition(xOffset + 300, yOffset + 30);
               
                //Position von startGameButton
                //startGameButton.setSize(50, 25);                              //entweder feste Größe für Button...
                startGameButton.adjustSize();                                   //..., oder angepasst.
                startGameButton.setPosition(xOffset + 230 , yOffset + 120);
 
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