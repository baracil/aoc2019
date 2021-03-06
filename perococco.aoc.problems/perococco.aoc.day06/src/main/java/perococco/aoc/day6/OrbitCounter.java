package perococco.aoc.day6;

import lombok.Getter;
import lombok.NonNull;

public class OrbitCounter implements OrbitWalker {

    private int depth = -1;

    private int count = 0;

    @Getter
    private int direct = 0;

    @Getter
    private int indirect = 0;

    @Override
    public void enter(@NonNull Body body) {
        depth++;
        if (depth > 0) {
            direct+=1;
            indirect+=depth-1;
        }
    }

    @Override
    public void leave(@NonNull Body body) {
        depth--;
    }

    public int sumOfDirectAndIndirectOrbits() {
        return direct+indirect;
    }

}
