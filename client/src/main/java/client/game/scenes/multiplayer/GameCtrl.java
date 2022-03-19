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
        subscribe("/topic/time", Integer.class , time -> {
            Platform.runLater(()->{
                mainCtrl.getLobbyCtrl().setCountdown(time);
            });
            System.out.println(time);
        });
        //TODO FIND WAY TO DEAL WITH SUBCLASSES OF QUESTION
        subscribe("/topic/questionhowmuch", QuestionHowMuch.class, question -> {
            Platform.runLater(()->{
                System.out.println("how much");
            });
        });
        subscribe("/topic/questionmoreexpensive", QuestionMoreExpensive.class, question -> {
            Platform.runLater(()->{
                System.out.println("more epxensive");
            });
        });
        subscribe("/topic/questionwhichone", QuestionWhichOne.class, question -> {
            Platform.runLater(()->{
                System.out.println("which one");
            });
        });
        System.out.println("subscribed");
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

    private void goToHowMuch(){

    }

    private void goToMoreExpensive(){

    }

    private void goToWhichOne(){

    }
}
