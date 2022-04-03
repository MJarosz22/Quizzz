package client.scenes;

import com.google.inject.Inject;

public class SplashScreenCtrl {


    private final MainCtrl mainCtrl;

    @Inject
    public SplashScreenCtrl(MainCtrl mainCtrl) {
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
