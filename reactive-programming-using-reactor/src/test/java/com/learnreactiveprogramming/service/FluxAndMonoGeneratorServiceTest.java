package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void testNamesFluxWithSize() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier
                .create(namesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void testNamesFluxWithData() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier
                .create(namesFlux)
                .expectNext("Alex", "Ben", "Chloe")
                .verifyComplete();
    }

    @Test
    void testNamesFluxWithCountAndData() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier
                .create(namesFlux)
                .expectNext("Alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testNamesFluxMap() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxMap();
        StepVerifier
                .create(namesFlux)
                .expectNext("ALEX")
                .expectNext("BEN")
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testNamesFluxFilter() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFilter(4);
        StepVerifier
                .create(namesFlux)
                .expectNext("Alex")
                .verifyComplete();
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    void testNamesFluxFilterWithSize(int nameLength) {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFilter(nameLength);
        StepVerifier
                .create(namesFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testNamesFluxFlatMap() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFilterFlatMap(5);
        StepVerifier
                .create(namesFlux)
                .expectNext("C", "H", "L", "O", "E")
                .verifyComplete();
    }

    void testNamesFluxFlatMapAsync() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFilterFlatMapAsync(3);
        Assertions
                .assertThrows(Error.class, () -> {
                    StepVerifier
                            .create(namesFlux)
                            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                            .verifyComplete();
                });
    }

    @Test
    void testNamesFluxFlatMapAsyncCount() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFilterFlatMapAsync(3);
        StepVerifier
                .create(namesFlux)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void testNamesFluxConcatMap() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMap(3);
        StepVerifier
                .create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void testNamesMonoFlatMap() {
        var namesFlux = fluxAndMonoGeneratorService.namesMonoFlatMap(3);
        StepVerifier
                .create(namesFlux)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void testNamesMonoFlatMapMany() {
        var namesFlux = fluxAndMonoGeneratorService.namesMonoFlatMapMany(3);
        StepVerifier
                .create(namesFlux)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void testNamesFluxTransform() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(3);
        StepVerifier
                .create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void testNamesFluxTransform_1() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxWithDefault(6);
        StepVerifier
                .create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void testNamesFluxTransform_2() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxWithSwitchIfEmpty(6);
        StepVerifier
                .create(namesFlux)
                .expectNext("DEFAULT")
                .verifyComplete();
    }

    @Test
    void testFluxConcat() {
        var namesFlux = fluxAndMonoGeneratorService.exploreConcat();
        StepVerifier
                .create(namesFlux)
                .expectNext("a", "b", "c", "d", "e", "f")
                .verifyComplete();
    }

    @Test
    void testFluxConcatWith() {
        var namesFlux = fluxAndMonoGeneratorService.exploreConcatWith();
        StepVerifier
                .create(namesFlux)
                .expectNext("a", "b", "c", "d", "e", "f")
                .verifyComplete();
    }

    @Test
    void testFluxMerge() {
        var namesFlux = fluxAndMonoGeneratorService.exploreMerge();
        StepVerifier
                .create(namesFlux)
                .expectNext("a", "d", "b", "e", "c", "f")
                .verifyComplete();
    }

    @Test
    void testFluxMergeWith() {
        var namesFlux = fluxAndMonoGeneratorService.exploreMergeWith();
        StepVerifier
                .create(namesFlux)
                .expectNext("a", "d", "b", "e", "c", "f")
                .verifyComplete();
    }

    @Test
    void testFluxZip() {
        var namesFlux = fluxAndMonoGeneratorService.exploreZip();
        StepVerifier
                .create(namesFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void testFluxZip_1() {
        var namesFlux = fluxAndMonoGeneratorService.exploreZip_1();
        StepVerifier
                .create(namesFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }
}