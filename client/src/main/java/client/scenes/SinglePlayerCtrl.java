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

    public void back() {
        this.textField.clear();
        mainCtrl.showSplash();
    }

    /**
     * To be added when making the main game scene, in order for the player to play
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
