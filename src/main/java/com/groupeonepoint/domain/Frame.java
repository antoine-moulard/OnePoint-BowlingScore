package com.groupeonepoint.domain;

import lombok.NonNull;

import java.util.List;
import java.util.Objects;

public record Frame(int firstRoll, int secondRoll, FrameType frameType) {
    private final static int MAX_PINS = 10;
    private final static int MIN_PINS = 0;

    private enum FrameType {
        STRIKE,
        SPARE,
        NORMAL
    }

    public Frame {
        validateTryValue(firstRoll, "firstRoll");
        validateTryValue(secondRoll, "secondRoll");
        validateTotalValue(firstRoll, secondRoll);
        Objects.requireNonNull(frameType, "Frame type can't be null");
    }

    private void validateTryValue(int tryValue, String fieldName) {
        if (tryValue < MIN_PINS || tryValue > MAX_PINS) {
            throw new IncorrectFrameValueException(
                    "The value '%d' is incorrect for field '%s'. Value range is: %d - %d"
                            .formatted(tryValue, fieldName, MIN_PINS, MAX_PINS)
            );
        }
    }

    private void validateTotalValue(int firstTry, int secondTry) {
        if (firstTry + secondTry > MAX_PINS) {
            throw new IncorrectFrameValueException(
                    "The score returned for this frame is incorrect: %d + %d = %d (max should be %d)"
                            .formatted(this.firstRoll, this.secondRoll, this.calculateScore(), MAX_PINS)
            );
        }
    }

    public static Frame of(int firstTry, int secondTry) {
        var type = determineFrameType(firstTry, secondTry);
        return new Frame(firstTry, secondTry, type);
    }

    public static Frame createStrike() {
        return new Frame(10, 0, FrameType.STRIKE);
    }

    public static Frame createSpare(int firstScore) {
        return new Frame(firstScore, MAX_PINS - firstScore, FrameType.SPARE);
    }

    public static Frame noScore() {
        return new Frame(0, 0, FrameType.NORMAL);
    }

    private static FrameType determineFrameType(int firstRoll, int secondRoll) {
        if (firstRoll == MAX_PINS) {
            return FrameType.STRIKE;
        } else if (firstRoll + secondRoll == MAX_PINS) {
            return FrameType.SPARE;
        } else {
            return FrameType.NORMAL;
        }
    }

    private int calculateScore() {
        return this.firstRoll + this.secondRoll;
    }

    /**
     * Calculate the score for this turn based on the next frames. If the list is empty no bonus score will be added.
     * <p>We want to use a list in the case the player do several strikes, then we need multiples frames to calculate the
     * bonus as it is based on the next roll (eg: the score for a strike followed by two other strikes is 30).</p>
     *
     * @param nextFrames can't be null, it may be empty.
     * @return the score for this frame plus its bonus.
     */
    public int calculateScore(@NonNull List<Frame> nextFrames) {
        return this.calculateScore() + calculateBonus(nextFrames);
    }

    private int calculateBonus(List<Frame> nextFrames) {
        return switch (this.frameType) {
            case STRIKE -> calculateBonusForStrikes(nextFrames);
            case SPARE -> calculateBonusForSpares(nextFrames);
            default -> 0;
        };
    }

    private int calculateBonusForStrikes(List<Frame> nextFrames) {
        if (nextFrames.isEmpty()) {
            return 0;
        }
        if (nextFrames.get(0).isStrike()) {
            return nextFrames.stream()
                    .limit(2).map(Frame::firstRoll).reduce(0, Integer::sum);
        } else {
            return nextFrames.get(0).calculateScore();
        }
    }

    private int calculateBonusForSpares(List<Frame> nextFrames) {
        return nextFrames.stream().limit(1).map(Frame::firstRoll).reduce(0, Integer::sum);
    }

    public boolean isStrike() {
        return this.frameType == FrameType.STRIKE;
    }

    public boolean isSpare() {
        return this.frameType == FrameType.SPARE;
    }
}
