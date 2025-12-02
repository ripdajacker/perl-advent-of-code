#!/usr/bin/perl
use builtin qw(true false);

use strict;
use warnings FATAL => 'all';

my $file = '../input/day2.in';

open my $info, $file or die "Could not open $file: $!";

sub hasRepeatingSubstrings {
    my ($str) = @_;
    my $len = length($str);

    if ($len < 2) {
        return false;
    }

    for my $i (0 .. $len / 2) {
        if ($i + 1 < $len && $len % ($i + 1) == 0) {
            my $pattern = substr($str, 0, $i + 1);

            my $found = true;
            my $lastIndex = 0;

            while ($found && $lastIndex < $len) {
                my $idx = index($str, $pattern, $lastIndex);
                if ($idx != $lastIndex) {
                    $found = false;
                }
                else {
                    $lastIndex = $lastIndex + $i + 1;
                }
            }

            if ($found == true) {
                return true;
            }
        }
    }

    return false;
}

sub hasRepeatingSubstringTwice {
    my ($str) = @_;
    my $len = length($str);

    if ($len % 2 != 0 || $len < 2) {
        return false;
    }

    return substr($str, 0, $len / 2) == substr($str, $len / 2);
}

my $part1 = 0;
my $part2 = 0;
while (my $line = <$info>) {
    my @ranges = split(",", $line);
    for my $i (0 .. scalar(@ranges) - 1) {
        my ($start, $end) = split('-', $ranges[$i]);
        for my $j ($start .. $end) {
            if (hasRepeatingSubstringTwice($j)) {
                $part1 += $j;
            }

            if (hasRepeatingSubstrings($j)) {
                $part2 += $j;
            }
        }
    }
}

print("Part 1: " . $part1 . "\n");
print("Part 2: " . $part2 . "\n");

close $info;
