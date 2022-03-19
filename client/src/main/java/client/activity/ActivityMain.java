package client.activity;

import client.MyFXML;
import client.MyModule;
import client.activity.scenes.ActivityOverviewCtrl;
import client.activity.scenes.AddActivityCtrl;
import client.activity.scenes.MainActivityCtrl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

public class ActivityMain extends Application {

    private static final Injector INJECTOR = Guice.createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        var overview = FXML.load(ActivityOverviewCtrl.class, "client", "client/game/scenes", "ActivityOverview.fxml");
        var add = FXML.load(AddActivityCtrl.class, "client", "client/game/scenes", "AddActivity.fxml");

        var mainCtrl = INJECTOR.getInstance(MainActivityCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add);
    }
}
