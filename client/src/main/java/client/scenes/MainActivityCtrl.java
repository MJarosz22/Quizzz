package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainActivityCtrl {

    private Stage primStage;

    private ActivityOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddActivityCtrl addCtrl;
    private Scene add;

    public void initialize(Stage primStage, Pair<ActivityOverviewCtrl, Parent> overview, Pair<AddActivityCtrl, Parent> add) {
        this.primStage = primStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        showOverview();
        primStage.show();
    }

    public void showOverview() {
        primStage.setTitle("Activities: Overview");
        primStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primStage.setTitle("Activities: Adding Activity");
        primStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }


}
