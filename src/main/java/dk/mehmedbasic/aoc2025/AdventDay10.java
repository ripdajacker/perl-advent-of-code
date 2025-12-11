package dk.mehmedbasic.aoc2025;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdventDay10 {
    public static void main(String[] args) throws IOException {
        read();
    }

    private static void read() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day10.in"));

        int lineNum = 1;


        int part1 = 0;
        for (String line : lines) {
            int goalEnd = line.indexOf("]") + 1;
            String goalLine = line.substring(1, goalEnd - 1).trim();

            long goal = 0;
            for (int i = goalLine.length() - 1; i >= 0; i--) {
                if (goalLine.charAt(i) == '#') {
                    goal |= (1L << (i));
                }
            }

            int state = 0;

            int joltageStart = line.indexOf("{");

            String switchesString = line.substring(goalEnd + 1, joltageStart).trim();
            List<Long> switchesList = new ArrayList<>();

            for (String switchText : switchesString.split(" ", -1)) {
                String replace = switchText.replace("(", "")
                        .replace(")", "");

                String[] split = replace.split(",", -1);
                long theSwitch = 0;
                for (int i = 0, splitLength = split.length; i < splitLength; i++) {
                    String number = split[i];
                    int bit = Integer.parseInt(number.trim());
                    theSwitch |= (1L << (bit));
                }

                switchesList.add(theSwitch);
            }


            long[] switches = new long[switchesList.size()];
            for (int i = 0; i < switchesList.size(); i++) {
                switches[i] = switchesList.get(i);
            }

            Diagram diagram = new Diagram(goal, state, switches, new byte[0]);

            int xxx = diagram.solvePart1();
            part1 += xxx;
            System.out.println(lineNum + "  " + xxx + "  " + diagram);

            lineNum++;
        }

        System.out.println(part1);
    }


    record Diagram(long goal, long state, long[] switches, byte[] joltageRequirements) {

        int solvePart1() {
            if (goal == state) {
                return 0;
            }

            int[] result = {Integer.MAX_VALUE};
            permute(switches, 0, result);
            return result[0];
        }

        // todo memoize known min
        void permute(long[] arr, int k, int[] oldMin) {

            if (oldMin[0] == 1) {
                return;
            }

            for (int i = k; i < arr.length && oldMin[0] > 1; i++) {
                long x = arr[i];
                arr[i] = arr[k];
                arr[k] = x;

                permute(arr, k + 1, oldMin);

                x = arr[i];
                arr[i] = arr[k];
                arr[k] = x;
            }


            if (k == arr.length - 1) {
                long state = 0;
                int min = oldMin[0];
                int end = Math.min(min, arr.length);
                for (int i = 0; i < end; i++) {
                    long b = arr[i];
                    state ^= b;

                    if (state == goal) {
                        oldMin[0] = Math.min(i + 1, min);
                        break;
                    }
                }
            }
        }
    }


}
