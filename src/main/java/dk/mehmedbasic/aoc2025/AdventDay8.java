package dk.mehmedbasic.aoc2025;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AdventDay8 {
    record Vector3(long x, long y, long z) {
        double distanceTo(Vector3 that) {
            double x2 = Math.pow(this.x - that.x, 2);
            double y2 = Math.pow(this.y - that.y, 2);
            double z2 = Math.pow(this.z - that.z, 2);

            return Math.sqrt(x2 + y2 + z2);
        }
    }

    record Pair(Vector3 one, Vector3 two, double distance) {
    }

    record Input(List<Vector3> junctionBoxes, List<Pair> pairs) {
    }

    public static void main(String[] args) throws IOException {
        Input input = readDistancesAndSort();
        List<Pair> pairs = input.pairs();


        List<Set<Vector3>> circuits = createCircuits(pairs);

        part1(pairs, circuits);
        part2(pairs, circuits, input.junctionBoxes());
    }

    private static void part1(List<Pair> pairs, List<Set<Vector3>> circuits) {
        for (Pair pair : pairs.subList(0, 1000)) {
            if (!inSameCircuit(circuits, pair.one, pair.two)) {
                mergePairIntoCircuit(circuits, pair);
            }
        }

        circuits.sort((a, b) -> -Integer.compare(a.size(), b.size()));
        System.out.println(circuits.get(0).size() * circuits.get(1).size() * circuits.get(2).size());
    }

    private static void mergePairIntoCircuit(List<Set<Vector3>> circuits, Pair pair) {
        Set<Vector3> circuit = getCircuit(circuits, pair.one);

        Set<Vector3> otherCircuit = getCircuit(circuits, pair.two);
        circuit.addAll(otherCircuit);

        circuit.add(pair.one);
        circuit.add(pair.two);

        // Merge circuits
        circuits.remove(circuit);
        circuits.remove(otherCircuit);

        circuits.add(circuit);
    }

    private static void part2(List<Pair> pairs, List<Set<Vector3>> circuits, List<Vector3> junctionBoxes) {
        for (Pair pair : pairs) {
            if (!inSameCircuit(circuits, pair.one, pair.two)) {
                mergePairIntoCircuit(circuits, pair);

                if (allConnected(circuits, junctionBoxes)) {

                    System.out.println(pair.one.x * pair.two.x);
                    break;

                }
            }
        }

    }

    private static List<Set<Vector3>> createCircuits(List<Pair> pairs) {
        List<Set<Vector3>> circuits = new ArrayList<>();
        Set<Vector3> filled = new HashSet<>();
        for (Pair pair : pairs) {
            if (!filled.contains(pair.one)) {
                filled.add(pair.one);

                HashSet<Vector3> e = new HashSet<>();
                e.add(pair.one);
                circuits.add(e);
            }
        }
        return circuits;
    }

    private static Input readDistancesAndSort() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day8.in"));

        List<Vector3> junctionBoxes = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split(",");

            long x = Long.parseLong(split[0]);
            long y = Long.parseLong(split[1]);
            long z = Long.parseLong(split[2]);

            junctionBoxes.add(new Vector3(x, y, z));
        }


        Set<Pair> pairSet = new HashSet<>();
        for (Vector3 box1 : junctionBoxes) {
            for (Vector3 box2 : junctionBoxes) {
                if (box1 != box2 && !pairSet.contains(new Pair(box2, box1, box2.distanceTo(box1)))) {
                    double distance = box1.distanceTo(box2);
                    Pair e = new Pair(box1, box2, distance);
                    pairSet.add(e);
                }
            }
        }

        List<Pair> pairs = new ArrayList<>(pairSet);
        pairs.sort(Comparator.comparingDouble(o -> o.distance));
        return new Input(junctionBoxes, pairs);
    }

    private static boolean allConnected(List<Set<Vector3>> connections, List<Vector3> junctionBoxes) {
        return connections.stream().anyMatch(s -> s.size() == junctionBoxes.size());
    }


    private static Set<Vector3> getCircuit(List<Set<Vector3>> circuits, Vector3 left) {
        for (Set<Vector3> circuit : circuits) {
            if (circuit.contains(left)) {
                return circuit;
            }
        }

        LinkedHashSet<Vector3> e = new LinkedHashSet<>();
        circuits.add(e);
        return e;
    }

    private static boolean inSameCircuit(List<Set<Vector3>> circuits, Vector3 left, Vector3 right) {
        for (Set<Vector3> circuit : circuits) {
            if (circuit.contains(left) && circuit.contains(right)) {
                return true;
            }
        }

        return false;
    }
}
