package edu.technopolis.advanced.boatswain.Solver;

public class Token<T> {
    enum Type {
        OPERATION,
        LEFT_PAREN,
        RIGHT_PAREN,
        NUMBER,
        FUNCTION
    }

    ;
    T token;
    Type tokenType;

    public T getToken() {
        return token;
    }

    public void setToken(T token) {
        this.token = token;
    }

    public Type getTokenType() {
        return tokenType;
    }

    public void setTokenType(Type tokenType) {
        this.tokenType = tokenType;
    }

    public Token(T token, Type tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }


}
