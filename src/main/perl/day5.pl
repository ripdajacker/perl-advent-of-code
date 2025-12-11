#!/usr/bin/perl
use builtin qw(true false);

use strict;
use warnings FATAL => 'all';
use List::Util qw(min max);
use Data::Dumper;

my $file = 'input/day5.in';

open my $info, $file or die "Could not open $file: $!";

my @ranges;
my @ingredients;

my $readingRanges = true;

while (my $line = <$info>) {
    if ($readingRanges == true) {
        if (builtin::trim($line) eq "") {
            $readingRanges = false;
        }
        else {
            my ($start, $end) = split('-', builtin::trim($line));
            my %r = (start => $start, end => $end);
            push(@ranges, \%r);
        }
    }
    else {
        if (builtin::trim($line) eq "") {
        }
        push(@ingredients, $line)
    }
}

sub isFresh {
    my ($number) = @_;

    for my $range (@ranges) {
        my $start = $range->{"start"};
        my $end = $range->{"end"};

        if ($number >= $start && $number <= $end) {
            return true;
        }
    }

    return false;
}

sub rangesOverlap {
    my ($range1, $range2)= @_;


    my $range1start = $range1->{"start"};
    my $range1end = $range1->{"end"};

    my $range2start = $range2->{"start"};
    my $range2end = $range2->{"end"};

    my $var1 = $range1start >= $range2start && $range1start <= $range2end;
    my $var2 = $range1end >= $range2start && $range1end <= $range2end;
    return $var1 || $var2;
}


sub mergeRanges {
    my @unsorted = @_;
    my @currentRanges = sort {$a->{start} <=> $b->{start}} @unsorted;

    my @result;

    my $currentRange = $currentRanges[0];
    for (my $i = 1; $i < $#currentRanges; $i++) {
        my $nextRange = $currentRanges[$i];

        print("range1: " . Dumper($currentRange));
        print("range2: " . Dumper($nextRange));

        if (rangesOverlap($currentRange, $nextRange) || rangesOverlap($nextRange, $currentRange)) {
            # ranges overlap
            my $newStart = min($currentRange->{"start"}, $nextRange->{"start"});
            my $newEnd = max($currentRange->{"end"}, $nextRange->{"end"});

            print("merging \n");

            my %newRange = (start => $newStart, end => $newEnd);
            $currentRange = \%newRange;
        } else {
            push(@result, $currentRange);
            $currentRange = $nextRange;
        }
    }

    print("final ranges: " . Dumper(@result) . "\n");

    return @currentRanges;
}

my $part1 = 0;
for my $ingredient (@ingredients) {
    if (isFresh($ingredient)) {
        $part1++;
    }
}

my $part2 = 0;

print("Part 1: " . $part1 . "\n");

my @newRanges = mergeRanges(@ranges);
print("Part 2: " . $part2 . "\n");

close $info;
