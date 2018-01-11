package edu.technopolis;

import java.util.Arrays;

/**
 * Created by Nusrat on 10/4/2017.
 */
//Нормальная реализация
public class CleverBigIntImpl extends BigInteger {
    private long[] number = new long[2];
    private int rank = 0;

    CleverBigIntImpl(long init) {
        number[number.length - 1] = init;
        rank = 1;
    }

    CleverBigIntImpl(CleverBigIntImpl init) {
        number = Arrays.copyOf(init.getNumberMassive(), init.size());
        rank = init.getRank();
    }

    public void add(CleverBigIntImpl addition) throws Exception {
        if (addition.getNumberMassive().length > this.number.length) {
            this.number = grewd(this.number, addition.getNumberMassive().length);
        } else if (addition.getNumberMassive().length < this.number.length) {
            addition.setNumberMassive(grewd(addition.getNumberMassive(), number.length));
        }
        for (int i = number.length - 1; i >= 0; i--) {
            addLong(addition.getNumberMassive()[i], i);
        }
    }

    private void addLong(long addition, int pos) {
        number[pos] += addition;
        if (number[pos] < 0) {
            number[pos] -= Long.MAX_VALUE;
            if (pos == 0) {
                pos = number.length - 1;
                this.number = grewd(this.number, this.getNumberMassive().length * 2);
            } else {
                pos--;
            }
            addLong(1, pos);
        }
    }

    @Override
    public String toString() {
        java.math.BigInteger out = java.math.BigInteger.ZERO;
        for (int i = number.length - 1; i > -1; i--) {
            java.math.BigInteger adder = java.math.BigInteger.valueOf(Long.MAX_VALUE);
            adder = adder.pow(number.length - 1 - i);
            adder = adder.multiply(java.math.BigInteger.valueOf(number[i]));
            out = out.add(adder);
        }

        return out.toString();
    }

    private long[] grewd(long[] number, int newSize) {
        long[] prevNumber = number;
        number = new long[newSize];
        System.arraycopy(prevNumber, 0, number, prevNumber.length, number.length - prevNumber.length);
        return number;
    }


    public void multiply(CleverBigIntImpl mult) throws Exception {
        CleverBigIntImpl multTo = new CleverBigIntImpl(mult);
        while (!multTo.toString().equals("0")) {
            multTo.add(new CleverBigIntImpl(-1));

        }
    }

    @Override
    public int size() {
        return number.length;
    }

    @Override
    public long[] getNumberMassive() {
        return number;
    }

    @Override
    public void setNumberMassive(long[] numberMassive) {
        this.number = numberMassive;
    }

    @Override
    public int getRank() {
        return rank;
    }

}
