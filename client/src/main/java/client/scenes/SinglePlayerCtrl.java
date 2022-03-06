package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.SimpleUser;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Objects;

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

    /**
     * To be added when making the main game scene, in order for the player to play
     */
    public void play() {
        if (!getTextField().equals("")) {
            if (mainCtrl.getPlayer() != null && mainCtrl.getPlayer().getCookie() != null && !Objects.equals(mainCtrl.getPlayer().getCookie(), "")) {
                //Player already has connected
                System.out.println("Already connected!");
                //TODO CREATE Pop-up/info that
                return;
            }
            SimpleUser player = server.addPlayer(getTextField());
            mainCtrl.setPlayer(player);
            System.out.println(player);
            mainCtrl.showLobby();
            //TODO ADD mainCtrl.showLobby(); and have a place to store SimpleUser Client-wide
        }
    }

    public String getTextField() {
        return textField.getText();
    }
}
