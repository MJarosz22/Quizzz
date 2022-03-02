package commons;

import java.util.ArrayList;
import java.util.List;

public abstract class Question {

    public String title;
    public Activity[] activities = new Activity[3];
    public List<Integer> usablePowerups;

    public Question(Activity[] activities){
        if(activities.length != 3) throw new IllegalArgumentException();
        System.arraycopy(activities, 0, this.activities, 0, 3);
        usablePowerups = new ArrayList<>();
        setPowerups();
    }

    public abstract void setPowerups();

}
