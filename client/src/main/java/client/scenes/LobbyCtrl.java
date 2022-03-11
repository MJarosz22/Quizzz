package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label labelName;

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    private TableColumn<SimpleUser, String> columnName;

    @Inject
    public LobbyCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
    }

    /**
     * When you press "LEAVE LOBBY" for the multi-player variant of the game, or "BACK"
     * in the singleplayer variant, the player should be disconnected and guided back to the splash screen.
     */
    public void back() {
        SimpleUser player = mainCtrl.getPlayer();
        server.disconnect(player);
        System.out.println(player.getName() + " disconnected!");
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    public void play() {
        //TODO CONNECT TO SERVER
//        mainCtrl.showPlayMode();
    }

    public void setLabelName(String name) {
        labelName.setText(name);
    }

    public void setTablePlayers(List<SimpleUser> players) {
        tablePlayers.setItems(FXCollections.observableList(players));
    }
}
