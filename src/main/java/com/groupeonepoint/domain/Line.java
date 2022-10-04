package com.groupeonepoint.domain;

import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Getter
public class Line {
    private final static int MAX_TURNS_IN_GAME = 10;
    private final static int MAX_TURNS_FOR_LAST_ROLL_WITH_SPARE_OR_STRIKE_BONUS = 11;
    private final static int MAX_TURNS_POSSIBLE_IN_GAME_WITH_BONUS = 12; // last frame is a strike, followed by another strike then final roll

    private final List<Frame> frames;

    private Line(List<Frame> frames) {
        this.frames = validateFrames(frames);
    }

    private List<Frame> validateFrames(List<Frame> frames) {
        Objects.requireNonNull(frames, "List of frames can't be null in Line object.");
        if (frames.size() > MAX_TURNS_POSSIBLE_IN_GAME_WITH_BONUS) {
            throw new IncorrectNumberOfFrameInLine(
                    "Too much frame in game, expected max %d; was %d"
                            .formatted(MAX_TURNS_POSSIBLE_IN_GAME_WITH_BONUS, frames.size())
            );
        }
        if (frames.size() >= MAX_TURNS_IN_GAME) {
            validateNumberOfFramesWhenLastFrameIsSpareOrStrike(frames);
        }
        return frames;
    }

    private void validateNumberOfFramesWhenLastFrameIsSpareOrStrike(List<Frame> frames) {
        if (frames.get(MAX_TURNS_IN_GAME - 1).isStrike() && frames.size() < MAX_TURNS_FOR_LAST_ROLL_WITH_SPARE_OR_STRIKE_BONUS) {
            throw new IncorrectNumberOfFrameInLine(
                    "Last frame in game was a strike, expected at least %d frames instead of %d"
                            .formatted(MAX_TURNS_FOR_LAST_ROLL_WITH_SPARE_OR_STRIKE_BONUS, frames.size())
            );
        }
        if ((!frames.get(MAX_TURNS_IN_GAME - 1).isStrike()) && frames.size() > MAX_TURNS_FOR_LAST_ROLL_WITH_SPARE_OR_STRIKE_BONUS) {
            throw new IncorrectNumberOfFrameInLine(
                    "Too much frame in game, the last frame was a spare and only one more roll should be possible. Expected: %d; given: %d"
                            .formatted(MAX_TURNS_FOR_LAST_ROLL_WITH_SPARE_OR_STRIKE_BONUS, frames.size())
            );
        }
    }

    public static Line of(List<Frame> frames) {
        return new Line(frames);
    }

    public int calculateScore() {
        return IntStream.range(0, Math.min(this.frames.size(), MAX_TURNS_IN_GAME))
                .mapToObj(i -> {
                    var frame = frames.get(i);
                    return frame.calculateScore(getTwoFollowingFrames(i, frame.frameType()));
                })
                .reduce(0, Integer::sum);
    }

    private List<Frame> getTwoFollowingFrames(int i, Frame.FrameType frameType) {
        if (i + 1 >= this.frames.size()) {
            return List.of();
        }
        return switch (frameType) {
            case STRIKE -> List.of(this.frames.get(i + 1), this.frames.get(i + 2));
            case SPARE -> List.of(this.frames.get(i + 1));
            default -> List.of();
        };
    }
}
