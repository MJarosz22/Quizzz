package client;

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;

public class ActivityMain extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var overview = FXML.load(ActivityOverviewCtrl.class, "client", "scenes", "ActivityOverview.fxml");
        //TODO: var add = FXML.load(AddActivityCtrl.class, "client", "scenes", "AddActivitty.fxml");

        var mainCtrl = INJECTOR.getInstance(MainActivityCtrl.class);
        mainCtrl.initialize(primaryStage, overview); //Add "var add" as the 3rd parameter
    }
}
