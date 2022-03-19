package client.game.scenes.multiplayer;

import client.game.Main;
import client.game.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.player.Player;
import commons.player.SimpleUser;
import commons.question.Answer;
import org.apache.catalina.Server;

import javax.inject.Inject;
import java.util.function.Consumer;

public class GameCtrl {

    private SimpleUser player;

    ServerUtils server;
    MainCtrl mainCtrl;

    @Inject
    public GameCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void start(){
        ServerUtils.initWebsocket();
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
}
