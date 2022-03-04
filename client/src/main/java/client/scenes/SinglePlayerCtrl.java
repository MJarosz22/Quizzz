package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import commons.SimpleUser;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Objects;

public class SinglePlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private static SimpleUser simpleUser;

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
            if(simpleUser != null && simpleUser.cookie != null && !Objects.equals(simpleUser.cookie, "")) {
                //Player already has connected
                System.out.println("Already connected!");
                //TODO SOMETHING
                return;
            }
            SimpleUser player = server.addPlayer(getTextField());
            simpleUser = player;
            System.out.println(player);
            //mainCtrl.showPlayMode();
        }
    }

    public String getTextField() {
        return textField.getText();
    }
}
