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
package client.scenes;

import client.utils.ServerUtils;
import commons.player.SimpleUser;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private SplashScreenCtrl splashCtrl;
    private Scene splash;

    private SinglePlayerCtrl singlePlayerCtrl;
    private Scene single;

    private MultiPlayerCtrl multiPlayerCtrl;
    private Scene multi;

    private LeaderBoardCtrl leaderBoardCtrl;
    private Scene leaderboard;

    private LobbyCtrl lobbyCtrl;
    private Scene lobby;

    private SimpleUser player;

    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash, Pair<SinglePlayerCtrl,
            Parent> single, Pair<MultiPlayerCtrl, Parent> multi,
                           Pair<LeaderBoardCtrl, Parent> leaderboard, Pair<LobbyCtrl, Parent> lobby) {
        this.primaryStage = primaryStage;
        this.splashCtrl = splash.getKey();
        this.splash = new Scene(splash.getValue());

        this.singlePlayerCtrl = single.getKey();
        this.single = new Scene(single.getValue());

        this.multiPlayerCtrl = multi.getKey();
        this.multi = new Scene(multi.getValue());

        this.leaderBoardCtrl = leaderboard.getKey();
        this.leaderboard = new Scene(leaderboard.getValue());

        this.lobbyCtrl = lobby.getKey();
        this.lobby = new Scene(lobby.getValue());

        showSplash();
        primaryStage.show();
    }

    public void showSplash() {
        primaryStage.setTitle("Splash Screen");
        primaryStage.setScene(splash);
    }

    public void showSinglePlayerMode() {
        primaryStage.setTitle("Quizz single");
        primaryStage.setScene(single);
    }

    public void showMultiPlayerMode() {
        primaryStage.setTitle("Quizzz multi");
        primaryStage.setScene(multi);
    }

    public void showLeaderBoard() {
        primaryStage.setTitle("Quizzz leader board");
        primaryStage.setScene(leaderboard);
    }

    public void showLobby() {
        primaryStage.setTitle("Quizzz lobby");
        lobbyCtrl.setLabelName(player.getName());
        lobbyCtrl.setTablePlayers(ServerUtils.getPlayers(player));
        primaryStage.setScene(lobby);
    }

    public SimpleUser getPlayer() {
        return player;
    }

    public void setPlayer(SimpleUser player) {
        this.player = player;
    }
}