package dk.mehmedbasic.aoc2025;


import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AdventDay11 {
    public static void main(String[] args) throws IOException {
        Graph graph = readGraph();


        Node root = graph.getNode("you");
        Node out = graph.getNode("out");

        System.out.printf("Part 1: %d\n", root.pathsTo(out));

        Node svr = graph.getNode("svr");
        Node fft = graph.getNode("fft");
        Node dac = graph.getNode("dac");

        System.out.println();

        long svrToFft = svr.pathsTo(fft);
        long fftToDac = fft.pathsTo(dac);
        long dacToOut = dac.pathsTo(out);

        System.out.printf("Paths from 'svr'->'fft': %d\n", svrToFft);
        System.out.printf("Paths from 'fft'->'dac': %d\n", fftToDac);
        System.out.printf("Paths from 'dac'->'out': %d\n", dacToOut);

        System.out.printf("Part 2: %d\n", svrToFft * fftToDac * dacToOut);
    }


    private static Graph readGraph() throws IOException {
        Graph graph = new Graph(new HashMap<>());

        List<String> lines = Files.readAllLines(Path.of("input/day11.in"));
        for (String line : lines) {
            if (line.trim().isBlank()) {
                continue;
            }

            int nameEnd = line.indexOf(":");

            Node node = graph.getNode(line.substring(0, nameEnd).trim());
            for (String connectedTo : line.substring(nameEnd + 1).split(" ", -1)) {
                String trimmedName = connectedTo.trim();
                if (!trimmedName.isBlank()) {
                    Node otherNode = graph.getNode(trimmedName);
                    node.pointTo(otherNode);
                }
            }
        }
        return graph;
    }


    record Graph(Map<String, Node> nodeMap) {
        Node getNode(String name) {
            return nodeMap.computeIfAbsent(name, ignored -> new Node(name));
        }
    }

    @EqualsAndHashCode(exclude = {"pointingTo", "pointingToThis"})
    static class Node {
        Set<Node> pointingTo = new HashSet<>();
        Set<Node> pointingToThis = new HashSet<>();

        String name;

        Node(String name) {
            this.name = name;
        }

        void pointTo(Node that) {
            that.pointingToThis.add(this);
            this.pointingTo.add(that);
        }

        long pathsTo(Node dest) {
            return pathsTo(dest, new HashMap<>());
        }

        private long pathsTo(Node dest, Map<Node, Long> memo) {
            if (this == dest) {
                memo.put(dest, 1L);
            }

            if (memo.containsKey(this)) {
                return memo.get(this);
            }

            long sum = 0;
            for (Node child : this.pointingTo) {
                sum += child.pathsTo(dest, memo);
            }

            memo.put(this, sum);
            return sum;
        }
    }
}
