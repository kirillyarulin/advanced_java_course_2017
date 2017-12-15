package edu.technopolis.advanced.boatswain.Solver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.ArrayList;


/**
 * Класс парсит и считает выражение
 */
public class Solver {

    MathContext context = new MathContext(20);

    public Solver() {

    }


    /**
     * Проверяет, можно ли  преобразовать str в число типа double
     *
     * @param str
     * @return true если можно преобразовать, false иначе
     */
    private boolean checkString(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Метод вычисляет значение переданного арифметического выражения, испольуя алгорим Дейкстры (Сортировочная станция)
     *
     * @param s Выражение, которое нужно вычислить. Каждый элемент s содержит отдельный токен выражения (число, знак арифметической операции или скобку)
     * @return
     * @throws ParseExceprion
     * @throws ArithmeticException
     */
    private BigDecimal build(ArrayList<Token> s) throws ParseExceprion, ArithmeticException {
        ArrayList<Token> queue = new ArrayList<>();
        ArrayDeque<Token> stack = new ArrayDeque<>();
        for (Token value : s) {
            if (value.getTokenType() == Token.Type.FUNCTION) {
                stack.push(value);
            } else if (value.getTokenType() == Token.Type.NUMBER) {
                queue.add(value);
            } else if (value.getTokenType() == Token.Type.OPERATION) {
                while (!stack.isEmpty() && stack.peek().getTokenType() == Token.Type.OPERATION &&
                        ((((OperationToken) stack.peek()).prior()) >= (((OperationToken) value).prior()))) {
                    queue.add(stack.pop());
                }
                stack.push(value);
            } else if (value.getTokenType() == Token.Type.LEFT_PAREN) {
                stack.push(new LeftParenToken());
            } else if (value.getTokenType() == Token.Type.RIGHT_PAREN) {
                while (!stack.isEmpty() && (stack.peek().getTokenType() != Token.Type.LEFT_PAREN)) {
                    queue.add(stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new ParseExceprion("parse error");
                }
                stack.pop();
                if (!stack.isEmpty() && stack.peek().getTokenType() == Token.Type.FUNCTION) {
                    queue.add(stack.pop());
                }
            }
        }
        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }
        stack.clear();
        try {
            for (Token aQueue : queue) {
                ((CalculatingToken) aQueue).calculate(stack, aQueue);
            }
        } catch (ClassCastException e) {
            throw new ParseExceprion();
        }
        if (stack.peek().getTokenType() != Token.Type.NUMBER) {
            throw new ParseExceprion("parse error");
        } else {
            return ((NumberToken) stack.peek()).getToken();
        }

    }


    /**
     * Вычисляет значение арифметического выражения
     *
     * @param str арифметическое выражение
     * @return результат арифметичческого выражения
     * @throws ParseExceprion
     * @throws ArithmeticException
     */
    public BigDecimal evaluate(String str) throws ParseExceprion, ArithmeticException {
        ArrayList<Token> values = new ArrayList<>();
        boolean inToken = false;
        StringBuilder curr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (OperationToken.isOperator(c) || RightParenToken.isRightParen(c) || LeftParenToken.isLeftParen(c)) {
                if (inToken) {
                    if (!checkString(curr.toString()) && !FunctionToken.isFunction(curr.toString())) {
                        throw new ParseExceprion("parse error");
                    }
                    if (checkString(curr.toString())) {
                        values.add(new NumberToken(new BigDecimal(curr.toString())));
                    } else {
                        values.add(new FunctionToken(FunctionToken.getFunction(curr.toString()), context));
                    }
                    inToken = false;
                    curr.setLength(0);
                }
                if (OperationToken.isOperator(c)) {
                    values.add(new OperationToken(c, context));
                }
                if (LeftParenToken.isLeftParen(c)) {
                    values.add(new LeftParenToken());
                }
                if (RightParenToken.isRightParen(c)) {
                    values.add(new RightParenToken());
                }
            } else {
                inToken = true;
                curr.append(c);
            }

        }
        if (inToken) {
            if (!checkString(curr.toString())) {
                throw new ParseExceprion("parse error");
            }
            values.add(new NumberToken(new BigDecimal(curr.toString())));
        }
        return build(values);
    }


}
