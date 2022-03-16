package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.GameInstance;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MultiPlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField textfieldName;

    @Inject
    public MultiPlayerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        this.textfieldName.clear();
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    public void join() {
        if (!getTextFieldName().equals("")) {
            SimpleUser player = server.addPlayer(new RequestToJoin(getTextFieldName(), GameInstance.MULTI_PLAYER));
            mainCtrl.setPlayer(player);
            LobbyCtrl lobbyCtrl = mainCtrl.getLobbyCtrl();
            lobbyCtrl.increaseNumberOfPlayers();
            this.textfieldName.clear();
            mainCtrl.getLobbyCtrl().init();
            mainCtrl.showLobby();
        }
        mainCtrl.showLobby();
    }

    public String getTextFieldName() {
        return textfieldName.getText();
    }
}
