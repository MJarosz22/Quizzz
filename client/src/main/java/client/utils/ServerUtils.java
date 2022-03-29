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

    public List<SimpleUser> getPlayers(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/" + player.getGameInstanceId() + "/players") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public String getCurrentQType(int gameInstanceId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/gameinstance/ " + gameInstanceId + "/getCurrentQType")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public List<Activity> getActivities() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public List<Activity> getActivitiesRandomly() throws NotFoundException {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities/random60")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });

    }

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


    public Activity addActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("api/activities")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    public SimpleUser addPlayer(RequestToJoin request) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/join") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(request, APPLICATION_JSON), SimpleUser.class);
    }

    public Activity updateActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities/" + activity.getActivityID())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }


    public boolean disconnect(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/" + player.getGameInstanceId() + "/disconnect") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .delete(new GenericType<>() {
                });
    }

    public SimpleUser addPlayerToLeaderboard(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/leaderboard/addPlayer")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(player, APPLICATION_JSON), SimpleUser.class);
    }

    public static List<SimpleUser> getLeaderboard(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/leaderboard")
                .request(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public int getLastGIIdMult() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client
                .target(SERVER).path("api/game/getLastGIIdMult")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public int getLastGIIdSingle() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/getLastGIIdSingle") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }


    public static List<SimpleUser> allPlayers(int gIId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/" + gIId + "/allPlayers") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public static List<SimpleUser> connectedPlayers(int gIId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/" + gIId + "/connectedPlayers") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public static List<String> connectedPlayersOnServer(String serverName) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/" + serverName + "/connectedPlayersOnServer") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public SimpleUser updatePlayer(SimpleUser player) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/" + player.getId() + "/updatePlayer")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(player, APPLICATION_JSON), SimpleUser.class);
    }

    public boolean startGame(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/" + player.getGameInstanceId() + "/start") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

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

    public void sendEmoji(SimpleUser player, String emoji) {
        ClientBuilder
                .newClient(new ClientConfig())
                .target(SERVER)
                .path("api/gameinstance/" + player.getGameInstanceId() + "/emoji")
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new Emoji(emoji), APPLICATION_JSON));
    }

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

    public List<String> availableServers() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("api/game/available-servers")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }


    public Activity deleteActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/activities/" + activity.getActivityID())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<>() {
                });
    }


}