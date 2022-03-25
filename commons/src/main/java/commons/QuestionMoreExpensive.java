package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Comparator;

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

    public QuestionMoreExpensive(Activity[] activities, int number) {
        this.setTitle("What requires more energy?");
        this.activities = activities;
        setNumber(number);
    }

    public QuestionMoreExpensive() {
    }

    /**
     * Getter for the 3 activities provided to the user as options. He/she will be asked to choose which one he/she considers
     * to consume more energy
     *
     * @return a list (fixed size 3) of Activity object instances, from which the user is asked to choose the
     * one that he/she thinks is the most energy-consuming
     */
    public Activity[] getActivities() {
        return this.activities;
    }

    /**
     * Setter for the 3 activities provided to the user as options
     *
     * @param activities a list (fixed size 3) of Activity object instances, from which the user is asked to choose the
     *                   one that he/she thinks is the most energy-consuming
     */
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

    /**
     * Additional methods that returns what number is assigned to the highest consumption activity from the 3 selected
     * 1 for the 1st option, 2 for the 2nd option and 3 for the 3rd option.
     * NOTE: 'activities' array was indexed starting from 0, (i+1) MUST have been returned instead.
     *
     * @return Index of the option that relates to the highest consumption activity
     */
    @Override
    public long getCorrectAnswer() {
        Activity maxActivity = Arrays.stream(activities).max(Comparator.comparingLong(Activity::getConsumption_in_wh)).get();
        for (int i = 0; i < 3; i++) {
            if (activities[i].equals(maxActivity)) return i + 1;
        }
        return 0;
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
