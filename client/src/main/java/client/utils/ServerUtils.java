package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import commons.Activity;
import commons.communication.RequestToJoin;
import commons.player.SimpleUser;
import commons.question.Answer;
import commons.question.Question;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String location = "localhost:8080";
    private static final String SERVER = "http://localhost:8080/";
    private static StompSession session;

    public static List<SimpleUser> getPlayers(SimpleUser player) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/ " + player.getGameInstanceId() + "/players") //
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
    public List<Activity> getActivitiesRandomly() throws NotFoundException{
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
                .get(new GenericType<>() {});
        if(response.getStatus() == 404) throw new FileNotFoundException();
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
                .target(SERVER).path("api/gameinstance/ " + player.getGameInstanceId() + "/disconnect") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .delete(new GenericType<>() {
                });
    }

    public boolean startGame(SimpleUser player){
        Client client = ClientBuilder.newClient(new ClientConfig());
        return client //
                .target(SERVER).path("api/gameinstance/ " + player.getGameInstanceId() + "/start") //
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie()) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>(){});
    }

    public static void initWebsocket(){
        session = connect("ws://" + location + "/websocket");
    }

    private static StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() { }).get();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }catch (ExecutionException e){
            throw new RuntimeException(e);
        }
    }


    public static <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer){
//        if(!session.isConnected()) initWebsocket();
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

    public boolean submitAnswer(SimpleUser player, Answer answer){
        return ClientBuilder
                .newClient(new ClientConfig())
                .target(SERVER)
                .path("api/gameinstance/" + player.getGameInstanceId() + "/answer")
                .request(APPLICATION_JSON).cookie("user-id", player.getCookie())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(answer, APPLICATION_JSON), Boolean.class);
    }

    public static void disconnectWebsocket(){
        session.disconnect();
    }

    public void send(String dest, Object o){
        session.send(dest, o);
    }

}