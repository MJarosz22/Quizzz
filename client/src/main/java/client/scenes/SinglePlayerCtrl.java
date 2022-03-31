package client.scenes;

import client.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.GameInstance;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class SinglePlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;
    private String playerName;

    @FXML
    private TextField textField;

    @Inject
    public SinglePlayerCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }


    /**
     * Method triggered when the "BACK" button is pressed
     * Goes back to the splash screen
     */
    public void back() {
        this.textField.clear();
        mainCtrl.showSplash();
    }

    /**
     * Method triggered when the player presses the "PLAY" button
     * It adds the player to the game instance and starts to show the questions
     */
    public void play() {
        if (!getTextField().equals("")) {
            SimpleUser player = server.addPlayer(new RequestToJoin(getTextField(), null, GameInstance.SINGLE_PLAYER));
            gameCtrl.setPlayer(player);
            playerName = player.getName();
            this.textField.clear();
            mainCtrl.showSinglePlayerGame();
        }
    }

    public String getTextField() {
        return textField.getText();
    }

    public void setTextField(String string) {
        this.textField.setText(string);
    }

    public String getPlayerName() {
        return playerName;
    }
}
