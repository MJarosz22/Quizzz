package server.api;

import commons.Activity;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Optional;
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

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    
    /**
     * Method that checks whether the source of an activity is a valid URL
     *
     * @param url - String object that is expected to be an URL
     * @return - true, if the given string is an URL, or false otherwise.
     */
    private static boolean isValidUrl(String url) {
        try {
            PropertyEditor urlEditor = new URLEditor();
            urlEditor.setAsText(url);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @GetMapping(path = {"", "/"})
    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity) {
        if (isNullOrEmpty(activity.source) || !isValidUrl(activity.source) || isNullOrEmpty(activity.title)
                || activity.title.length() > 140 || activity.consumption <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Activity savedActivity = activityRepository.save(new Activity(activity.title, activity.consumption, activity.source));
        return ResponseEntity.ok(savedActivity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable("id") long id, @RequestBody Activity activity) {
        Optional<Activity> activityData = activityRepository.findById(id);
        if (activityData.isPresent()) {
            Activity newActivity = activityData.get();
            if (!isNullOrEmpty(activity.title)) newActivity.title = activity.title;
            if (activity.consumption > 0) newActivity.consumption = activity.consumption;
            if (!isNullOrEmpty(activity.source) && isValidUrl(activity.source)) newActivity.source = activity.source;
            return ResponseEntity.ok(activityRepository.save(newActivity));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Activity> deleteActivity(@PathVariable("id") long id) {
        if (activityRepository.existsById(id)) {
            try {
                activityRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/random")
    public ResponseEntity<Activity> getRandom() {
        List<Activity> allAct = getAll();
        if (allAct.size() == 0) return ResponseEntity.notFound().build();
        int idx = random.nextInt(allAct.size());
        return ResponseEntity.ok(allAct.get(idx));
    }

    @DeleteMapping("/all")
    public ResponseEntity<Activity> deleteAll() {
        try {
            activityRepository.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

}
