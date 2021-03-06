package perococco.aoc.day24;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author perococco
 **/
public class TestPart1 {

    private static final ImmutableList<String> LINES = ImmutableList.of(
            "....#",
            "#..#.",
            "#..##",
            "..#..",
            "#...."
    );

    @Test
    public void testPart1() {
        final Day24Part1Problem problem = new Day24Part1Problem(LINES);
        final int solution = problem.solution();

        Assertions.assertEquals(2129920, solution);
    }
}
