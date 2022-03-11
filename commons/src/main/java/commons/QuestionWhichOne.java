package commons;

public class QuestionWhichOne extends Question{

    private String title;
    private Activity activity;

    public QuestionWhichOne(Activity activity) {
        this.setTitle("How much energy does it take?");
        this.activity = activity;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


}
