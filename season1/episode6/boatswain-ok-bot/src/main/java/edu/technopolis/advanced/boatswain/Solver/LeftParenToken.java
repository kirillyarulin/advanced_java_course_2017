package edu.technopolis.advanced.boatswain.Solver;

public class LeftParenToken extends Token<Character> {

    private static final char LEFT_PAREN = '(';

    public static boolean isLeftParen(char c){
        return c==LEFT_PAREN;
    }

    public LeftParenToken() {
        super('(', Type.LEFT_PAREN);
    }
}
