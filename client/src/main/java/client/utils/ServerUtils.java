package client.utils;

import commons.Activity;
import commons.player.Player;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javassist.Loader;
import org.glassfish.jersey.client.ClientConfig;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    public static List<SimpleUser> getPlayers(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/ " + player.getGameInstanceId() + "/players") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
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

    public List<Activity> getActivitiesRandomly() {
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
                .target(SERVER).path("api/activities/" + activity.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    public boolean disconnect(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/ " + player.getGameInstanceId() + "/disconnect") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .delete(new GenericType<>() {
                });
    }

    public int getLastGIIdMult() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/getLastGIIdMult") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
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


    public static List<SimpleUser> getPlayerList(int gIId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/ " + gIId + "/playerlist") //
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

    // ------------------------------------ ADDITIONAL METHODS ------------------------------------ //

    /**
     * Additional method that checks whether a player hasn't disconnected from a game, by comparing cookies, which are
     * used as identifiers (as each player has an unique cookie).
     *
     * @param player - SimpleUser object that represents the player we are interested in
     * @return true, if the player has not disconnected yet, or false otherwise
     */
    public boolean containsPlayer(SimpleUser player) {
        Optional<Boolean> contains = getPlayerList(player.getGameInstanceId())
                .stream().map(x -> x.getCookie().equals(player.getCookie())).findFirst();
        return (player != null
                && getPlayerList(player.getGameInstanceId()) != null
                && contains.isPresent()
        );
    }
}