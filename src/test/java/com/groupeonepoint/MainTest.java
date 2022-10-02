package com.groupeonepoint;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class MainTest {

    @Test
    void itShouldCorrectlyRun() {
        var args = new String[]{"9- 9- 9- 9- 9- 9- 9- 9- 9- 9-"};
        assertThatCode(() -> Main.main(args)).doesNotThrowAnyException();
    }

}