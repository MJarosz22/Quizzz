package client;

import client.scenes.ActivityOverviewCtrl;
import client.scenes.AddActivityCtrl;
import client.scenes.MainActivityCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class ActivityMain extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        var overview = FXML.load(ActivityOverviewCtrl.class, "client", "scenes", "ActivityOverview.fxml");
        var add = FXML.load(AddActivityCtrl.class, "client", "scenes", "AddActivity.fxml");

        var mainCtrl = INJECTOR.getInstance(MainActivityCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add);
    }
}
