#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

my $file = '../input/day1.in';

open my $info, $file or die "Could not open $file: $!";

my @numbers = ();

my $current = 50;

my $part1 = 0;
my $part2 = 0;
while (my $line = <$info>) {
    my $direction = substr $line, 0, 1;
    my $num = builtin::trim(substr $line, 1);

    if ($direction eq "L") {
        for (my $i = 0; $i < $num; $i++) {
            $current--;

            if ($current == 0) {
                $part2++;
            }

            $current = $current % 100;
        }
    }
    elsif ($direction eq "R") {
        for (my $i = 0; $i < $num; $i++) {
            $current++;

            if ($current == 0 || $current == 100) {
                $part2++;
            }

            $current = $current % 100;
        }
    }

    if ($current == 0) {
        $part1++;
    }

    push(@numbers, builtin::trim($line))
}

print("Part 1: " . $part1 . "\n");
print("Part 2: " . $part2 . "\n");

close $info;
