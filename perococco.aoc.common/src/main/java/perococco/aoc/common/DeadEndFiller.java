package perococco.aoc.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

/**
 * @author perococco
 **/
@RequiredArgsConstructor
public class DeadEndFiller<T> {

    public static <T> T[] fill(
            @NonNull T[] array,
            int width, int height,
            @NonNull Predicate<T> emptyPredicate,
            @NonNull Predicate<T> wallPredicate,
            @NonNull T wall) {
        new DeadEndFiller<>(array,width,height,emptyPredicate,wallPredicate,wall).fill();
        return array;
    }


    private final T[] array;

    private final int width;

    private final int height;

    @NonNull
    private final Predicate<T> emptyPredicate;

    @NonNull
    private final Predicate<T> wallPredicate;

    @NonNull
    private final T wall;

    public void fill() {
        for (int i = 0; i < array.length; i++) {
            fillPosition(i);
        }
    }

    private void fillPosition(int index) {
        int current = index;
        while (isEmpty(current)) {
            final int next = nextIndexInDeadEnd(current);
            if (next < 0) {
                break;
            }
            array[current] = wall;
            current = next;
        }
    }

    private boolean isEmpty(int index) {
        return emptyPredicate.test(array[index]);
    }

    private boolean isWall(int x, int y) {
        if (x<0 || x >=width || y < 0 || y>=height) {
            return true;
        }
        return wallPredicate.test(array[x+y*width]);
    }

    private static final int UP = 1<<3;
    private static final int DOWN = 1<<2;
    private static final int RIGHT = 1<<1;
    private static final int LEFT = 1<<0;

    private int nextIndexInDeadEnd(int index) {
        final int x = index%width;
        final int y = index/width;

        final int up = isWall(x,y-1)?0:UP;
        final int down = isWall(x,y+1)?0:DOWN;
        final int right = isWall(x+1,y)?0:RIGHT;
        final int left = isWall(x-1,y)?0:LEFT;

        final int flag = up|down|left|right;
        switch (flag) {
            case UP : return index-width;
            case DOWN : return index+width;
            case LEFT : return index-1;
            case RIGHT : return index+1;
            default:
                return -1;
        }


    }

}
