package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final Random random;
    private final ActivityRepository activityRepository;

    public ActivityController(Random random, ActivityRepository activityRepository) {
        this.random = random;
        this.activityRepository = activityRepository;
    }

    @GetMapping(path = {"", "/"})
    public List<Activity> getAll(){
        return activityRepository.findAll();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Activity> add(@RequestBody Activity activity){
        if(isNullOrEmpty(activity.source) || isNullOrEmpty(activity.title) || activity.title.length() > 140){
            return ResponseEntity.badRequest().build();
        }
        Activity savedActivity = activityRepository.save(new Activity(activity.title, activity.consumption, activity.source));
        return ResponseEntity.ok(savedActivity);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
