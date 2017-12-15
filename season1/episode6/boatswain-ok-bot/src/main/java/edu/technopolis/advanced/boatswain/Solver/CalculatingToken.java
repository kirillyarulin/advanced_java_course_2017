package edu.technopolis.advanced.boatswain.Solver;

import java.util.ArrayDeque;

public interface CalculatingToken {
    void calculate(ArrayDeque<Token> stack, Token currToken) throws ParseExceprion, ArithmeticException;
}
