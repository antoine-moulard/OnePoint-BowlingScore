package com.groupeonepoint.domain;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Line {

    private final List<Frame> frames;

    private Line(List<Frame> frames) {
        this.frames = Objects.requireNonNull(frames);
    }

    public static Line of(List<Frame> frames) {
        return new Line(frames);
    }

    public int calculateScore() {
//        return frames.stream()
//                .map(Frame::calculateScore)
//
//                .reduce(0, Integer::sum);
        return 0;
    }

}
