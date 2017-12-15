package edu.technopolis.advanced.boatswain.Solver;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.HashMap;

import ch.obermuhlner.math.big.BigDecimalMath;

public class FunctionToken extends Token<BigDecimalFunction<BigDecimal, BigDecimal, MathContext>> implements CalculatingToken
{

    private static final HashMap<String, BigDecimalFunction<BigDecimal, BigDecimal, MathContext> > functionsList = new HashMap<String, BigDecimalFunction<BigDecimal, BigDecimal, MathContext>>();
    private MathContext context;
    static
    {
        functionsList.put("sin", BigDecimalMath::sin);
        functionsList.put("cos", BigDecimalMath::cos);
        functionsList.put("tg", BigDecimalMath::tan);
        functionsList.put("asin", BigDecimalMath::asin);
        functionsList.put("acos", BigDecimalMath::acos);
        functionsList.put("atg", BigDecimalMath::atan);
        functionsList.put("sh", BigDecimalMath::sinh);
        functionsList.put("cs", BigDecimalMath::cosh);
        functionsList.put("tgh", BigDecimalMath::tanh);
        functionsList.put("log", BigDecimalMath::log);
        functionsList.put("sqrt", BigDecimalMath::sqrt);
    }

    FunctionToken(BigDecimalFunction<BigDecimal, BigDecimal, MathContext> token, MathContext context) {
        super(token, Type.FUNCTION);
        this.context = context;
    }


    static boolean isFunction(String s)
    {
        return functionsList.containsKey(s);

    }
    static BigDecimalFunction<BigDecimal, BigDecimal, MathContext> getFunction(String s)
    {
        return functionsList.get(s);
    }

    @Override
    public void calculate(ArrayDeque<Token> stack, Token currToken) throws ParseExceprion, ArithmeticException {
        if (stack.peek().getTokenType() == Type.NUMBER)
        {
            stack.push(new NumberToken(getToken().apply((BigDecimal) stack.pop().getToken(), context)));
            return;
        }
        throw new ParseExceprion("parse error");
    }
}