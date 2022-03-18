package commons;

import commons.player.Player;
import commons.question.Question;
import commons.question.QuestionHowMuch;
import commons.question.QuestionMoreExpensive;
import commons.question.QuestionWhichOne;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameInstance {

    public static final int SINGLE_PLAYER = 0;
    public static final int MULTI_PLAYER = 1;

    private int id;
    private int type;
    private List<Player> players;
    private List<Question> questions;
    private GameState state = GameState.INLOBBY;

    public GameInstance(int id, int type) {
        this.id = id;
        if (type < 0 || type > 1) throw new IllegalArgumentException();
        this.type = type;
        players = new ArrayList<>();
    }

    /**
     * Generates 20 questions based on 60 activities
     *
     * @param activities List of 60 activities
     */
    public void generateQuestions(List<Activity> activities) {
        /*TODO Make it so that these activities actually get merged into 20 questions and ensure there are no duplicates (if possible)
         In order to make sure there are no duplicates, we can get use of "seeds". */
        if (activities.size() != 60) throw new IllegalArgumentException();
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i%3==2) questions.add(new QuestionMoreExpensive
                    (new Activity[] {activities.get(3*i), activities.get(3*i + 1), activities.get(3*i + 2)}));
            if(i%3 == 1) questions.add(new QuestionHowMuch(activities.get(i)));
            else questions.add(new QuestionWhichOne(activities.get(i)));
        }
        this.questions = questions;
    }

    public Question getRandomQuestion() {
        Random random = new Random();
        int idx = random.nextInt(this.questions.size());
        Question question = this.questions.get(idx);
        this.questions.remove(idx);
        return question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GameInstance that = (GameInstance) o;

        return new EqualsBuilder().append(id, that.id).append(type, that.type)
                .append(players, that.players).append(questions, that.questions).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id).append(type).append(players).append(questions).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .append("players", players)
                .append("questions", questions)
                .toString();
    }
}
