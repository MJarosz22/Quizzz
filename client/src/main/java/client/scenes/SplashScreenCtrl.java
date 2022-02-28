package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public SplashScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void playSingle()
    {
        mainCtrl.showSinglePlayerMode();
    }

    public void playMulti()
    {
        mainCtrl.showMultiPlayerMode();
    }

    public void showLB()
    {
        mainCtrl.showLeaderBoard();
    }


}
