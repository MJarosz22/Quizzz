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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.transaction.annotation.Transactional;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestActivityRepository implements ActivityRepository {

    public final List<Activity> activities = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Activity> findAll() {
        calledMethods.add("findAll");
        return activities;
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Activity> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends Activity> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Activity> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub

    }

    @Override
    public Activity getOne(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Activity getById(Long id) {
        call("getById");
        return find(id).get();
    }

    private Optional<Activity> find(Long id) {
        return activities.stream().filter(a -> a.getActivityID() == id).findFirst();
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Activity> S save(S entity) {
        if (!findById(entity.getActivityID()).isPresent()) {
            call("save");
            entity.setActivityID(currentID());
            activities.add(entity);
            return entity;
        } else {
            call("replace");
            Activity activity = findById(entity.getActivityID()).get();
            entity.setActivityID(activity.getActivityID());
            activity.title = entity.title;
            activity.setConsumption_in_wh(entity.getConsumption_in_wh());
            activity.source = entity.source;
            return entity;
        }
    }

    private long currentID() {
        int count = 0;
        for (String s : calledMethods)
            if (s.equals("save"))
                count++;
        return count;
    }

    @Override
    public Optional<Activity> findById(Long id) {
        calledMethods.add("findById");
        return find(id);
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return find(id).isPresent();
    }

    @Override
    public long count() {
        return activities.size();
    }

    @Query("delete from Activity a where a.id = ?1")
    @Transactional
    @Modifying
    @Override
    public void deleteById(Long id) {
        calledMethods.add("deleteById");
        Activity activity = getById(id);
        activities.remove(activity);
    }

    @Override
    public void delete(Activity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll(Iterable<? extends Activity> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Activity> long count(Example<S> example) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <S extends Activity> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <S extends Activity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        return null;
    }
}