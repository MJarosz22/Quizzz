package server.api;

import commons.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.beans.PropertyEditor;
import java.util.*;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final Random random;

    private final ActivityRepository activityRepository;
    private final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    public ActivityController(Random random, ActivityRepository activityRepository) {
        this.random = random;
        this.activityRepository = activityRepository;
    }

    @GetMapping(path = {"", "/"})
    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity) {
        if (isNullOrEmpty(activity.getSource())
                || isNullOrEmpty(activity.getId())
                || !isValidUrl(activity.getSource())
                || isNullOrEmpty(activity.getTitle())
                || !isValidTitle(activity.getTitle())
                || activity.getConsumption_in_wh() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Activity savedActivity = activityRepository.save(new Activity(
                activity.getId(),
                activity.getImage_path(),
                activity.getTitle(),
                activity.getConsumption_in_wh(),
                activity.getSource()));
        return ResponseEntity.ok(savedActivity);
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


    /**
     * Method that validates an activity title
     * A valid title should have <= 140 characters and be one-sentenced.
     * Note that we consider a title to be valid even if it does not have an end of sentence punctuatio('.', '?' or '!'
     *
     * @param title - String object that needs to be validated
     * @return - true, if the given title is valide, or false otherwise.
     */
    private static boolean isValidTitle(String title) {
        int endOfSentence = 0;
        int size = title.length();
        for (char ch : title.toCharArray()) {
            if (ch == '.' || ch == '!' || ch == '?')
                endOfSentence++;
        }

        return size <= 140 && endOfSentence <= 1;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable("id") long id, @RequestBody Activity activity) {
        Optional<Activity> activityData = activityRepository.findById(id);
        if (activityData.isPresent()) {
            Activity newActivity = activityData.get();
            if (!isNullOrEmpty(activity.getId())) newActivity.setId(activity.getId());
            newActivity.setImage_path(activity.getImage_path());
            if (!isNullOrEmpty(activity.getTitle()) && isValidTitle(activity.getTitle()))
                newActivity.setTitle(activity.getTitle());
            if (activity.getConsumption_in_wh() > 0) newActivity.setConsumption_in_wh(activity.getConsumption_in_wh());
            if (!isNullOrEmpty(activity.getSource()) && isValidUrl(activity.getSource()))
                newActivity.setSource(activity.getSource());
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

    /**
     * GET API endpoint that 'smartly' generates 60 optional activities using a HashSet(for performance reasons)
     * for one gameInstance. This method is used in the SinglePlayerGameCtrl when initialising one specific gameInstance, so that questions
     * can be generated on the basis of our randomly selected activities.
     *
     * @return 200 OK - in case there is at least one activity that can be chosen, or 404 NOT_FOUND otherwise
     */
    @GetMapping("/random60")
    public ResponseEntity<List<Activity>> getRandom60() {
        //hard coded -> size of all activities - 60
        long countIds = activityRepository.count();
        if(activityRepository.count() == 0) {
            logger.error("No activities found for lobby...");
            return ResponseEntity.notFound().build();
        }
        int idRandom = (int) Math.abs(Math.random() * countIds) - 60;
        Set<Activity> setOfActivities = new HashSet<>();
        int limit = 60;
        int i = 0;
        long random_consumption = (long) ((Math.random() * (10000 - 50)) + 50);
        long random_consumption_max = random_consumption +(50*random_consumption)/ 100;
        long random_consumption_min = random_consumption -(50*random_consumption)/ 100;
        while (i < limit) {
            Optional<Activity> a = activityRepository.findById((long) idRandom);
            if (a.isPresent() && !setOfActivities.contains(a.get()) && a.get().getConsumption_in_wh() <=random_consumption_max
                    && a.get().getConsumption_in_wh()>=random_consumption_min) {
                setOfActivities.add(a.get());
                i++;
            }
            idRandom = (int) Math.abs(Math.random() * countIds) - 60;
        }
        if (setOfActivities.isEmpty()) return ResponseEntity.notFound().build();
        List<Activity> listOfActivities = new ArrayList<>(setOfActivities);
        return ResponseEntity.ok(listOfActivities);
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
