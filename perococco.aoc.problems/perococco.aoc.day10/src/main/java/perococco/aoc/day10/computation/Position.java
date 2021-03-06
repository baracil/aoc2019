package perococco.aoc.day10.computation;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * @author perococco
 **/
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Position {

    @NonNull
    public static Position create(int x, int y) {
        return new Position(x,y);
    }

    private final int x;

    private final int y;

    @NonNull
    public Direction subtract(@NonNull Position reference) {
        return Direction.create(this.x-reference.x, this.y - reference.y);
    }

    @NonNull
    public RadialRelativePosition positionRelativeTo(@NonNull Position reference) {
        return RadialRelativePosition.create(x - reference.x, y - reference.y);
    }

    public boolean notEqual(Position other) {
        return x != other.x || y != other.y;
    }
}
