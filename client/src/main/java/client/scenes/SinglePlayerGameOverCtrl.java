package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;

public class SinglePlayerGameOverCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public SinglePlayerGameOverCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method that is triggered when the user presses 'PLAY AGAIN' button in GameOver screen
     */
    public void back() {
        SinglePlayerCtrl singlePlayerCtrl = mainCtrl.getSinglePlayerCtrl();
        String previousTextField = singlePlayerCtrl.getPlayerName();
        singlePlayerCtrl.setTextField(previousTextField);
        SimpleUser player = mainCtrl.getPlayer();
        server.disconnect(player);
        mainCtrl.showSplash();
    }
}
