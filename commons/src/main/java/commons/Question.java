package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class Question {

    @Id
    private String title;

    private Activity[] activities = new Activity[3];
    private PowerUp[] usablePowerups;

    public Question(Activity[] activities) {
        if (activities.length != 3) throw new IllegalArgumentException();
        System.arraycopy(activities, 0, this.activities, 0, 3);
        usablePowerups = new PowerUp[]{};
        setPowerups();
    }

    public abstract void setPowerups();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Activity[] getActivities() {
        return activities;
    }

    public void setActivities(Activity[] activities) {
        this.activities = activities;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return new EqualsBuilder().append(title, question.title)
                .append(activities, question.activities).append(usablePowerups, question.usablePowerups).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(title).append(activities).append(usablePowerups).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("activities", activities)
                .append("usablePowerups", usablePowerups)
                .toString();
    }
}
