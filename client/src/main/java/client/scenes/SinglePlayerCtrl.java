package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SinglePlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField textField;

    @Inject
    public SinglePlayerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        this.textField.clear();
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    public void play() {
        if (!getTextField().equals("")) {
            Player newPlayer = new Player(getTextField());
            server.addPlayer(newPlayer);
            this.textField.clear();
            mainCtrl.showSinglePlayerGame();
        }
    }

    public String getTextField() {
        return textField.getText();
    }
}
