package com.groupeonepoint.application;

import com.groupeonepoint.domain.Frame;
import com.groupeonepoint.domain.Line;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BowlingGameFactory {
    private static final String SEPARATOR = " ";

    public static Line createLineFromInput(String input) {
        if (Objects.equals(input, "")) {
            return Line.of(List.of());
        }
        var framesInputStr = Stream.of(input.split(SEPARATOR)).toList();

        var framesInput = IntStream.range(0, framesInputStr.size())
                .mapToObj(i -> {
                    var frameRawInput = framesInputStr.get(i);
                    return FrameInput.from(frameRawInput, i);
                })
                .collect(Collectors.toCollection(ArrayList::new));

        handleBonusSpare(framesInput);
        var frames = framesInput.stream().map(FrameInput::toDomain).toList();
        return Line.of(frames);
    }

    private static void handleBonusSpare(List<FrameInput> framesInput) {
        if (framesInput.get(framesInput.size() - 1).isBonusSpare()) {
            var bonus = framesInput.get(framesInput.size() - 1);
            framesInput.add(new FrameInput(List.of(bonus.rollsInput().get(bonus.rollsInput().size() - 1), "0"), 10));
        }
    }

    public record FrameInput(List<String> rollsInput, int positionInGame) {
        private static final String VALID_CHAR = "([0-9-\\/X])";
        private static final String STRIKE_CHAR = "X";
        private static final String SPARE_CHAR = "/";
        private static final String MISS_CHAR = "-";

        public FrameInput {
            Objects.requireNonNull(rollsInput);
            validateCorrectNumberOfRoll(rollsInput, positionInGame);
            validateValueOfRolls(rollsInput);
        }

        public boolean isBonusSpare() {
            return this.positionInGame == 9 && this.rollsInput.size() == 3;
        }

        private void validateCorrectNumberOfRoll(List<String> rollsInput, int positionInGame) {
            if (rollsInput.size() == 0 || rollsInput.size() > 3) {
                throw new IncorrectGameInput(rollsInput, "Incorrect number of rolls in frame.");
            }
            if (rollsInput.size() > 2 && positionInGame < 9) {
                throw new IncorrectGameInput(rollsInput, "Incorrect number of rolls in frame. " +
                        "Three character will only be accepted for spare bonus roll.");
            }
            if (Objects.equals(rollsInput.get(0), STRIKE_CHAR) && rollsInput.size() != 1) {
                throw new IncorrectGameInput(rollsInput, "Frame is a strike, only one character '%s' was expected."
                        .formatted(STRIKE_CHAR));
            }
        }

        private void validateValueOfRolls(List<String> rollsInput) {
            rollsInput.forEach(s -> {
                if (!s.matches(VALID_CHAR)) {
                    throw new IncorrectGameInput(rollsInput, "The character '%s' is invalid.".formatted(s));
                }
            });
        }

        public static FrameInput from(String input, int index) {
            var rolls = Stream.of(input.split("")).toList();
            return new FrameInput(rolls, index);
        }

        public Frame toDomain() {
            if (Objects.equals(getFirstChar(), STRIKE_CHAR)) {
                return Frame.createStrike();
            } else if (Objects.equals(getSecondChar(), SPARE_CHAR)) {
                return Frame.createSpare(Integer.parseInt(getFirstChar()));
            } else {
                return Frame.of(
                        transformIntoInteger(getFirstChar()),
                        transformIntoInteger(getSecondChar())
                );
            }
        }

        private Integer transformIntoInteger(String s) {
            s = Objects.equals(s, MISS_CHAR) ? "0" : s;
            return Integer.parseInt(s);
        }

        private String getFirstChar() {
            return this.rollsInput.get(0);
        }

        private String getSecondChar() {
            return this.rollsInput.get(1);
        }

    }

}
