package edu.technopolis.advanced.boatswain.Solver;

import java.util.function.Function;

public class FunctionToken implements Token{
    Function<Double, Double> function;
    String token;

    public FunctionToken(String token, Function<Double, Double> function)
    {
        this.function = function;
        this.token = token;
    }

    void setFunction( Function<Double, Double> function)
    {
        this.function = function;
    }

    Function<Double, Double> getFunction(  )
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
