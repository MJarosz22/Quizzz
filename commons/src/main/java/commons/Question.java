package commons;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

public abstract class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int type;
    private String title;
    private List<Integer> disabledPowerUps;

    public Question(int type, String title, List<Integer> disabledPowerUps) {
        this.type = type;
        this.title = title;
        this.disabledPowerUps = disabledPowerUps;
    }

    public Question() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id
                && type == question.type
                && Objects.equals(title, question.title)
                && Objects.equals(disabledPowerUps, question.disabledPowerUps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, title, disabledPowerUps);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getDisabledPowerUps() {
        return disabledPowerUps;
    }

    public void setDisabledPowerUps(List<Integer> disabledPowerUps) {
        this.disabledPowerUps = disabledPowerUps;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", disabledPowerUps=" + disabledPowerUps;
    }
}
