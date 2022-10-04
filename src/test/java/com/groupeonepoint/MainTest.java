package com.groupeonepoint;

import com.groupeonepoint.application.BowlingGameFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MainTest {

    @Test
    void itShouldStart() {
        var args = new String[]{"9- 9- 9- 9- 9- 9- 9- 9- 9- 9-"};
        assertThatCode(() -> Main.main(args)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource
    void basicTestsShouldRunCorrectly(String input, int expectedScore) {
        // >>>   test all the code here, you may add your own code   <<<

        var line = BowlingGameFactory.createLineFromInput(input);
        var score = line.calculateScore();
        System.out.printf("Score is : %d%n", score);
        assertThat(score).isEqualTo(expectedScore);
    }

    private static Stream<Arguments> basicTestsShouldRunCorrectly() {
        return Stream.of(
                Arguments.of("X X X X X X X X X X X X", 300),
                Arguments.of("9- 9- 9- 9- 9- 9- 9- 9- 9- 9-", 90),
                Arguments.of("5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/5", 150),
                Arguments.of("51 42 3- 5- 41 5/ 45 -- -- --", 48),
                Arguments.of("51 42 3-", 15)
        );
    }

}