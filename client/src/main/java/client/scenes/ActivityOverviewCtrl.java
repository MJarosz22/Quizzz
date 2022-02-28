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
        columnID.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().id));
        columnID.setCellFactory(TextFieldTableCell.forTableColumn());

        columnImagePath.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().image_path));
        columnImagePath.setCellFactory(TextFieldTableCell.forTableColumn());

        columnTitle.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().title));
        columnTitle.setCellFactory(TextFieldTableCell.forTableColumn());

        columnConsumption.setCellValueFactory(q -> new SimpleIntegerProperty(q.getValue().consumption_in_wh));
        columnConsumption.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        columnSource.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().source));
        columnSource.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void addActivity() {
        mainActivityCtrl.showAdd();
    }


    public void editID(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.id = productStringCellEditEvent.getNewValue();
        server.updateActivity(activity);
        refresh();
    }

    public void editImagePath(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.image_path = productStringCellEditEvent.getNewValue();
        server.updateActivity(activity);
        refresh();
    }

    public void editTitle(TableColumn.CellEditEvent<Activity, String> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.title = productStringCellEditEvent.getNewValue();
        server.updateActivity(activity);
        refresh();
    }


    public void editConsumption(TableColumn.CellEditEvent<Activity, Number> productStringCellEditEvent) {
        Activity activity = table.getSelectionModel().getSelectedItem();
        activity.consumption_in_wh = productStringCellEditEvent.getNewValue().intValue();
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
