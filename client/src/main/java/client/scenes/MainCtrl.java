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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private SplashScreenCtrl splashCtrl;
    private Scene splash;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private SinglePlayerCtrl singlePlayerCtrl;
    private Scene single;

    private MultiPlayerCtrl multiPlayerCtrl;
    private Scene multi;

    private LeaderBoardCtrl leaderBoardCtrl;
    private Scene leaderboard;

    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash,
            Pair<AddQuoteCtrl, Parent> add, Pair<SinglePlayerCtrl, Parent> single, Pair<MultiPlayerCtrl, Parent> multi,
                           Pair<LeaderBoardCtrl, Parent> leaderboard) {
        this.primaryStage = primaryStage;
        this.splashCtrl = splash.getKey();
        this.splash = new Scene(splash.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.singlePlayerCtrl = single.getKey();
        this.single = new Scene(single.getValue());

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

    public void showMultiPlayerMode() {
        primaryStage.setTitle("Quizzz multi");
        primaryStage.setScene(multi);
    }

    public void showLeaderBoard() {
        primaryStage.setTitle("Quizzz leader board");
        primaryStage.setScene(leaderboard);
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
}