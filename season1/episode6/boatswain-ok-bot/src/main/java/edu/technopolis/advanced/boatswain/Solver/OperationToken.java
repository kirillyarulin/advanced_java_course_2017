package edu.technopolis.advanced.boatswain.Solver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;

public class OperationToken extends Token<Character> implements CalculatingToken {
    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char TIMES = '*';
    private static final char DIVISION = '/';
    MathContext context;

    public static boolean isOperator(char c)
    {
        return c==PLUS || c==MINUS || c==TIMES || c==DIVISION;
    }

    public OperationToken(Character token, MathContext  context) {
        super(token, Type.OPERATION);
        this.context = context;
    }



    @Override
    public void calculate(ArrayDeque<Token> stack, Token currToken) throws ParseExceprion, ArithmeticException {
        BigDecimal b;
        BigDecimal a;
       try {
           b = ((NumberToken) stack.pop()).getToken();
           a = ((NumberToken) stack.pop()).getToken();
       } catch (Exception e)
       {
           throw new ParseExceprion();
       }
        switch (token)
        {
        case PLUS: stack.push(new NumberToken(a.add(b,context))); break;
        case MINUS: stack.push(new NumberToken(a.subtract(b,context))); break;
        case TIMES: stack.push(new NumberToken(a.multiply(b,context))); break;
        case DIVISION: stack.push(new NumberToken(a.divide(b,BigDecimal.ROUND_HALF_UP)));
        }
    }


    public int prior() {
        switch (getToken()) {
        case PLUS:
        case MINUS:
            return 1;
        case TIMES:
        case DIVISION:
            return 2;
        default:
            return 0;
        }
    }



}
