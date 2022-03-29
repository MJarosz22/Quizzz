package server.api;

import commons.*;
import commons.player.Player;
import commons.player.SimpleUser;
import commons.powerups.AnswerPU;
import commons.powerups.PointsPU;
import commons.powerups.TimePU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/gameinstance")
public class GameInstanceController {

    //TODO ADD SCORING

    private final Logger logger = LoggerFactory.getLogger(GameInstanceController.class);
    private final List<GameInstanceServer> gameInstances;
    private final GameController gameController;

    public GameInstanceController(GameController gameController) {
        this.gameController = gameController;
        gameInstances = gameController.getGameInstances();
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
        if (currentPlayer == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
                .stream().map(x -> x.unsafe()).collect(Collectors.toList()));
    }

    @DeleteMapping("/{gameInstanceId}/disconnect")
    public ResponseEntity<Boolean> disconnect(@PathVariable int gameInstanceId,
                                              @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        Player removePlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (removePlayer == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        logger.info("[GI " + (gameInstanceId) + "] PLAYER (" + removePlayer.getId() + ") DISCONNECTED");
        return ResponseEntity.ok(gameInstances.get(gameInstanceId).disconnectPlayer(removePlayer));
    }

    @GetMapping("/{gameInstanceId}/getCurrentQType")
    public ResponseEntity<String> getCurrentQType(@PathVariable int gameInstanceId) {
        if (gameInstanceId < 0 || gameInstanceId >= gameInstances.size()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(gameInstances.get(gameInstanceId).getCurrentQuestion().getClass().getName());
    }

    @GetMapping("/{gameInstanceId}/question")
    public ResponseEntity<Question> getQuestion(@PathVariable int gameInstanceId,
                                                @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        Player player = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (player == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        GameInstanceServer gameInstance = gameInstances.get(player.getGameInstanceId());
        if (gameInstance.getState() != GameState.INQUESTION) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gameInstance.getCurrentQuestion());
    }

    @GetMapping("/{gameInstanceId}/timeleft")
    public ResponseEntity<Integer> getTimeLeft(@PathVariable int gameInstanceId,
                                               @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        Player player = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (player == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        GameInstanceServer gameInstance = gameInstances.get(player.getGameInstanceId());
        if (gameInstance.getState() != GameState.INQUESTION && gameInstance.getState() != GameState.POSTQUESTION)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gameInstance.getTimeLeft());
    }

    @PostMapping("/{gameInstanceId}/answer")
    public ResponseEntity<Boolean> answerQuestion(@PathVariable int gameInstanceId, @RequestBody Answer answer,
                                                  @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        Player player = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (player == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        GameInstanceServer gameInstance = gameInstances.get(player.getGameInstanceId());
        if (gameInstance.getState() != GameState.INQUESTION) return ResponseEntity.ok(false);
        return ResponseEntity.ok(gameInstance.answerQuestion(player, answer));
    }


    @GetMapping("/{gameInstanceId}/correctanswer")
    public ResponseEntity<Long> getCorrectAnswer(@PathVariable int gameInstanceId,
                                                 @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        Player player = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (player == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        GameInstanceServer gameInstance = gameInstances.get(player.getGameInstanceId());
        if (gameInstance.getState() != GameState.POSTQUESTION) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gameInstance.getCorrectAnswer());
    }

    @GetMapping("/{gameInstanceId}/start")
    public ResponseEntity<Boolean> startGame(@PathVariable int gameInstanceId,
                                             @CookieValue(name = "user-id", defaultValue = "null") String cookie) {
        if (gameInstances.get(gameInstanceId).getState().equals(GameState.STARTING)) return ResponseEntity.ok(true);
        Player reqPlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (reqPlayer == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        logger.info("[GI " + (gameInstanceId) + "] Game is starting in 5 seconds...");
        gameInstances.get(gameInstanceId).startCountdown();

        return ResponseEntity.ok(true);
    }

    @PostMapping("/{gameInstanceId}/emoji")
    public ResponseEntity<Boolean> sendEmoji(@PathVariable int gameInstanceId,
                                             @CookieValue(name = "user-id", defaultValue = "null") String cookie,
                                             @RequestBody Emoji emoji) {
        if (gameInstances.get(gameInstanceId).getState().equals(GameState.STARTING)) return ResponseEntity.ok(true);
        Player reqPlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (reqPlayer == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        gameInstances.get(gameInstanceId).sendEmoji(emoji);
        logger.info("Emoji received: " + emoji);
        return ResponseEntity.ok(true);
    }

    /**
     * Check if the game is in the right state and make a call to reduce time for players
     *
     * @param gameInstanceId
     * @param cookie
     * @param timePU
     * @return true if the game is in the right state, and the call was made successfully
     */
    @PostMapping("/{gameInstanceId}/decrease-time")
    public ResponseEntity<Boolean> decreaseTime(@PathVariable int gameInstanceId,
                                                @CookieValue(name = "user-id", defaultValue = "null") String cookie,
                                                @RequestBody TimePU timePU) {
        if (!gameInstances.get(gameInstanceId).getState().equals(GameState.INQUESTION))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Player reqPlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (reqPlayer == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        gameInstances.get(gameInstanceId).decreaseTime(timePU);
        logger.info("Time decreased by " + timePU.getPercentage() + "%");
        return ResponseEntity.ok(true);
    }

    /**
     * Check if the game is in the right state and make a call to notify other players that a player used this powerup
     *
     * @param gameInstanceId
     * @param cookie
     * @param answerPU
     * @return true if the game is in the right state, and the call was made successfully
     */
    @PostMapping("/{gameInstanceId}/remove-incorrect-answer")
    public ResponseEntity<Boolean> removeAnswer(@PathVariable int gameInstanceId,
                                                @CookieValue(name = "user-id", defaultValue = "null") String cookie,
                                                @RequestBody AnswerPU answerPU) {
        if (!gameInstances.get(gameInstanceId).getState().equals(GameState.INQUESTION))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Player reqPlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (reqPlayer == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        gameInstances.get(gameInstanceId).removeAnswer(answerPU);
        logger.info(reqPlayer.getName() + " removed an incorrect answer");
        return ResponseEntity.ok(true);
    }

    /**
     * Check if the game is in the right state and make a call to notify other players that a player used this powerup
     *
     * @param gameInstanceId
     * @param cookie
     * @param pointsPU
     * @return true if the game is in the right state, and the call was made successfully
     */
    @PostMapping("/{gameInstanceId}/double-points")
    public ResponseEntity<Boolean> doublePoints(@PathVariable int gameInstanceId,
                                                @CookieValue(name = "user-id", defaultValue = "null") String cookie,
                                                @RequestBody PointsPU pointsPU) {
        if (!gameInstances.get(gameInstanceId).getState().equals(GameState.INQUESTION))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Player reqPlayer = getPlayerFromGameInstance(gameInstanceId, cookie);
        if (reqPlayer == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        gameInstances.get(gameInstanceId).doublePoints(pointsPU);
        logger.info(reqPlayer.getName() + " doubled their points.");
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{gameInstanceId}/gameInstanceType")
    public ResponseEntity<Integer> gameInstanceType(@PathVariable int gameInstanceId) {
        if (gameInstanceId < 0 || gameInstanceId >= gameInstances.size()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(gameInstances.get(gameInstanceId).getType());
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
}
