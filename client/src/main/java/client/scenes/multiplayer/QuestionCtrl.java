package client.scenes.multiplayer;

import commons.Answer;
import commons.player.SimpleUser;
import commons.powerups.PowerUp;

public interface QuestionCtrl {

    public void postQuestion(Answer answer);

    public void resetUI();

    public void showEmoji(String type);

    public void showDisconnect(SimpleUser playerDisconnect);

    public void reduceTimer(int percentage);

    public void setPowerUps();

    public void showPowerUpUsed(PowerUp powerUp);
}
