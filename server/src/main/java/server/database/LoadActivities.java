package server.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Configuration
public class LoadActivities {

    @Bean
    ApplicationRunner init(ActivityRepository repo){
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<Activity> activities = Arrays.asList(mapper.readValue(Paths
                            .get("server/src/main/resources/activities.json").toFile(), Activity[].class));
                    repo.saveAll(activities);
                    System.out.println("Activities added to repo");
                }catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}

