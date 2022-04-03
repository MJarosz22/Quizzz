package client.utils;

import commons.Activity;
import commons.Answer;
import commons.Emoji;
import commons.powerups.*;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String location = "localhost:8080";
    private static final String SERVER = "http://" + location + "/";
    private StompSession session;


    /**
     * Returns all players from a gameInstance (if you are also connected to that gameInstance)
     *
     * @param player One of the players in the gameInstance
     * @return List of all players connected to gameInstance
     */
    public List<SimpleUser> getPlayers(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/" + player.getGameInstanceId() + "/players") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }


    /**
     * Gets the current type of question in the specified gameInstance
     * @param gameInstanceId the gameInstance to get the type of question for
     * @return the name of the current question type
     */
    public String getCurrentQType(int gameInstanceId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/gameinstance/ " + gameInstanceId + "/getCurrentQType")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets all the activities in the activity repository
     * @return a list of all the activities
     */
    public List<Activity> getActivities() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets 60 random activities to be used in the game
     * @return the list of the random 60 activities
     * @throws NotFoundException
     */
    public List<Activity> getActivitiesRandomly() throws NotFoundException {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities/random60")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });

    }

    /**
     * Gets the image for a certain activity
     * @param activity the activity to get the image for
     * @return the image for the activity
     * @throws FileNotFoundException
     */
    public InputStream getImage(Activity activity) throws FileNotFoundException {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/activities/" + activity.getImage_path())
                .request("image/*")
                .accept("image/*")
                .get(new GenericType<>() {
                });
        if (response.getStatus() == 404) throw new FileNotFoundException();
        return response.readEntity(InputStream.class);
    }

    /**
     * Adds a certain activity to the activity repository
     * @param activity the new activity to be added
     * @return the activity that was added
     */
    public Activity addActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("api/activities")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    /**
     * Lets a client join a gameInstance as a player
     *
     * @param request Request of player (includes name of player and gameType(Singleplayer or Multiplayer))
     * @return Simple User (Including name, cookie and gameInstanceID)
     */
    public SimpleUser addPlayer(RequestToJoin request) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/join") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(request, APPLICATION_JSON), SimpleUser.class);
    }

    /**
     * Updates a certain activity that already exists in the activity repository
     * @param activity the activity to be updated
     * @return the updated activity
     */
    public Activity updateActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities/" + activity.getActivityID())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }


    /**
     * Method that disconnects a certain player from the gameInstance that he/she is in
     * @param player the player that has to be disconnected
     * @return true if the player was successfully disconnected, false otherwise
     */
    public boolean disconnect(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/" + player.getGameInstanceId() + "/disconnect") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .delete(new GenericType<>() {
                });
    }

    /**
     * Adds a player to the global leaderboard repository
     * @param player the player to be added to the leaderboard repository
     * @return the player that was added to the leaderboard repository
     */
    public SimpleUser addPlayerToLeaderboard(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/leaderboard/addPlayer")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(player, APPLICATION_JSON), SimpleUser.class);
    }


    public String getServerName(int gameInstanceId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/gameinstance/ " + gameInstanceId + "/getServerName")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets all entries in the leaderboard repository
     * @return a list of Simple Users that are all the entries in the global leaderboard
     */
    public static List<SimpleUser> getLeaderboard(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/leaderboard")
                .request(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Method that returns last instance of a multiplayer ID
     *
     * @return the ID of the last Multiplayer Game Instance
     */
    public int getLastGIIdMult() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/game/getLastGIIdMult")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }


    /**
     * Method that returns last instance of a single player ID
     *
     * @return the ID of the last Single Player Game Instance
     */
    public int getLastGIIdSingle() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/getLastGIIdSingle") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets all the players in the specified Game Instance
     * @param gIId the Game Instance to get the players from
     * @return a list of Simple Users that belong to the Game Instance
     */
    public static List<SimpleUser> allPlayers(int gIId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/" + gIId + "/allPlayers") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets all the connected players in the specified Game Instance
     * @param gIId the Game Instance to get the connected players from
     * @return a list of connected Simple Users that belong to the Game Instance
     */
    public static List<SimpleUser> connectedPlayers(int gIId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/" + gIId + "/connectedPlayers") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets the names of all the connected players on the specified server
     * @param serverName the server on which the players are connected
     * @return a list of the names of the connected players
     */
    public static List<String> connectedPlayersOnServer(String serverName) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/" + serverName + "/connectedPlayersOnServer") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Method that updates the score of a given player on the server-side
     *
     * @param player SimpleUser instance that needs to have his/her score updated
     * @return the updated player
     */
    public SimpleUser updatePlayer(SimpleUser player) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/" + player.getId() + "/updatePlayer")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(player, APPLICATION_JSON), SimpleUser.class);
    }


    /**
     * Starts the game, by changing the status of the gameInstance the specified player is in
     * @param player the player that pressed the play button
     * @return true if the game successfully started, false otherwise
     */
    public boolean startGame(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/" + player.getGameInstanceId() + "/start") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }


    /**
     * Gets the time left for the question that a certain player is shown
     * @param player a player from the gameInstance for which we want to get the time left
     * @return the time left
     */
    public int getTimeLeft(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/" + player.getGameInstanceId() + "/timeleft") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public void initWebsocket() {
        session = connect("ws://" + location + "/websocket");
    }

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            //TODO TYPE CHECKING
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }


    /**
     * Sends the answer of the player to the server
     * @param player the player that answered
     * @param answer the answer of the player
     * @return true if the answer was sent successfully, false otherwise
     */
    public boolean submitAnswer(SimpleUser player, Answer answer) {
        return ClientBuilder
                .newClient(new ClientConfig())
                .target(SERVER)
                .path("api/gameinstance/" + player.getGameInstanceId() + "/answer")
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(answer, APPLICATION_JSON), Boolean.class);
    }

    /**
     * Send a request to reduce time by the given percentage
     *
     * @param player     who used the powerUp
     * @param percentage by which the time should be reduced
     */
    public void useTimePowerup(SimpleUser player, int percentage) {
        ClientBuilder
                .newClient(new ClientConfig())
                .target(SERVER)
                .path("api/gameinstance/" + player.getGameInstanceId() + "/decrease-time")
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new TimePU(player.getCookie(), player.getName(), percentage), APPLICATION_JSON));
    }


    /**
     * Sends a request to the server for a player to use the double points power up
     * @param player the player that used the power up
     */
    public void usePointsPowerup(SimpleUser player) {
        ClientBuilder
                .newClient(new ClientConfig())
                .target(SERVER)
                .path("api/gameinstance/" + player.getGameInstanceId() + "/double-points")
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new PointsPU(player.getCookie(), player.getName()), APPLICATION_JSON));
    }


    /**
     * Sends a request to the server for a player to use the remove one incorrect answer power up
     * @param player the player that used the power up
     */
    public void useAnswerPowerup(SimpleUser player) {
        ClientBuilder
                .newClient(new ClientConfig())
                .target(SERVER)
                .path("api/gameinstance/" + player.getGameInstanceId() + "/remove-incorrect-answer")
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new AnswerPU(player.getCookie(), player.getName()), APPLICATION_JSON));
    }


    /**
     * Sends a request to the server for a player to use a certain emoji
     * @param player the player that used the emoji
     * @param emoji the emoji that was used by the player
     */
    public void sendEmoji(SimpleUser player, String emoji) {
        ClientBuilder
                .newClient(new ClientConfig())
                .target(SERVER)
                .path("api/gameinstance/" + player.getGameInstanceId() + "/emoji")
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new Emoji(emoji, player), APPLICATION_JSON));
    }

    /**
     * Gets the type of a certain gameInstance
     * @param gameInstanceId the id of the gameInstance to get the type for
     * @return 0 if the game is single player, 1 if it's multiplayer
     */
    public Integer gameInstanceType(int gameInstanceId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/gameinstance/ " + gameInstanceId + "/gameInstanceType")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public void disconnectWebsocket() {
        session.disconnect();
    }

    public void send(String dest, Object o) {
        session.send(dest, o);
    }


    // ------------------------------------ ADDITIONAL METHODS ------------------------------------ //

    /**
     * Additional method that checks whether a player hasn't disconnected from a game, by comparing cookies, which are
     * used as identifiers (as each player has an unique cookie).
     *
     * @param player - SimpleUser object that represents the player we are interested in
     * @return true, if the player has not disconnected yet, or false otherwise
     */
    public boolean containsPlayer(SimpleUser player) {
        if (player == null || connectedPlayers(player.getGameInstanceId()) == null) return false;
        Optional<Boolean> contains = connectedPlayers(player.getGameInstanceId())
                .stream().map(x -> x.getCookie().equals(player.getCookie())).findFirst();
        return (contains.isPresent());
    }

    /**
     * Gets the list of available server
     * @return a list of the names of the available servers
     */
    public List<String> availableServers() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("api/game/available-servers")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }


    /**
     * Deletes a  certain activity
     * @param activity the activity to be deleted
     * @return the deleted activity
     */
    public Activity deleteActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities/" + activity.getActivityID())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<>() {
                });
    }

}