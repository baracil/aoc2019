package perococco.aoc.day18._private;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Keyring {

    @NonNull
    private final Set<Key> obtainedKeys = new HashSet<>();

    @NonNull
    private final Set<Key> missingKeys;

    public Keyring(@NonNull ImmutableSet<Key> allKeys) {
        this.missingKeys = new HashSet<>(allKeys);
    }

    public boolean canOpen(@NonNull ImmutableCollection<Door> doors) {
        return doors.isEmpty() || doors.stream().allMatch(this::canOpen);
    }

    public void addKey(@NonNull Key key) {
        this.obtainedKeys.add(key);
        this.missingKeys.remove(key);
    }

    public void removeKey(@NonNull Key key) {
        obtainedKeys.remove(key);
        this.missingKeys.add(key);
    }

    public boolean isAlreadyObtained(@NonNull Key key) {
        return obtainedKeys.contains(key);
    }

    public boolean isComplete() {
        return missingKeys.isEmpty();
    }

    public boolean canOpen(@NonNull Door door) {
        return obtainedKeys.contains(door.key());
    }

    public Stream<Key> missingKeys() {
        return missingKeys.stream();
    }

    public boolean areAlreadyObtained(@NonNull ImmutableCollection<Key> intermediaryKeys) {
        return intermediaryKeys.isEmpty() || intermediaryKeys.stream().allMatch(this::isAlreadyObtained);
    }

    public void reset() {
        missingKeys.addAll(obtainedKeys);
        obtainedKeys.clear();
    }

    @Override
    public String toString() {
        return "Keyring{" + obtainedKeys.stream().map(Key::displayedId).collect(Collectors.joining()) + '}';
    }
}
