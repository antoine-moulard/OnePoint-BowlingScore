package com.groupeonepoint.application;

import java.util.List;

public class IncorrectGameInput extends IllegalArgumentException {

    public IncorrectGameInput(List<String> rollsInput, String message) {
        super("The following rolls can't be parsed: '%s'. Reason: %s".formatted(rollsInput, message));
    }
}
