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

    private SinglePlayerGameCtrl singlePlayerGameCtrl;
    private Scene singleGame;

    private SinglePlayerGameOverCtrl singlePlayerGameOverCtrl;
    private Scene singleGameOver;

    private MultiPlayerCtrl multiPlayerCtrl;
    private Scene multi;

    private LobbyCtrl lobbyCtrl;
    private Scene lobby;

    private LeaderBoardCtrl leaderBoardCtrl;
    private Scene leaderboard;

    private ActivityOverviewCtrl activityOverviewCtrl;
    private Scene overview;

    private AddActivityCtrl addActivityCtrl;
    private Scene add;

    private SimpleUser player;

    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash, Pair<SinglePlayerCtrl,
            Parent> single, Pair<SinglePlayerGameCtrl, Parent> singleGame,
                           Pair<SinglePlayerGameOverCtrl, Parent> singleGameOver,
                           Pair<MultiPlayerCtrl, Parent> multi,
                           Pair<LeaderBoardCtrl, Parent> leaderboard, Pair<LobbyCtrl, Parent> lobby,
                           Pair<ActivityOverviewCtrl, Parent> overview, Pair<AddActivityCtrl, Parent> add) {

        this.primaryStage = primaryStage;
        this.splashCtrl = splash.getKey();
        this.splash = new Scene(splash.getValue());

        this.singlePlayerCtrl = single.getKey();
        this.single = new Scene(single.getValue());

        this.singlePlayerGameCtrl = singleGame.getKey();
        this.singleGame = new Scene(singleGame.getValue());

        this.singlePlayerGameOverCtrl = singleGameOver.getKey();
        this.singleGameOver = new Scene(singleGameOver.getValue());

        this.multiPlayerCtrl = multi.getKey();
        this.multi = new Scene(multi.getValue());

        this.leaderBoardCtrl = leaderboard.getKey();
        this.leaderboard = new Scene(leaderboard.getValue());

        this.lobbyCtrl = lobby.getKey();
        this.lobby = new Scene(lobby.getValue());

        this.activityOverviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addActivityCtrl = add.getKey();
        this.add = new Scene(add.getValue());

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

    public void showSinglePlayerGame() {
        primaryStage.setTitle("Quizz single");
        primaryStage.setScene(singleGame);
        singlePlayerGameCtrl.initialize();
    }

    public void showMultiPlayerMode() {
        primaryStage.setTitle("Quizzz multi");
        primaryStage.setScene(multi);
    }

    public void showLeaderBoard() {
        primaryStage.setTitle("Quizzz leader board");
        leaderBoardCtrl.setTablePlayers(ServerUtils.getLeaderboard(player));
        primaryStage.setScene(leaderboard);
    }

    public void showSinglePlayerGameOver() {
        primaryStage.setTitle("GAME OVER");
        singlePlayerGameOverCtrl.setTablePlayers(ServerUtils.getLeaderboard(player));
        primaryStage.setScene(singleGameOver);
    }

    public void showLobby() {
        primaryStage.setTitle("Quizzz lobby");
        //lobbyCtrl.setLabelName(player.getName());
        //lobbyCtrl.setTablePlayers(ServerUtils.getPlayers(player));
        primaryStage.setScene(lobby);
        lobbyCtrl.initialize();
    }

    public void showAdmin() {
        primaryStage.setTitle("Activity overview");
        activityOverviewCtrl.refresh();
        primaryStage.setScene(overview);
    }

    public void showAddActivity() {
        primaryStage.setTitle("Add activity");
        primaryStage.setScene(add);
    }

    public SimpleUser getPlayer() {
        return player;
    }

    public LobbyCtrl getLobbyCtrl() {
        return lobbyCtrl;
    }

    public SinglePlayerCtrl getSinglePlayerCtrl() {
        return singlePlayerCtrl;
    }

    public void setPlayer(SimpleUser player) {
        this.player = player;
    }

}