package client.game.scenes.pregame;

import client.game.scenes.MainCtrl;
import client.game.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.GameInstance;
import commons.communication.RequestToJoin;
import commons.player.SimpleUser;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MultiPlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;

    @FXML
    private TextField textfieldName, textFieldServer;

    @Inject
    public MultiPlayerCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }

    public void back() {
        this.textfieldName.clear();
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    public void join() {
        if (!getTextFieldName().equals("")) {
            SimpleUser player = server.addPlayer(new RequestToJoin(getTextFieldName(), getTextFieldServer() , GameInstance.MULTI_PLAYER));
            gameCtrl.setPlayer(player);
            gameCtrl.start();

            this.textfieldName.clear();
            this.textFieldServer.clear();
            mainCtrl.getLobbyCtrl().init();
            mainCtrl.showLobby();
        }
        mainCtrl.showLobby();
    }

    public String getTextFieldName() {
        return textfieldName.getText();
    }

    public String getTextFieldServer() {
        return textFieldServer.getText();
    }
}
