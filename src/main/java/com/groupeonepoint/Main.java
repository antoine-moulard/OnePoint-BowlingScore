package com.groupeonepoint;

import com.groupeonepoint.application.BowlingGameFactory;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        var listArgs = Stream.of(args).toList();
        if (listArgs.isEmpty()) {
            System.out.println("No string in arguments. No score to calculate.");
            return;
        }
        var input = listArgs.get(0);
        if (listArgs.size() > 1) {
            input = String.join(" ", listArgs);
        }

        var line = BowlingGameFactory.createLineFromInput(input);
        var score = line.calculateScore();
        System.out.printf("Score is : %d%n", score);
    }
}