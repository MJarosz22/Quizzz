package commons;

public class QuestionHowMuch extends Question{

    private String title;
    private Activity activity;

    public QuestionHowMuch(Activity activity) {
        this.title = "How much energy does it take?";
        this.activity = activity;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
