package perococco.aoc.day20._private;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import perococco.aoc.common.AOCException;
import perococco.aoc.common.ArrayOfChar;
import perococco.aoc.common.Position;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

/**
 * @author perococco
 **/
public class Maze {

    private final boolean[] walls;

    private final int width;

    private final int height;

    private final boolean recursive;

    private final int startIndex;

    private final int endIndex;

    private final ImmutableMap<Integer,UnaryOperator<Pos>> teletransporters;

    private final ImmutableSet<Integer> outsidePortals;

    private final IntUnaryOperator moveUp;

    private final IntUnaryOperator moveDown;

    private final IntUnaryOperator moveLeft;

    private final IntUnaryOperator moveRight;


    public Maze(boolean[] walls, int width, int height, boolean recursive, @NonNull Position start, @NonNull Position end, @NonNull ImmutableList<Teletransporter> teletransporters) {
        this.walls = walls;
        this.width = width;
        this.height = height;
        this.recursive = recursive;

        final ToIntFunction<Position> toIndex = p -> p.x()+p.y()*width;

        final Function<Teletransporter,UnaryOperator<Pos>> transporter = t -> {
            final int to = toIndex.applyAsInt(t.to());
            final int delta = recursive?(t.goesDeeper()?1:-1):0;
            return p -> new Pos(this,p.level()+delta,to);
        };

        this.teletransporters = teletransporters.stream()
                                                .collect(ImmutableMap.toImmutableMap(t -> toIndex.applyAsInt(t.from()), transporter));

        this.outsidePortals = teletransporters.stream()
                                              .filter(Teletransporter::goesDeeper)
                                              .map(Teletransporter::to)
                                              .mapToInt(toIndex).boxed().collect(ImmutableSet.toImmutableSet());

        this.startIndex = toIndex.applyAsInt(start);
        this.endIndex = toIndex.applyAsInt(end);

        this.moveUp = i -> i - width;
        this.moveDown = i -> i + width;
        this.moveLeft = i -> i - 1;
        this.moveRight = i -> i + 1;
    }



    public static Maze createNormal(ArrayOfChar input) {
        return MazeBuilder.buildNormal(input);
    }

    public static Maze createRecursive(ArrayOfChar input) {
        return MazeBuilder.buildRecursive(input);
    }

    private Pos getPos(Pos pos, IntUnaryOperator operator) {
        final int index = operator.applyAsInt(pos.index());
        final UnaryOperator<Pos> teletransporter = teletransporters.get(index);
        if (teletransporter != null) {
            return teletransporter.apply(pos);
        } else {
            return new Pos(this,pos.level(),index);
        }
    }


    public Pos positionAbove(Pos pos) {
        return getPos(pos, moveUp);
    }

    public Pos positionBelow(Pos pos) {
        return getPos(pos, moveDown);
    }

    public Pos positionLeftOf(Pos pos) {
        return getPos(pos, moveLeft);
    }

    public Pos positionRightOf(Pos pos) {
        return getPos(pos, moveRight);
    }

    public Pos toPos(@NonNull Position position) {
        final int x = position.x();
        final int y = position.y();
        if (x<0 || y < 0 || x >=width || y >= height) {
            throw new AOCException("Position outside of the maze");
        }
        return new Pos(this,0, x+width*y);
    }



    public int findPathLength(@NonNull Position start, @NonNull Position end) {
        return findPathLength(toPos(start),toPos(end));

    }

    public int findPathLength() {
        return findPathLength(new Pos(this,0,startIndex),new Pos(this,0,endIndex));

    }

    private static final ImmutableList<UnaryOperator<Pos>> DISPLACEMENTS = ImmutableList.of(
            Pos::up,
            Pos::down,
            Pos::left,
            Pos::right
    );

    private int findPathLength(@NonNull Pos start, @NonNull Pos end) {
        final Map<Pos,Integer> distances = new HashMap<>();
        final Deque<Pos> queue = new LinkedList<>();

        distances.put(start,0);
        queue.addLast(start);
        while (!queue.isEmpty()) {
            final Pos current = queue.removeFirst();
            final int distance = distances.get(current);

            for (UnaryOperator<Pos> displacement : DISPLACEMENTS) {
                final Pos next = displacement.apply(current);
                if (distances.containsKey(next) || next.isWall()) {
                    continue;
                }

                if (next.equals(end)) {
                    return distance+1;
                }
                distances.put(next,distance+1);
                queue.addLast(next);
            }
        }

        return -1;

    }

    public void printToStandardOutput() {
        int idx = 0;
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                System.out.print(walls[idx++]?"#":".");
            }
            System.out.println();
        }
    }

    public boolean isWall(Pos pos) {
        final boolean wall = walls[pos.index()];
        if (wall || !recursive) {
            return wall;
        }

        if (pos.level() == 0) {
            return outsidePortals.contains(pos.index());
        }

        return pos.index() == startIndex || pos.index() == endIndex;
    }
}
