package client.scenes;

import client.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.List;

public class MultiPlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;

    @FXML
    private TextField textfieldName, textfieldServer;

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

    public void join() {
        if (!getTextFieldName().equals("") && !containsName(getTextFieldName())) {
            gameCtrl.start(getTextFieldName(), getTextFieldServer());
            this.textfieldName.clear();
            this.textfieldServer.clear();
            mainCtrl.getLobbyCtrl().init();
            mainCtrl.showLobby();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "This name already exists. Try a different one");
            alert.show();
            System.out.println("NAME ALREADY EXISTS!"); //We must make an actual pop-up
        }
    }

    public String getTextFieldName() {
        return textfieldName.getText();
    }

    public String getTextFieldServer() {
        return textfieldServer.getText();
    }

    private boolean containsName(String name) {
        boolean nameExists = false;
        int lastGIId = server.getLastGIIdMult();
        List<SimpleUser> simpleUserList = server.connectedPlayers(lastGIId);
        int i = 0;
        while (!nameExists && i < simpleUserList.size()) {
            if (simpleUserList.get(i).getName().toLowerCase().trim().equals(name.toLowerCase().trim())) {
                nameExists = true;
            }
            i++;
        }

        return nameExists;
    }

}
