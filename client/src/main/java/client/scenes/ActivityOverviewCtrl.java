package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;

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

        columnTitle.setCellFactory(TextFieldTableCell.forTableColumn());

        columnConsumption.setCellValueFactory(q -> new SimpleLongProperty(q.getValue().getConsumption_in_wh()));
        columnConsumption.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        columnSource.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void addActivity() {
        mainActivityCtrl.showAdd();
    }


    public void editTitle(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.title = productStringCellEditEvent.getNewValue();
        server.updateActivity(activity);
        refresh();
    }


    public void editConsumption(TableColumn.CellEditEvent<Activity, Number> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.setConsumption_in_wh((long) productStringCellEditEvent.getNewValue().intValue());
        server.updateActivity(activity);
        refresh();
    }

    public void editSource(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.source = productStringCellEditEvent.getNewValue();
        server.updateActivity(activity);
        refresh();
    }

    public void refresh() {
        var activities = server.getActivities();
        data = FXCollections.observableList(activities);
        table.setItems(data);
    }
}
