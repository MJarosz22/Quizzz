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
import client.scenes.multiplayer.GameCtrl;
import client.scenes.multiplayer.HowMuchCtrl;
import client.scenes.multiplayer.MoreExpensiveCtrl;
import client.scenes.multiplayer.WhichOneCtrl;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.player.SimpleUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        var home = FXML.load(SplashScreenCtrl.class, "client", "scenes", "SplashScreen.fxml");
        var single = FXML.load(SinglePlayerCtrl.class, "client", "scenes", "SinglePlayer.fxml");
        var singleGame = FXML.load(SinglePlayerGameCtrl.class, "client", "scenes", "SinglePlayerGame.fxml");
        var singleGameOver = FXML.load(SinglePlayerGameOverCtrl.class, "client", "scenes", "SinglePlayerGameOver.fxml");
        var multi = FXML.load(MultiPlayerCtrl.class, "client", "scenes", "Multiplayer.fxml");
        var leaderboard = FXML.load(LeaderBoardCtrl.class, "client", "scenes", "LeaderBoard.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        var lobby = FXML.load(LobbyCtrl.class, "client", "scenes", "Lobby.fxml");
        var overview = FXML.load(ActivityOverviewCtrl.class, "client", "scenes", "ActivityOverview.fxml");
        var add = FXML.load(AddActivityCtrl.class, "client", "scenes", "AddActivity.fxml");

        var gameCtrl = INJECTOR.getInstance(GameCtrl.class);
        var moreExpensive = FXML.load(MoreExpensiveCtrl.class,
                "client", "scenes", "GameMoreExpensive.fxml");
        var howMuch = FXML.load(HowMuchCtrl.class, "client", "scenes", "GameHowMuch.fxml");
        var whichOne = FXML.load(WhichOneCtrl.class, "client", "scenes", "GameWhichOne.fxml");

        mainCtrl.initialize(primaryStage, home, single, singleGame, singleGameOver, multi,
                leaderboard, lobby, gameCtrl, moreExpensive, howMuch, whichOne, overview, add);
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to close the game?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result))
                event.consume();
            else
                Platform.exit();
        });
    }

    /**
     * Method called whenever a client is closed(by pressing the 'x' button of the window).
     * If there exists a player that did not disconnect (left game) already and he/she pressed the 'x' button, he/she
     * must be removed from the server.
     */
    @Override
    public void stop() {
        var gameCtrl = INJECTOR.getInstance(GameCtrl.class);
        SimpleUser player = gameCtrl.getPlayer();
        ServerUtils server = new ServerUtils();
        if (player != null) //TODO IS THIS THE RIGHT IF STATEMENT TO CHECK WHETHER PLAYER EXISTS?
            gameCtrl.disconnect();

//        if (server.containsPlayer(player) && SinglePlayerGameCtrl.getGameIsOver() == false )
//            server.disconnect(player); //SINGLEPLAYER IMPL (DON'T KNOW HOW IT CORRELATES WITH MP)
    }

}