package server.api;


import commons.GameInstance;
import commons.Question;
import commons.player.Player;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/game")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final ActivityRepository activityRepository;
    private final Random random;
    private final List<GameInstance> gameInstances;
    private final List<SimpleUser> players;
    private static int currentMPGIId = 0; //Current ID of gameInstance for multiplayer


    /**
     * Creates the GameController and initializes the first gameInstance
     *
     * @param random             Random class
     * @param activityRepository Repository of all Activities
     */
    public GameController(Random random, ActivityRepository activityRepository) {
        this.random = random;
        this.activityRepository = activityRepository;
        System.out.println(this.activityRepository.findAll());
        gameInstances = new ArrayList<>();
        gameInstances.add(new GameInstance(gameInstances.size(), GameInstance.MULTI_PLAYER));
/*
        //TODO Make it so that these activities actually get merged into 20 questions and ensure there are no duplicates (if possible)
        // TODO: In order to make sure there are no duplicates, we can get use of "seeds".
        Activity[] activities = new Activity[60];
        List<Activity> allActivities = activityRepository.findAll();
        for (int i = 0; i < 60; i++) {
            activities[i] = allActivities.get(random.nextInt(allActivities.size()));
        }
        gameInstances.get(0).generateQuestions(activities);

 */
        players = new ArrayList<>();
    }

//    ---------------------------------------------------------------------------
//    ---------------------------     PRE-LOBBY     -----------------------------
//    ---------------------------------------------------------------------------

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
                GameInstance gameInstance = new GameInstance(gameInstances.size(), GameInstance.SINGLE_PLAYER);
                gameInstances.add(gameInstance);
                savedPlayer = new SimpleUser(players.size(), request.getName(),
                        gameInstance.getId(), tokenCookie.getValue());
                players.add(savedPlayer);
                gameInstance.getPlayers().add(savedPlayer.toPlayer(gameInstance));
                logger.info("[GI " + (gameInstance.getId()) + "] PLAYER (" + savedPlayer.getId() +
                        ") STARTED SP GAME: NAME=" + savedPlayer.getName());
                //GameInstance newGameInstance = new GameInstance(gameInstances.size(), GameInstance.SINGLE_PLAYER);
                //gameInstances.add(newGameInstance);
                //currentMPGIId = newGameInstance.getId();
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

    /**
     * Gets a question from gameInstance
     *
     * @param gameInstanceId The gameInstance you want a question from
     * @param questionNumber Number of question you request
     * @param cookie         Cookie of player
     * @return Requested question
     */
    @GetMapping("/{gameInstanceId}/q{questionNumber}")
    public ResponseEntity<Question> getQuestion(@PathVariable int gameInstanceId, @PathVariable int questionNumber,
                                                @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        if (gameInstanceId < 0 || gameInstanceId > gameInstances.size() - 1
                || questionNumber > 19 || questionNumber < 0) return ResponseEntity.badRequest().build();

        Player currentPlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (currentPlayer == null) return ResponseEntity.badRequest().build();
        GameInstance currGI = gameInstances.get(gameInstanceId);
        logger.info("[GI " + (currGI.getId()) + "] PLAYER (" + currentPlayer.getId() + ") REQUESTED QUESTION N. " + questionNumber);
        Question question = currGI.getQuestions().get(questionNumber);
        return ResponseEntity.ok(question);
    }


    /**
     * Returns all players from a gameInstance (if you are also connected to that gameInstance)
     *
     * @param gameInstanceId ID of GameInstance
     * @param cookie         Cookie of player
     * @return List of all players connected to gameInstance
     */
    @GetMapping("/{gameInstanceId}/players")
    public ResponseEntity<List<SimpleUser>> getPlayers(@PathVariable int gameInstanceId,
                                                       @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        if (getPlayerFromGameInstance(gameInstanceId, cookie) == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(gameInstances.get(gameInstanceId).getPlayers()
                .stream().map(p -> p.toSimpleUser().unsafe()).collect(Collectors.toList()));
    }

    @DeleteMapping("/{gameInstanceId}/disconnect")
    public ResponseEntity<Boolean> disconnect(@PathVariable int gameInstanceId,
                                              @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        Player removePlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (removePlayer == null) return ResponseEntity.badRequest().build();
        logger.info("[GI " + (gameInstanceId) + "] PLAYER (" + removePlayer.getId() + ") DISCONNECTED");
        return ResponseEntity.ok(gameInstances.get(gameInstanceId).getPlayers().remove(removePlayer));
    }

    /**
     * Additional method that checks whether cookie given is from a player connected to gameInstance with ID
     *
     * @param gameInstanceId ID of GameInstance
     * @param cookie         Cookie of player
     * @return An instance of class 'Player' if exists, otherwise null
     */
    private Player getPlayerFromGameInstance(int gameInstanceId, String cookie) {
        GameInstance currGI = gameInstances.get(gameInstanceId);
        Optional<Player> optPlayer = currGI.getPlayers().stream().filter(p -> p.getCookie().equals(cookie)).findFirst();
        if (optPlayer.isEmpty()) return null;
        else return optPlayer.get();
    }

    /**
     * Method that returns last instance of a multiplayer ID
     *
     * @return the ID of the last Multiplayer Game Instance
     */
    @GetMapping("/getLastGIIdMult")
    public ResponseEntity<Integer> getLastGIIdMult() {
        if (currentMPGIId < 0 || currentMPGIId > gameInstances.size()) return  ResponseEntity.badRequest().build();
        return ResponseEntity.ok(currentMPGIId);
    }

    /**
     * Additional method that returns the player list of a game instance
     *
     * @param gameInstanceId ID of GameInstance
     * @return the ID of the last Multiplayer Game Instance
     */
    @GetMapping("/{gameInstanceId}/playerlist")
    public ResponseEntity<List<SimpleUser>> getPlayerList(@PathVariable int gameInstanceId) {
        if (currentMPGIId < 0 || currentMPGIId > gameInstances.size()) return  ResponseEntity.badRequest().build();
        return ResponseEntity.ok(gameInstances.get(gameInstanceId).getPlayers()
                .stream().map(p -> p.toSimpleUser().unsafe()).collect(Collectors.toList()));
    }

    /*
    @GetMapping("/getLastGIId")
    public ResponseEntity<Integer> getLastGIId() {
        int lastGIId = gameInstances.get(gameInstances.size() - 1).getId();
        logger.info("[GI " + lastGIId);
        return ResponseEntity.ok(lastGIId);
    }
     */
}
