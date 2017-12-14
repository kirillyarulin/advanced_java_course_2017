package edu.technopolis.advanced.boatswain.Solver;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Класс парсит и считает выражение
 */
public class Solver {
    private static final char LEFT_PAREN = '(';
    private static final char RIGHT_PAREN = ')';
    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char TIMES = '*';
    private static final char DIVISION = '/';
    private static final HashMap<String, Function<Double, Double> > functionsList = new HashMap<String, Function<Double, Double> >();

   public Solver()
   {
       functionsList.put("sin", Math::sin);
       functionsList.put("cos", Math::cos);
       functionsList.put("tg", Math::tan);
       functionsList.put("ctg", ExMath::ctg);
       functionsList.put("asin", Math::asin);
       functionsList.put("acos", Math::acos);
       functionsList.put("atg", Math::atan);
       functionsList.put("sec", ExMath::sec);
       functionsList.put("cosec", ExMath::cosec);
       functionsList.put("sh", Math::sinh);
       functionsList.put("cs", Math::cosh);
       functionsList.put("tgh", Math::tanh);
       functionsList.put("log", Math::log);
       functionsList.put("sqrt", Math::sqrt);

   }


    /**
     * Проверяет, является ли с оператором
     * @param c
     * @return true если c оператор, false в противном случае
     */
    private boolean isOperat(Token c) {
        return c instanceof TerminateToken && !checkString(c.getToken()) && (c.getToken().charAt(0) == PLUS || c.getToken().charAt(0) == MINUS || c.getToken().charAt(0) == DIVISION || c.getToken().charAt(0) == TIMES);
    }

    /**
     * Проверяет, является ли c оператором или скобками
     * @param c
     * @return true если c оператор или скобка, false в противном случае
     */
    private boolean isOperatOrParen(char c) {
        return (c == PLUS || c == MINUS || c == DIVISION || c == TIMES || c == LEFT_PAREN || c == RIGHT_PAREN);
    }

    /**
     * Проверяет, является ли ch цифрой
     * @param ch
     * @return true если ch цифра, false в противном случае
     */
    private boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * Проверяет, можно ли  преобразовать str в число типа double
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
     * @param s Выражение, которое нужно вычислить. Каждый элемент s содержит отдельный токен выражения (число, знак арифметической операции или скобку)
     * @return
     * @throws ParseExceprion
     * @throws ArithmeticException
     */
    private BigDecimal build(ArrayList<Token> s) throws ParseExceprion, ArithmeticException, ClassCastException {
        ArrayList<Token> queue = new ArrayList<>();
        Deque<Token> stack = new ArrayDeque<>();
        for (Token value : s) {
            if (value instanceof FunctionToken)
                stack.push(value);
            if (checkString(value.getToken())) {
                queue.add(value);
            } else if (isOperat(value)) {
                while (!stack.isEmpty() && isOperat(stack.peek()) && (prior((TerminateToken)stack.peek()) >= prior((TerminateToken)value))) {
                    queue.add(stack.pop());
                }
                stack.push(value);
            } else if (value.getToken().charAt(0) == LEFT_PAREN) {
                stack.push(new TerminateToken(LEFT_PAREN + ""));
            } else if (value.getToken().charAt(0) == RIGHT_PAREN) {
                while (!stack.isEmpty() && !stack.peek().getToken().equals(""+LEFT_PAREN)) {
                    queue.add(stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new ParseExceprion("parse error");
                }
                stack.pop();
                if (!stack.isEmpty() && stack.peek() instanceof FunctionToken) {
                    queue.add(stack.pop());

                }
            }

        }
        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }
        stack.clear();
        for (Token aQueue : queue) {
            if (aQueue instanceof NumberToken) {
                stack.push(aQueue);
            } else {
                try {
                   if (aQueue instanceof FunctionToken)
                   {
                       NumberToken arg = (NumberToken) stack.pop();
                       Double d = ((FunctionToken) aQueue).getFunction().apply(arg.getDoubleNum().doubleValue());
                       if (d.isNaN())
                           throw new ArithmeticException();
                       stack.push(new NumberToken(d.toString()));
                   } else
                   if (aQueue instanceof TerminateToken)
                   {
                       NumberToken arg1 = (NumberToken) stack.pop();
                       NumberToken arg2 = (NumberToken) stack.pop();
                       stack.push(new NumberToken(calcOper((TerminateToken)aQueue, arg2, arg1).toString()));
                   }

                } catch (NoSuchElementException e) {
                    throw new ParseExceprion("parse error");
                }

            }
        }
        try {
            return ((NumberToken)stack.pop()).getDoubleNum();
        } catch (NoSuchElementException e)
        {
            throw new ParseExceprion("parse error");
        }

    }

    /**
     * Возвращает приоритет переданной операции
     * @param s
     * @return
     */
    private int prior(TerminateToken s) {
        switch (s.getToken().charAt(0)) {
        case PLUS:
        case MINUS:
            return 1;
        case TIMES:
        case DIVISION:
            return 2;
        default:
            return 0;
        }
    }

    /**
     * возвращает результат операции c значений l и r
     * @param c операция
     * @param l левый операнд
     * @param r правый операнд
     * @return результат операции
     * @throws ArithmeticException
     */
    private BigDecimal calcOper(TerminateToken c, NumberToken l, NumberToken r) throws ArithmeticException {
        switch (c.getToken().charAt(0)) {
        case PLUS:
            return l.getDoubleNum().add(r.getDoubleNum());
        case MINUS:
            return l.getDoubleNum().subtract( r.getDoubleNum());
        case TIMES:
            return l.getDoubleNum().multiply(r.getDoubleNum());
        case DIVISION:
            return l.getDoubleNum().divide(r.getDoubleNum(), 10, BigDecimal.ROUND_HALF_UP);
        default:
            return null;
        }
    }
    
    


    boolean isFunction(String s)
    {
        return functionsList.containsKey(s);

    }



    Function<Double, Double> getFunction (String s)
    {
        return functionsList.get(s);
    }


    /**
     * Вычисляет значение арифметического выражения
     * @param str арифметическое выражение
     * @return результат арифметичческого выражения
     * @throws ParseExceprion
     * @throws ArithmeticException
     * @throws ClassCastException
     */
    public BigDecimal evaluate(String str) throws ParseExceprion, ArithmeticException, ClassCastException {
        ArrayList<Token> values = new ArrayList<>();
        boolean inToken = false;
        StringBuilder curr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (isOperatOrParen(c)) {
                if (inToken) {
                    if (!checkString(curr.toString()) && !isFunction(curr.toString()))
                        throw new ParseExceprion("parse error");
                    if (checkString(curr.toString()))
                        values.add(new NumberToken(curr.toString()));
                    else
                        values.add(new FunctionToken(curr.toString(), getFunction(curr.toString())));
                    inToken = false;
                    curr.setLength(0);
                }
                values.add(new TerminateToken("" + c));
            } else {
                inToken = true;
                curr.append(c);
            }

        }
        if (inToken) {
            if (!checkString(curr.toString()))
                throw new ParseExceprion("parse error");
            values.add(new NumberToken(curr.toString()));
        }
        return build(values);
    }


}
