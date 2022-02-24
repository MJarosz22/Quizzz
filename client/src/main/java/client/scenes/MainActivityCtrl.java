package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainActivityCtrl {

    private Stage primStage;

    private ActivityOverviewCtrl overviewCtrl;
    private Scene overview;

    public void initialize(Stage primStage, Pair<ActivityOverviewCtrl, Parent> overview){
        this.primStage = primStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        showOverview();
        primStage.show();
    }

    public void showOverview(){
        primStage.setTitle("Activities: Overview");
        primStage.setScene(overview);
        overviewCtrl.refresh();
    }


}
