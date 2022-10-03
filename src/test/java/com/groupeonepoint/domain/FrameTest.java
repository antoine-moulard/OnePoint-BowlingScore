package com.groupeonepoint.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FrameTest {

    @ParameterizedTest(name = "It should throw an exception for values {0} and {1}")
    @MethodSource
    void itShouldThrowIfTriesValuesAreIncorrect(int firstRoll, int secondRoll) {
        assertThatThrownBy(() -> Frame.of(firstRoll, secondRoll)).isInstanceOf(IncorrectFrameValueException.class);
    }

    private static Stream<Arguments> itShouldThrowIfTriesValuesAreIncorrect() {
        return Stream.of(
                Arguments.of(100, 0),
                Arguments.of(1, 100),
                Arguments.of(-1, 2),
                Arguments.of(5, -7),
                Arguments.of(9, 9),
                Arguments.of(9, 2),
                Arguments.of(0, -1),
                Arguments.of(11, 0),
                Arguments.of(0, 11)
        );
    }

    @ParameterizedTest(name = "score expected for tries {0}, {1} is {2}")
    @MethodSource
    void itShouldCalculateCorrectly(int firstRoll, int secondRoll, int expected) {
        var frame = Frame.of(firstRoll, secondRoll);
        assertThat(frame.calculateScore(List.of())).isEqualTo(expected);
    }

    private static Stream<Arguments> itShouldCalculateCorrectly() {
        return Stream.of(
                Arguments.of(5, 0, 5),
                Arguments.of(0, 5, 5),
                Arguments.of(10, 0, 10),
                Arguments.of(9, 1, 10),
                Arguments.of(1, 2, 3),
                Arguments.of(2, 1, 3)
        );
    }

    @ParameterizedTest(name = "Do we have a strike for first and second try {0}, {1}? {2}")
    @MethodSource
    void itShouldDetectStrike(int firstRoll, int secondRoll, boolean expected) {
        var frame = Frame.of(firstRoll, secondRoll);
        assertThat(frame.isStrike()).isEqualTo(expected);
    }

    private static Stream<Arguments> itShouldDetectStrike() {
        return Stream.of(
                Arguments.of(10, 0, true),
                Arguments.of(0, 10, false),
                Arguments.of(5, 5, false),
                Arguments.of(1, 1, false)
        );
    }

    @ParameterizedTest(name = "Do we have a spare for first and second try {0}, {1}? {2}")
    @MethodSource
    void itShouldDetectSpare(int firstRoll, int secondRoll, boolean expected) {
        var frame = Frame.of(firstRoll, secondRoll);
        assertThat(frame.isSpare()).isEqualTo(expected);
    }

    private static Stream<Arguments> itShouldDetectSpare() {
        return Stream.of(
                Arguments.of(1, 2, false),
                Arguments.of(9, 0, false),
                Arguments.of(5, 4, false),
                Arguments.of(9, 1, true),
                Arguments.of(2, 8, true)
        );
    }

    @ParameterizedTest
    @MethodSource
    void itShouldCorrectlyCalculateScoreBasedOnBonusFromNextFrame(Frame frame, List<Frame> nextTurns, int expectedScore) {
        assertThat(frame.calculateScore(nextTurns)).isEqualTo(expectedScore);
    }

    private static Stream<Arguments> itShouldCorrectlyCalculateScoreBasedOnBonusFromNextFrame() {
        return Stream.of(
                // second frame in list should be ignored, first one is not a strike so the first frame count for two rolls
                Arguments.of(Frame.createStrike(), List.of(Frame.of(4, 3), Frame.of(5, 4)), 17),
                Arguments.of(Frame.createStrike(), List.of(Frame.createSpare(1), Frame.of(5, 4)), 20),
                Arguments.of(Frame.createStrike(), List.of(Frame.noScore(), Frame.of(5, 4)), 10),
                // second frame in list should be used as the first one is a strike and the frame does not have a second roll.
                Arguments.of(Frame.createStrike(), List.of(Frame.createStrike(), Frame.createStrike()), 30),
                Arguments.of(Frame.createStrike(), List.of(Frame.createStrike(), Frame.createSpare(5)), 25),
                Arguments.of(Frame.createStrike(), List.of(Frame.createStrike(), Frame.of(4, 5)), 24),
                Arguments.of(Frame.createStrike(), List.of(Frame.createStrike(), Frame.noScore()), 20),

                Arguments.of(Frame.createSpare(5), List.of(Frame.noScore()), 10),
                Arguments.of(Frame.createSpare(7), List.of(Frame.noScore()), 10),
                Arguments.of(Frame.createSpare(5), List.of(Frame.of(4, 3)), 14),
                Arguments.of(Frame.createSpare(5), List.of(Frame.of(4, 3), Frame.of(3, 3)), 14), // second frame should be ignored
                Arguments.of(Frame.createSpare(5), List.of(Frame.createStrike(), Frame.of(3, 3)), 20), // second frame should be ignored
                Arguments.of(Frame.createSpare(5), List.of(Frame.createSpare(5)), 15)
        );
    }
}