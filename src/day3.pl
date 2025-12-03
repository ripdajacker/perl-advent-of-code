#!/usr/bin/perl
use builtin qw(true false);

use strict;
use warnings FATAL => 'all';

my $file = '../input/day3.in';

open my $info, $file or die "Could not open $file: $!";

sub findMaxFromIndex {
    my ($startIdx, $endIdx, @chars) = @_;
    my $max = -1;
    my $idx = 0;

    for (my $i = $startIdx; $i <= $endIdx; $i++) {
        my $char = $chars[$i];
        if ($char > $max) {
            $idx = $i;
            $max = $char;
        }
    }

    return ($max, $idx);
}

sub findLargestNumberSubstring {
    my ($line, $tailSize) = @_;
    my @chars = split(//, $line);

    my $result = "";
    my $lastMaxIndex = 0;
    for (my $x = 0; $x < $tailSize; $x++) {
        my $endOfWindow = $#chars - $tailSize + $x;
        my ($max, $idxOfMax) = findMaxFromIndex($lastMaxIndex, $endOfWindow, @chars);

        $result = $result . "" . $max;
        $lastMaxIndex = $idxOfMax + 1;
    }

    return $result;
}

my $part1 = 0;
my $part2 = 0;
while (my $line = <$info>) {
    $part1 += findLargestNumberSubstring($line, 2);
    $part2 += findLargestNumberSubstring($line, 12);
}

print("Part 1: " . $part1 . "\n");
print("Part 2: " . $part2 . "\n");

close $info;
