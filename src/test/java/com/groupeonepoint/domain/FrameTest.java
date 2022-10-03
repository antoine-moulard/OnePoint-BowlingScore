package com.groupeonepoint.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FrameTest {

    @ParameterizedTest(name = "It should throw an exception for values {0} and {1}")
    @MethodSource
    void itShouldThrowIfTriesValuesAreIncorrect(int firstTry, int secondTry) {
        assertThatThrownBy(() -> Frame.of(firstTry, secondTry)).isInstanceOf(IncorrectFrameValueException.class);
    }

    private static Stream<Arguments> itShouldThrowIfTriesValuesAreIncorrect() {
        return Stream.of(
                Arguments.of(100, 0),
                Arguments.of(1, 100),
                Arguments.of(-1, 2),
                Arguments.of(5, -7),
                Arguments.of(9, 9),
                Arguments.of(9, 2),
                Arguments.of(11, 0),
                Arguments.of(0, 11)
        );
    }

    @ParameterizedTest(name = "score expected for tries {0}, {1} is {2}")
    @MethodSource
    void itShouldCalculateCorrectly(int firstTry, int secondTry, int expected) {
        var frame = Frame.of(firstTry, secondTry);
        assertThat(frame.calculateScore()).isEqualTo(expected);
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
    void itShouldDetectStrike(int firstTry, int secondTry, boolean expected) {
        var frame = Frame.of(firstTry, secondTry);
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
    void itShouldDetectSpare(int firstTry, int secondTry, boolean expected) {
        var frame = Frame.of(firstTry, secondTry);
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

}