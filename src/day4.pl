#!/usr/bin/perl
use builtin qw(true false);

use strict;
use warnings FATAL => 'all';

my $file = '../input/day4.in';

sub readBoard {
    open my $info, $file or die "Could not open $file: $!";

    my %state = ();

    my $rowIndex = 0;
    while (my $line = <$info>) {
        my @chars = split(//, $line);
        for (my $x = 0; $x < $#chars; $x++) {
            my $char = $chars[$x];
            $state{$rowIndex}{$x} = $char;
        }

        $rowIndex++;
    }
    close $info;

    return %state;
}

sub cloneBoard {
    my (%board) = @_;
    my %clone = ();
    for (my $rowIdx = 0; $rowIdx < keys(%board); $rowIdx++) {
        for (my $colIdx = 0; $colIdx < keys(%{$board{$rowIdx}}); $colIdx++) {
            my $cell = $board{$rowIdx}{$colIdx};
            $clone{$rowIdx}{$colIdx} = $cell;
        }
    }

    return %clone;
}

sub countAdjacentRolls {
    my ($startRow, $startCol, %board) = @_;

    my $result = 0;

    for (my $rowIdx = $startRow - 1; $rowIdx <= $startRow + 1; $rowIdx++) {
        if ($rowIdx < 0 || $rowIdx >= (keys(%board))) {
            next;
        }

        for (my $colIdx = $startCol - 1; $colIdx <= $startCol + 1; $colIdx++) {
            if ($colIdx < 0 || $colIdx > (keys(%{$board{$rowIdx}})) || ($rowIdx == $startRow && $colIdx == $startCol)) {
                next;
            }

            my $cell = $board{$rowIdx}{$colIdx};
            if (defined($cell) && $cell eq "@") {
                $result++;
            }
        }
    }

    return $result;
}

sub solvePart1 {
    my %board = readBoard();

    my $result = 0;

    for (my $rowIdx = 0; $rowIdx < keys(%board); $rowIdx++) {
        for (my $colIdx = 0; $colIdx < keys(%{$board{$rowIdx}}); $colIdx++) {

            my $cell = $board{$rowIdx}{$colIdx};
            if ($cell eq "@") {
                my $count = countAdjacentRolls($rowIdx, $colIdx, %board);
                if ($count < 4) {
                    $result++;
                }
            }

        }
    }

    return $result;
}

sub solvePart2 {
    my %board = readBoard();
    my %state = cloneBoard(%board);

    my $change = true;
    my $result = 0;

    while ($change == true) {
        $change = false;

        for (my $rowIdx = 0; $rowIdx < keys(%board); $rowIdx++) {
            for (my $colIdx = 0; $colIdx < keys(%{$board{$rowIdx}}); $colIdx++) {

                my $cell = $board{$rowIdx}{$colIdx};
                if ($cell eq "@") {

                    my $count = countAdjacentRolls($rowIdx, $colIdx, %board);
                    if ($count < 4) {
                        $change = true;
                        $state{$rowIdx}{$colIdx} = ".";
                        $result++;
                    }
                }

            }
        }
        %board = cloneBoard(%state);
    }

    return $result;
}

print("Part 1: " . solvePart1() . "\n");
print("Part 2: " . solvePart2() . "\n");
