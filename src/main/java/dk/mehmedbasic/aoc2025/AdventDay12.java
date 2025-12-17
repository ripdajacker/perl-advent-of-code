package dk.mehmedbasic.aoc2025;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdventDay12 {
    public static void main(String[] args) throws IOException {
        System.out.println(read());
    }


    private static int read() throws IOException {
        int result = 0;

        List<Integer> shapes = new ArrayList<>();

        int state = 0;
        List<String> lines = Files.readAllLines(Path.of("input/day12.in"));

        int currentShape = 0;
        for (String line : lines) {
            if (line.matches("\\d:.*$")) {
                continue;
            }


            if (state == 0) {
                if (line.isBlank()) {
                    shapes.add(currentShape);
                    currentShape = 0;
                } else {
                    char[] charArray = line.toCharArray();
                    for (char c : charArray) {
                        if (c == '#') {
                            currentShape++;
                        }
                    }
                }
            }

            if (line.matches("\\d+x\\d+:.*")) {
                state = 1;
            }

            if (state == 1) {
                String[] split = line.split(":");
                String gridSize = split[0];

                String[] gridXy = gridSize.split("x");

                int x = Integer.parseInt(gridXy[0]);
                int y = Integer.parseInt(gridXy[1]);
                int area = x * y;

                String[] indices = split[1].trim().split(" ");
                int sum = 0;
                for (int i = 0; i < indices.length; i++) {
                    int shapeCount = Integer.parseInt(indices[i]);
                    sum += shapes.get(i) * shapeCount;
                }

                if (sum <= area) {
                    result++;
                }
            }
        }
        return result;
    }

}
