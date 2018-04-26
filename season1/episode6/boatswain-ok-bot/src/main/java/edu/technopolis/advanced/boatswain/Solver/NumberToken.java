package edu.technopolis.advanced.boatswain.Solver;

import java.math.BigDecimal;
import java.util.ArrayDeque;

public class NumberToken extends Token<BigDecimal> implements CalculatingToken {

    public NumberToken(BigDecimal token) {
        super(token, Type.NUMBER);
    }

    @Override
    public void calculate(ArrayDeque<Token> stack, Token currToken) throws ParseExceprion, ArithmeticException {
        stack.push(currToken);
    }
}
