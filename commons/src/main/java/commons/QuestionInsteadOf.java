package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Random;

/**
 * This is a multiple-choice type of question.
 * This question type asks the user to choose, for a certain activity, the activity he/she thinks will match the energy
 * consumption of the given activity.
 * - activity: the Activity object instance that the user is asked to guess how much energy it consumes.
 * - Activity[3] activities: a list (fixed size 3) of Activity Object instances, from which the user has to choose the
 * one that matches the consumption of the single activity.
 * - title: the title of this specific question.
 */
public class QuestionInsteadOf extends Question {

    private Activity activity;
    private Activity[] activities = new Activity[3];
    private int correctAnswer;

    public QuestionInsteadOf(Activity activity, Activity[] activities, int number) {
        this.setTitle("Instead of this, what could you do?");
        this.activity = activity;
        this.activities = activities;
        setNumber(number);
        checkIfValidQuestionActivity();
    }

    public QuestionInsteadOf() {}

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity[] getActivities() {
        return this.activities;
    }

    public void setActivities(Activity[] activities) {
        this.activities = activities;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public long getAnswer() {
        return activities[correctAnswer - 1].getConsumption_in_wh();
    }

    /**
     * Gets the activities' titles to set them as the answers after the radio button
     *
     * @return a String[] with the titles
     */
    public String[] getAnswers() {
        String[] activitiesTitles = new String[3];
        for(int i = 0; i < 3; i++) {
            activitiesTitles[i] = activities[i].getTitle();
        }
        return activitiesTitles;
    }

    @Override
    public long getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Firstly checks if one of the activities somewhat has the same consumption, by looking in a range of 5 percent
     * If that isn't the case, the method picks one activity to be the correct answer and then calls changeActivity
     */
    public void checkIfValidQuestionActivity(){
        long activityLowerBound = (long) (activity.getConsumption_in_wh()*0.95);
        long activityUpperBound = (long) (activity.getConsumption_in_wh()*1.05);
        for(int i = 0; i < 3; i++){
            if(activityLowerBound <= activities[i].getConsumption_in_wh() &&
                    activities[i].getConsumption_in_wh() <= activityUpperBound) {
                correctAnswer = i + 1;
            } else if(i == 2) {
                Random random = new Random();
                correctAnswer = random.nextInt(3) + 1;
                changeActivity(activities[correctAnswer - 1]);
            }
        }
    }

    /**
     * Firstly calculates the amount of times that the given activity has to happen in order for the activity to match
     * the activityAnswer
     * Secondly calls changeActivityTitle
     * If there isn't a clear unit in the question, the method just adds the amount and "times" to the title
     *
     * @param activityAnswer, which is the activity that is the correct answer
     */
    public void changeActivity(Activity activityAnswer) {
        double times = (double) activityAnswer.getConsumption_in_wh() / activity.getConsumption_in_wh();
        String title = activity.getTitle();
        if(title.contains("second")) {
            activity.setTitle(changeActivityTitle(times, title, "second"));
        } else if(title.contains("minute")) {
            activity.setTitle(changeActivityTitle(times, title, "minute"));
        } else if(title.contains("hour")) {
            activity.setTitle(changeActivityTitle(times, title, "hour"));
        } else if(title.contains("day")) {
            activity.setTitle(changeActivityTitle(times, title, "day"));
        } else if(title.contains("month")) {
            activity.setTitle(changeActivityTitle(times, title, "month"));
        } else if(title.contains("year")) {
            activity.setTitle(changeActivityTitle(times, title, "year"));
        } else if(title.contains("times")) {
            activity.setTitle(changeActivityTitle(times, title, "times"));
        } else if(times == 1) {
            times = ((int)(times * 100))/100d;
            activity.setTitle(title + " " + times + " time");
        } else {
            times = ((int)(times * 100))/100d;
            activity.setTitle(title + " " + times + " times");
        }
    }

    /**
     * Changes the title of the given activity, so that the amount matches the consumption of the randomly chosen
     * correct activity in checkIfValidQuestionActivity
     *
     * @param times, the amount of times calculated in changeActivity
     * @param title, the title of the activity that needs to change
     * @param unit, the unit that was in the title
     * @return the new title of the activity that needs to change
     */
    public String changeActivityTitle(double times, String title, String unit) {
        String[] titleArray = title.split(" ");
        for(int i = 0; i < titleArray.length; i++) {
            if(titleArray[i].contains(unit)) {
                try {
                    times = (int)(times*Integer.parseInt(titleArray[i-1]) * 100) / 100d;
                } catch (NumberFormatException e) {
                    times = ((int)(times * 100))/100d;
                    return title + " " + times + " times";
                }
                titleArray[i-1] = String.valueOf(times);
                if(!unit.endsWith("s") && times != 1){
                    titleArray[i] = unit + "s";
                } else if(unit.endsWith("ly")){
                    titleArray[i] = unit.split("ly")[0];
                }
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < titleArray.length-1; i++) {
            builder.append(titleArray[i] + " ");
        }
        builder.append(titleArray[titleArray.length-1]);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        QuestionInsteadOf q = (QuestionInsteadOf) o;

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
