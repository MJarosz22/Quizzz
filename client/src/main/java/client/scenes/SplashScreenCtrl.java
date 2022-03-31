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

    /**
     * Shows the admin panel when the "ADMIN" button is pressed
     */
    public void adminView() {
        mainCtrl.showAdmin();
    }

    /**
     * Shows the single player game when the "SINGLE PLAYER" button is pressed
     */
    public void playSingle() {
        mainCtrl.showSinglePlayerMode();
    }

    /**
     * Shows the multiplayer game when the "MULTIPLAYER" button is pressed
     */
    public void playMulti() {
        mainCtrl.showMultiPlayerMode();
    }

    /**
     * Shows the global leaderboard when the "GLOBAL LEADERBOARD" button is pressed
     */
    public void showLB() {
        mainCtrl.showLeaderBoard();
    }
    
}
