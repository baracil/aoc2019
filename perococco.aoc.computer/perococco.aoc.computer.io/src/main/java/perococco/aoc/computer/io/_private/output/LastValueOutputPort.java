package perococco.aoc.computer.io._private.output;

import lombok.NonNull;
import perococco.aoc.computer.io.OutputTransformer;

import java.util.Optional;

public class LastValueOutputPort<T> extends BaseOutputPort<Optional<T>,T> {

    private T value = null;

    public LastValueOutputPort(@NonNull OutputTransformer<T> outputTransformer) {
        super(outputTransformer);
    }

    @Override
    public void write(@NonNull T value) {
        this.value = value;
    }

    @NonNull
    @Override
    public Optional<T> programOutputAccessor() {
        return Optional.ofNullable(value);
    }
}
