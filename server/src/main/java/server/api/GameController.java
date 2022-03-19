package server.api;


import commons.GameInstance;
import commons.Question;
import commons.player.Player;
import commons.GameInstance;
import commons.communication.RequestToJoin;
import commons.player.SimpleUser;
import commons.question.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MessageConverter;
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
    private final List<GameInstanceServer> gameInstances;
    private final List<SimpleUser> players;
    private static int currentMPGIId = 0; //Current ID of gameInstance for multiplayer


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
        gameInstances.add(new GameInstanceServer(gameInstances.size(), GameInstance.MULTI_PLAYER, this, msgs));
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
                GameInstanceServer gameInstance = new GameInstanceServer(gameInstances.size(), GameInstance.SINGLE_PLAYER, this, msgs);
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
                GameInstanceServer currGameInstance = gameInstances.get(currentMPGIId);
                savedPlayer = new SimpleUser(players.size(), request.getName(),
                        currGameInstance.getId(), tokenCookie.getValue());
                players.add(savedPlayer);
                currGameInstance.getPlayers().add(savedPlayer.toPlayer(currGameInstance));
                logger.info("[GI " + (currGameInstance.getId()) + "] PLAYER (" + savedPlayer.getId() +
                        ") JOINED: NAME=" + savedPlayer.getName());
                currGameInstance.updatePlayerList();
                break;

            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).body(savedPlayer);
    }

    /**
     * Gets a question from gameInstance
     * @param gameInstanceId The gameInstance you want a question from
     * @param questionNumber Number of question you request
     * @param cookie Cookie of player
     * @return Requested question
     */
    @GetMapping("/{gameInstanceId}/q{questionNumber}")
    public ResponseEntity<Question> getQuestion(@PathVariable int gameInstanceId, @PathVariable int questionNumber,
                                                @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        if (gameInstanceId < 0 || gameInstanceId > gameInstances.size() - 1
                || questionNumber > 19 || questionNumber < 0) return ResponseEntity.badRequest().build();



    /**
     * Returns all players from a gameInstance (if you are also connected to that gameInstance)
     * @param gameInstanceId ID of GameInstance
     * @param cookie Cookie of player
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
        if(removePlayer == null) return ResponseEntity.badRequest().build();
        logger.info("[GI " + (gameInstanceId) + "] PLAYER (" + removePlayer.getId() + ") DISCONNECTED");
        return ResponseEntity.ok(gameInstances.get(gameInstanceId).getPlayers().remove(removePlayer));
    }

    @GetMapping(value = "/activities/{activityFolder}/{activityFile}",
    produces = "image/jpg")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String activityFolder, @PathVariable String activityFile){
        try{
            InputStream inputStream = new FileInputStream(ActivityLoader.path + activityFolder + "/" + activityFile);
            return ResponseEntity.ok(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e) {
            logger.debug("Image " + activityFolder + "/" + activityFile + " not found!");
        }
        return ResponseEntity.notFound().build();
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

    @MessageMapping("/time")
    @SendTo("/topic/time")
    public int setTime(int time){
        return time;
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
    @MessageMapping("/question")
    @SendTo("/topic/question")
    public Question test(Question question){
        return question;
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
    public void createNewMultiplayerLobby(){
        GameInstanceServer newGameInstance = new GameInstanceServer(gameInstances.size(), GameInstance.MULTI_PLAYER, this, msgs);
        gameInstances.add(newGameInstance);
        currentMPGIId = newGameInstance.getId();
    }

    public List<GameInstanceServer> getGameInstances(){
        return gameInstances;
    }

}
