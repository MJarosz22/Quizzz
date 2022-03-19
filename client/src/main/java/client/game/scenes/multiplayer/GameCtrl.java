package client.game.scenes.multiplayer;

import client.game.Main;
import client.game.scenes.MainCtrl;
import client.game.scenes.pregame.LobbyCtrl;
import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import commons.player.Player;
import commons.player.SimpleUser;
import commons.question.*;
import javafx.application.Platform;
import org.apache.catalina.Server;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GameCtrl {

    private SimpleUser player;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final LobbyCtrl lobbyCtrl;
    private final HowMuchCtrl howMuchCtrl;
    private final MoreExpensiveCtrl moreExpensiveCtrl;
    private final WhichOneCtrl whichOneCtrl;

    @Inject
    public GameCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.lobbyCtrl = mainCtrl.getLobbyCtrl();
        this.howMuchCtrl = mainCtrl.getHowMuchCtrl();
        this.moreExpensiveCtrl = mainCtrl.getMoreExpensiveCtrl();
        this.whichOneCtrl = mainCtrl.getWhichOneCtrl();
    }

    public void start(){
        server.initWebsocket();
        subscribe("/topic/" + getPlayer().getGameInstanceId() + "/time", Integer.class , time -> {
            Platform.runLater(()->{
                mainCtrl.getLobbyCtrl().setCountdown(time);
            });
        });

        //TODO FIND WAY TO DEAL WITH SUBCLASSES OF QUESTION
        subscribe("/topic/" + getPlayer().getGameInstanceId() + "/questionhowmuch", QuestionHowMuch.class, question -> {
            Platform.runLater(()->{
                goToHowMuch(question);
            });
        });
        subscribe("/topic/" + getPlayer().getGameInstanceId() + "/questionmoreexpensive", QuestionMoreExpensive.class, question -> {
            Platform.runLater(()->{
                goToMoreExpensive(question);
            });
        });
        subscribe("/topic/" + getPlayer().getGameInstanceId() + "/questionwhichone", QuestionWhichOne.class, question -> {
            Platform.runLater(()->{
                goToWhichOne(question);
            });
        });
    }

    public <T> void subscribe(String destination, Class<T> type, Consumer<T> consumer){
        ServerUtils.registerForMessages(destination, type, consumer);
    }

    public void disconnect(){
        server.disconnectWebsocket();
        server.disconnect(player);
    }


    public void submitAnswer(Answer answer){
        server.submitAnswer(player, answer);
    }

    public SimpleUser getPlayer() {
        return player;
    }

    public void setPlayer(SimpleUser player) {
        this.player = player;
    }

    private void goToHowMuch(QuestionHowMuch question){
        mainCtrl.showHowMuch(question);
    }

    private void goToMoreExpensive(QuestionMoreExpensive question){
        mainCtrl.showMoreExpensive();
    }

    private void goToWhichOne(QuestionWhichOne question){
        mainCtrl.showWhichOne();
    }
}
