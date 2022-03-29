package client.scenes;

import client.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
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
        if (!containsServer(getTextFieldServer())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Provided server is not available!");
            alert.show();
        } else if (containsName(getTextFieldName())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "This name already exists. Try a different one");
            alert.show();
        } else if (getTextFieldName().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "This is an empty name. Try a different one");
            alert.show();
        } else {
            gameCtrl.start(getTextFieldName(), getTextFieldServer());
            this.textfieldName.clear();
            mainCtrl.getLobbyCtrl().init();
            mainCtrl.showLobby();
        }
    }

    public String getTextFieldName() {
        return textfieldName.getText();
    }

    public String getTextFieldServer() {
        return textfieldServer.getText();
    }

    private boolean containsName(String name) {
        List<String> playerNames = server.connectedPlayersOnServer(getTextFieldServer());
        return listContains(playerNames, name);
    }

    private boolean containsServer(String serverName) {
        List<String> availableServers = server.availableServers();
        return availableServers.contains(serverName);
    }

    private boolean listContains(List<String> list, String string) {
        if (list == null || list.isEmpty()) return false;

        for (String s : list) {
            if (s.toLowerCase().trim().equals(string.toLowerCase().trim())) {
                return true;
            }
        }

        return false;
    }
}
