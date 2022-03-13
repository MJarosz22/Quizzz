package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This is a multiple-choice type of question.
 * This question type asks the user to choose from the 3 given activities the one that consumes the highest amount of energy.
 * - Activity[3] activities: a list (fixed size 3) of Activity object instances, from which the user is asked to choose the
 * one that he/she thinks is the most energy-consuming.
 * - title: the title of this specific question, may be changed later to "Choose which activity from these 3 you think
 * it consumes the most energy: ".
 */
public class QuestionMoreExpensive extends Question {

    private Activity[] activities = new Activity[3];

    public QuestionMoreExpensive(Activity[] activities) {
        this.setTitle("What requires more energy?");
        this.activities = activities;
    }

    public QuestionMoreExpensive() {
    }

    public Activity[] getActivities() {
        return this.activities;
    }

    public void setActivities(Activity[] activities) {
        this.activities = activities;
    }

    /**
     * Additional method that returns the highest consumption from the 3 activities.
     * In other words, the consumption associated to the activity that consumes the most energy.
     *
     * @return the maximum consumption of an activity
     */
    public long getAnswer() {
        long max = 0;
        if (activities[0].getConsumption_in_wh() > max) max = activities[0].getConsumption_in_wh();
        if (activities[1].getConsumption_in_wh() > max) max = activities[1].getConsumption_in_wh();
        if (activities[2].getConsumption_in_wh() > max) max = activities[2].getConsumption_in_wh();
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        QuestionMoreExpensive q = (QuestionMoreExpensive) o;

        return new EqualsBuilder()
                .append(getTitle(), q.getTitle())
                .append(activities, q.activities)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getTitle())
                .append(activities)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", getTitle())
                .append("activities", activities)
                .toString();
    }

}
