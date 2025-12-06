#!/usr/bin/perl
use builtin qw(true false);

use strict;
use warnings FATAL => 'all';
use List::Util qw(min max);
use Data::Dumper;

my $file = 'input/day6.in';

open my $info, $file or die "Could not open $file: $!";

my @problems;

while (my $line = <$info>) {
    push(@problems, $line);
}

my @operations = split(/\s+/, $problems[-1]);
splice(@problems, -1);

my @results;

my $part1 = 0;

for (my $i = 0; $i <= $#operations; $i++) {
    my $colResult;

    my $operator = $operations[$i];
    for my $input (@problems) {
        my @numbers = split(/\s+/, builtin::trim($input));
        my $operand = $numbers[$i];

        if (!defined($colResult)) {
            $colResult = $operand;
        }
        else {
            my $script = $colResult . " " . $operator . " " . $operand;
            $colResult = eval($script);
        }
    }

    $part1 = $part1 + $colResult;
    push(@results, $colResult);
}

my $part2 = 0;

my @colWidths;

for (my $i = 0; $i <= $#operations; $i++) {
    my $colWidth = 0;
    for my $input (@problems) {
        my @numbers = split(/\s+/, $input);
        my $operand = builtin::trim($numbers[$i]);

        $colWidth = max($colWidth, length($operand));
    }

    push(@colWidths, $colWidth);
}

print(Dumper(@colWidths));

sub columnBoundaries {
    my ($col) = @_;

    my $end = $col;

    for (my $i = 0; $i <= $col; $i++) {
        $end += $colWidths[$i];
    }

    my $start = 0;
    if ($col > 0) {
        $start = $end - $colWidths[$col];
    }

    return ($start, $end);
}

for (my $i = 0; $i <= $#operations; $i++) {
    my ($start, $end) = columnBoundaries($i);
    printf("Cols %d: %d - %d\n", $i, $start, $end);
}

for (my $i = $#operations; $i >= 0; $i--) {
    my $colResult;

    my $operator = $operations[$i];
    my ($start, $end) = columnBoundaries($i);
    printf("Boundaries: %d %d '%d'\n", $start, $end, $i);

    for (my $c = $colWidths[$i] ; $c >= 0; $c--) {
        my $currentNumber = "";

        for my $input (@problems) {
            my $substr = substr($input, $start, $end);
            $substr =~ s/\n//g;

            my @numbers = split(//, $substr, -1);
            # printf("Substr: '%s'\n", $substr);
            #printf("Digit: (%d) '%s'\n", $c, $numbers[$c]);
            if (!(builtin::trim($numbers[$c]) eq "")) {
                $currentNumber = $currentNumber . $numbers[$c];
            }

            # if (!defined($colResult)) {
            #     $colResult = $operand;
            # }
            # else {
            #     my $script = $colResult . " " . $operator . " " . $operand;
            #     $colResult = eval($script);
            # }
        }

        print("Current num: " . $currentNumber . "\n");

        if (!defined($colResult) && !(builtin::trim($currentNumber) eq "")) {
            $colResult = $currentNumber;
        }
        elsif(!(builtin::trim($currentNumber) eq "")) {
            my $script = $colResult . " " . $operator . " " . $currentNumber;
            $colResult = eval($script);
        }
    }

    if (defined($colResult)) {
        printf("Result for %d: %d\n", $i, $colResult);
        $part2 = $part2 + $colResult;
    }
    else {
        printf("Result for %d: %d\n", $i, 0);
    }
    #    push(@results, $colResult);
}

print("Part 1: " . $part1 . "\n");
print("Part 2: " . $part2 . "\n");

close $info;
