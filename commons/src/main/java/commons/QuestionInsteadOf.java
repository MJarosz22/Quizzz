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
        checkIfValidAnswer();
    }

    public QuestionInsteadOf() {}

    public Activity getActivity() {
        return this.activity;
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

    public String[] getAnswers() {
        String[] activitiesTitles = new String[3];
        for(int i = 0; i < 3; i++) {
            activitiesTitles[i] = activities[i].getTitle();
        }
        return activitiesTitles;
    }

    public void checkIfValidAnswer(){
        long activityLowerBound = (long) (activity.getConsumption_in_wh()*0.95);
        long activityUpperBound = (long) (activity.getConsumption_in_wh()*1.05);
        for(int i = 0; i < 3; i++){
            if(activityLowerBound <= activities[i].getConsumption_in_wh() ||
                    activities[i].getConsumption_in_wh() <= activityUpperBound) {
                correctAnswer = i + 1;
            }
            else {
                Random random = new Random();
                correctAnswer = random.nextInt(3);
                activities[correctAnswer-1] = changeActivity(activities[correctAnswer-1]);
            }
        }
    }

    @Override
    public long getCorrectAnswer() {
        return correctAnswer;
    }

    public Activity changeActivity(Activity correctActivity) {
        int times = (int) (activity.getConsumption_in_wh() / correctActivity.getConsumption_in_wh());
        Activity answer = new Activity();
        String title = correctActivity.getTitle();
        if(title.contains("second")) {
            answer.setTitle(changeActivityTitle(times, title, "second"));
        }
        else if(title.contains("minute")) {
            answer.setTitle(changeActivityTitle(times, title, "minute"));
        }
        else if(title.contains("hour")) {
            answer.setTitle(changeActivityTitle(times, title, "hour"));
        }
        else if(title.contains("day")) {
            answer.setTitle(changeActivityTitle(times, title, "day"));
        }
        else if(title.contains("month")) {
            answer.setTitle(changeActivityTitle(times, title, "month"));
        }
        else if(title.contains("year")) {
            answer.setTitle(changeActivityTitle(times, title, "year"));
        }
        else if(title.contains("times")) {
            answer.setTitle(changeActivityTitle(times, title, "times"));
        }
        else {
            answer.setTitle(title + " " + times + " times");
        }
        return answer;
    }

    public String changeActivityTitle(int times, String title, String unit) {
        String[] titleArray = title.split(" ");
        for(int i = 0; i < titleArray.length; i++) {
            if(titleArray[i].contains(unit)) {
                try {
                    times = times*Integer.parseInt(titleArray[i-1]);
                } catch (NumberFormatException e) {
                     return title + " " + times + " times";
                }
                titleArray[i-1] = String.valueOf(times);
                if(!unit.endsWith("s")){
                    titleArray[i] = unit + "s";
                }
                if(unit.endsWith("ly")){
                    titleArray[i] = unit.split("ly")[0];
                }
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < titleArray.length; i++) {
            builder.append(titleArray[i] + " ");
        }
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
