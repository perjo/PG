package info.pj;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Calculator {

    private static final double R = 0.75;

    private final Set<Individual> founders;
    private final Set<Individual> descendants;
    private final List<Pair> pairs;

    public Calculator(Set<Individual> population) {
        List<Individual> individuals = population.stream()
                .sorted(Comparator.comparing(Individual::getYear))
                .toList();

        this.pairs = individuals.stream()
                .flatMap(i -> individuals.stream().map(j -> new Pair(i, j, relatedness(i,j))))
                .toList();

        this.founders = individuals.stream()
                .filter(Individual::founder)
                .collect(Collectors.toSet());

        this.descendants = individuals.stream()
                .filter(individual -> !individual.founder())
                .collect(Collectors.toSet());
    }

    public static double relatedness(Individual i1, Individual i2) {
        if (i1 == null || i2 == null) {
            return 0d;
        }
        if (i1.equals(i2)) {
            return 1d;
        }
        List<Individual> ordered = Stream.of(i1, i2)
                .sorted(Comparator.comparing(Individual::getYear))
                .toList();

        Individual old = ordered.get(0);
        Individual young = ordered.get(1);

        return relatedness(old, young.getSire()) / 2d + relatedness(old, young.getDam()) / 2d;
    }

    public double founderEquivalents() {
        List<Double> proportionalContributions = findProportinalContributionsForFounders();

        double pSquaredSum = proportionalContributions.stream()
                .mapToDouble(p -> p * p)
                .sum();

        return 1d / pSquaredSum;
    }

    private List<Double> findProportinalContributionsForFounders() {
        return founders.stream()
                .map(founder -> findContributionFromFounder(pairs, founder))
                .toList();
    }

    public double founderGenomeEquivalents() {
        List<Double> proportionalContributions = findProportinalContributionsForFounders();

        double pSquaredWeightedSum = proportionalContributions.stream()
                .mapToDouble(p -> p * p / R)
                .sum();

        return 1d / pSquaredWeightedSum;

    }

    private double findContributionFromFounder(List<Pair> pairs, Individual founder) {
        Double contributionFromFounder = pairs.stream()
                .filter(pair -> founder.equals(pair.i2) && descendants.contains(pair.i1))
                .collect(Collectors.averagingDouble(p -> p.r));

        System.out.println("Contribution from founder " + founder.getName() + ": " + contributionFromFounder);

        return contributionFromFounder;
    }

    private static class Pair {
        private final Individual i1;
        private final Individual i2;

        private final double r;


        private Pair(Individual i1, Individual i2, double r) {
            this.i1 = i1;
            this.i2 = i2;
            this.r = r;
        }
    }
}
