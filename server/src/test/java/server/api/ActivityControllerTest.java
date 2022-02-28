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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Random;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
        var actual = sut.addActivity(getActivity(null, null,null, 1, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void addActivityTest() {
        sut.addActivity(getActivity("id-1", "00/1.png","Boil 2L of water", 120, "https://www.some-site.com"));
        assertEquals(true, repo.calledMethods.contains("save"));
    }

    @Test
    public void getAllTest() {
        sut.addActivity(getActivity("id-1", "00/1.png", "Boil 2L of water", 120, "https://www.some-site.com"));
        sut.addActivity(getActivity("id-2", "00/2.png", "Do another activity", 15, "https://www.another-site.com"));
        sut.addActivity(getActivity("id-3", "00/3.png", "Take a shower for 10 minutes", 60, "https://www.showers.com"));
        List<Activity> activities = sut.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void updateActivityTest() {
        sut.addActivity(getActivity("id-1", "00/1.png","Boil 2L of water", 120, "https://www.some-site.com"));
        sut.addActivity(getActivity("id-2", "00/2.png","Do another activity", 15, "https://www.another-site.com"));
        sut.addActivity(getActivity("id-3", "00/3.png","Take a shower for 10 minutes", 60, "https://www.showers.com"));

        var actual = getActivity("id-1", "00/1.png","Activity changed by using updateActivity method", 65, "https://www.my-idea.com");
        actual.activityID = 2;

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
        sut.addActivity(getActivity("id-1", "00/1.png","Boil 2L of water", 120, "https://www.some-site.com"));
        sut.addActivity(getActivity("id-2", "00/2.png","Do another activity", 15, "https://www.another-site.com"));
        sut.addActivity(getActivity("id-3", "00/3.png","Take a shower for 10 minutes", 60, "https://www.showers.com"));

        var actual = sut.deleteActivity(5);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void deleteActivityTest() {
        var activity1 = getActivity("id-1", "00/1.png","Boil 2L of water", 120, "https://www.some-site.com");
        sut.addActivity(activity1);
        var activity2 = getActivity("id-2", "00/2.png","Do another activity", 15, "https://www.another-site.com");
        sut.addActivity(activity2);
        var activity3 = getActivity("id-3", "00/3.png","Take a shower for 10 minutes", 60, "https://www.showers.com");
        sut.addActivity(activity3);

        var actual = sut.deleteActivity((long) 2);

        assertEquals(OK, actual.getStatusCode());
        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("deleteById"));

        assertEquals(2, sut.getAll().size());
    }

    @Test
    public void testCorrectIndexing() {
        var activity1 = getActivity("id-1", "00/1.png","Boil 2L of water", 120, "https://www.some-site.com");
        sut.addActivity(activity1);
        var activity2 = getActivity("id-2", "00/2.png","Do another activity", 15, "https://www.another-site.com");
        sut.addActivity(activity2);
        var activity3 = getActivity("id-3", "00/3.png","Take a shower for 10 minutes", 60, "https://www.showers.com");
        sut.addActivity(activity3);
        sut.deleteActivity(2);
        sut.deleteActivity(3);
        sut.addActivity(new Activity("id-23", "00/23.png","test1", 10, "https://www.google.com/?client=safari"));
        sut.addActivity(new Activity("id-213", "00/213.png","test2", 11, "https://www.google.com/?client=safari"));
        sut.addActivity(new Activity("id-452", "00/452.png","test3", 113, "https://www.google.com/?client=safari"));
        assertEquals(6, sut.getAll().get(3).id);
    }


    private static Activity getActivity(String id, String image_path, String title, int consumption, String source) {
        return new Activity(id, image_path, title, consumption, source);
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
