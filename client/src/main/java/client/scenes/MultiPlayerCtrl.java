package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.GameInstance;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import jakarta.ws.rs.ClientErrorException;
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
        if (!getTextFieldName().equals("")) {
            try{
                SimpleUser player = server.addPlayer(new RequestToJoin(getTextFieldName(), GameInstance.MULTI_PLAYER));
                mainCtrl.setPlayer(player);
                LobbyCtrl lobbyCtrl = mainCtrl.getLobbyCtrl();
                lobbyCtrl.changePrompt();
                this.textfieldName.clear();
                mainCtrl.showLobby();
            }catch (ClientErrorException e){
                Alert alert = new Alert(Alert.AlertType.ERROR,"This name already exists. Try a different one");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please type in a name!");
            alert.show();
        }
    }

    public String getTextFieldName() {
        return textfieldName.getText();
    }

}
