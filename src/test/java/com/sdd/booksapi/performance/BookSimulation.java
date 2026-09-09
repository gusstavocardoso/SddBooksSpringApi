package com.sdd.booksapi.performance;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BookSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    ScenarioBuilder scn = scenario("Book API Load Test")
            .exec(session -> session.set("isbn", UUID.randomUUID().toString().substring(0, 13)))
            .exec(http("Create Book")
                    .post("/api/v1/books")
                    .body(StringBody("{\"titulo\": \"Performance Testing\", \"autor\": \"Tester\", \"isbn\": \"#{isbn}\", \"dataPublicacao\": \"2023-01-01\"}"))
                    .check(status().is(201)))
            .pause(1)
            .exec(http("Get All Books")
                    .get("/api/v1/books")
                    .check(status().is(200)));

    {
        setUp(
            scn.injectOpen(
                rampUsersPerSec(1).to(10).during(Duration.ofSeconds(10)),
                constantUsersPerSec(10).during(Duration.ofSeconds(20))
            )
        ).protocols(httpProtocol);
    }
}
