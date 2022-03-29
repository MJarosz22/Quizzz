package commons;

import commons.player.Player;
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
        if (activities.size() != 60) throw new IllegalArgumentException();
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int remainder = i % 4;
            int mod = i / 4;
            if(questions.size() == 20) break;
            if (remainder == 3) questions.add(new QuestionMoreExpensive
                    (new Activity[]{
                            activities.get(9 * mod + 6),
                            activities.get(9 * mod + 7),
                            activities.get(9 * mod + 8)},
                            i + 1));
            else if (remainder == 2) questions.add(new QuestionHowMuch(activities.get(9 * mod + 5), i + 1));
            else if(remainder == 1) questions.add(new QuestionWhichOne(activities.get(9 * mod + 4), i + 1));
            else questions.add(new QuestionInsteadOf(activities.get(9 * mod),
                        new Activity[]{activities.get(9 * mod + 1),
                                activities.get(9 * mod + 2), activities.get(9 * mod + 3)}, i + 1));
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
