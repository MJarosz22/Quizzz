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
    private TableColumn<Activity, String> columnID;

    @FXML
    private TableColumn<Activity, String> columnImagePath;

    @FXML
    private TableColumn<Activity, String> columnTitle;

    @FXML
    private TableColumn<Activity, Number> columnConsumption_in_wh;

    @FXML
    private TableColumn<Activity, String> columnSource;

    @Inject

    public ActivityOverviewCtrl(ServerUtils server, MainActivityCtrl mainActivityCtrl) {
        this.server = server;
        this.mainActivityCtrl = mainActivityCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnID.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getId()));
        columnID.setCellFactory(TextFieldTableCell.forTableColumn());

        columnImagePath.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getImage_path()));
        columnImagePath.setCellFactory(TextFieldTableCell.forTableColumn());

        columnTitle.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getTitle()));
        columnTitle.setCellFactory(TextFieldTableCell.forTableColumn());

        columnConsumption_in_wh.setCellValueFactory(q -> new SimpleLongProperty(q.getValue().getConsumption_in_wh()));
        columnConsumption_in_wh.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        columnSource.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getSource()));
        columnSource.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void addActivity() {
        mainActivityCtrl.showAdd();
    }


    public void editID(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.setId(productStringCellEditEvent.getNewValue());
        server.updateActivity(activity);
        refresh();
    }

    public void editImagePath(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.setImage_path(productStringCellEditEvent.getNewValue());
        server.updateActivity(activity);
        refresh();
    }

    public void editTitle(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.setTitle(productStringCellEditEvent.getNewValue());
        server.updateActivity(activity);
        refresh();
    }


    public void editConsumption(TableColumn.CellEditEvent<Activity, Number> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.setConsumption_in_wh(productStringCellEditEvent.getNewValue().longValue());
        server.updateActivity(activity);
        refresh();
    }

    public void editSource(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.setSource(productStringCellEditEvent.getNewValue());
        server.updateActivity(activity);
        refresh();
    }

    public void refresh() {
        var activities = server.getActivities();
        data = FXCollections.observableList(activities);
        table.setItems(data);
    }
}
