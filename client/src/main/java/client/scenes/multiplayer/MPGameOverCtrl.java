package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MPGameOverCtrl {

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    TableColumn<String, SimpleUser> nameColumn;

    @FXML
    TableColumn<Integer, SimpleUser> scoreColumn;

    @FXML
    TableColumn<String, Integer> positionColumn;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;

    @Inject
    public MPGameOverCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }

    public void init(List<SimpleUser> players) {
        System.out.println(players.get(0).getScore());
            setTablePlayers(players);
    }

    /**
     * Method that is triggered when the user presses 'PLAY AGAIN' button in GameOver screen
     */
    public void back() {
        mainCtrl.showSplash();
    }

    public void setTablePlayers(List<SimpleUser> players) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        //show player positions
        positionColumn.setCellFactory(col -> {
            TableCell<String,Integer> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));
            return cell;
        });


        //sort players
        var sortedPlayers = players.stream().sorted(Comparator
                        .comparingLong(SimpleUser::getScore).reversed())
                .collect(Collectors.toList());


        // Load players into table
        ObservableList<SimpleUser> data = FXCollections.observableList(sortedPlayers);
        tablePlayers.setItems(data);


        // alternative for loading players
        /*for (SimpleUser simpleUser : sortedPlayers){
            tablePlayers.getItems().add(simpleUser);
        }*/
    }
}


