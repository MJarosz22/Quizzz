package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.*;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import javafx.application.Platform;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Consumer;

public class GameCtrl {

    private SimpleUser player;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private List<SimpleUser> players;

    @Inject
    public GameCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void start(String name) {
        player = server.addPlayer(new RequestToJoin(name, GameInstance.MULTI_PLAYER));
        server.initWebsocket();
        subscribeToWebsockets();
    }

    public <T> void subscribe(String destination, Class<T> type, Consumer<T> consumer) {
        server.registerForMessages(destination, type, consumer);
    }

    public void disconnect() {
        server.disconnectWebsocket();
        server.disconnect(player);
    }

    private void subscribeToWebsockets() {
        subscribe("/topic/" + player.getGameInstanceId() + "/time", Integer.class, time ->
                Platform.runLater(() -> mainCtrl.getLobbyCtrl().setCountdown(time)));
        subscribe("/topic/" + player.getGameInstanceId() + "/players", Integer.class, amountOfPlayers -> {
            players = server.getPlayers(player);
            Platform.runLater(() -> mainCtrl.getLobbyCtrl().updatePlayers(players));
        });

        subscribe("/topic/" + player.getGameInstanceId() + "/emoji", Emoji.class, emoji -> {
            System.out.println(emoji.getType());
            Platform.runLater(() -> mainCtrl.getCurrentQuestionScene().showEmoji(emoji.getType()));
        });

        subscribe("/topic/" + player.getGameInstanceId() + "/postquestion", Answer.class, answer ->
                Platform.runLater(() -> mainCtrl.getCurrentQuestionScene().postQuestion(answer)));

        //TODO FIND WAY TO DEAL WITH SUBCLASSES OF QUESTION
        //TODO MAKE IT SO THAT TIMERS WITHIN QUESTION CLASSES STOP WHEN DISCONNECTED
        subscribe("/topic/" + getPlayer().getGameInstanceId() + "/questionhowmuch", QuestionHowMuch.class, question ->
                Platform.runLater(() -> goToHowMuch(question)));
        subscribe("/topic/" + getPlayer().getGameInstanceId() + "/questionmoreexpensive", QuestionMoreExpensive.class, question ->
                Platform.runLater(() -> goToMoreExpensive(question)));
        subscribe("/topic/" + getPlayer().getGameInstanceId() + "/questionwhichone", QuestionWhichOne.class, question ->
                Platform.runLater(() -> goToWhichOne(question)));

    }

    public void submitAnswer(Answer answer) {
        server.submitAnswer(player, answer);
    }

    public SimpleUser getPlayer() {
        return player;
    }

    public void setPlayer(SimpleUser player) {
        this.player = player;
    }

    private void goToHowMuch(QuestionHowMuch question) {
        mainCtrl.showHowMuch(question);
    }

    private void goToMoreExpensive(QuestionMoreExpensive question) {
        mainCtrl.showMoreExpensive(question);
    }

    private void goToWhichOne(QuestionWhichOne question) {
        mainCtrl.showWhichOne(question);
    }
}
