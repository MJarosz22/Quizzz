package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class ActivityOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainActivityCtrl mainActivityCtrl;

    private ObservableList<Activity> data;

    @FXML
    private TableView<Activity> table;

    @FXML
    private TableColumn<Activity, String> columnTitle;

    @FXML
    private TableColumn<Activity, Number> columnConsumption;

    @FXML
    private TableColumn<Activity, String> columnSource;

    @Inject

    public ActivityOverviewCtrl(ServerUtils server, MainActivityCtrl mainActivityCtrl) {
        this.server = server;
        this.mainActivityCtrl = mainActivityCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnTitle.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().title));
        columnConsumption.setCellValueFactory(q -> new SimpleIntegerProperty(q.getValue().consumption));
        columnSource.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().source));
    }

    public void addActivity() {
        mainActivityCtrl.showAdd();
    }

    public void refresh() {
        var activities = server.getActivities();
        data = FXCollections.observableList(activities);
        table.setItems(data);
    }
}
