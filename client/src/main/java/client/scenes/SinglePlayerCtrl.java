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
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    public void play() {
        if(!getTextField().equals("")) {
            Player player = server.addPlayer(getTextField());
            System.out.println(player);
            //mainCtrl.showPlayMode();
        }
    }

    public String getTextField() {
        return textField.getText();
    }
}
