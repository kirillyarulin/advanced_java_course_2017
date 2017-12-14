package edu.technopolis.advanced.boatswain.Solver;

public class TerminateToken implements Token{
    String token;

    public TerminateToken(String token) {
        this.token = token;
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
