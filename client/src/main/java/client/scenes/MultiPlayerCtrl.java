package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class MultiPlayerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public MultiPlayerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    /*public void play()
    {
        mainCtrl.showPlayMode();
    }*/
}
