package edu.technopolis;

/**
 * Created by cilci_000 on 07.10.2017.
 */
public class MyFibonacciAlgorithm implements FibonacciAlgorithm{

    @Override
    public String evaluate(int index) {
        if(index <= 2)
            return "1";

        BigNumber a = new BigNumber();
        BigNumber b = new BigNumber();
        BigNumber c = new BigNumber();

        a.setValue(1);
        b.setValue(1);

        for (int it = 2; it < index; it++) {
            c = a.add(b);
            a = b;
            b = c;
        }
        return c.toString();
    }
}
