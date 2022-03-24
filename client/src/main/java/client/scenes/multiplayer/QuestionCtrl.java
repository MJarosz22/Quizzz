package client.scenes.multiplayer;

import commons.Answer;

public interface QuestionCtrl {

    public void postQuestion(Answer answer);

    public void resetUI();

    public void showEmoji(String type);
}
