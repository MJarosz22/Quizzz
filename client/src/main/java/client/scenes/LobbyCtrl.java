package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class LobbyCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public LobbyCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    public void play() {
        //TODO CONNECT TO SERVER
//        mainCtrl.showPlayMode();
    }
}
