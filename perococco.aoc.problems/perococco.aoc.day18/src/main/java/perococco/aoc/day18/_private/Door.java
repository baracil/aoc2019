package perococco.aoc.day18._private;

import lombok.NonNull;

public class Door {

    public Door(@NonNull Key keyOfTheDoor) {
        this.id = keyOfTheDoor.id().toLowerCase();
        this.keyOfTheDoor = keyOfTheDoor;
    }

    @NonNull
    private final String id;

    @NonNull
    private final Key keyOfTheDoor;

    @NonNull
    public Key key() {
        return keyOfTheDoor;
    }

    @Override
    public String toString() {
        return "Door{" +
               "id='" + id + "'}";
    }

    public String idOfItsKey() {
        return keyOfTheDoor.id();
    }
}
