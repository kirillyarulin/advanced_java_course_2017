package edu.technopolis;


/**
 * Это <b>неправильный</b> с точки зрения задания алгоритм.
 */
public class DummyFibonacciAlgorithm implements edu.technopolis.FibonacciAlgorithm {

    @Override
    public String evaluate(int index) throws Exception {
        if (index < 3) {
            return "1";
        }
        CleverBigIntImpl one = new CleverBigIntImpl(1);
        CleverBigIntImpl two = new CleverBigIntImpl(1);
        CleverBigIntImpl result = new CleverBigIntImpl(one);
        for (int i = 2; i < index; i++) {
            if (i == 183) {
                i++;
                i--;
            }
            result.add(one);
            one = new CleverBigIntImpl(two);
            two = new CleverBigIntImpl(result);
        }
        return result.toString();
    }

}
