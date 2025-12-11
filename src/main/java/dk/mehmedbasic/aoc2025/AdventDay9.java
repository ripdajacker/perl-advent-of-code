package dk.mehmedbasic.aoc2025;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AdventDay9 {
    record Vector2(int x, int y) {
        long areaOf(Vector2 that) {
            long minX = Math.min(this.x, that.x);
            long maxX = Math.max(this.x, that.x);

            long minY = Math.min(this.y, that.y);
            long maxY = Math.max(this.y, that.y);

            long xDelta = maxX - minX + 1;
            long yDelta = maxY - minY + 1;

            return Math.max(xDelta, 1) * Math.max(yDelta, 1);
        }
    }

    record Pair(Vector2 one, Vector2 two, long area) {
    }

    record Input(List<Vector2> points, List<Pair> pairs) {
    }

    record Bounds(int min, int max) {
        boolean within(int... numbers) {
            for (int that : numbers) {
                if (that < min || that > max) {
                    return false;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        Input input = read();
        List<Pair> pairs = input.pairs();

        System.out.println(pairs.get(0).area);


        List<Vector2> byX = new ArrayList<>(input.points());
        byX.sort(Comparator.comparingInt(Vector2::x).thenComparing(Vector2::y));

        List<Vector2> byY = new ArrayList<>(input.points());
        byY.sort(Comparator.comparingInt(Vector2::y).thenComparing(Vector2::x));


        List<Vector2> traced = new ArrayList<>();
        for (int i = 0; i < byX.size() - 1; i += 2) {
            Vector2 p1 = byX.get(i);
            Vector2 p2 = byX.get(i + 1);

            for (int y = p1.y; y <= p2.y; y++) {
                traced.add(new Vector2(p1.x, y));
            }
        }

        for (int i = 0; i < byY.size() - 1; i += 2) {
            Vector2 p1 = byY.get(i);
            Vector2 p2 = byY.get(i + 1);

            for (int x = p1.x; x <= p2.x; x++) {
                traced.add(new Vector2(x, p1.y));
            }
        }

        byX = new ArrayList<>(traced);
        byX.sort(Comparator.comparingInt(Vector2::x).thenComparing(Vector2::y));

        byY = new ArrayList<>(traced);
        byY.sort(Comparator.comparingInt(Vector2::y).thenComparing(Vector2::x));

        int minX = byX.get(0).x;
        int maxX = byX.get(byX.size() - 1).x;


        Map<Integer, Bounds> xBounds = new HashMap<>();
        Map<Integer, Bounds> yBounds = new HashMap<>();

        int first = 0;
        int last = 0;
        for (int x = minX; x <= maxX; x++) {
            first = last;

            for (int i = last; i < byX.size(); i++) {
                Vector2 p = byX.get(i);
                if (p.x != x) {
                    yBounds.put(x, new Bounds(byX.get(first).y, byX.get(i - 1).y));
                    last = i;
                    break;
                }
            }


            if (x == maxX) {
                yBounds.put(x, new Bounds(byX.get(first).y, byX.get(byX.size() - 1).y));
            }
        }


        int minY = byY.get(0).y;
        int maxY = byY.get(byX.size() - 1).y;

        last = 0;
        for (int y = minY; y <= maxY; y++) {
            first = last;

            for (int i = last; i < byY.size(); i++) {
                Vector2 p = byY.get(i);
                if (p.y != y) {
                    xBounds.put(y, new Bounds(byY.get(first).x, byY.get(i - 1).x));
                    last = i;
                    break;
                }
            }

            if (y == maxY) {
                xBounds.put(y, new Bounds(byY.get(first).x, byY.get(byY.size() - 1).x));
            }
        }

        List<Pair> list = pairs.stream().filter(pair -> {
            int minX1 = Math.min(pair.one.x, pair.two.x);
            int maxX1 = Math.max(pair.one.x, pair.two.x);

            int minY1 = Math.min(pair.one.y, pair.two.y);
            int maxY1 = Math.max(pair.one.y, pair.two.y);

            for (int x = minX1; x <= maxX1; x++) {
                if (!yBounds.get(x).within(minY1, maxY1)) {
                    return false;
                }
            }

            for (int y = minY1; y <= maxY1; y++) {
                if (!xBounds.get(y).within(minX1, maxX1)) {
                    return false;
                }
            }

            return true;
        }).toList();


        System.out.println(list.get(0).area);
    }


    private static Input read() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day9.in"));

        List<Vector2> junctionBoxes = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split(",");

            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);

            junctionBoxes.add(new Vector2(x, y));
        }


        Set<Pair> pairSet = new HashSet<>();
        for (Vector2 box1 : junctionBoxes) {
            for (Vector2 box2 : junctionBoxes) {
                if (box1 != box2 && !pairSet.contains(new Pair(box2, box1, box2.areaOf(box1)))) {
                    long distance = box1.areaOf(box2);
                    Pair e = new Pair(box1, box2, distance);
                    pairSet.add(e);
                }
            }
        }

        List<Pair> pairs = new ArrayList<>(pairSet);
        pairs.sort(Comparator.comparingDouble(o -> -o.area));
        return new Input(junctionBoxes, pairs);
    }


}
