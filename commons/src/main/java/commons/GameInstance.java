package commons;

import java.util.ArrayList;
import java.util.List;

public class GameInstance {

    public static final int SINGLE_PLAYER = 0;
    public static final int MULTI_PLAYER = 1;

    public int id;
    public int type;
    public List<Player> players;
    public List<Question> questions;

    public GameInstance(int id, int type){
        this.id = id;
        if(type < 0 || type > 1) throw new IllegalArgumentException();
        this.type = type;
        players = new ArrayList<>();

        this.questions = generateQuestions();
    }

    private void init(){
    }

    private List<Question> generateQuestions(){
        List<Question> questions = new ArrayList<>();
        //TODO REQUEST 20 QUESTIONS FROM SERVER
        return questions;
    }

}
