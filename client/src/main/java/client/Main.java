/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.player.SimpleUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var home = FXML.load(SplashScreenCtrl.class, "client", "scenes", "SplashScreen.fxml");
        var single = FXML.load(SinglePlayerCtrl.class, "client", "scenes", "SinglePlayer.fxml");
        var singleGame = FXML.load(SinglePlayerGameCtrl.class, "client", "scenes", "SinglePlayerGame.fxml");
        var singleGameOver = FXML.load(SinglePlayerGameOverCtrl.class, "client", "scenes", "SinglePlayerGameOver.fxml");
        var multi = FXML.load(MultiPlayerCtrl.class, "client", "scenes", "Multiplayer.fxml");
        var leaderboard = FXML.load(LeaderBoardCtrl.class, "client", "scenes", "LeaderBoard.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        var lobby = FXML.load(LobbyCtrl.class, "client", "scenes", "Lobby.fxml");

        mainCtrl.initialize(primaryStage, home, single, singleGame, singleGameOver, multi, leaderboard, lobby);
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to close the game?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if(ButtonType.NO.equals(result)) event.consume();
            Platform.exit();
        });
    }

    /**
     * Method called whenever a client is closed (by pressing the 'x' button of the window).
     * TODO: POP-UP asking for confirmation of closing the client.
     */
    @Override
    public void stop() {
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        ServerUtils serverUtils = new ServerUtils();
        SimpleUser player = mainCtrl.getPlayer();
        if (player != null) {
            serverUtils.disconnect(mainCtrl.getPlayer());
            System.out.println(player.getName() + " disconnected!");
        }
    }
}