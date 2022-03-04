package server.api;


import commons.Activity;
import commons.GameInstance;
import commons.Player;
import commons.SimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/game")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final ActivityRepository activityRepository;
    private final Random random;
    private final List<GameInstance> gameInstances;
    private final List<SimpleUser> players;


    /**
     * Creates the GameController and initializes the first gameInstance
     * @param random Random class
     * @param activityRepository Repository of all Activities
     */
    public GameController(Random random, ActivityRepository activityRepository){
        this.random = random;
        this.activityRepository = activityRepository;
        System.out.println(this.activityRepository.findAll());
        gameInstances = new ArrayList<>();
        gameInstances.add(new GameInstance(gameInstances.size(), GameInstance.MULTI_PLAYER));

        //TODO Make it so that these activities actually get merged into 20 questions and ensure there are no duplicates (if possible)
        Activity[] activities = new Activity[60];
        List<Activity> allActivities = activityRepository.findAll();
        for(int i = 0; i < 60; i++) {
            activities[i] = allActivities.get(random.nextInt(allActivities.size()));
        }

        players = new ArrayList<>();
    }

//    ---------------------------------------------------------------------------
//    ---------------------------     PRE-LOBBY     -----------------------------
//    ---------------------------------------------------------------------------

    /**
     * Lets a client join a gameInstance as a player
     * @param name Name of new player
     * @return Simple User (Including name, cookie and gameInstanceID)
     */
    @PostMapping("/join")
    public ResponseEntity<SimpleUser> addPlayer(@RequestBody String name){
        if(isNullOrEmpty(name)) return ResponseEntity.badRequest().build();

        ResponseCookie tokenCookie = ResponseCookie.from("user-id", DigestUtils.md5DigestAsHex(
                                (name + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8))).build();

        SimpleUser savedPlayer = new SimpleUser(players.size(), name, gameInstances.get(gameInstances.size() - 1).getId(), tokenCookie.getValue());
        players.add(savedPlayer);
        gameInstances.get(gameInstances.size() - 1).getPlayers().add(savedPlayer.toPlayer(gameInstances.get(gameInstances.size() - 1)));
        logger.info("[GI " + (gameInstances.size() - 1) + "] PLAYER ("+ savedPlayer.getId() + ") JOINED: NAME=" + savedPlayer.getName());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).body(savedPlayer);
    }

//    ---------------------------------------------------------------------------
//    --------------------     HELPER FUNCTIONS     -----------------------------
//    ---------------------------------------------------------------------------

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
