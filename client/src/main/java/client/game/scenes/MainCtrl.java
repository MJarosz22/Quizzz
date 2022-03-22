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
package client.game.scenes;

import client.game.scenes.multiplayer.*;
import client.game.scenes.pregame.LobbyCtrl;
import client.game.scenes.pregame.MultiPlayerCtrl;
import client.game.scenes.pregame.SinglePlayerCtrl;
import client.game.scenes.pregame.SplashScreenCtrl;
import client.game.scenes.singleplayer.SinglePlayerGameCtrl;
import client.game.scenes.singleplayer.SinglePlayerGameOverCtrl;
import commons.question.QuestionHowMuch;
import commons.question.QuestionMoreExpensive;
import commons.question.QuestionWhichOne;
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

    private GameCtrl gameCtrl;

    private MoreExpensiveCtrl moreExpensiveCtrl;
    private Scene moreExpensive;

    private HowMuchCtrl howMuchCtrl;
    private Scene howMuch;

    private WhichOneCtrl whichOneCtrl;
    private Scene whichOne;

    public MoreExpensiveCtrl getMoreExpensiveCtrl() {
        return moreExpensiveCtrl;
    }

    public HowMuchCtrl getHowMuchCtrl() {
        return howMuchCtrl;
    }

    public WhichOneCtrl getWhichOneCtrl() {
        return whichOneCtrl;
    }

    private QuestionCtrl currentQuestionScene;


    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash, Pair<SinglePlayerCtrl,
            Parent> single, Pair<SinglePlayerGameCtrl, Parent> singleGame,
                           Pair<SinglePlayerGameOverCtrl, Parent> singleGameOver,
                           Pair<MultiPlayerCtrl, Parent> multi,
                           Pair<LeaderBoardCtrl, Parent> leaderboard, Pair<LobbyCtrl, Parent> lobby, GameCtrl gameCtrl,
                           Pair<MoreExpensiveCtrl, Parent> moreExpensive, Pair<HowMuchCtrl, Parent> howMuch, Pair<WhichOneCtrl, Parent> whichOne) {

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

        this.gameCtrl = gameCtrl;

        this.moreExpensiveCtrl = moreExpensive.getKey();
        this.moreExpensive = new Scene(moreExpensive.getValue());

        this.howMuchCtrl = howMuch.getKey();
        this.howMuch = new Scene(howMuch.getValue());

        this.whichOneCtrl = whichOne.getKey();
        this.whichOne = new Scene(whichOne.getValue());


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
        primaryStage.setScene(leaderboard);
    }

    public void showSinglePlayerGameOver() {
        primaryStage.setTitle("GAME OVER");
        primaryStage.setScene(singleGameOver);
    }

    public void showLobby() {
        primaryStage.setTitle("Quizzz lobby");
        primaryStage.setScene(lobby);
    }

    public LobbyCtrl getLobbyCtrl() {
        return lobbyCtrl;
    }

    public void showMoreExpensive(QuestionMoreExpensive question) {
        currentQuestionScene = moreExpensiveCtrl;
        primaryStage.setTitle("Quizzz More Expensive");
        moreExpensiveCtrl.init(question);
        primaryStage.setScene(moreExpensive);
    }

    public void showHowMuch(QuestionHowMuch question) {
        currentQuestionScene = howMuchCtrl;
        primaryStage.setTitle("Quizzz How Much");
        howMuchCtrl.init(question);
        primaryStage.setScene(howMuch);
    }

    public void showWhichOne(QuestionWhichOne question) {
        currentQuestionScene = whichOneCtrl;
        primaryStage.setTitle("Quizzz Which One");
        whichOneCtrl.init(question);
        primaryStage.setScene(whichOne);
    }

    public QuestionCtrl getCurrentQuestionScene() {
        return currentQuestionScene;
    }

    public SinglePlayerCtrl getSinglePlayerCtrl() {
        return singlePlayerCtrl;
    }
}