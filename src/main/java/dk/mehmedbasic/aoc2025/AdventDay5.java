package dk.mehmedbasic.aoc2025;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AdventDay5 {
    public static void main(String[] args) throws IOException {
        List<Range> ranges = read();

        List<Range> currentRanges = ranges;

        boolean modified = true;
        while (modified) {
            modified = false;

            Set<Range> merged = new HashSet<>();

            for (Range range1 : currentRanges) {
                for (Range range2 : currentRanges) {
                    if (range1 != range2) {
                        if (range1.overlaps(range2) || range2.overlaps(range1)) {
                            merged.add(range1.merge(range2));
                            modified = true;
                        }
                    }
                }
            }

            Iterator<Range> iterator = currentRanges.iterator();
            while (iterator.hasNext()) {
                Range next = iterator.next();

                for (Range mergedRange : merged) {
                    if (next.overlaps(mergedRange) || mergedRange.overlaps(next)) {
                        iterator.remove();
                        break;
                    }
                }
            }

            if (modified) {
                merged.addAll(currentRanges);
                currentRanges = new ArrayList<>(merged);
            }
        }

        long sum = 0L;
        for (Range range : currentRanges) {
            sum += range.end - range.start;
            sum++;
        }

        System.out.println(sum);
    }

    private static List<Range> read() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input/day5.in"));

        List<Range> ranges = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isBlank()) {
                break;
            } else {
                String[] split = line.split("-");

                Range range = new Range(Long.parseLong(split[0]), Long.parseLong(split[1]));
                ranges.add(range);
            }
        }
        return ranges;
    }

    record Range(long start, long end) {
        boolean overlaps(Range that) {
            return (start >= that.start && start <= that.end)
                    || (end >= that.start && end <= that.end);
        }


        public Range merge(Range that) {
            long newStart = Math.min(start, that.start);
            long newEnd = Math.max(end, that.end);

            return new Range(newStart, newEnd);
        }
    }
}
