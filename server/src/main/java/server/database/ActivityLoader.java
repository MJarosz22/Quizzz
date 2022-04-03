package server.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Configuration
public class ActivityLoader {

    private static final String location = "activities";
    public static final String relativePath = "server/src/main/resources/";
    public static final String path = relativePath + location + "/";
    private final static Logger logger = LoggerFactory.getLogger(ActivityLoader.class);

    //Credits to StackOverflow user Oliv at https://stackoverflow.com/questions/10633595/java-zip-how-to-unzip-folder
    /**
     * Method to unzip the activity.zip file
     */
    public static void unzip(InputStream is, Path targetDir) throws IOException {
        targetDir = targetDir.toAbsolutePath();
        try (ZipInputStream zipIn = new ZipInputStream(is)) {
            for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null; ) {
                Path resolvedPath = targetDir.resolve(ze.getName()).normalize();
                if (!resolvedPath.startsWith(targetDir)) {
                    // see: https://snyk.io/research/zip-slip-vulnerability
                    throw new RuntimeException("Entry with an illegal path: "
                            + ze.getName());
                }
                if (ze.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    try {
                        Files.copy(zipIn, resolvedPath);
                    }catch (FileAlreadyExistsException e){
                        logger.debug("File " + ze.getName() + " already exists.");
                    }
                }
            }
        }
    }


    /**
     * Method to add the activities form the activities.json file to the activity repository
     * @param repo the activity repository to add the activities to
     */
    @Bean
    ApplicationRunner init(ActivityRepository repo){
        try{
            unzip(new FileInputStream(new File(relativePath + "activities.zip")), new File(relativePath + location).toPath());
            logger.info("Successfully unzipped activities.zip");
        }catch (IOException e){
            logger.warn("Activities.zip is missing (or something else went wrong while loading activities.zip)");
            logger.info("It should be located at resources/activities.zip");
        }
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<Activity> activities = Arrays.asList(mapper.readValue(Paths
                        .get(relativePath + location + "/activities.json").toFile(), Activity[].class));
                activities = activities.stream().filter(x -> x.getSource().length() < 150).collect(Collectors.toList());
                repo.saveAll(activities);
                logger.info("Activities added to repo");
            }catch (IllegalArgumentException ex) {
                logger.error("Something went wrong while loading activities.json");
            }catch (FileNotFoundException e){
                logger.warn("activities.json not found");
                logger.info("It should be located at resources/activities/activities.json");
            }
        };
    }

}

