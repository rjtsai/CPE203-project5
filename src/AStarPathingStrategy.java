import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {

    public List<Point> getPath(Node current, List<Point> path,
                               BiPredicate<Point, Point> withinReach, Point start) {
        if (withinReach.test(current.getPos(), start)) {
            path.add(current.getPos());
            return path;
        } else {
            path.add(current.getPos());
            return getPath(current.getPrev(), path, withinReach, start);
        }
    }


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        // path to be returned
        List<Point> fPath = new LinkedList<>();
        // working path
        List<Point> path = new LinkedList<>();
        // comparator for f val
        Comparator<Node> fComp = Comparator.comparing(Node::getF);
        // open list queue
        PriorityQueue<Node> openList = new PriorityQueue<>(fComp);
        // open list hashMap
        Map<Point, Node> openListMap = new HashMap<>();
        // closed list hashMap
        Map<Point, Node> closedList = new HashMap<>();

        Node current;

        openList.add(new Node(start, start.calculateD(start),
                start.calculateD(end),
                start.calculateD(start) + start.calculateD(end),
                null));

        while (!openList.isEmpty()) {

            current = openList.remove();

            List<Point> neighbors = potentialNeighbors.apply(current.getPos())
                    .filter(canPassThrough)
                    .filter(p -> !closedList.containsKey(p))
                    .filter(p -> !p.equals(start) && !p.equals(end)).collect(Collectors.toList());

            if (withinReach.test(current.getPos(), end)) {
                path = getPath(current, path, withinReach, start);

                for (int i = path.size() - 1; i >= 0; i--) {
                    if (path.get(i) != end) {
                        fPath.add(path.get(i));
                    } else {
                        break;
                    }
                }
                break;
            }

            for (Point p : neighbors) {
                int g = current.getPos().calculateD(start);
                int h = p.calculateD(end);

                Node temp = new Node(p, g, h, h + g, current);

                if (openListMap.containsKey(p) && g < openListMap.get(p).getG()) {
                    openListMap.replace(p, temp);
                    openList.remove(openListMap.get(p));
                    openList.add(temp);
                } else if (!openListMap.containsKey(p)) {
                    openListMap.put(temp.getPos(), temp);
                    openList.add(temp);
                }

                closedList.put(current.getPos(), current);
            }
        }

        return fPath;
    }
}