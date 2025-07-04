package com.example.testjava17;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.BiFunction;

@SpringBootApplication
@Slf4j
public class TestJava17Application {
    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println(add.apply(3, 5)); // in ra 8
        SpringApplication.run(TestJava17Application.class, args);
    }
}
