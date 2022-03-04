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
        var actual = sut.addActivity(getActivity(null, 1, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addActivityTest() {
        sut.addActivity(getActivity("Boil 2L of water", 120, "https://www.some-site.com"));
        assertEquals(true, repo.calledMethods.contains("save"));
    }

    @Test
    public void getAllTest() {
        sut.addActivity(getActivity("Boil 2L of water", 120, "www.some-site.com"));
        sut.addActivity(getActivity("Do another activity", 15, "www.another-site.com"));
        sut.addActivity(getActivity("Take a shower for 10 minutes", 60, "www.showers.com"));
        List<Activity> activities = sut.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void updateActivityTest() {
        sut.addActivity(getActivity("Boil 2L of water", 120, "https://www.some-site.com"));
        sut.addActivity(getActivity("Do another activity", 15, "https://www.another-site.com"));
        sut.addActivity(getActivity("Take a shower for 10 minutes", 60, "https://www.showers.com"));

        var actual = getActivity("Activity changed by using updateActivity method", 65, "https:/www.my-idea.com");
        actual.id = 2;

        printActivities(sut);
        System.out.println();
        sut.updateActivity((long) 2, actual);

        assertTrue(repo.calledMethods.contains("findById"));
        assertEquals(actual, repo.getById((long) 2));
        assertTrue(repo.calledMethods.contains("replace"));

        //printActivities(sut);
    }


    @Test
    public void deleteActivityFailsTest() {
        sut.addActivity(getActivity("Boil 2L of water", 120, "https://www.some-site.com"));
        sut.addActivity(getActivity("Do another activity", 15, "https://www.another-site.com"));
        sut.addActivity(getActivity("Take a shower for 10 minutes", 60, "https://www.showers.com"));

        var actual = sut.deleteActivity(5);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void deleteActivityTest() {
        var activity1 = getActivity("Boil 2L of water", 120, "https://www.some-site.com");
        sut.addActivity(activity1);
        var activity2 = getActivity("Do another activity", 15, "https://www.another-site.com");
        sut.addActivity(activity2);
        var activity3 = getActivity("Take a shower for 10 minutes", 60, "https://www.showers.com");
        sut.addActivity(activity3);

        var actual = sut.deleteActivity((long) 2);

        assertEquals(OK, actual.getStatusCode());
        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("deleteById"));

        assertEquals(2, sut.getAll().size());
    }

    @Test
    public void testCorrectIndexing() {
        var activity1 = getActivity("Boil 2L of water", 120, "https://www.some-site.com");
        sut.addActivity(activity1);
        var activity2 = getActivity("Do another activity", 15, "https://www.another-site.com");
        sut.addActivity(activity2);
        var activity3 = getActivity("Take a shower for 10 minutes", 60, "https://www.showers.com");
        sut.addActivity(activity3);
        sut.deleteActivity(2);
        sut.deleteActivity(3);
        sut.addActivity(new Activity("test1", 10, "https://www.google.com/?client=safari"));
        sut.addActivity(new Activity("test2", 11, "https://www.google.com/?client=safari"));
        sut.addActivity(new Activity("test3", 113, "https://www.google.com/?client=safari"));
        assertEquals(6, sut.getAll().get(3).id);
    }


    private static Activity getActivity(String title, int consumption, String source) {
        return new Activity(title, consumption, source);
    }

    private static void printActivities(ActivityController sut) {
        // Testing-purpose ONLY
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
