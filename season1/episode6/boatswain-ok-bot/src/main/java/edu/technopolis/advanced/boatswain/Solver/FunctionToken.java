package edu.technopolis.advanced.boatswain.Solver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

public class FunctionToken implements Token{
    BigDecimalFunction<BigDecimal, BigDecimal, MathContext> function;
    String token;

    public FunctionToken(String token, BigDecimalFunction<BigDecimal, BigDecimal, MathContext> function)
    {
        this.function = function;
        this.token = token;
    }

    void setFunction( BigDecimalFunction<BigDecimal, BigDecimal, MathContext> function)
    {
        this.function = function;
    }

    BigDecimalFunction<BigDecimal, BigDecimal, MathContext> getFunction(  )
    {
        return function;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String s) {
        this.token = s;
    }

}
