package com.groupeonepoint.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @Test
    void itShouldThrowWhenGivenNullListOfFrames() {
        assertThatThrownBy(() -> Line.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("List of frames can't be null in Line object.");
    }

    @ParameterizedTest
    @MethodSource
    void itShouldThrowIfFramesGivenAreInvalid(List<Frame> frames) {
        assertThatThrownBy(() -> Line.of(frames)).isInstanceOf(IncorrectNumberOfFrameInLine.class);
    }

    private static Stream<Arguments> itShouldThrowIfFramesGivenAreInvalid() {
        List<Frame> tooMuchFrames = new ArrayList<>();
        for (var i = 0; i < 15; ++i) {
            tooMuchFrames.add(Frame.noScore());
        }

        List<Frame> maxBonusFramesWithoutStrikes = new ArrayList<>();
        for (var i = 0; i < 12; ++i) {
            maxBonusFramesWithoutStrikes.add(Frame.createSpare(5));
        }
        return Stream.of(
                Arguments.of(tooMuchFrames),
                Arguments.of(maxBonusFramesWithoutStrikes)
        );
    }

    @ParameterizedTest
    @MethodSource
    void itShouldCorrectlyCalculateScore(List<Frame> frames, int expectedScore) {
        var line = Line.of(frames);

        assertThat(line.calculateScore()).isEqualTo(expectedScore);
    }

    private static Stream<Arguments> itShouldCorrectlyCalculateScore() {
        List<Frame> zeroScore = new ArrayList<>();
        List<Frame> nineAndMiss = new ArrayList<>();
        for (var i = 0; i < 10; ++i) {
            zeroScore.add(Frame.noScore());
            nineAndMiss.add(Frame.of(9, 0));
        }

        List<Frame> fiveAndSpare = new ArrayList<>();
        List<Frame> sixAndSpare = new ArrayList<>();
        for (var i = 0; i < 11; ++i) {
            fiveAndSpare.add(Frame.createSpare(5));
            sixAndSpare.add(Frame.createSpare(6));
        }

        List<Frame> onlyStrikes = new ArrayList<>();
        for (var i = 0; i < 12; ++i) {
            onlyStrikes.add(Frame.createStrike());
        }

        return Stream.of(
                Arguments.of(zeroScore, 0),
                Arguments.of(nineAndMiss, 90),
                Arguments.of(fiveAndSpare, 150),
                Arguments.of(sixAndSpare, 160),
                Arguments.of(onlyStrikes, 300), // perfect score

                Arguments.of(incompleteButValidGame(), 48),
                Arguments.of(List.of(Frame.createStrike()), 10),
                Arguments.of(List.of(Frame.createSpare(1)), 10),
                Arguments.of(List.of(), 0)
        );
    }

    private static List<Frame> incompleteButValidGame() {
        List<Frame> game = new ArrayList<>();

        game.add(Frame.noScore()); // = 0
        game.add(Frame.createStrike()); // 10 + 10 = 20
        game.add(Frame.createSpare(4)); // 10 + 3 = 33
        game.add(Frame.of(3, 2)); // 38
        game.add(Frame.noScore());
        game.add(Frame.createStrike()); // 10 + ? = 48
        return game;
    }
}