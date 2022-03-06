package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class LeaderBoardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public LeaderBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        mainCtrl.showSplash();
    }
}
