package perococco.aoc.day21._private;

import lombok.NonNull;

/**
 * @author perococco
 **/
public interface ParameterNames {

    @NonNull
    String trueForm(int index);

    @NonNull
    String falseForm(int index);
}
