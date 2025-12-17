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

            int joltageStart = line.indexOf("{");

            String switchesString = line.substring(goalEnd + 1, joltageStart).trim();
            List<Long> switchesList = new ArrayList<>();

            for (String switchText : switchesString.split(" ", -1)) {
                String replace = switchText.replace("(", "")
                        .replace(")", "");

                String[] split = replace.split(",", -1);
                long theSwitch = 0;
                for (String number : split) {
                    int bit = Integer.parseInt(number.trim());
                    theSwitch |= 1L << bit;
                }

                switchesList.add(theSwitch);
            }


            long[] switches = new long[switchesList.size()];
            for (int i = 0; i < switchesList.size(); i++) {
                switches[i] = switchesList.get(i);
            }

            Diagram diagram = new Diagram(goal, switches, new byte[0]);

            part1 += diagram.solvePart1();
            System.out.println(lineNum + "  " + diagram.solvePart1() + "  " + diagram);

            lineNum++;
        }

        System.out.println(part1);
    }


    record Diagram(long goal, long[] switches, byte[] joltageRequirements) {

        int solvePart1() {
            if (goal == 0) {
                return 0;
            }

            int min = Integer.MAX_VALUE;

            int patternCount = 1 << switches.length;
            for (int pattern = 0; pattern < patternCount; pattern++) {
                long currentState = 0;

                int press = 0;
                for (int bit = 0; bit < switches.length; bit++) {
                    boolean buttonSet = (pattern & (1 << bit)) != 0;
                    if (buttonSet) {
                        currentState = currentState ^ switches[bit];
                        press++;
                    }

                    if (currentState == goal) {
                        min = Math.min(press, min);
                        break;
                    }
                }
            }

            return min;
        }
    }
}
