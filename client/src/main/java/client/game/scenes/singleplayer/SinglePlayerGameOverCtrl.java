package client.game.scenes.singleplayer;

import client.game.scenes.MainCtrl;
import client.game.scenes.pregame.SinglePlayerCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;

public class SinglePlayerGameOverCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public SinglePlayerGameOverCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        SinglePlayerCtrl singlePlayerCtrl = mainCtrl.getSinglePlayerCtrl();
        String previousTextField = singlePlayerCtrl.getPlayerName();
        singlePlayerCtrl.setTextField(previousTextField);
        mainCtrl.showSplash();
    }
}
