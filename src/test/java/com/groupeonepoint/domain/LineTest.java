package com.groupeonepoint.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @ParameterizedTest
    @MethodSource
    void itShouldCorrectlyCalculateScore(List<Frame> frames, int expectedScore) {
        var line = Line.of(frames);

        assertThat(line.calculateScore()).isEqualTo(expectedScore);
    }

    private static Stream<Arguments> itShouldCorrectlyCalculateScore() {
        List<Frame> nineAndMiss = new ArrayList<>();
        for (var i = 0; i < 10; ++i) {
            nineAndMiss.add(Frame.of(9, 0));
        }

        List<Frame> fiveAndSpare = new ArrayList<>();
        for (var i = 0; i < 10; ++i) {
            fiveAndSpare.add(Frame.of(5, 5));
        }
        fiveAndSpare.add(Frame.of(5, 0));
        return Stream.of(
                Arguments.of(nineAndMiss, 90),
                Arguments.of(fiveAndSpare, 150)
        );
    }
}