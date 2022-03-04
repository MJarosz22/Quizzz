package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class GameInstance {

    public static final int SINGLE_PLAYER = 0;
    public static final int MULTI_PLAYER = 1;

    private int id;
    private int type;
    private List<Player> players;
    private List<Question> questions;

    public GameInstance(int id, int type) {
        this.id = id;
        if (type < 0 || type > 1) throw new IllegalArgumentException();
        this.type = type;
        players = new ArrayList<>();

        this.questions = generateQuestions();
    }

    private List<Question> generateQuestions() {
        List<Question> questions = new ArrayList<>();
        //TODO REQUEST 20 QUESTIONS FROM SERVER
        return questions;
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
