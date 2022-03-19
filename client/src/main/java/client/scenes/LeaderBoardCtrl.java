package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderBoardCtrl {

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

    @Inject
    public LeaderBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {

        mainCtrl.showSplash();
        tablePlayers.getItems().clear();
    }

    public void setTablePlayers(List<SimpleUser> players) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        positionColumn.setCellFactory(col -> {
            TableCell<String, Integer> indexCell = new TableCell<>();
            ReadOnlyObjectProperty<TableRow<String>> rowProperty = indexCell.tableRowProperty();
            ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
                TableRow<String> row = rowProperty.get();
                if (row != null) {
                    int rowIndex = row.getIndex();
                    if (rowIndex < row.getTableView().getItems().size()) {
                        return Integer.toString(rowIndex);
                    }
                }
                return null;
            }, rowProperty);
            indexCell.textProperty().bind(rowBinding);
            return indexCell;
        });

        //sort players
        var sortedPlayers = players.stream().sorted(Comparator
                .comparingLong(SimpleUser::getScore).reversed())
                .collect(Collectors.toList());
        // Load objects into table
        ObservableList<SimpleUser> data = FXCollections.observableList(sortedPlayers);
        tablePlayers.setItems(data);
        /**for (SimpleUser simpleUser : sortedPlayers){
            tablePlayers.getItems().add(simpleUser);
        }**/
    }
}
