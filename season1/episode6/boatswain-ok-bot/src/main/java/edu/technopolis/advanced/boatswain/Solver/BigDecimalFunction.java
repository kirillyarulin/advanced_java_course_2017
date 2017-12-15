package edu.technopolis.advanced.boatswain.Solver;

@FunctionalInterface
public interface BigDecimalFunction<T,R,V> {
    R apply(T a1, V a2);
}
