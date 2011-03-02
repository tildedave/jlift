#!/usr/bin/perl

#print("hey, what's up: $#ARGV");

#if ($#ARGV < 0) {
#  die("Not enough arguments");
#}

#open(DATA, "<$ARGV[0]");
$sum = 0;
$currentInt = 0;
$total = 0;
while($currentInt = <STDIN>) {
  ++$total;
  $sum += $currentInt;
}

$average = $sum / $total;

print("$average\n");
