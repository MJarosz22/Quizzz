package commons;

public class MultipleChoiceQuestion extends Question {

    public MultipleChoiceQuestion(Activity[] activities) {
        super(activities);
    }

    public Activity getAnswer() {
        //TODO ADD FUNCTIONAL ANSWER FUNCTION BASED ON LOWEST/HIGHEST CONSUMPTION
        return getActivities()[0];
    }

    @Override
    public void setPowerups() {
        //TODO ADD POWERUPS
    }
}
