package server.api;


import commons.GameInstance;
import commons.communication.RequestToJoin;
import commons.player.SimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityLoader;
import server.database.ActivityRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("api/game")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final ActivityRepository activityRepository;
    private SimpMessagingTemplate msgs;
    protected ActivityController activityController;
    private final Random random;
    private final List<ServerGameInstance> gameInstances;
    private final List<SimpleUser> players;
    private static int currentMPGIId; //Current ID of gameInstance for multiplayer


    /**
     * Creates the GameController and initializes the first gameInstance
     *
     * @param random             Random class
     * @param activityRepository Repository of all Activities
     */
    public GameController(Random random, ActivityRepository activityRepository, SimpMessagingTemplate msgs, ActivityController activityController) {
        this.random = random;
        this.activityRepository = activityRepository;
        this.msgs = msgs;
        this.activityController = activityController;
        gameInstances = new ArrayList<>();
        gameInstances.add(new ServerGameInstance(gameInstances.size(), GameInstance.MULTI_PLAYER, this, msgs));

        players = new ArrayList<>();
    }


    /**
     * Lets a client join a gameInstance as a player
     *
     * @param request Request of player (includes name of player and gameType(Singleplayer or Multiplayer))
     * @return Simple User (Including name, cookie and gameInstanceID)
     */
    @PostMapping("/join")
    public ResponseEntity<SimpleUser> addPlayer(@RequestBody RequestToJoin request) {
        if (request == null) return ResponseEntity.badRequest().build();
        ResponseCookie tokenCookie = ResponseCookie.from("user-id", DigestUtils.md5DigestAsHex(
                (request.getName() + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8))).build();

        SimpleUser savedPlayer;
        switch (request.getGameType()) {
            case GameInstance.SINGLE_PLAYER:
                ServerGameInstance gameInstance = new ServerGameInstance(gameInstances.size(), GameInstance.SINGLE_PLAYER, this, msgs);
                gameInstances.add(gameInstance);
                savedPlayer = new SimpleUser(players.size(), request.getName(),
                        gameInstance.getId(), tokenCookie.getValue());
                players.add(savedPlayer);
                gameInstance.getPlayers().add(savedPlayer.toPlayer(gameInstance));
                logger.info("[GI " + (gameInstance.getId()) + "] PLAYER (" + savedPlayer.getId() +
                        ") STARTED SP GAME: NAME=" + savedPlayer.getName());
                break;

            case GameInstance.MULTI_PLAYER:
                GameInstance currGameInstance = gameInstances.get(currentMPGIId);
                savedPlayer = new SimpleUser(players.size(), request.getName(),
                        currGameInstance.getId(), tokenCookie.getValue());
                players.add(savedPlayer);
                currGameInstance.getPlayers().add(savedPlayer.toPlayer(currGameInstance));
                logger.info("[GI " + (currGameInstance.getId()) + "] PLAYER (" + savedPlayer.getId() +
                        ") JOINED: NAME=" + savedPlayer.getName());
                break;

            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).body(savedPlayer);
    }

    @GetMapping(value = "/activities/{activityFolder}/{activityFile}",
            produces = "image/jpg")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String activityFolder, @PathVariable String activityFile) {
        try {
            InputStream inputStream = new FileInputStream(ActivityLoader.path + activityFolder + "/" + activityFile);
            return ResponseEntity.ok(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e) {
            logger.debug("Image " + activityFolder + "/" + activityFile + " not found!");
        }
        return ResponseEntity.notFound().build();
    }


    @MessageMapping("/time")
    @SendTo("/topic/time")
    public int setTime(int time) {
        return time;
    }

    public List<ServerGameInstance> getGameInstances() {
        return gameInstances;
    }

}
