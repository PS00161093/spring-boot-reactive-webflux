package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.
                just("Alex")
                .log();
    }

    public Flux<String> namesFluxMap() {
        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .map(String::toUpperCase)
                .log();
    }

    public Flux<String> namesFluxFilter(int nameLength) {
        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .filter(name -> name.length() == nameLength)
                .log();
    }

    public Flux<String> namesFluxFilterFlatMap(int nameLength) {
        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() == nameLength)
                .flatMap(this::splitString)
                .log();
    }

    private Flux<String> splitString(String str) {
        var charsArray = str.split("");
        return Flux
                .fromArray(charsArray);
    }

    public Flux<String> namesFluxFilterFlatMapAsync(int nameLength) {
        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameLength)
                .flatMap(this::splitStringWithDelay)
                .log();
    }

    private Flux<String> splitStringWithDelay(String str) {
        var charsArray = str.split("");
        var delay = new Random().nextInt(1000);
        return Flux
                .fromArray(charsArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> namesFluxConcatMap(int nameLength) {
        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameLength)
                .concatMap(this::splitStringWithDelay)
                .log();
    }

    public Mono<List<String>> namesMonoFlatMap(int nameLength) {
        return Mono
                .just("Alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > nameLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    private Mono<List<String>> splitStringMono(String name) {
        var charArray = name.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> namesMonoFlatMapMany(int nameLength) {
        return Mono
                .just("Alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > nameLength)
                .flatMapMany(this::splitString)
                .log();
    }

    public Flux<String> namesFluxTransform(int nameLength) {
        Function<Flux<String>, Flux<String>> filterMap =
                name -> name
                        .map(String::toUpperCase)
                        .filter(n -> n.length() > nameLength);

        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> namesFluxWithDefault(int nameLength) {
        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .filter(name -> name.length() == nameLength)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFluxWithSwitchIfEmpty(int nameLength) {
        var defaultFlux = Flux.just("default").map(String::toUpperCase);

        return Flux
                .fromIterable(List.of("Alex", "Ben", "Chloe"))
                .filter(name -> name.length() == nameLength)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> exploreConcat() {
        var abcFlux = Flux.just("a", "b", "c");
        var defFlux = Flux.just("d", "e", "f");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> exploreConcatWith() {
        var abcFlux = Flux.just("a", "b", "c");
        var defFlux = Flux.just("d", "e", "f");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> exploreMerge() {
        var abcFlux = Flux
                .just("a", "b", "c")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux
                .just("d", "e", "f")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> exploreMergeWith() {
        var abcFlux = Flux
                .just("a", "b", "c")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux
                .just("d", "e", "f")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> exploreMergeSequential() {
        var abcFlux = Flux.just("a", "b", "c");
        var defFlux = Flux.just("d", "e", "f");

        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    public Flux<String> exploreZip() {
        var abcFlux = Flux.just("a", "b", "c");
        var defFlux = Flux.just("d", "e", "f");

        return Flux
                .zip(abcFlux, defFlux, (first, second) -> first.toUpperCase() + second.toUpperCase())
                .log();
    }

    public Flux<String> exploreZip_1() {
        var abcFlux = Flux.just("a", "b", "c");
        var defFlux = Flux.just("d", "e", "f");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");

        return Flux
                .zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .map(String::toUpperCase)
                .log();
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService
                .namesFlux()
                .subscribe(name -> System.out.println("Name is : " + name));

        fluxAndMonoGeneratorService
                .nameMono()
                .subscribe(name -> System.out.println("Name is : " + name));
    }
}
