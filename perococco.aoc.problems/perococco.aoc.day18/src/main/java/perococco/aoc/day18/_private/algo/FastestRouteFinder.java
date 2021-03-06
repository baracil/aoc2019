package perococco.aoc.day18._private.algo;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perococco.aoc.day18._private.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Find the fastest route between a key and all the others (except it self)
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FastestRouteFinder {

    @NonNull
    public static ImmutableList<Route> findFastestRouteToOtherKeys(@NonNull Maze maze, @NonNull Key reference) {
        return new FastestRouteFinder(maze, reference, maze.keyPosition(reference)).find();
    }

    @NonNull
    private final Maze maze;

    @NonNull
    private final Key reference;

    @NonNull
    private final Pos referencePosition;


    private final Set<Pos> visited = new HashSet<>();

    private final Deque<Node> queue = new LinkedList<>();

    private List<Route> routes;

    @NonNull
    private ImmutableList<Route> find() {
        routes = new ArrayList<>(maze.numberOfKeys());

        {
            final Node node = new Node(referencePosition);
            visited.add(node.position);
            queue.add(node);
        }

        while (queueIsNotEmpty()) {
            final Node current = queue.removeFirst();
            final Pos pos = current.position;

            if (pos.isKeyAndNotStartingPoint() && !current.isRoot()) {
                final Route route = current.createRoute();
                routes.add(route);
            }

            current.addAsChildIfValid(pos.up());
            current.addAsChildIfValid(pos.down());
            current.addAsChildIfValid(pos.left());
            current.addAsChildIfValid(pos.right());

            current.forEachChild(n -> {
                visited.add(n.position);
                queue.addLast(n);
            });


        }

        return ImmutableList.copyOf(routes);
    }

    public boolean queueIsNotEmpty() {
        return !queue.isEmpty();
    }

    private class Node {

        private final Node parent;

        @NonNull
        private final Pos position;

        private final int length;

        private final List<Node> children = new ArrayList<>();

        public Node(@NonNull Pos position) {
            this.parent = null;
            this.position = position;
            this.length = 0;
        }

        public Node(@NonNull Node parent, @NonNull Pos position) {
            this.parent = parent;
            this.position = position;
            this.length = parent.length + 1;
        }

        public void forEachChild(@NonNull Consumer<? super Node> nodeConsumer) {
            children.forEach(nodeConsumer);
        }

        public void addAsChildIfValid(@NonNull Pos position) {
            if (position.isWall() || visited.contains(position)) {
                return;
            }
            this.addToChildren(position);
        }

        public boolean isRoot() {
            return parent == null;
        }

        public Route createRoute() {
            final ImmutableList<Pos> stepsFromStartToEnd = pathFromRootWithoutStartAndEnd();

            final Keyring keyring = new Keyring(maze.allKeys());
            keyring.addKey(reference);

            final Route.Builder routeBuilder = Route.builder();
            routeBuilder.trip(new Trip(reference, position.getKey().orElseThrow()));
            routeBuilder.length(length);

            for (Pos pos : stepsFromStartToEnd) {
                pos.getDoor().filter(d -> !keyring.canOpen(d)).ifPresent(routeBuilder::doorOnTheWay);
                pos.getKey().ifPresent(routeBuilder::intermediaryKey);
            }

            return routeBuilder.build();
        }

        private ImmutableList<Pos> pathFromRootWithoutStartAndEnd() {
            final ImmutableList.Builder<Pos> builder = ImmutableList.builder();
            Node current = this;
            while (current!=null) {
                final Pos position = current.position;
                if (!position.equals(this.position)
                    && !position.equals(referencePosition)
                    && position.isNotAStartingPoint()
                ) {
                    builder.add(position);
                }
                current = current.parent;
            }

            return builder.build().reverse();
        }

        @Override
        public String toString() {
            return "Node{" +
                   "position=" + position +
                   ", children=" + children.stream().map(n->n.position).map(Object::toString).collect(
                    Collectors.joining(",")) +
                   '}';
        }

        public void addToChildren(Pos pos) {
            this.children.add(new Node(this,pos));
        }
    }
}
