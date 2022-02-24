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

import java.util.List;
import java.util.Optional;
import java.util.Random;

import commons.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Quote;
import server.database.QuoteRepository;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final Random random;
    private final QuoteRepository repo;

    public QuoteController(Random random, QuoteRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    public List<Quote> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quote> getById(@PathVariable("id") long id) {
        return ResponseEntity.of(repo.findById(id));
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Quote> add(@RequestBody Quote quote) {

        if (quote.person == null || isNullOrEmpty(quote.person.firstName) || isNullOrEmpty(quote.person.lastName)
                || isNullOrEmpty(quote.quote)) {
            return ResponseEntity.badRequest().build();
        }

        Quote saved = repo.save(quote);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("/rnd")
    public ResponseEntity<Quote> getRandom() {
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(repo.getById((long) idx));
    }

    @GetMapping("/middle-quote")
    public Quote getMiddleQuote() {
        try {
            if (repo.count() > 0) {
                long size = repo.count();
                List<Quote> quotes = repo.findAll();
                return quotes.get((int) (size / 2)); // Middle quote
            } else
                return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Quote> removeById(@PathVariable("id") long id) {
        if (id <= 0 || !repo.existsById(id))
            ResponseEntity.of(Optional.empty());

        Optional<Quote> q = repo.findById(id);
        repo.deleteById(id);
        return ResponseEntity.of(q);
    }

    @PostMapping("/greet-person")
    public Quote greetPerson(@RequestBody Person person) {
        try {
            if (person.firstName == null || person.lastName == null
                    || person.firstName.equals("") || person.lastName.equals("")) {
                return new Quote(new Person("-", "-"), "You are not a person");
            } else {
                String q = "Hi there, " + person.firstName + " " + person.lastName + "!";
                Quote quote = new Quote(new Person(person.firstName, person.lastName), q);
                repo.save(quote);
                return quote;
            }

        } catch (NullPointerException e) {
            return new Quote(new Person("-", "-"), "missing body");
        }
    }
}