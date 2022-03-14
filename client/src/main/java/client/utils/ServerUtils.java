package client.utils;

import commons.Activity;
import communication.RequestToJoin;
import commons.player.SimpleUser;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

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

    public int getLastGIId() {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/getLastGIId") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public static List<SimpleUser> getPlayerList(int gIId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/game/ " + gIId + "/players") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }
}