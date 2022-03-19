package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SinglePlayerGameOverCtrl {

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    TableColumn<String, SimpleUser> nameColumn;

    @FXML
    TableColumn<Integer, SimpleUser> scoreColumn;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public SinglePlayerGameOverCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        SinglePlayerCtrl singlePlayerCtrl = mainCtrl.getSinglePlayerCtrl();
        String previousTextField = singlePlayerCtrl.getPlayerName();
        singlePlayerCtrl.setTextField(previousTextField);
        mainCtrl.showSplash();
    }

    public void setTablePlayers(List<SimpleUser> players) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        var sortedPlayers = players.stream().sorted(Comparator
                        .comparingLong(SimpleUser::getScore).reversed())
                .collect(Collectors.toList());
        // Load objects into table
        for (SimpleUser simpleUser : sortedPlayers){
            tablePlayers.getItems().add(simpleUser);

        }
    }
}
