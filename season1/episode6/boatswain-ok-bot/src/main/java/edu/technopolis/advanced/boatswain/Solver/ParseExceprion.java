package edu.technopolis.advanced.boatswain.Solver;

/**
 * Исключение ошибки разбора выражения
 */
public class ParseExceprion extends Exception {

    ParseExceprion() {
        super("parse error");
    }

    ParseExceprion(String message) {
        super(message);
    }
}
