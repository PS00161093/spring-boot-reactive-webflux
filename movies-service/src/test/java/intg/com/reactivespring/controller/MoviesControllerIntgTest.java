package com.reactivespring.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.reactivespring.domain.Movie;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(
        properties = {
                "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
                "restClient.reviewsUrl=http://localhost:8084/v1/reviews"
        }
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoviesControllerIntgTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testRetrieveMovieById() {
        var mviId = 1;
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/v1/movieinfos" + "/" + mviId))
                        .willReturn(
                                WireMock.aResponse()
                                        .withHeader("Content-type", "application/json")
                                        .withBodyFile("movieinfo.json"))
        );

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/v1/reviews"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withHeader("Content-type", "application/json")
                                        .withBodyFile("reviews.json"))
        );

        webTestClient
                .get()
                .uri("/v1/movies/{id}", mviId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var movie = movieEntityExchangeResult.getResponseBody();
                    assertNotNull(movie);
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                    assertEquals(2005, movie.getMovieInfo().getYear());
                    assertEquals(2, movie.getMovieInfo().getCast().size());
                    assertEquals(2, movie.getReviewList().size());
                });
    }

    @Test
    void testRetrieveMovieByIdWith404ErrorFromMovieInfo() {
        var mviId = 1;
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/v1/movieinfos" + "/" + mviId))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(404))
        );

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/v1/reviews"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withHeader("Content-type", "application/json")
                                        .withBodyFile("reviews.json"))
        );

        webTestClient
                .get()
                .uri("/v1/movies/{id}", mviId)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void testRetrieveMovieById404ErrorFromReview() {
        var mviId = 1;
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/v1/movieinfos" + "/" + mviId))
                        .willReturn(
                                WireMock.aResponse()
                                        .withHeader("Content-type", "application/json")
                                        .withBodyFile("movieinfo.json"))
        );

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/v1/reviews"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(404))
        );

        webTestClient
                .get()
                .uri("/v1/movies/{id}", mviId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var movie = movieEntityExchangeResult.getResponseBody();
                    assertNotNull(movie);
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                    assertEquals(0, movie.getReviewList().size());
                });

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/v1/reviews")));
    }

    @Test
    @Order(1)
    void testRetrieveMovieByIdWith5xxErrorFromMovieInfo() {
        var mviId = 1;
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/v1/movieinfos" + "/" + mviId))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(500)
                                        .withBody("MovieInfo Service Unavailable"))
        );

        webTestClient
                .get()
                .uri("/v1/movies/{id}", mviId)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Server exception in MovieInfoService : MovieInfo Service Unavailable");

        WireMock.verify(4, WireMock.getRequestedFor(WireMock.urlEqualTo("/v1/movieinfos" + "/" + mviId)));
    }

    @Test
    void testRetrieveMovieById5xxErrorFromReview() {
        var mviId = 1;

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/v1/reviews"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(500)
                                        .withBody("Review Service Unavailable"))
        );

        webTestClient
                .get()
                .uri("/v1/movies/{id}", mviId)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Server exception in ReviewService : Review Service Unavailable");
    }
}
