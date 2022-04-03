package client.scenes;

import com.google.inject.Inject;

public class SplashScreenCtrl {


    private final MainCtrl mainCtrl;

    @Inject
    public SplashScreenCtrl(MainCtrl mainCtrl) {
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
