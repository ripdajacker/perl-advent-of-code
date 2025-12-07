#!/usr/bin/perl
use builtin qw(true false);

use strict;
use warnings FATAL => 'all';
use List::Util qw(min max);
use Data::Dumper;

my $file = 'input/day7.in';

open my $info, $file or die "Could not open $file: $!";

my @grid;

while (my $line = <$info>) {
    push(@grid, $line);
}

my $startingCol = index($grid[0], "S");
substr($grid[1], $startingCol, 1, "|");

for (my $rowIdx = 2; $rowIdx <= $#grid; $rowIdx++) {
    my $prevRow = $grid[$rowIdx - 1];
    my $rowStr = $grid[$rowIdx];

    for (my $colIdx = 0; $colIdx < length($rowStr); $colIdx++) {
        if (substr($rowStr, $colIdx, 1) eq "^" && substr($prevRow, $colIdx, 1) eq "|") {
            print("Found ^ at idx " . $colIdx . "\n");
            substr($rowStr, $colIdx - 1, 1, "|");
            substr($rowStr, $colIdx + 1, 1, "|");
        }
        elsif (substr($prevRow, $colIdx, 1) eq "|") {
            substr($rowStr, $colIdx, 1, "|");
        }
    }

    $grid[$rowIdx] = $rowStr;
}

my $part1 = 0;
for (my $rowIdx = 0; $rowIdx <= $#grid; $rowIdx++) {
    my $rowStr = $grid[$rowIdx];
    my $prevRow = $grid[$rowIdx - 1];
    for (my $colIdx = 0; $colIdx < length($rowStr); $colIdx++) {
        if (substr($rowStr, $colIdx, 1) eq "^" && substr($prevRow, $colIdx, 1) eq "|") {
            $part1 += 1;
        }
    }
}

for (my $rowIdx = 0; $rowIdx <= $#grid; $rowIdx++) {
    my $rowStr = $grid[$rowIdx];
    print($rowStr);
}

my $part2 = 0;
print("Part 1: " . $part1 . "\n");
print("Part 2: " . $part2 . "\n");

close $info;
