package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.GameInstance;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.List;

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
        if (!getTextFieldName().equals("") && !containsName(getTextFieldName())) {

            SimpleUser player = server.addPlayer(new RequestToJoin(getTextFieldName(), GameInstance.MULTI_PLAYER));
            mainCtrl.setPlayer(player);
            LobbyCtrl lobbyCtrl = mainCtrl.getLobbyCtrl();
            System.out.println(player);
            lobbyCtrl.changePrompt();
            this.textfieldName.clear();
            mainCtrl.showLobby();
            //TODO Make it so that player goes directly into game instead of going to lobby
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"This name already exists. Try a different one");
            alert.show();
            System.out.println("NAME ALREADY EXISTS!"); //We must make an actual pop-up
        }

    }

    public String getTextFieldName() {
        return textfieldName.getText();
    }

    private boolean containsName(String name) {
        boolean nameExists = false;
        int lastGIId = server.getLastGIIdMult();
        List<SimpleUser> simpleUserList = server.connectedPlayers(lastGIId);
        int i = 0;
        while (!nameExists && i < simpleUserList.size()){
            if (simpleUserList.get(i).getName().toLowerCase().trim().equals(name.toLowerCase().trim())){
                nameExists = true;
            }
                i++;
        }

        return nameExists;
    }

}
