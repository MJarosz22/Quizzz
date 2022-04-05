/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;


public class ActivityControllerTest {

    public int nextInt;
    private MyRandom random;
    private TestActivityRepository repo;

    private ActivityController sut;

    @BeforeEach
    public void setup() {
        random = new MyRandom();
        repo = new TestActivityRepository();
        sut = new ActivityController(random, repo);
    }

    @Test
    public void cannotAddNullPerson() {
        var actual = sut.addActivity(getActivity(null, null, null, 1L, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }


    @Test
    public void addActivityTest() {
        sut.addActivity(getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com"));
        assertEquals(true, repo.calledMethods.contains("save"));
    }

    @Test
    public void addActivityEdgeCasesTest() {
        var actual = sut.addActivity(getActivity(null, "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = sut.addActivity(getActivity("id-1", "00/1.png", null, 120L, "https://www.some-site.com"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = sut.addActivity(getActivity("id-1", "00/1.png", "Boil some water", -15L, "https://www.some-site.com"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());

    }

    @Test
    public void getAllTest() {
        sut.addActivity(getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com"));
        sut.addActivity(getActivity("id-2", "00/2.png", "Do another activity", 15L, "https://www.another-site.com"));
        sut.addActivity(getActivity("id-3", "00/3.png", "Take a shower for 10 minutes", 60L, "https://www.showers.com"));
        List<Activity> activities = sut.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
    }


    @Test
    public void updateActivityTest() {
        sut.addActivity(getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com"));
        sut.addActivity(getActivity("id-2", "00/2.png", "Do another activity", 15L, "https://www.another-site.com"));
        sut.addActivity(getActivity("id-3", "00/3.png", "Take a shower for 10 minutes", 60L, "https://www.showers.com"));

        var actual = getActivity("id-1", "00/1.png", "Activity changed by using updateActivity method", 65L, "https://www.my-idea.com");
        actual.setActivityID(2L);

        printActivities(sut);
        System.out.println();
        sut.updateActivity(2, actual);

        assertTrue(repo.calledMethods.contains("findById"));
        assertEquals(actual, repo.getById((long) 2));
        assertTrue(repo.calledMethods.contains("replace"));
    }

    @Test
    public void updateActivityFailsTest() {
        Activity activity = sut.addActivity(getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com")).getBody();
        var actual = sut.updateActivity(15L, activity);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void deleteAllTest() {
        var actual = sut.deleteAll();
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void deleteActivityFailsTest() {
        sut.addActivity(getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com"));
        sut.addActivity(getActivity("id-2", "00/2.png", "Do another activity", 15L, "https://www.another-site.com"));
        sut.addActivity(getActivity("id-3", "00/3.png", "Take a shower for 10 minutes", 60L, "https://www.showers.com"));

        var actual = sut.deleteActivity(5);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void deleteActivityTest() {
        var activity1 = getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com");
        sut.addActivity(activity1);
        var activity2 = getActivity("id-2", "00/2.png", "Do another activity", 15L, "https://www.another-site.com");
        sut.addActivity(activity2);
        var activity3 = getActivity("id-3", "00/3.png", "Take a shower for 10 minutes", 60L, "https://www.showers.com");
        sut.addActivity(activity3);

        var actual = sut.deleteActivity((long) 2);

        assertEquals(OK, actual.getStatusCode());
        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("deleteById"));

        assertEquals(2, sut.getAll().size());
    }


    @Test
    public void testCorrectIndexing() {
        var activity1 = getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com");
        sut.addActivity(activity1);
        var activity2 = getActivity("id-2", "00/2.png", "Do another activity", 15L, "https://www.another-site.com");
        sut.addActivity(activity2);
        var activity3 = getActivity("id-3", "00/3.png", "Take a shower for 10 minutes", 60L, "https://www.showers.com");
        sut.addActivity(activity3);
        sut.deleteActivity(2);
        sut.deleteActivity(3);
        sut.addActivity(new Activity("id-23", "00/23.png", "test1", 10L, "https://www.google.com/?client=safari"));
        sut.addActivity(new Activity("id-213", "00/213.png", "test2", 11L, "https://www.google.com/?client=safari"));
        sut.addActivity(new Activity("id-452", "00/452.png", "test3", 113L, "https://www.google.com/?client=safari"));
        assertEquals(6L, sut.getAll().get(3).getActivityID());
    }


    @Test
    public void testInvalidTitle() {
        var actual = sut.addActivity(
                getActivity("id-23", "00/23.png", "This? title is invalid!", 10L, "https://www.google.com/?client=safari")
        );
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testInvalidUrl() {
        var actual = sut.addActivity(
                getActivity("id-23", "00/23.png", "test1", 10L, "not-a-valid-url.com")
        );
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getRandomEmptyTest() {
        var actual = sut.getRandom();
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void getRandomTest() {
        sut.addActivity(getActivity("id-1", "00/1.png", "Boil 2L of water", 120L, "https://www.some-site.com"));
        sut.addActivity(getActivity("id-2", "00/2.png", "Do another activity", 15L, "https://www.another-site.com"));
        sut.addActivity(getActivity("id-3", "00/3.png", "Take a shower for 10 minutes", 60L, "https://www.showers.com"));
        var actual = sut.getRandom();
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getRandom60EmptyTest(){
        var actual = sut.getRandom60();
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }



    private static Activity getActivity(String id, String image_path, String title, Long consumption, String source) {
        return new Activity(id, image_path, title, consumption, source);
    }

    // Test-purpose ONLY
    private static void printActivities(ActivityController sut) {
        List<Activity> activities = sut.getAll();
        activities.forEach(activity -> {
            System.out.println(activity);
        });
    }


    @SuppressWarnings("serial")
    public class MyRandom extends Random {
        //TODO: future usability (when GET rnd will be implemented in ActivityController
        public boolean wasCalled = false;

        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}
