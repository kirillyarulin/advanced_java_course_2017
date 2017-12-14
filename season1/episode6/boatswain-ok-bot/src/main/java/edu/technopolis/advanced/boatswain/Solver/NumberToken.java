package edu.technopolis.advanced.boatswain.Solver;

import java.math.BigDecimal;

public class NumberToken implements Token {
    String token;

    public NumberToken( String token)
    {
        this.token = token;
    }


    BigDecimal getDoubleNum()
    {
        try {
            return new BigDecimal(token);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String s) {
        this.token = token;

    }
}
