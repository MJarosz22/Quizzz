package client.game.scenes.multiplayer;

import client.utils.ServerUtils;

import java.util.function.Consumer;

public class GameCtrl {

    public void start(){
        ServerUtils.initWebsocket();
    }

    public <T> void subscribe(String destination, Class<T> type, Consumer<T> consumer){
        ServerUtils.registerForMessages(destination, type, consumer);
    }

    public void disconnect(){
        ServerUtils.disconnectWebsocket();
    }



}
