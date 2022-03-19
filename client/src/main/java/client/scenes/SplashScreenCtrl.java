package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public SplashScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void adminView() {
        mainCtrl.showAdmin();
    }

    public void playSingle() {
        mainCtrl.showSinglePlayerMode();
    }

    public void playMulti() {
        mainCtrl.showMultiPlayerMode();
    }

    public void showLB() {
        mainCtrl.showLeaderBoard();
    }
    
}
