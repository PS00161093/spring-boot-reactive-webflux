package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
