package server.api;


import commons.GameInstance;
import commons.GameState;
import commons.player.Player;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityLoader;
import server.database.ActivityRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/game")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final SimpMessagingTemplate msgs;
    public ActivityController activityController;
    private final Random random;
    private final List<GameInstanceServer> gameInstances;
    private final List<SimpleUser> players;
    private Map<String, Integer> serverNames;
    private static int currentMPGIId = 0; //Current ID of gameInstance for multiplayer
    private static int currentSPGIId = 0; //Current ID of gameInstance for singleplayer


    /**
     * Creates the GameController and initializes the first gameInstance
     *
     * @param random             Random class
     * @param activityRepository Repository of all Activities
     */
    public GameController(Random random, ActivityRepository activityRepository, SimpMessagingTemplate msgs, ActivityController activityController) {
        this.random = random;
        this.msgs = msgs;
        this.activityController = activityController;
        this.gameInstances = new ArrayList<>();
        this.players = new ArrayList<>();
        this.serverNames = new HashMap<>();

        addServer("default");
        addServer("first");
        addServer("second");

        // ASSUMPTION: we consider the current last multiplayerGameInstanceID to be 0 (the one matching "default" serverName)
        currentMPGIId = 0;
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
                GameInstanceServer gameInstance = new GameInstanceServer(gameInstances.size(), GameInstance.SINGLE_PLAYER, this, msgs, null);
                gameInstances.add(gameInstance);
                currentSPGIId = gameInstance.getId();
                savedPlayer = new SimpleUser(players.size(), request.getName(),
                        gameInstance.getId(), tokenCookie.getValue());
                players.add(savedPlayer);
                gameInstance.getPlayers().add(savedPlayer.toPlayer(gameInstance));
                logger.info("[GI " + (gameInstance.getId()) + "] PLAYER (" + savedPlayer.getId() +
                        ") STARTED SP GAME: NAME=" + savedPlayer.getName());
                break;

            case GameInstance.MULTI_PLAYER:
                GameInstanceServer currGameInstance;
                if (serverNames.containsKey(request.getServerName())) {
                    currGameInstance = gameInstances.get(serverNames.get(request.getServerName()));
                } else {
                    throw new IllegalArgumentException("Server not found!");
                }
                if (currGameInstance.getState() != GameState.INLOBBY)
                    throw new IllegalArgumentException("Wait for the game to end!");
                savedPlayer = new SimpleUser(players.size(), request.getName(),
                        currGameInstance.getId(), tokenCookie.getValue());
                players.add(savedPlayer);
                currGameInstance.getPlayers().add(savedPlayer.toPlayer(currGameInstance));
                logger.info("[GI " + (currGameInstance.getId()) + "] PLAYER (" + savedPlayer.getId() +
                        ") ENTERED MP LOBBY: NAME=" + savedPlayer.getName());
                if (msgs != null)
                    currGameInstance.updatePlayerList();
                break;

            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).body(savedPlayer);
    }

    /**
     * Method that returns an InputStream object instance that resides at the specific {activityFolder}/{activityFile}
     *
     * @param activityFolder folder with the activities
     * @param activityFile   the wanted file
     * @return InputStream with the file
     */
    @GetMapping(value = "/activities/{activityFolder}/{activityFile}",
            produces = "image/jpg")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String activityFolder, @PathVariable String activityFile) {
        try {
            InputStream inputStream = new FileInputStream(ActivityLoader.path + activityFolder + "/" + activityFile);
            return ResponseEntity.ok(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e) {
            logger.debug("Image " + activityFolder + "/" + activityFile + " not found!");
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Method that returns last instance of a multiplayer ID
     *
     * @return the ID of the last Multiplayer Game Instance
     */
    @GetMapping("/getLastGIIdMult")
    public ResponseEntity<Integer> getLastGIIdMult() {
        if (currentMPGIId < 0 || currentMPGIId >= gameInstances.size()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(currentMPGIId);
    }

    /**
     * Additional method that returns all the players that have participated in a given game instance
     *
     * @param gameInstanceId ID of GameInstance
     * @return ResponseEntity object that returns 400 BAD SYNTAX if the gameInstanceId is not in the appropriate range, or
     * 200 STATUS OK with a body consisting of the list of all players from a game uniquely identified
     * by gameInstanceID
     */
    @GetMapping("/{gameInstanceId}/allPlayers")
    public ResponseEntity<List<SimpleUser>> allPlayers(@PathVariable int gameInstanceId) {
        if (gameInstanceId < 0 || gameInstanceId >= gameInstances.size()) return ResponseEntity.badRequest().build();
        List<SimpleUser> playerList = players.stream().filter(x -> x.getGameInstanceId() == gameInstanceId).collect(Collectors.toList());
        return ResponseEntity.ok(playerList);
    }

    /**
     * Additional method that returns all the players that are currently playing in a given game instance
     *
     * @param gameInstanceId ID of GameInstance
     * @return ResponseEntity object that returns 400 BAD SYNTAX if the gameInstanceId is not in the appropriate range, or
     * 200 STATUS OK with a body consisting of the list of currently playing users from a game uniquely identified
     * by gameInstanceID
     */
    @GetMapping("/{gameInstanceId}/connectedPlayers")
    public ResponseEntity<List<SimpleUser>> connectedPlayers(@PathVariable int gameInstanceId) {
        if (gameInstanceId < 0 || gameInstanceId >= gameInstances.size()) return ResponseEntity.badRequest().build();
        List<SimpleUser> playerList = gameInstances.get(gameInstanceId).getPlayers().stream().map(Player::toSimpleUser).collect(Collectors.toList());
        return ResponseEntity.ok(playerList);
    }


    /**
     * Method that returns last instance of a singleplayer ID
     *
     * @return the ID of the last SinglePlayer Game Instance
     */
    @GetMapping("/getLastGIIdSingle")
    public ResponseEntity<Integer> getLastGIIdSingle() {
        if (currentSPGIId < 0 || currentSPGIId >= gameInstances.size()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(currentSPGIId);

    }

    /**
     * Method that updates the score of a given player on the server-side
     *
     * @param id     long primitive that uniquely identifies our SimpleUser instance
     * @param player SimpleUser instance that needs to have his/her score updated
     * @return ResponseEntity that returns 404 NOT_FOUND if the player does not exist, or 200 STATUS OK with a body
     * consisting of information regarding modified player
     */
    @PutMapping("/{id}/updatePlayer")
    public ResponseEntity<SimpleUser> updatePlayer(@PathVariable("id") long id, @RequestBody SimpleUser player) {
        if (player == null)
            return ResponseEntity.notFound().build();

        SimpleUser playerToModify = null;

        for (SimpleUser pl : players)
            if (pl.getId() == player.getId()) {
                playerToModify = pl;
                break;
            }
        if (playerToModify == null)
            return ResponseEntity.notFound().build();
        else {
            playerToModify.setScore(player.getScore());
            List<Player> listOfPlayers = gameInstances.get(player.getGameInstanceId()).getPlayers();
            for (Player pl : listOfPlayers)
                if (pl.getId() == player.getId()) {
                    pl.setScore(player.getScore());
                    break;
                }
            logger.info("[GI " + (player.getGameInstanceId()) + "] PLAYER (" + player.getId() + ") HAS NOW: " + player.getScore() + " POINTS!");
            return ResponseEntity.ok(playerToModify);
        }

    }

    //    ---------------------------------------------------------------------------
    //    ----------------------------     SERVERS     ------------------------------
    //    ---------------------------------------------------------------------------

    /**
     * Method that returns a list of available servers
     *
     * @return List of all the servers with gameState = INLOBBY
     */
    @GetMapping("/available-servers")
    public ResponseEntity<List<String>> getServers() {
        List<String> res = new ArrayList<>();
        for (String serverName : serverNames.keySet()) {
            if (gameInstances.get(serverNames.get(serverName)).getState() == GameState.INLOBBY)
                res.add(serverName);
        }
        return ResponseEntity.ok(res);
    }

    /**
     * Method that returns a List of all servers
     *
     * @return List of all servers
     */
    @GetMapping("/servers")
    public ResponseEntity<Set<String>> getAllServers() {
        return ResponseEntity.ok(serverNames.keySet());
    }

    /**
     * Method that returns a List of all players connected to a given server.
     *
     * @param serverName name of the server
     * @return List of all players connected to a given server
     */
    @GetMapping("/{serverName}/connectedPlayersOnServer")
    public ResponseEntity<List<String>> connectedPlayersOnServer(@PathVariable String serverName) {
        if (serverName == null) return ResponseEntity.badRequest().build();
        List<String> availableServers = getServers().getBody();
        if (!availableServers.contains(serverName)) return ResponseEntity.badRequest().build();


        GameInstanceServer lastGIS = null;
        for (GameInstanceServer gi : gameInstances)
            if (gi.getType() == GameInstance.MULTI_PLAYER && gi.getServerName().equals(serverName))
                lastGIS = gi;

        List<String> playerNames = lastGIS.getPlayers().stream().map(Player::getName).collect(Collectors.toList());
        return ResponseEntity.ok(playerNames);
    }

    /**
     * Method that adds a new server to the list of all servers
     *
     * @param serverName
     * @return serverName if added successfully, if the server is already there
     */
    @PostMapping("/servers")
    public ResponseEntity<String> addServer(@RequestBody String serverName) {
        if (this.serverNames.containsKey(serverName))
            return ResponseEntity.badRequest().build();
        createNewMultiplayerLobby(serverName);
        return ResponseEntity.ok(serverName);
    }

    /**
     * Method that changes the name of a given server, if the server does not have any connected players
     *
     * @param oldServerName the server to rename
     * @param newServerName new server name
     * @return newServerName if renamed succesfully, bad request if oldServerName not found or if the names are the same
     */
    @PutMapping("/{oldServerName}/updateServer")
    public ResponseEntity<String> updateServer(@PathVariable String oldServerName, @RequestBody String newServerName) {
        if (!this.serverNames.containsKey(oldServerName) || oldServerName.equals(newServerName))
            return ResponseEntity.badRequest().build();
        if (this.gameInstances.get(this.serverNames.get(oldServerName)).getPlayers().size() != 0)
            return ResponseEntity.badRequest().build();
        serverNames.put(newServerName, serverNames.get(oldServerName));
        if (gameInstances.get(serverNames.get(newServerName)) == null)
            createNewMultiplayerLobby(newServerName);
        gameInstances.get(serverNames.get(oldServerName)).setServerName(newServerName);
        serverNames.remove(oldServerName);
        return ResponseEntity.ok(newServerName);
    }

    /**
     * Method that deletes a server from the server list, if the server does not have any connected players
     *
     * @param serverName to remove
     * @return removed server name, bad request if not found
     */
    @DeleteMapping("/{serverName}/removeServer")
    public ResponseEntity<String> removeServer(@PathVariable String serverName) {
        if (!this.serverNames.containsKey(serverName))
            return ResponseEntity.notFound().build();
        if (this.gameInstances.get(this.serverNames.get(serverName)).getPlayers().size() != 0)
            return ResponseEntity.badRequest().build();
        serverNames.remove(serverName);
        return ResponseEntity.ok().build();

    }


    // ------------------------------------ ADDITIONAL METHODS ------------------------------------------------------

    /**
     * Creates a new multiplayer gameInstanceServer, with the next available Id, this class as a controller,
     * and assigns it to a given, putting it in a map as an serverName - Id pair.
     *
     * @param serverName Server to create the lobby on
     */
    public void createNewMultiplayerLobby(String serverName) {
        GameInstanceServer newGameInstance = new GameInstanceServer(gameInstances.size(), GameInstance.MULTI_PLAYER, this, msgs, serverName);
        newGameInstance.setState(GameState.INLOBBY);
        gameInstances.add(newGameInstance);
        currentMPGIId = newGameInstance.getId();
        serverNames.put(serverName, currentMPGIId);
    }

    /**
     * A getter for all the gameInstances
     *
     * @return a list of all gameInstances
     */
    public List<GameInstanceServer> getGameInstances() {
        return gameInstances;
    }

    /**
     * A getter for all the players
     *
     * @return A list of all players
     */
    public List<SimpleUser> getPlayers() {
        return players;
    }

    /**
     * A getter for the map of serverNames and gameInstance Ids
     *
     * @return A Map of all serverName - gameInstanceID pairs
     */
    public Map<String, Integer> getServerNames() {
        return serverNames;
    }

    /**
     * A getter for ID of the current MP gameInstance
     *
     * @return An integer representing current MP gameInstance ID
     */
    public int getCurrentMPGIId() {
        return currentMPGIId;
    }

}
