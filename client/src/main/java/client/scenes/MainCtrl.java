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

import commons.SimpleUser;
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

    private LeaderBoardCtrl leaderBoardCtrl;
    private Scene leaderboard;

    private SimpleUser player;

    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash, Pair<SinglePlayerCtrl,
            Parent> single, Pair<SinglePlayerGameCtrl, Parent> singleGame,
                           Pair<SinglePlayerGameOverCtrl, Parent> singleGameOver,
                           Pair<MultiPlayerCtrl, Parent> multi,
                           Pair<LeaderBoardCtrl, Parent> leaderboard) {

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
    }

    public void showMultiPlayerMode() {
        primaryStage.setTitle("Quizzz multi");
        primaryStage.setScene(multi);
    }

    public void showLeaderBoard() {
        primaryStage.setTitle("Quizzz leader board");
        primaryStage.setScene(leaderboard);
    }

    public void showSinglePlayerGameOver() {
        primaryStage.setTitle("GAME OVER");
        primaryStage.setScene(singleGameOver);
    }

    public SimpleUser getPlayer() {
        return player;
    }

    public void setPlayer(SimpleUser player) {
        this.player = player;
    }
}