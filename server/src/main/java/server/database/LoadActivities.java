package server.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Configuration
public class LoadActivities {

    private final Logger logger = LoggerFactory.getLogger(LoadActivities.class);

    @Bean
    ApplicationRunner init(ActivityRepository repo){
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<Activity> activities = Arrays.asList(mapper.readValue(Paths
                        .get("server/src/main/resources/activities.json").toFile(), Activity[].class));
                repo.saveAll(activities);
                logger.info("Activities added to repo");
            }catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        };
    }
}

