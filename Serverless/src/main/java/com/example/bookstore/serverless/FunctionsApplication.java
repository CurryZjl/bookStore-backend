package com.example.bookstore.serverless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@SpringBootApplication
public class FunctionsApplication {
    @Bean
    public Function<Flux<Integer[]>, Flux<Integer>> price() {
        return e->e.map(value -> value[0]*value[1]);
    }

    public static void main(String[] args) {
        SpringApplication.run(FunctionsApplication.class, args);
    }
}
