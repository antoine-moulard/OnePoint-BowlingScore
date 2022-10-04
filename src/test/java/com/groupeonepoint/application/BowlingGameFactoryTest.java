package com.groupeonepoint.application;

import com.groupeonepoint.domain.Frame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BowlingGameFactoryTest {

    @ParameterizedTest
    @MethodSource
    void itShouldCorrectlyParseAndCreateGameFromInput(String input, List<Frame> frames) {
        var line = BowlingGameFactory.createLineFromInput(input);
        assertThat(line.getFrames()).containsExactlyElementsOf(frames);
    }

    private static Stream<Arguments> itShouldCorrectlyParseAndCreateGameFromInput() {
        List<Frame> zeroScore = new ArrayList<>();
        List<Frame> nineAndMiss = new ArrayList<>();
        for (var i = 0; i < 10; ++i) {
            zeroScore.add(Frame.noScore());
            nineAndMiss.add(Frame.of(9, 0));
        }

        List<Frame> fiveAndSpare = new ArrayList<>();
        for (var i = 0; i < 10; ++i) {
            fiveAndSpare.add(Frame.createSpare(5));
        }
        fiveAndSpare.add(Frame.of(5, 0));

        List<Frame> onlyStrikes = new ArrayList<>();
        for (var i = 0; i < 12; ++i) {
            onlyStrikes.add(Frame.createStrike());
        }

        return Stream.of(
                Arguments.of("-- -- -- -- -- -- -- -- -- --", zeroScore),
                Arguments.of("9- 9- 9- 9- 9- 9- 9- 9- 9- 9-", nineAndMiss),
                Arguments.of("5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/5", fiveAndSpare),
                Arguments.of("X X X X X X X X X X X X", onlyStrikes), // perfect score
                Arguments.of("-- X 4/ 32 -- X", incompleteButValidGame()),
                Arguments.of("", List.of())
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