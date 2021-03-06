package perococco.aoc.launcher;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import perococco.aoc.input.Day;
import perococco.aoc.input.Part;

public class TestAllProgram {

    private static Object[][] solutions() {
        return new Object[][] {
                {Day.DAY_1, Part.PART_1, 3497399},
                {Day.DAY_1, Part.PART_2, 5243207},
                {Day.DAY_2, Part.PART_1, 6568671},
                {Day.DAY_2, Part.PART_2, 3951},
                {Day.DAY_3, Part.PART_1, 221},
                {Day.DAY_3, Part.PART_2, 18542},
                {Day.DAY_4, Part.PART_1, 594},
                {Day.DAY_4, Part.PART_2, 364},
                {Day.DAY_5, Part.PART_1, 9219874},
                {Day.DAY_5, Part.PART_2, 5893654},
                {Day.DAY_6, Part.PART_1, 145250},
                {Day.DAY_6, Part.PART_2, 274},
        };
    }

    @ParameterizedTest
    @MethodSource("solutions")
    void testAllProgram(@NonNull Day day, @NonNull Part part, @NonNull Object expectedResult) {
        final Problem problem = new Problem(day,part);
        final Object actualResult = problem.solution();

        Assertions.assertEquals(expectedResult,actualResult);
    }
}
