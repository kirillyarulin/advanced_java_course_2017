package edu.technopolis.advanced.boatswain.Solver;

public class RightParenToken extends Token<Character> {
    private static final char RIGHT_PAREN = ')';

    public static boolean isRightParen(char c) {
        return c == RIGHT_PAREN;
    }

    public RightParenToken() {
        super(RIGHT_PAREN, Type.RIGHT_PAREN);
    }
}
