package com.groupeonepoint.domain;

public record Frame(int firstTry, int secondTry) {
    private final static int MAX_PINS = 10;
    private final static int MIN_PINS = 0;

    public Frame {
        validateTryValue(firstTry, "firstTry");
        validateTryValue(secondTry, "secondTry");
        validateTotalValue(firstTry, secondTry);
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
                            .formatted(this.firstTry, this.secondTry, this.calculateScore(), MAX_PINS)
            );
        }
    }

    public static Frame of(int firstTry, int secondTry) {
        return new Frame(firstTry, secondTry);
    }

    public int calculateScore() {
        return firstTry() + secondTry();
    }

    public static Frame createStrike() {
        return new Frame(MAX_PINS, 0);
    }

    public boolean isStrike() {
        return firstTry() == MAX_PINS;
    }

    public boolean isSpare() {
        return firstTry() + secondTry() == MAX_PINS;
    }


}
