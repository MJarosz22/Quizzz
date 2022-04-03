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

    /**
     * Method triggered when the "BACK" button is pressed
     * The splash screen is displayed
     */
    public void back() {
        this.textfieldName.clear();
        mainCtrl.showSplash();
    }

    /**
     * Method triggered when the player presses the "JOIN LOBBY" button
     * Displays an alert if the server is unavailable
     * Displays an alert if the chosen name is taken
     * Displays an alert if the player doesn't choose a name
     * Else it adds the player to the lobby
     */
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

    /**
     * Getter for the name the player inputs
     * @return the chosen name of the player
     */
    public String getTextFieldName() {
        return textfieldName.getText();
    }

    /**
     * Getter for the server the player inputs
     * @return the chosen server of the player
     */
    public String getTextFieldServer() {
        return textfieldServer.getText();
    }

    /**
     * Method to determine if another player in the lobby already has the name the player inputs
     * @param name the player input
     * @return true if the name is already chose, false otherwise
     */
    private boolean containsName(String name) {
        List<String> playerNames = server.connectedPlayersOnServer(getTextFieldServer());
        return listContains(playerNames, name);
    }

    /**
     * Method to determine if the server the player is trying to play on already hosts an ongoing game
     * @param serverName the server the player inputs
     * @return true if the server is hosting a game, false otherwise
     */
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
